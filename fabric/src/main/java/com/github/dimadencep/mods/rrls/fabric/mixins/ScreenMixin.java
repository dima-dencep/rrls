package com.github.dimadencep.mods.rrls.fabric.mixins;

import com.github.dimadencep.mods.rrls.fabric.RrlsFabric;
import net.minecraft.client.gui.AbstractParentElement;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Screen.class) // TODO remove
public abstract class ScreenMixin extends AbstractParentElement implements Drawable {
    @Inject(at = @At("TAIL"), method = "render")
    private void init(MatrixStack matrices, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        RrlsFabric.renderInGame.accept(matrices);
    }
}