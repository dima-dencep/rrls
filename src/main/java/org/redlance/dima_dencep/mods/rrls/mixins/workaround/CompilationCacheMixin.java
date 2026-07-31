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
import com.llamalad7.mixinextras.sugar.Cancellable;
import net.minecraft.client.renderer.PostChain;
import net.minecraft.client.renderer.ShaderManager;
import net.minecraft.resources.Identifier;
import org.redlance.dima_dencep.mods.rrls.Rrls;
import org.redlance.dima_dencep.mods.rrls.utils.OverlayHelper;
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
                    target = "Lnet/minecraft/client/renderer/ShaderManager$CompilationCache;loadPostChain(Lnet/minecraft/resources/Identifier;Ljava/util/Set;)Lnet/minecraft/client/renderer/PostChain;"
            )
    )
    private PostChain rrls$suppressMissingCache(ShaderManager.CompilationCache instance, Identifier id, Set<Identifier> allowedTargets, Operation<PostChain> original, @Cancellable CallbackInfoReturnable<?> cir) {
        try {
            return original.call(instance, id, allowedTargets);
        } catch (Exception ex) {
            if (OverlayHelper.isCurrentRenderingState()) {
                Rrls.LOGGER.warn("Failed to compile!", ex);
                cir.setReturnValue(null);
                return null;
            } else {
                throw ex;
            }
        }
    }
}
