/*
 * Copyright 2023 - 2024 dima_dencep.
 *
 * Licensed under the Open Software License, Version 3.0 (the "License");
 * you may not use this file except in compliance with the License.
 *
 * You may obtain a copy of the License at
 *     https://spdx.org/licenses/OSL-3.0.txt
 */

package com.github.dimadencep.mods.rrls.mixins.compat;

import com.github.dimadencep.mods.rrls.ConfigExpectPlatform;
import com.github.dimadencep.mods.rrls.Rrls;
import com.github.dimadencep.mods.rrls.utils.DummyDrawContext;
import com.github.dimadencep.mods.rrls.utils.OverlayHelper;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Overlay;
import net.minecraft.client.renderer.GameRenderer;
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
    Minecraft minecraft;

    @Inject(
            method = "render",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/GuiGraphics;flush()V"
            )
    )
    public void rrls$miniRender(float tickDelta, long startTime, boolean tick, CallbackInfo ci, @Local(ordinal = 0) GuiGraphics drawContext) {
        try {
            Overlay overlay = this.minecraft.overlay;

            if (OverlayHelper.isRenderingState(overlay)) {
                overlay.render(DummyDrawContext.INSTANCE, 0, 0, minecraft.getDeltaFrameTime());

                if (ConfigExpectPlatform.miniRender())
                    overlay.rrls$miniRender(drawContext);
            }

        } catch (RuntimeException ex) {
            Rrls.LOGGER.error(ex);
        }
    }
}
