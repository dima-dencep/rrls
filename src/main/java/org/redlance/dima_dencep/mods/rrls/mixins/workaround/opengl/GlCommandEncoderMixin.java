/*
 * Copyright 2023 - 2026 dima_dencep.
 *
 * Licensed under the Open Software License, Version 3.0 (the "License");
 * you may not use this file except in compliance with the License.
 *
 * You may obtain a copy of the License at
 *     https://spdx.org/licenses/OSL-3.0.txt
 */

package org.redlance.dima_dencep.mods.rrls.mixins.workaround.opengl;

import org.spongepowered.asm.mixin.Mixin;

@Mixin(targets = "com.mojang.renderpearl.backend.opengl.GlCommandEncoder")
public class GlCommandEncoderMixin {
    /*@WrapOperation(
            method = "trySetup",
            at = @At(
                    value = "NEW",
                    target = "(Ljava/lang/String;)Ljava/lang/IllegalStateException;"
            )
    )
    public IllegalStateException rrls$safeSetup(String s, Operation<IllegalStateException> original, @Cancellable CallbackInfoReturnable<Boolean> cir) {
        IllegalStateException exc = original.call(s);
        if (OverlayHelper.isCurrentRenderingState()) {
            Rrls.LOGGER.warn("Failed to setup!", exc);
            cir.setReturnValue(false);

            return null;
        } else {
            return exc;
        }
    }*/
}
