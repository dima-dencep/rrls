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

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.GuiSpriteManager;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.metadata.gui.GuiSpriteScaling;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(GuiGraphics.class)
public abstract class DrawContentMixin {
    @WrapOperation(
            method = "*",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/texture/GuiAtlasManager;getSprite(Lnet/minecraft/util/Identifier;)Lnet/minecraft/client/texture/Sprite;"
            )
    )
    public TextureAtlasSprite rrls$fixSpriteCrash(GuiSpriteManager instance, ResourceLocation objectId, Operation<TextureAtlasSprite> original) {
        try {
            return original.call(instance, objectId);
        } catch (Throwable th) {
            return null;
        }
    }

    @WrapOperation(
            method = "*",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/texture/GuiAtlasManager;getScaling(Lnet/minecraft/client/texture/Sprite;)Lnet/minecraft/client/texture/Scaling;"
            )
    )
    public GuiSpriteScaling rrls$fixSpriteCrash(GuiSpriteManager instance, TextureAtlasSprite sprite, Operation<GuiSpriteScaling> original) {
        if (sprite == null)
            return null;

        return original.call(instance, sprite);
    }

    @WrapOperation(
            method = "drawGuiTexture(Lnet/minecraft/util/Identifier;IIIIIIIII)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/DrawContext;drawSprite(Lnet/minecraft/client/texture/Sprite;IIIII)V"
            )
    )
    public void rrls$fixSpriteCrash(GuiGraphics instance, TextureAtlasSprite sprite, int x, int y, int z, int width, int height, Operation<Void> original) {
        if (sprite == null)
            return;

        original.call(instance, sprite, x, y, z, width, height);
    }
}
