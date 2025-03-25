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

import net.minecraft.client.renderer.ShaderManager;
import org.redlance.dima_dencep.mods.rrls.RrlsConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Used to prevent the game from crashing if rrls failed to load the reloader early.
 */
@Mixin(ShaderManager.CompilationCache.class)
public class CompilationCacheMixin {
    @Inject(
            method = {
                    "loadPostChain"
            },
            at = @At(
                    value = "NEW",
                    target = "(Ljava/lang/String;)Lnet/minecraft/client/renderer/ShaderManager$CompilationException;"
            ),
            cancellable = true
    )
    private void rrls$supressMissingCache(CallbackInfoReturnable<?> cir) {
        if (RrlsConfig.hideType().forceClose()) {
            cir.setReturnValue(null);
        }
    }
}
