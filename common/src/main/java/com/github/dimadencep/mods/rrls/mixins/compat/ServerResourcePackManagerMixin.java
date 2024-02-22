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

import com.github.dimadencep.mods.rrls.ConfigExpectPlatform;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.resource.server.ServerResourcePackManager;
import net.minecraft.network.packet.c2s.common.ResourcePackStatusC2SPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.UUID;

@Mixin(ServerResourcePackManager.class)
public class ServerResourcePackManagerMixin {
    @Inject(
            method = "onAdd",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/resource/server/ServerResourcePackManager;onPackChanged()V"
            )
    )
    public void earlyResourcePackStatusSend(UUID id, ServerResourcePackManager.PackEntry pack, CallbackInfo ci) {
        if (ConfigExpectPlatform.earlyPackStatusSend()) {
            ClientPlayNetworkHandler handler = MinecraftClient.getInstance().getNetworkHandler();

            if (handler != null && handler.getConnection() != null && handler.getConnection().isOpen()) {
                handler.getConnection().send(new ResourcePackStatusC2SPacket(id, ResourcePackStatusC2SPacket.Status.SUCCESSFULLY_LOADED));
            }
        }
    }
}
