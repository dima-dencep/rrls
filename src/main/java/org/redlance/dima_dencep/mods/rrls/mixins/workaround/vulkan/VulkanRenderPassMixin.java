/*
 * Copyright 2023 - 2026 dima_dencep.
 *
 * Licensed under the Open Software License, Version 3.0 (the "License");
 * you may not use this file except in compliance with the License.
 *
 * You may obtain a copy of the License at
 *     https://spdx.org/licenses/OSL-3.0.txt
 */

package org.redlance.dima_dencep.mods.rrls.mixins.workaround.vulkan;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Cancellable;
import com.mojang.blaze3d.vulkan.VulkanRenderPass;
import org.redlance.dima_dencep.mods.rrls.Rrls;
import org.redlance.dima_dencep.mods.rrls.utils.OverlayHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(VulkanRenderPass.class)
public class VulkanRenderPassMixin {
    @WrapOperation(
            method = "setPipeline",
            at = @At(
                    value = "NEW",
                    target = "(Ljava/lang/String;)Ljava/lang/IllegalStateException;"
            )
    )
    public IllegalStateException rrls$safeSetPipeline(String s, Operation<IllegalStateException> original, @Cancellable CallbackInfo ci) {
        return rrls$handleVulkanThrow("set pipeline", s, original, ci);
    }

    @WrapOperation(
            method = {"drawIndexed", "multiDrawIndexed(Ljava/nio/IntBuffer;III)V"},
            at = @At(
                    value = "NEW",
                    target = "(Ljava/lang/String;)Ljava/lang/IllegalStateException;"
            )
    )
    public IllegalStateException rrls$safeDraw(String s, Operation<IllegalStateException> original, @Cancellable CallbackInfo ci) {
        return rrls$handleVulkanThrow("draw", s, original, ci);
    }

    @Unique
    private static IllegalStateException rrls$handleVulkanThrow(String from, String s, Operation<IllegalStateException> original, @Cancellable CallbackInfo ci) {
        IllegalStateException exc = original.call(s);
        if (OverlayHelper.isCurrentRenderingState()) {
            Rrls.LOGGER.warn("Failed to {}!", from, exc);
            ci.cancel();
            return null;
        } else {
            return exc;
        }
    }
}
