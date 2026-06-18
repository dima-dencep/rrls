/*
 * Copyright 2023 - 2026 dima_dencep.
 *
 * Licensed under the Open Software License, Version 3.0 (the "License");
 * you may not use this file except in compliance with the License.
 *
 * You may obtain a copy of the License at
 *     https://spdx.org/licenses/OSL-3.0.txt
 */

package org.redlance.dima_dencep.mods.rrls.mixins;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import org.jspecify.annotations.Nullable;
import org.objectweb.asm.Opcodes;
import org.redlance.dima_dencep.mods.rrls.Rrls;
import org.redlance.dima_dencep.mods.rrls.RrlsConfig;
import org.redlance.dima_dencep.mods.rrls.utils.DummyGuiGraphics;
import org.redlance.dima_dencep.mods.rrls.utils.OverlayHelper;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.gui.screens.Overlay;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Gui.class)
public class GuiMixin {
    @Shadow
    private @Nullable Overlay overlay;

    @Inject(
            method = "extractRenderState",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/Hud;extractDeferredSubtitles()V"
            )
    )
    public void rrls$miniRender(DeltaTracker deltaTracker, boolean shouldRenderLevel, boolean resourcesLoaded, CallbackInfo ci, @Local(ordinal = 0) GuiGraphicsExtractor graphics) {
        try {
            if (OverlayHelper.isRenderingState(this.overlay)) {
                rrls$enableScissor(graphics, () -> this.overlay.extractRenderState(
                        DummyGuiGraphics.INSTANCE, 0, 0, deltaTracker.getGameTimeDeltaTicks()
                ));

                if (this.overlay != null && RrlsConfig.INSTANCE.miniRender())
                    this.overlay.rrls$miniRender(graphics, deltaTracker.getGameTimeDeltaTicks());
            }

        } catch (RuntimeException ex) {
            Rrls.LOGGER.error("Failed to draw overlay!", ex);
        }
    }

    @ModifyReturnValue(
            method = "overlay",
            at = @At(
                    value = "RETURN"
            )
    )
    public Overlay rrls$blockOverlay(Overlay original) {
        if (RrlsConfig.INSTANCE.blockOverlay() && OverlayHelper.isRenderingState(original)) return null;
        return original;
    }

    @Unique
    private static void rrls$enableScissor(GuiGraphicsExtractor graphics, Runnable runnable) {
        if (RrlsConfig.INSTANCE.enableScissor()) {
            graphics.pose().pushMatrix();
            graphics.enableScissor(0, 0, 0, 0);

            runnable.run();

            graphics.disableScissor();
            graphics.pose().popMatrix();
        } else {
            runnable.run();
        }
    }

    @WrapOperation(
            method = {"handleKeybinds", "extractRenderState"},
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/client/gui/Gui;overlay:Lnet/minecraft/client/gui/screens/Overlay;",
                    opcode = Opcodes.GETFIELD
            )
    )
    public Overlay rrls$miniRender(Gui instance, Operation<Overlay> original) {
        Overlay overlay = original.call(instance);
        if (OverlayHelper.isRenderingState(overlay)) return null;
        return overlay;
    }
}
