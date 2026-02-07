/*
 * Copyright 2023 - 2026 dima_dencep.
 *
 * Licensed under the Open Software License, Version 3.0 (the "License");
 * you may not use this file except in compliance with the License.
 *
 * You may obtain a copy of the License at
 *     https://spdx.org/licenses/OSL-3.0.txt
 */

package org.redlance.dima_dencep.mods.rrls.mixins.workaround.textures;

import com.mojang.blaze3d.textures.GpuTexture;
import com.mojang.blaze3d.textures.GpuTextureView;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.MissingTextureAtlasSprite;
import net.minecraft.client.renderer.texture.ReloadableTexture;
import org.redlance.dima_dencep.mods.rrls.utils.TextureUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@SuppressWarnings("ConstantConditions")
@Mixin(AbstractTexture.class)
public class AbstractTextureMixin {
    @Shadow
    protected GpuTexture texture;
    @Shadow
    protected GpuTextureView textureView;

    @Inject(
            method = "getTexture",
            at = @At(
                    value = "HEAD"
            ),
            cancellable = true
    )
    public void rrls$useMissingTexture(CallbackInfoReturnable<GpuTexture> cir) {
        if (this.texture == null && (Object) this instanceof ReloadableTexture reloadable) {
            TextureUtils.reloadTextureSync(Minecraft.getInstance().getTextureManager(), reloadable);
        }

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
        if (this.textureView == null && (Object) this instanceof ReloadableTexture reloadable) {
            TextureUtils.reloadTextureSync(Minecraft.getInstance().getTextureManager(), reloadable);
        }

        if (this.textureView == null) cir.setReturnValue(Minecraft.getInstance().getTextureManager()
                .getTexture(MissingTextureAtlasSprite.getLocation())
                .getTextureView()
        );
    }
}
