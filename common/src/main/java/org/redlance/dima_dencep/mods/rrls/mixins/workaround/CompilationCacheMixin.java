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
import com.llamalad7.mixinextras.sugar.Cancellable;
import net.minecraft.client.renderer.CompiledShaderProgram;
import net.minecraft.client.renderer.ShaderManager;
import net.minecraft.client.renderer.ShaderProgram;
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
    @WrapOperation(
            method = "getOrCompileProgram",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/renderer/ShaderManager$CompilationCache;compileProgram(Lnet/minecraft/client/renderer/ShaderProgram;)Lnet/minecraft/client/renderer/CompiledShaderProgram;"
            )
    )
    private CompiledShaderProgram rrls$supressMissingCache(ShaderManager.CompilationCache instance, ShaderProgram shaderProgram, Operation<CompiledShaderProgram> original, @Cancellable CallbackInfoReturnable<?> cir) {
        CompiledShaderProgram compiledShaderProgram = original.call(instance, shaderProgram);

        if (compiledShaderProgram == null && RrlsConfig.hideType().forceClose()) {
            cir.setReturnValue(null);
        }

        return compiledShaderProgram;
    }

    @Inject(
            method = {
                    "loadPostChain",
                    "compileProgram"
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
