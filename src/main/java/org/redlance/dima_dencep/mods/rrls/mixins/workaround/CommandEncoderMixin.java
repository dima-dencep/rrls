/*
 * Copyright 2023 - 2026 dima_dencep.
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
import com.mojang.renderpearl.frontend.FrontendCommandEncoder;
import org.objectweb.asm.Opcodes;
import org.redlance.dima_dencep.mods.rrls.utils.OverlayHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(FrontendCommandEncoder.class)
public class CommandEncoderMixin {
    @WrapOperation(
            method = {
                    "writeToTexture(Lcom/mojang/renderpearl/api/textures/GpuTexture;Lcom/mojang/blaze3d/platform/NativeImage;IIII)V",
                    "writeToTexture(Lcom/mojang/renderpearl/api/textures/GpuTexture;Ljava/nio/ByteBuffer;IIIIII)V",
                    "createRenderPass(Lcom/mojang/renderpearl/api/commands/RenderPassDescriptor;)Lcom/mojang/renderpearl/api/commands/RenderPass;"
            },
            at = @At(
                    value = "FIELD",
                    target = "Lcom/mojang/renderpearl/frontend/FrontendCommandEncoder;isInRenderPass:Z",
                    opcode = Opcodes.GETFIELD
            )
    )
    private boolean rrls$exitRenderPass(FrontendCommandEncoder instance, Operation<Boolean> original) {
        return !OverlayHelper.isCurrentRenderingState() && original.call(instance);
    }
}
