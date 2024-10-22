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
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.GuiSpriteManager;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.metadata.gui.GuiSpriteScaling;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.function.Function;

@Mixin(GuiGraphics.class)
public abstract class GuiGraphicsMixin {
    @WrapOperation(
            method = {
                    "blitSprite(Ljava/util/function/Function;Lnet/minecraft/resources/ResourceLocation;IIIII)V",
                    "blitSprite(Ljava/util/function/Function;Lnet/minecraft/resources/ResourceLocation;IIIIIIII)V"
            },
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/GuiSpriteManager;getSprite(Lnet/minecraft/resources/ResourceLocation;)Lnet/minecraft/client/renderer/texture/TextureAtlasSprite;"
            )
    )
    public TextureAtlasSprite rrls$fixSpriteCrash(GuiSpriteManager instance, ResourceLocation location, Operation<TextureAtlasSprite> original) {
        try {
            return original.call(instance, location);
        } catch (Throwable th) {
            return null;
        }
    }

    @WrapOperation(
            method = {
                    "blitSprite(Ljava/util/function/Function;Lnet/minecraft/resources/ResourceLocation;IIIII)V",
                    "blitSprite(Ljava/util/function/Function;Lnet/minecraft/resources/ResourceLocation;IIIIIIII)V"
            },
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/GuiSpriteManager;getSpriteScaling(Lnet/minecraft/client/renderer/texture/TextureAtlasSprite;)Lnet/minecraft/client/resources/metadata/gui/GuiSpriteScaling;"
            )
    )
    public GuiSpriteScaling rrls$fixSpriteCrash(GuiSpriteManager instance, TextureAtlasSprite sprite, Operation<GuiSpriteScaling> original) {
        if (sprite == null) {
            return null;
        }

        return original.call(instance, sprite);
    }

    @WrapOperation(
            method = "blitSprite(Ljava/util/function/Function;Lnet/minecraft/resources/ResourceLocation;IIIIIIII)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/GuiGraphics;blitSprite(Ljava/util/function/Function;Lnet/minecraft/client/renderer/texture/TextureAtlasSprite;IIII)V"
            )
    )
    public void rrls$fixSpriteCrash(GuiGraphics instance, Function<ResourceLocation, RenderType> function, TextureAtlasSprite arg, int i, int j, int k, int l, Operation<Void> original) {
        if (arg == null) {
            return;
        }

        original.call(instance, function, arg, i, j, k, l);
    }
}
