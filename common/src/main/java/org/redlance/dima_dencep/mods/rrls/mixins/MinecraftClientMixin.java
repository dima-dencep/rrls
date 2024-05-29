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

import net.minecraft.client.ResourceLoadStateTracker;
import org.redlance.dima_dencep.mods.rrls.config.DoubleLoad;
import org.redlance.dima_dencep.mods.rrls.ConfigExpectPlatform;
import org.redlance.dima_dencep.mods.rrls.Rrls;
import org.redlance.dima_dencep.mods.rrls.utils.OverlayHelper;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.concurrent.CompletableFuture;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Overlay;
import net.minecraft.client.main.GameConfig;
import net.minecraft.network.chat.Component;

@Mixin(Minecraft.class)
public abstract class MinecraftClientMixin {
    @Shadow
    protected abstract void addResourcePackLoadFailToast(@Nullable Component component);
    @Shadow
    protected abstract CompletableFuture<Void> reloadResourcePacks(boolean bl);
    @Shadow
    protected abstract void onGameLoadFinished();

    @Shadow
    @Final
    private ResourceLoadStateTracker reloadStateTracker;

    @Inject(
            method = "<init>",
            at = @At(
                    value = "TAIL"
            )
    )
    public void rrls$init(GameConfig gameConfig, CallbackInfo ci) {
        if (ConfigExpectPlatform.forceClose()) {
            this.reloadStateTracker.finishReload();
            this.onGameLoadFinished();
        }
    }

    @Inject(
            method = "clearResourcePacksOnError",
            at = @At(
                    value = "HEAD"
            ),
            cancellable = true
    )
    public void rrls$onResourceReloadFailure(Throwable throwable, Component errorMessage, CallbackInfo ci) {
        if (!ConfigExpectPlatform.resetResources()) {
            ci.cancel();

            Rrls.LOGGER.error("Caught error loading resourcepacks!", throwable);

            if (ConfigExpectPlatform.doubleLoad().isLoad()) {
                reloadResourcePacks(ConfigExpectPlatform.doubleLoad() == DoubleLoad.FORCE_LOAD)
                        .thenRun(() -> addResourcePackLoadFailToast(errorMessage));
            }
        }
    }

    @Inject(
            method = "clearResourcePacksOnError",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/Minecraft;reloadResourcePacks(Z)Ljava/util/concurrent/CompletableFuture;",
                    shift = At.Shift.BEFORE
            ),
            cancellable = true
    )
    public void rrls$doubleLoad(Throwable throwable, Component errorMessage, CallbackInfo ci) {
        if (!ConfigExpectPlatform.doubleLoad().isLoad()) {
            ci.cancel();
        }
    }

    @ModifyArg(
            method = "clearResourcePacksOnError",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/Minecraft;reloadResourcePacks(Z)Ljava/util/concurrent/CompletableFuture;",
                    ordinal = 0
            ),
            require = 0
    )
    public boolean rrls$doubleLoad(boolean error) { // always true
        return ConfigExpectPlatform.doubleLoad() == DoubleLoad.FORCE_LOAD;
    }

    @WrapOperation(
            method = {
                    "tick",
                    "handleKeybinds",
                    "doWorldLoad"
            },
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/client/Minecraft;overlay:Lnet/minecraft/client/gui/screens/Overlay;"
            )
    )
    public Overlay rrls$miniRender(Minecraft instance, Operation<Overlay> original) {
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
