package smsk.smoothscroll.mixin.Widgets;

import net.minecraft.client.gui.widget.EntryListWidget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import smsk.smoothscroll.SmoothSc;

@Mixin(EntryListWidget.class)
public abstract class ScrollableWidgetMixin {
    @Shadow private double scrollAmount;
    
    @Unique private double smoothscroll$targetScroll = 0;
    @Unique private double smoothscroll$currentScroll = 0;
    @Unique private double smoothscroll$scrollVelocity = 0;
    @Unique private boolean smoothscroll$initialized = false;
    
    @Inject(method = "mouseScrolled", at = @At("HEAD"), cancellable = true)
    private void onMouseScrolled(double mouseX, double mouseY, double amount, CallbackInfoReturnable<Boolean> cir) {
        if (!SmoothSc.getConfig().enableInWidgets) {
            return;
        }
        
        if (!smoothscroll$initialized) {
            smoothscroll$targetScroll = scrollAmount;
            smoothscroll$currentScroll = scrollAmount;
            smoothscroll$initialized = true;
        }
        
        double scrollDelta = amount * 20.0 * SmoothSc.getConfig().getScrollSpeed();
        smoothscroll$targetScroll = Math.max(0, smoothscroll$targetScroll - scrollDelta);
        
        cir.setReturnValue(true);
    }
    
    @Inject(method = "render", at = @At("HEAD"))
    private void onRender(CallbackInfo ci) {
        if (!SmoothSc.getConfig().enableInWidgets) {
            return;
        }
        
        if (!smoothscroll$initialized) {
            smoothscroll$targetScroll = scrollAmount;
            smoothscroll$currentScroll = scrollAmount;
            smoothscroll$initialized = true;
        }
        
        double diff = smoothscroll$targetScroll - smoothscroll$currentScroll;
        smoothscroll$scrollVelocity += diff * SmoothSc.getConfig().getAcceleration();
        smoothscroll$scrollVelocity *= SmoothSc.getConfig().getFriction();
        smoothscroll$currentScroll += smoothscroll$scrollVelocity;
        
        scrollAmount = smoothscroll$currentScroll;
        
        if (Math.abs(diff) < 0.1 && Math.abs(smoothscroll$scrollVelocity) < 0.1) {
            smoothscroll$currentScroll = smoothscroll$targetScroll;
            smoothscroll$scrollVelocity = 0;
        }
    }
}
