/*
 * Copyright 2023 - 2024 dima_dencep.
 *
 * Licensed under the Open Software License, Version 3.0 (the "License");
 * you may not use this file except in compliance with the License.
 *
 * You may obtain a copy of the License at
 *     https://spdx.org/licenses/OSL-3.0.txt
 */

package org.redlance.dima_dencep.mods.rrls.mixins;

import net.minecraft.client.DeltaTracker;
import org.redlance.dima_dencep.mods.rrls.Rrls;
import org.redlance.dima_dencep.mods.rrls.RrlsConfig;
import org.redlance.dima_dencep.mods.rrls.utils.DummyGuiGraphics;
import org.redlance.dima_dencep.mods.rrls.utils.OverlayHelper;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Overlay;
import net.minecraft.client.renderer.GameRenderer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public class GameRendererMixin {
    @Shadow
    @Final
    private Minecraft minecraft;

    @Inject(
            method = "render",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/GuiGraphics;flush()V",
                    ordinal = 1
            )
    )
    public void rrls$miniRender(DeltaTracker deltaTracker, boolean renderLevel, CallbackInfo ci, @Local(ordinal = 0) GuiGraphics graphics) {
        try {
            Overlay overlay = this.minecraft.overlay;

            if (OverlayHelper.isRenderingState(overlay)) {
                rrls$enableScissor(graphics, () -> overlay.render(
                        DummyGuiGraphics.INSTANCE, 0, 0, deltaTracker.getGameTimeDeltaTicks()
                ));

                if (RrlsConfig.miniRender())
                    overlay.rrls$miniRender(graphics);
            }

        } catch (RuntimeException ex) {
            Rrls.LOGGER.error("Failed to draw overlay!", ex);
        }
    }

    @Unique
    private static void rrls$enableScissor(GuiGraphics graphics, Runnable runnable) {
        if (RrlsConfig.enableScissor()) {
            graphics.pose().pushPose();
            graphics.enableScissor(0, 0, 0, 0);

            runnable.run();

            graphics.disableScissor();
            graphics.pose().popPose();
        } else {
            runnable.run();
        }
    }
}
