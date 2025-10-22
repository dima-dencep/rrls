/*
 * Copyright 2023 - 2025 dima_dencep.
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
import com.llamalad7.mixinextras.sugar.Cancellable;
import net.minecraft.CrashReport;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import org.redlance.dima_dencep.mods.rrls.Rrls;
import org.redlance.dima_dencep.mods.rrls.utils.OverlayHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * For mods that render entities in the title screen (other than essential)
 */
@Mixin(EntityRenderDispatcher.class)
public class EntityRenderDispatcherMixin {
    @Unique
    private static final Minecraft RRLS$MINECRAFT = Minecraft.getInstance();

    @WrapOperation(
            method = "submit",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/renderer/entity/EntityRenderDispatcher;getRenderer(Lnet/minecraft/client/renderer/entity/state/EntityRenderState;)Lnet/minecraft/client/renderer/entity/EntityRenderer;"
            )
    )
    public EntityRenderer<?, ?> rrls$workaroundEntityCrash(EntityRenderDispatcher instance, EntityRenderState renderState, Operation<EntityRenderer<?, ?>> original) {
        try {
            return original.call(instance, renderState);
        } catch (Throwable th) {
            if (OverlayHelper.isCurrentRenderingState() && RRLS$MINECRAFT.level == null) {
                return null;
            }

            throw th;
        }
    }

    @WrapOperation(
            method = {
                    "submit"
            },
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/CrashReport;forThrowable(Ljava/lang/Throwable;Ljava/lang/String;)Lnet/minecraft/CrashReport;"
            )
    )
    public CrashReport rrls$workaroundEntityCrash(Throwable crashreport, String reportedexception, Operation<CrashReport> original, @Cancellable CallbackInfo ci) {
        if (OverlayHelper.isCurrentRenderingState() && RRLS$MINECRAFT.level == null) {
            Rrls.LOGGER.warn("Preventing: {}", reportedexception, crashreport);
            ci.cancel();
            return null;
        }
        return original.call(crashreport, reportedexception);
    }
}
