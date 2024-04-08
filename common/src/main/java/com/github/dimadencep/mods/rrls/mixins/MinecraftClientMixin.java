/*
 * Copyright 2023 - 2024 dima_dencep.
 *
 * Licensed under the Open Software License, Version 3.0 (the "License");
 * you may not use this file except in compliance with the License.
 *
 * You may obtain a copy of the License at
 *     https://spdx.org/licenses/OSL-3.0.txt
 */

package com.github.dimadencep.mods.rrls.mixins;

import com.github.dimadencep.mods.rrls.ConfigExpectPlatform;
import com.github.dimadencep.mods.rrls.Rrls;
import com.github.dimadencep.mods.rrls.config.DoubleLoad;
import com.github.dimadencep.mods.rrls.utils.OverlayHelper;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
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
import org.spongepowered.asm.mixin.injection.ModifyArg;
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
    ) // TODO refactor when @WrapMethod
    public void rrls$onResourceReloadFailure(Throwable exception, Text resourceName, MinecraftClient.LoadingContext loadingContext, CallbackInfo ci) {
        if (!ConfigExpectPlatform.resetResources()) {
            Rrls.LOGGER.error("Caught error loading resourcepacks!", exception);

            if (ConfigExpectPlatform.doubleLoad().isLoad())
                reloadResources(ConfigExpectPlatform.doubleLoad() == DoubleLoad.FORCE_LOAD, loadingContext)
                        .thenRun(() -> showResourceReloadFailureToast(resourceName));

            ci.cancel();
        }
    }

    @Inject(
            method = "onResourceReloadFailure",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/MinecraftClient;reloadResources(ZLnet/minecraft/client/MinecraftClient$LoadingContext;)Ljava/util/concurrent/CompletableFuture;",
                    shift = At.Shift.BEFORE
            ),
            cancellable = true
    )
    public void rrls$doubleLoad(Throwable exception, Text resourceName, MinecraftClient.LoadingContext loadingContext, CallbackInfo ci) {
        if (!ConfigExpectPlatform.doubleLoad().isLoad())
            ci.cancel();
    }

    @ModifyArg(
            method = "onResourceReloadFailure",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/MinecraftClient;reloadResources(ZLnet/minecraft/client/MinecraftClient$LoadingContext;)Ljava/util/concurrent/CompletableFuture;",
                    ordinal = 0
            ),
            require = 0
    )
    public boolean rrls$doubleLoad(boolean force) {
        return ConfigExpectPlatform.doubleLoad() == DoubleLoad.FORCE_LOAD;
    }

    @WrapOperation(
            method = {
                    "tick",
                    "handleInputEvents"
            },
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/client/MinecraftClient;overlay:Lnet/minecraft/client/gui/screen/Overlay;"
            )
    )
    public Overlay rrls$miniRender(MinecraftClient instance, Operation<Overlay> original) {
        Overlay overlay = original.call(instance);

        if (OverlayHelper.isRenderingState(overlay))
            return null;

        return overlay;
    }

    @ModifyReturnValue(
            method = "getOverlay",
            at = @At(
                    value = "RETURN"
            )
    )
    public Overlay rrls$blockOverlay(Overlay original) {
        if (ConfigExpectPlatform.blockOverlay() && OverlayHelper.isRenderingState(original))
            return null;

        return original;
    }
}
