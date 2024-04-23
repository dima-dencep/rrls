/*
 * Copyright 2023 - 2024 dima_dencep.
 *
 * Licensed under the Open Software License, Version 3.0 (the "License");
 * you may not use this file except in compliance with the License.
 *
 * You may obtain a copy of the License at
 *     https://spdx.org/licenses/OSL-3.0.txt
 */

package com.github.dimadencep.mods.rrls.mixins.workaround;

import net.minecraft.client.renderer.ShaderInstance;
import com.mojang.blaze3d.vertex.VertexBuffer;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(VertexBuffer.class)
public class VertexBufferMixin {
    @Inject(
            method = "_drawWithShader",
            at = @At(
                    value = "HEAD"
            ),
            cancellable = true
    )
    public void rrls$workaroundNullShader(Matrix4f viewMatrix, Matrix4f projectionMatrix, ShaderInstance shader, CallbackInfo ci) {
        if (shader == null)
            ci.cancel();
    }
}
