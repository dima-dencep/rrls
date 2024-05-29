/*
 * Copyright 2023 - 2024 dima_dencep.
 *
 * Licensed under the Open Software License, Version 3.0 (the "License");
 * you may not use this file except in compliance with the License.
 *
 * You may obtain a copy of the License at
 *     https://spdx.org/licenses/OSL-3.0.txt
 */

package org.redlance.dima_dencep.mods.rrls.mixins.compat;

import net.minecraft.network.protocol.game.ClientboundResourcePackPacket;
import net.minecraft.network.protocol.game.ServerboundResourcePackPacket;
import org.redlance.dima_dencep.mods.rrls.ConfigExpectPlatform;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.multiplayer.ClientPacketListener;

@Mixin(ClientPacketListener.class)
public abstract class ServerPackManagerMixin {
    @Shadow
    protected abstract void send(ServerboundResourcePackPacket.Action action);

    @Inject(
            method = "handleResourcePack",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/multiplayer/ClientPacketListener;downloadCallback(Ljava/util/concurrent/CompletableFuture;)V"
            )
    )
    public void earlyResourcePackStatusSend(ClientboundResourcePackPacket packet, CallbackInfo ci) {
        if (ConfigExpectPlatform.earlyPackStatusSend()) {
            send(ServerboundResourcePackPacket.Action.SUCCESSFULLY_LOADED);
        }
    }
}
