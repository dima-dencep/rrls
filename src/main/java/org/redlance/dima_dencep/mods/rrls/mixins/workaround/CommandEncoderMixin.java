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
import com.mojang.blaze3d.systems.CommandEncoder;
import org.objectweb.asm.Opcodes;
import org.redlance.dima_dencep.mods.rrls.utils.OverlayHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(CommandEncoder.class)
public class CommandEncoderMixin {
    @WrapOperation(
            method = {
                    "writeToTexture(Lcom/mojang/blaze3d/textures/GpuTexture;Lcom/mojang/blaze3d/platform/NativeImage;IIII)V",
                    "writeToTexture(Lcom/mojang/blaze3d/textures/GpuTexture;Ljava/nio/ByteBuffer;IIIIII)V",
                    "createRenderPass(Lcom/mojang/blaze3d/systems/RenderPassDescriptor;)Lcom/mojang/blaze3d/systems/RenderPass;"
            },
            at = @At(
                    value = "FIELD",
                    target = "Lcom/mojang/blaze3d/systems/CommandEncoder;isInRenderPass:Z",
                    opcode = Opcodes.GETFIELD
            )
    )
    private boolean rrls$exitRenderPass(CommandEncoder instance, Operation<Boolean> original) {
        return !OverlayHelper.isCurrentRenderingState() && original.call(instance);
    }
}
