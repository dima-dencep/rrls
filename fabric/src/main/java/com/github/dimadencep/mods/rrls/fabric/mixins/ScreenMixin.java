package com.github.dimadencep.mods.rrls.fabric.mixins;

import com.github.dimadencep.mods.rrls.fabric.FabricMainMod;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.AbstractParentElement;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.client.util.math.MatrixStack;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Screen.class) // TODO remove
public abstract class ScreenMixin extends AbstractParentElement implements Drawable {
    @Shadow @Nullable protected MinecraftClient client;
    @Shadow public int width;

    @Inject(at = @At("TAIL"), method = "render")
    private void init(MatrixStack matrices, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        if (FabricMainMod.reloadHandler.getReload() != null && FabricMainMod.config.showInGui)
            drawCenteredTextWithShadow(matrices, this.client.textRenderer, I18n.translate(FabricMainMod.config.reloadText), this.width / 2, 70, FabricMainMod.config.rgbText ? FabricMainMod.getColor() : -1);
    }
}