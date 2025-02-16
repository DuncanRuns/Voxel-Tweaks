package me.duncanruns.voxeltweaks.mixin;

import com.mamiyaotaru.voxelmap.util.MessageUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(targets = {"com.mamiyaotaru.voxelmap.persistent.WorldMatcher$1"}, remap = false)
public abstract class WorldMatcherRunnableMixin {
    @Redirect(method = "run", at = @At(value = "INVOKE", target = "Lcom/mamiyaotaru/voxelmap/util/MessageUtils;chatInfo(Ljava/lang/String;)V", ordinal = 1))
    private void redirectUnknownWorldMessageToDebug(String s) {
        MessageUtils.printDebug(s);
    }
}
