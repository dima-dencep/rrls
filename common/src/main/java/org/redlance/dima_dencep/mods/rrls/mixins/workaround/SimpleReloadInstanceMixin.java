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
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiSpriteManager;
import net.minecraft.client.gui.font.FontManager;
import net.minecraft.client.resources.SplashManager;
import net.minecraft.client.resources.language.LanguageManager;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.util.Unit;
import org.redlance.dima_dencep.mods.rrls.ConfigExpectPlatform;
import org.redlance.dima_dencep.mods.rrls.Rrls;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.function.BiFunction;

@Mixin(targets = "net.minecraft.server.packs.resources.SimpleReloadInstance$1")
public class SimpleReloadInstanceMixin {
    @Shadow
    @Final
    PreparableReloadListener val$listener;

    @WrapOperation(
            method = "wait",
            at = @At(
                    value = "INVOKE",
                    target = "Ljava/util/concurrent/CompletableFuture;thenCombine(Ljava/util/concurrent/CompletionStage;Ljava/util/function/BiFunction;)Ljava/util/concurrent/CompletableFuture;"
            )
    )
    public <U, V> CompletableFuture<V> rrls$async(CompletableFuture<Unit> instance, CompletionStage<? extends U> other, BiFunction<?, ? super U, ? extends V> fn, Operation<CompletableFuture<V>> original) {
        if (ConfigExpectPlatform.hideType().forceClose() && rrls$filterListener(val$listener)) {
            Rrls.LOGGER.info("Skip wait for {}!", val$listener.getName());

            return CompletableFuture.completedFuture(fn.apply(null, null));
        }

        return original.call(instance, other, fn);
    }

    @Unique
    private static boolean rrls$filterListener(PreparableReloadListener listener) {
        if (listener instanceof FontManager fontManager) {
            return !fontManager.fontSets.containsKey(Minecraft.DEFAULT_FONT);
        }

        if (listener instanceof LanguageManager languageManager) {
            return languageManager.getLanguages().size() == 1;
        }

        if (listener instanceof SplashManager splashManager) {
            return splashManager.splashes.isEmpty();
        }

        if (listener instanceof GuiSpriteManager spriteManager) {
            return spriteManager.textureAtlas.texturesByName.isEmpty();
        }

        return false;
    }
}
