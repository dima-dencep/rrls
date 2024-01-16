/*
 * Copyright 2023 dima_dencep.
 *
 * Licensed under the Open Software License, Version 3.0 (the "License");
 * you may not use this file except in compliance with the License.
 *
 * You may obtain a copy of the License at
 *     https://github.com/dima-dencep/rrls/blob/HEAD/LICENSE
 */

package com.github.dimadencep.mods.rrls.mixins;

import com.github.dimadencep.mods.rrls.ConfigExpectPlatform;
import com.github.dimadencep.mods.rrls.Rrls;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.RunArgs;
import net.minecraft.client.gui.screen.Overlay;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.concurrent.CompletableFuture;

@Mixin(MinecraftClient.class)
public abstract class MinecraftClientMixin {
    @Shadow
    protected abstract void showResourceReloadFailureToast(@Nullable Text description);
    @Shadow
    protected abstract CompletableFuture<Void> reloadResources(boolean force, @Nullable MinecraftClient.LoadingContext loadingContext);
    @Shadow
    protected abstract void onFinishedLoading(@Nullable MinecraftClient.LoadingContext loadingContext);

    @Inject(
            method = "<init>",
            at = @At(
                    value = "RETURN"
            )
    )
    public void rrls$init(RunArgs args, CallbackInfo ci, @Local(ordinal = 0) MinecraftClient.LoadingContext loadingContext) {
        if (ConfigExpectPlatform.forceClose())
            onFinishedLoading(loadingContext);
    }

    @Inject(
            method = "onResourceReloadFailure",
            at = @At(
                    value = "HEAD"
            ),
            cancellable = true
    )
    public void rrls$onResourceReloadFailure(Throwable exception, Text resourceName, MinecraftClient.LoadingContext loadingContext, CallbackInfo ci) {
        if (!ConfigExpectPlatform.resetResources()) {
            Rrls.LOGGER.error("Caught error loading resourcepacks!", exception);

            this.reloadResources(true, loadingContext).thenRun(() -> this.showResourceReloadFailureToast(resourceName));

            ci.cancel();
        }
    }

    @Redirect(
            method = {
                    "tick",
                    "handleInputEvents"
            },
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/client/MinecraftClient;overlay:Lnet/minecraft/client/gui/screen/Overlay;"
            )
    )
    public Overlay rrls$safeOverlays(MinecraftClient instance) {
        return Rrls.tryGetOverlay();
    }
}
