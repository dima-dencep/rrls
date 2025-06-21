/*
 * Copyright 2023 - 2025 dima_dencep.
 *
 * Licensed under the Open Software License, Version 3.0 (the "License");
 * you may not use this file except in compliance with the License.
 *
 * You may obtain a copy of the License at
 *     https://spdx.org/licenses/OSL-3.0.txt
 */

package org.redlance.dima_dencep.mods.rrls.mixins.workaround.textures;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Cancellable;
import com.mojang.blaze3d.textures.GpuTexture;
import com.mojang.blaze3d.textures.GpuTextureView;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.MissingTextureAtlasSprite;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * I could probably upload the texture right here,
 * but I'm too lazy to figure out what mojang did with the renderer
 */
@Mixin(AbstractTexture.class)
public class AbstractTextureMixin {
    @Shadow
    protected GpuTexture texture;
    @Shadow
    protected GpuTextureView textureView;

    @WrapOperation(
            method = {
                    "setClamp",
                    "setFilter",
                    "setUseMipmaps"
            },
            at = @At(
                    value = "NEW",
                    target = "(Ljava/lang/String;)Ljava/lang/IllegalStateException;"
            )
    )
    private IllegalStateException rrls$supress(String s, Operation<IllegalStateException> original, @Cancellable CallbackInfo ci) {
        ci.cancel();
        return null;
    }

    @Inject(
            method = "getTexture",
            at = @At(
                    value = "HEAD"
            ),
            cancellable = true
    )
    public void rrls$useMissingTexture(CallbackInfoReturnable<GpuTexture> cir) {
        if (this.texture == null) cir.setReturnValue(Minecraft.getInstance().getTextureManager()
                .getTexture(MissingTextureAtlasSprite.getLocation())
                .getTexture()
        );
    }

    @Inject(
            method = "getTextureView",
            at = @At(
                    value = "HEAD"
            ),
            cancellable = true
    )
    public void rrls$useMissingTextureView(CallbackInfoReturnable<GpuTextureView> cir) {
        if (this.textureView == null) cir.setReturnValue(Minecraft.getInstance().getTextureManager()
                .getTexture(MissingTextureAtlasSprite.getLocation())
                .getTextureView()
        );
    }
}
