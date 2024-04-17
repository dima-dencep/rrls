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

import com.github.dimadencep.mods.rrls.config.RrlsConfig;
import com.github.dimadencep.mods.rrls.Rrls;
import com.github.dimadencep.mods.rrls.config.enums.DoubleLoad;
import com.github.dimadencep.mods.rrls.utils.OverlayHelper;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import org.jetbrains.annotations.Nullable;
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
    protected abstract CompletableFuture<Void> reloadResourcePacks(boolean bl, @Nullable Minecraft.GameLoadCookie gameLoadCookie);
    @Shadow
    protected abstract void onResourceLoadFinished(@Nullable Minecraft.GameLoadCookie gameLoadCookie);

    @Inject(
            method = "<init>",
            at = @At(
                    value = "RETURN"
            )
    )
    public void rrls$init(GameConfig args, CallbackInfo ci, @Local(ordinal = 0) Minecraft.GameLoadCookie loadingContext) {
        if (RrlsConfig.INSTANCE.forceClose.get())
            onResourceLoadFinished(loadingContext);
    }

    @Inject(
            method = "clearResourcePacksOnError",
            at = @At(
                    value = "HEAD"
            ),
            cancellable = true
    ) // TODO refactor when @WrapMethod
    public void rrls$onResourceReloadFailure(Throwable exception, Component resourceName, Minecraft.GameLoadCookie loadingContext, CallbackInfo ci) {
        if (!RrlsConfig.INSTANCE.resetResources.get()) {
            Rrls.LOGGER.error("Caught error loading resourcepacks!", exception);

            if (RrlsConfig.INSTANCE.doubleLoad.get().isLoad())
                reloadResourcePacks(RrlsConfig.INSTANCE.doubleLoad.get() == DoubleLoad.FORCE_LOAD, loadingContext)
                        .thenRun(() -> addResourcePackLoadFailToast(resourceName));

            ci.cancel();
        }
    }

    @Inject(
            method = "clearResourcePacksOnError",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/Minecraft;reloadResourcePacks(ZLnet/minecraft/client/Minecraft$GameLoadCookie;)Ljava/util/concurrent/CompletableFuture;",
                    shift = At.Shift.BEFORE
            ),
            cancellable = true
    )
    public void rrls$doubleLoad(Throwable exception, Component resourceName, Minecraft.GameLoadCookie loadingContext, CallbackInfo ci) {
        if (!RrlsConfig.INSTANCE.doubleLoad.get().isLoad())
            ci.cancel();
    }

    @ModifyArg(
            method = "clearResourcePacksOnError",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/Minecraft;reloadResourcePacks(ZLnet/minecraft/client/Minecraft$GameLoadCookie;)Ljava/util/concurrent/CompletableFuture;",
                    ordinal = 0
            ),
            require = 0
    )
    public boolean rrls$doubleLoad(boolean force) {
        return RrlsConfig.INSTANCE.doubleLoad.get() == DoubleLoad.FORCE_LOAD;
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
        if (RrlsConfig.INSTANCE.blockOverlay.get() && OverlayHelper.isRenderingState(original))
            return null;

        return original;
    }
}
