/*
 * Copyright 2023 - 2025 dima_dencep.
 *
 * Licensed under the Open Software License, Version 3.0 (the "License");
 * you may not use this file except in compliance with the License.
 *
 * You may obtain a copy of the License at
 *     https://spdx.org/licenses/OSL-3.0.txt
 */

package org.redlance.dima_dencep.mods.rrls.mixins.compat;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.renderer.CubeMap;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.ReloadableTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.resources.Identifier;
import org.redlance.dima_dencep.mods.rrls.utils.OverlayHelper;
import org.redlance.dima_dencep.mods.rrls.utils.TextureUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(CubeMap.class)
public class CubeMapMixin {
    @WrapOperation(
            method = "registerTextures",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/renderer/texture/TextureManager;register(Lnet/minecraft/resources/Identifier;Lnet/minecraft/client/renderer/texture/AbstractTexture;)V"
            )
    )
    public void rrls$earlyRegister(TextureManager instance, Identifier path, AbstractTexture texture, Operation<Void> original) {
        original.call(instance, path, texture);

        if (OverlayHelper.isCurrentRenderingState() && texture instanceof ReloadableTexture reloadableTexture) {
            TextureUtils.reloadTexture(instance.resourceManager, path, reloadableTexture);
        }
    }
}
