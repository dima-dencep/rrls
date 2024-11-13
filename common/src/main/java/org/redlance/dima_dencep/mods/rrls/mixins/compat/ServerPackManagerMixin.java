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

import org.redlance.dima_dencep.mods.rrls.RrlsConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.UUID;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.resources.server.ServerPackManager;
import net.minecraft.network.protocol.common.ServerboundResourcePackPacket;

@Mixin(ServerPackManager.class)
public class ServerPackManagerMixin {
    @Inject(
            method = "pushNewPack",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/resources/server/ServerPackManager;registerForUpdate()V"
            )
    )
    public void earlyResourcePackStatusSend(UUID id, ServerPackManager.ServerPackData packData, CallbackInfo ci) {
        if (RrlsConfig.earlyPackStatusSend()) {
            ClientPacketListener handler = Minecraft.getInstance().getConnection();

            if (handler != null && handler.getConnection() != null && handler.getConnection().isConnected()) {
                handler.getConnection().send(new ServerboundResourcePackPacket(id, ServerboundResourcePackPacket.Action.SUCCESSFULLY_LOADED));
            }
        }
    }
}
