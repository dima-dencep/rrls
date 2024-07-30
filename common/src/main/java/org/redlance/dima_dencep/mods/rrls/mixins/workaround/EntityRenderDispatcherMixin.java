/*
 * Copyright 2023 - 2024 dima_dencep.
 *
 * Licensed under the Open Software License, Version 3.0 (the "License");
 * you may not use this file except in compliance with the License.
 *
 * You may obtain a copy of the License at
 *     https://spdx.org/licenses/OSL-3.0.txt
 */

package org.redlance.dima_dencep.mods.rrls.mixins.workaround;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.world.entity.Entity;
import org.redlance.dima_dencep.mods.rrls.ConfigExpectPlatform;
import org.redlance.dima_dencep.mods.rrls.Rrls;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityRenderDispatcher.class)
public class EntityRenderDispatcherMixin {
    @Unique
    private static final Minecraft RRLS$MINECRAFT = Minecraft.getInstance();

    @WrapOperation(
            method = "render",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/renderer/entity/EntityRenderDispatcher;getRenderer(Lnet/minecraft/world/entity/Entity;)Lnet/minecraft/client/renderer/entity/EntityRenderer;"
            )
    )
    public <T extends Entity> EntityRenderer<? super T> rrls$workaroundEntityCrash(EntityRenderDispatcher instance, T entityrenderer, Operation<EntityRenderer<? super T>> original) {
        try {
            return original.call(instance, entityrenderer);
        } catch (Throwable th) {
            if (ConfigExpectPlatform.hideType().forceClose() && RRLS$MINECRAFT.level == null) {
                return null;
            }

            throw th;
        }
    }

    @Inject(
            method = "render",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/CrashReport;forThrowable(Ljava/lang/Throwable;Ljava/lang/String;)Lnet/minecraft/CrashReport;"
            ),
            cancellable = true
    )
    public <E extends Entity> void rrls$workaroundEntityCrash(E entity, double x, double y, double z, float rotationYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight, CallbackInfo ci) {
        if (ConfigExpectPlatform.hideType().forceClose() && RRLS$MINECRAFT.level == null) {
            Rrls.LOGGER.warn("Preverting entity ({}) crash.", entity);

            ci.cancel();
        }
    }
}
