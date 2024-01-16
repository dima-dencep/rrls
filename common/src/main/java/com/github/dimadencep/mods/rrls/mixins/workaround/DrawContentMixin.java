/*
 * Copyright 2023 dima_dencep.
 *
 * Licensed under the Open Software License, Version 3.0 (the "License");
 * you may not use this file except in compliance with the License.
 *
 * You may obtain a copy of the License at
 *     https://github.com/dima-dencep/rrls/blob/HEAD/LICENSE
 */

package com.github.dimadencep.mods.rrls.mixins.workaround;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.texture.GuiAtlasManager;
import net.minecraft.client.texture.Scaling;
import net.minecraft.client.texture.Sprite;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(DrawContext.class)
public abstract class DrawContentMixin {
    @WrapOperation(
            method = "*",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/texture/GuiAtlasManager;getSprite(Lnet/minecraft/util/Identifier;)Lnet/minecraft/client/texture/Sprite;"
            )
    )
    public Sprite rrls$fixSpriteCrash(GuiAtlasManager instance, Identifier objectId, Operation<Sprite> original) {
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
    public Scaling rrls$fixSpriteCrash(GuiAtlasManager instance, Sprite sprite, Operation<Scaling> original) {
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
    public void rrls$fixSpriteCrash(DrawContext instance, Sprite sprite, int x, int y, int z, int width, int height, Operation<Void> original) {
        if (sprite == null)
            return;

        original.call(instance, sprite, x, y, z, width, height);
    }
}
