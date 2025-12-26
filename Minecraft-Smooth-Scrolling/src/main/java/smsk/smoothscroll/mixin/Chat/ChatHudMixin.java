package smsk.smoothscroll.mixin.Chat;

import net.minecraft.client.gui.hud.ChatHud;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import smsk.smoothscroll.SmoothSc;

@Mixin(ChatHud.class)
public abstract class ChatHudMixin {
    @Shadow private int scrolledLines;
    
    @Unique private double smoothscroll$targetScroll = 0;
    @Unique private double smoothscroll$currentScroll = 0;
    @Unique private double smoothscroll$scrollVelocity = 0;
    
    @Inject(method = "scroll", at = @At("HEAD"), cancellable = true)
    private void onScroll(double amount, CallbackInfo ci) {
        if (!SmoothSc.getConfig().enableInChat) {
            return;
        }
        
        smoothscroll$targetScroll += amount;
        ci.cancel();
    }
    
    @Inject(method = "render", at = @At("HEAD"))
    private void onRender(CallbackInfo ci) {
        if (!SmoothSc.getConfig().enableInChat) {
            return;
        }
        
        double diff = smoothscroll$targetScroll - smoothscroll$currentScroll;
        smoothscroll$scrollVelocity += diff * SmoothSc.getConfig().getAcceleration();
        smoothscroll$scrollVelocity *= SmoothSc.getConfig().getFriction();
        smoothscroll$currentScroll += smoothscroll$scrollVelocity;
        
        scrolledLines = (int) Math.round(smoothscroll$currentScroll);
        
        if (Math.abs(diff) < 0.01 && Math.abs(smoothscroll$scrollVelocity) < 0.01) {
            smoothscroll$currentScroll = smoothscroll$targetScroll;
            smoothscroll$scrollVelocity = 0;
        }
    }
}
