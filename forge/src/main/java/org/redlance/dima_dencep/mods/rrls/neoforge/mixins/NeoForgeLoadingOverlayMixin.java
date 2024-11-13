/*
 * Copyright 2023 - 2024 dima_dencep.
 *
 * Licensed under the Open Software License, Version 3.0 (the "License");
 * you may not use this file except in compliance with the License.
 *
 * You may obtain a copy of the License at
 *     https://spdx.org/licenses/OSL-3.0.txt
 */

package org.redlance.dima_dencep.mods.rrls.neoforge.mixins;

import org.redlance.dima_dencep.mods.rrls.RrlsConfig;
import org.redlance.dima_dencep.mods.rrls.utils.DummyGuiGraphics;
import org.redlance.dima_dencep.mods.rrls.utils.OverlayHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.LoadingOverlay;
import net.minecraft.server.packs.resources.ReloadInstance;
import net.neoforged.fml.earlydisplay.DisplayWindow;
import net.neoforged.fml.loading.progress.ProgressMeter;
import net.neoforged.fml.loading.progress.StartupNotificationManager;
import net.neoforged.neoforge.client.loading.NeoForgeLoadingOverlay;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;
import java.util.function.Consumer;

@Mixin(NeoForgeLoadingOverlay.class)
public abstract class NeoForgeLoadingOverlayMixin extends LoadingOverlay {
    @Shadow
    @Final
    private Minecraft minecraft;
    @Shadow
    @Final
    private ProgressMeter progressMeter;
    @Shadow
    @Final
    private DisplayWindow displayWindow;
    @Shadow
    private float currentProgress;

    public NeoForgeLoadingOverlayMixin(Minecraft client, ReloadInstance monitor, Consumer<Optional<Throwable>> exceptionHandler, boolean reloading) {
        super(client, monitor, exceptionHandler, reloading);
    }

    @Inject(
            method = "render",
            at = @At(
                    value = "HEAD"
            ),
            cancellable = true
    )
    public void rrls$render(GuiGraphics context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        boolean earlyLoadingScreenClosed = !StartupNotificationManager.getCurrentProgress().contains(progressMeter);

        if (context instanceof DummyGuiGraphics || RrlsConfig.skipForgeOverlay() || earlyLoadingScreenClosed) {
            if (!earlyLoadingScreenClosed) { // Stop forge's early loading screen
                progressMeter.complete();
                displayWindow.close();

                super.currentProgress = currentProgress;

            } else {
                currentProgress = super.currentProgress; // Sync progress (For mod compat?)
            }

            super.render(context, mouseX, mouseY, delta);

            ci.cancel();
        } else {
            rrls$setState(OverlayHelper.lookupState(minecraft.screen, false)); // Forge loading overlay is loading overlay :)
        }
    }

    @ModifyConstant(
            method = "render",
            constant = @Constant(
                    floatValue = 1000.0F,
                    ordinal = 0
            ),
            require = 0
    )
    public float rrls$changeAnimationSpeed(float instance) {
        if (!rrls$getState().isRendering()) {
            return RrlsConfig.animationSpeed();
        }

        return instance;
    }
}
