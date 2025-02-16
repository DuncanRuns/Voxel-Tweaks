package me.duncanruns.voxeltweaks.mixin;

import com.mamiyaotaru.voxelmap.persistent.GuiPersistentMap;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = GuiPersistentMap.class, remap = false)
public abstract class GuiPersistentMapMixin {
    @Shadow
    private MutableText multiworldButtonNameRed;

    @Shadow
    private Text multiworldButtonName;

    @Inject(method = "init", at = @At("TAIL"), remap = true)
    private void replaceRedText(CallbackInfo ci) {
        multiworldButtonNameRed = multiworldButtonName.copy();
    }
}
