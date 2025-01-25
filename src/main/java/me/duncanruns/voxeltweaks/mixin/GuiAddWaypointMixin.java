package me.duncanruns.voxeltweaks.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mamiyaotaru.voxelmap.gui.GuiAddWaypoint;
import com.mamiyaotaru.voxelmap.gui.overridden.GuiScreenMinimap;
import com.mamiyaotaru.voxelmap.gui.overridden.IPopupGuiScreen;
import com.mamiyaotaru.voxelmap.gui.overridden.PopupGuiButton;
import me.duncanruns.voxeltweaks.VoxelTweaks;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = GuiAddWaypoint.class)
public abstract class GuiAddWaypointMixin extends GuiScreenMinimap {
    @Shadow
    private TextFieldWidget waypointZ;

    @Shadow
    private TextFieldWidget waypointY;

    @Shadow
    private TextFieldWidget waypointX;


    @Inject(method = "init", at = @At(value = "INVOKE", target = "Lcom/mamiyaotaru/voxelmap/gui/GuiAddWaypoint;addDrawableChild(Lnet/minecraft/client/gui/Element;)Lnet/minecraft/client/gui/Element;", ordinal = 3, shift = At.Shift.AFTER))
    private void addPasteButton(CallbackInfo ci) {
        int x = 2 * (waypointZ.getX() - waypointY.getX()) + waypointY.getX();
        int y = 2 * (waypointZ.getY() - waypointY.getY()) + waypointY.getY();
        int w = waypointY.getWidth();
        int h = 20;
        this.addDrawableChild(new PopupGuiButton(x, y, w, h, Text.literal("Paste"), b -> paste(), (IPopupGuiScreen) this));
    }

    @Unique
    private void paste() {
        int[] clipboardCoords = VoxelTweaks.getClipboardCoords();
        if (clipboardCoords == null) return;
        this.waypointX.setText(String.valueOf(clipboardCoords[0]));
        if (clipboardCoords[1] != Integer.MIN_VALUE) this.waypointY.setText(String.valueOf(clipboardCoords[1]));
        this.waypointZ.setText(String.valueOf(clipboardCoords[2]));
    }
}
