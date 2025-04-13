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
import net.minecraft.client.renderer.PostChain;
import net.minecraft.client.renderer.ShaderManager;
import net.minecraft.resources.ResourceLocation;
import org.redlance.dima_dencep.mods.rrls.Rrls;
import org.redlance.dima_dencep.mods.rrls.RrlsConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Set;

/**
 * Used to prevent the game from crashing if rrls failed to load the reloader early.
 */
@Mixin(ShaderManager.CompilationCache.class)
public class CompilationCacheMixin {
    @WrapOperation(
            method = "getOrLoadPostChain",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/renderer/ShaderManager$CompilationCache;loadPostChain(Lnet/minecraft/resources/ResourceLocation;Ljava/util/Set;)Lnet/minecraft/client/renderer/PostChain;"
            )
    )
    private PostChain rrls$supressMissingCache(ShaderManager.CompilationCache instance, ResourceLocation name, Set<ResourceLocation> externalTargets, Operation<PostChain> original, @Cancellable CallbackInfoReturnable<?> cir) {
        PostChain postChain = original.call(instance, name, externalTargets);

        if (postChain == null && RrlsConfig.hideType().forceClose()) {
            cir.setReturnValue(null);
        }

        return postChain;
    }

    @WrapOperation(
            method = {
                    "loadPostChain"
            },
            at = @At(
                    value = "NEW",
                    target = "(Ljava/lang/String;)Lnet/minecraft/client/renderer/ShaderManager$CompilationException;"
            )
    )
    private ShaderManager.CompilationException rrls$supressMissingCache(String s, Operation<ShaderManager.CompilationException> original, @Cancellable CallbackInfoReturnable<Boolean> cir) {
        ShaderManager.CompilationException exc = original.call(s);
        if (RrlsConfig.hideType().forceClose()) {
            Rrls.LOGGER.warn("Failed to compile!", exc);
            cir.setReturnValue(null);
            return null;
        } else {
            return exc;
        }
    }
}
