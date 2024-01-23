/*
 * Copyright 2023 dima_dencep.
 *
 * Licensed under the Open Software License, Version 3.0 (the "License");
 * you may not use this file except in compliance with the License.
 *
 * You may obtain a copy of the License at
 *     https://github.com/dima-dencep/rrls/blob/HEAD/LICENSE
 */

package com.github.dimadencep.mods.rrls.mixins.compat;

import com.github.dimadencep.mods.rrls.ConfigExpectPlatform;
import com.github.dimadencep.mods.rrls.utils.DummyDrawContext;
import com.github.dimadencep.mods.rrls.utils.OverlayHelper;
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
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

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
        if (!OverlayHelper.isRenderingState(original))
            return original;

        original.render(DummyDrawContext.INSTANCE, 0, 0, client.getLastFrameDuration());

        return null;
    }

    @Inject(
            method = "render",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/DrawContext;draw()V"
            )
    )
    public void rrls$miniRender(float tickDelta, long startTime, boolean tick, CallbackInfo ci, @Local(ordinal = 0) DrawContext drawContext) {
        Overlay overlay = this.client.getOverlay();

        if (ConfigExpectPlatform.miniRender() && OverlayHelper.isRenderingState(overlay))
            overlay.rrls$miniRender(drawContext);
    }
}
