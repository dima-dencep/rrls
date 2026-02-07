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

import org.redlance.dima_dencep.mods.rrls.RrlsConfig;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import org.redlance.dima_dencep.mods.rrls.config.DoubleLoad;
import org.redlance.dima_dencep.mods.rrls.Rrls;
import org.redlance.dima_dencep.mods.rrls.utils.OverlayHelper;
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
    @Shadow
    private boolean gameLoadFinished;
    @Shadow
    @Nullable
    public Overlay overlay;

    @Inject(
            method = "<init>",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/mojang/jtracy/TracyClient;isAvailable()Z",
                    shift = At.Shift.AFTER,
                    remap = false
            )
    )
    public void rrls$init(GameConfig gameConfig, CallbackInfo ci, @Local(ordinal = 0) Minecraft.GameLoadCookie gameLoadCookie) {
        if (!OverlayHelper.isCurrentRenderingState()) return;

        try {
            onResourceLoadFinished(gameLoadCookie);
        } catch (Throwable th) {
            Rrls.LOGGER.error("Failed to complete load early!", th);
            this.gameLoadFinished = false;
        }
    }

    @WrapWithCondition(
            method = "onGameLoadFinished",
            at = @At(
                    value = "INVOKE",
                    target = "Ljava/lang/Runnable;run()V"
            )
    )
    public boolean rrls$fixDH(Runnable instance) {
        instance.run(); // Forbid DH from redirecting the method.
        return false;
    }

    @Inject(
            method = "clearResourcePacksOnError",
            at = @At(
                    value = "HEAD"
            ),
            cancellable = true
    )
    public void rrls$onResourceReloadFailure(Throwable throwable, Component errorMessage, Minecraft.GameLoadCookie gameLoadCookie, CallbackInfo ci) {
        if (!RrlsConfig.INSTANCE.resetResources()) {
            ci.cancel();

            Rrls.LOGGER.error("Caught error loading resource packs!", throwable);

            if (RrlsConfig.INSTANCE.doubleLoad().isLoad()) {
                reloadResourcePacks(RrlsConfig.INSTANCE.doubleLoad() == DoubleLoad.FORCE_LOAD, gameLoadCookie)
                        .thenRun(() -> addResourcePackLoadFailToast(errorMessage));
            }
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
    public void rrls$doubleLoad(Throwable throwable, Component errorMessage, Minecraft.GameLoadCookie gameLoadCookie, CallbackInfo ci) {
        if (!RrlsConfig.INSTANCE.doubleLoad().isLoad()) {
            ci.cancel();
        }
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
    public boolean rrls$doubleLoad(boolean error) { // always true
        return RrlsConfig.INSTANCE.doubleLoad() == DoubleLoad.FORCE_LOAD;
    }

    @WrapOperation(
            method = "handleKeybinds",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/client/Minecraft;overlay:Lnet/minecraft/client/gui/screens/Overlay;"
            )
    )
    public Overlay rrls$miniRender(Minecraft instance, Operation<Overlay> original) {
        Overlay overlay = original.call(instance);
        if (OverlayHelper.isRenderingState(overlay)) return null;
        return overlay;
    }

    @WrapOperation(
            method = "tick",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/client/Minecraft;overlay:Lnet/minecraft/client/gui/screens/Overlay;",
                    ordinal = 2
            )
    )
    public Overlay rrls$miniRenderTick(Minecraft instance, Operation<Overlay> original) {
        return rrls$miniRender(instance, original);
    }

    @WrapOperation(
            method = "doWorldLoad",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/client/Minecraft;overlay:Lnet/minecraft/client/gui/screens/Overlay;",
                    ordinal = 0
            )
    )
    public Overlay rrls$miniRenderWorldLoad(Minecraft instance, Operation<Overlay> original) {
        return rrls$miniRender(instance, original);
    }

    @ModifyReturnValue(
            method = "getOverlay",
            at = @At(
                    value = "RETURN"
            )
    )
    public Overlay rrls$blockOverlay(Overlay original) {
        if (RrlsConfig.INSTANCE.blockOverlay() && OverlayHelper.isRenderingState(original)) return null;
        return original;
    }

    @WrapOperation(
            method = "setScreenAndShow",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/Minecraft;renderFrame(ZZ)V"
            )
    )
    public void rrls$removeTick(Minecraft instance, boolean recordGpuUtilization, boolean renderLevel, Operation<Void> original) {
        if (!OverlayHelper.isRenderingState(overlay)) original.call(instance, recordGpuUtilization, renderLevel);
    }
}
