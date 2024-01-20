package com.github.dimadencep.mods.rrls.mixins.compat;

import com.github.dimadencep.mods.rrls.ConfigExpectPlatform;
import com.github.dimadencep.mods.rrls.accessor.SplashAccessor;
import com.github.dimadencep.mods.rrls.utils.DummyDrawContext;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Overlay;
import net.minecraft.client.render.GameRenderer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(GameRenderer.class)
public class GameRendererMixin {

    @Shadow
    @Final
    MinecraftClient client;

    @ModifyExpressionValue(
            method = "render",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/MinecraftClient;getOverlay()Lnet/minecraft/client/gui/screen/Overlay;",
                    ordinal = 0
            )
    )
    public Overlay rrls$fixOverlayRendering(Overlay original, @Local(ordinal = 0) DrawContext drawContext) { // TODO @Local(name = "i", ordinal = 0, index = 7) int mouseX, @Local(name = "j", ordinal = 1, index = 8) int mouseY
        if (original == null || original.rrls$getAttachType() != SplashAccessor.AttachType.HIDE)
            return original;

        original.render(DummyDrawContext.INSTANCE, 0, 0, client.getLastFrameDuration());

        if (ConfigExpectPlatform.miniRender())
            original.rrls$miniRender(drawContext);

        return null;
    }
}
