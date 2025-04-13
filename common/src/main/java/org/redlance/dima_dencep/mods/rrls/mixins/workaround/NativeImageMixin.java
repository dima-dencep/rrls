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

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.mojang.blaze3d.platform.NativeImage;
import org.redlance.dima_dencep.mods.rrls.Rrls;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(NativeImage.class)
public class NativeImageMixin {
    @Unique
    private boolean rrls$closed;

    @WrapMethod(
            method = "_upload"
    )
    private void rrls$useClosed(int level, int xOffset, int yOffset, int unpackSkipPixels, int unpackSkipRows, int width, int height, boolean blur, boolean clamp, boolean mipmap, boolean autoClose, Operation<Void> original) {
        try {
            original.call(level, xOffset, yOffset, unpackSkipPixels, unpackSkipRows, width, height, blur, clamp, mipmap, autoClose);
        } catch (Throwable th) {
            if (this.rrls$closed) {
                Rrls.LOGGER.warn("Attempting to upload a closed texture!", th);
            } else {
                throw th;
            }
        }
    }

    @Inject(
            method = "close",
            at = @At(
                    value = "FIELD",
                    target = "Lcom/mojang/blaze3d/platform/NativeImage;useStbFree:Z"
            )
    )
    private void rrls$setClosed(CallbackInfo ci) {
        this.rrls$closed = true;
    }
}
