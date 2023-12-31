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

import com.github.dimadencep.mods.rrls.Rrls;
import com.github.dimadencep.mods.rrls.config.ModConfig;
import net.minecraft.client.network.ClientCommonNetworkHandler;
import net.minecraft.network.packet.c2s.common.ResourcePackStatusC2SPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.concurrent.CompletableFuture;

@Mixin(ClientCommonNetworkHandler.class)
public abstract class ClientCommonNetworkHandlerMixin {
    @Shadow
    protected abstract void sendResourcePackStatus(ResourcePackStatusC2SPacket.Status status);

    @Inject(
            method = "sendResourcePackStatusAfter",
            at = @At(
                    value = "HEAD"
            ),
            cancellable = true
    )
    public void earlyResourcePackStatusSend(CompletableFuture<?> future, CallbackInfo ci) {
        if (Rrls.MOD_CONFIG.earlyPackStatus.earlySend()) {
            sendResourcePackStatus(ResourcePackStatusC2SPacket.Status.SUCCESSFULLY_LOADED);
        }
        if (Rrls.MOD_CONFIG.earlyPackStatus == ModConfig.PackStatus.SEND_DENY) {
            ci.cancel();
        }
    }
}
