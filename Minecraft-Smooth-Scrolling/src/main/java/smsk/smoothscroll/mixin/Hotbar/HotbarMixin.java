package smsk.smoothscroll.mixin.Hotbar;

import net.minecraft.client.gui.hud.InGameHud;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(InGameHud.class)
public class HotbarMixin {
    @Unique private double smoothscroll$targetSlot = 0;
    @Unique private double smoothscroll$currentSlot = 0;
    @Unique private double smoothscroll$velocity = 0;
}
