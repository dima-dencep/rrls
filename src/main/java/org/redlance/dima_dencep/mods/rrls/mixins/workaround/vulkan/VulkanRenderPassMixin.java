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

import org.spongepowered.asm.mixin.Mixin;

@Mixin(targets = "com.mojang.renderpearl.backend.vulkan.VulkanRenderPass")
public class VulkanRenderPassMixin {
    /*@WrapOperation(
            method = "setPipeline",
            at = @At(
                    value = "NEW",
                    target = "(Ljava/lang/String;)Ljava/lang/IllegalArgumentException;"
            )
    )
    public IllegalArgumentException rrls$safeSetPipeline(String s, Operation<IllegalArgumentException> original, @Cancellable CallbackInfo ci) {
        return rrls$handleVulkanThrow("set pipeline", s, original, ci);
    }

    @WrapOperation(
            method = "pushDescriptors",
            at = @At(
                    value = "NEW",
                    target = "(Ljava/lang/String;)Ljava/lang/IllegalStateException;"
            )
    )
    public IllegalStateException rrls$safeDraw(String s, Operation<IllegalStateException> original, @Cancellable CallbackInfo ci) {
        return rrls$handleVulkanThrow("draw", s, original, ci);
    }

    @Unique
    private static <T extends Throwable> T rrls$handleVulkanThrow(String from, String s, Operation<T> original, CallbackInfo ci) {
        T exc = original.call(s);
        if (OverlayHelper.isCurrentRenderingState()) {
            Rrls.LOGGER.warn("Failed to {}!", from, exc);
            ci.cancel();
            return null;
        } else {
            return exc;
        }
    }*/
}
