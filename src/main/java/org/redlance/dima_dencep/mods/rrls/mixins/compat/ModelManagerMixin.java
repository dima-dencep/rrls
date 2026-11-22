/*
 * Copyright 2023 - 2026 dima_dencep.
 *
 * Licensed under the Open Software License, Version 3.0 (the "License");
 * you may not use this file except in compliance with the License.
 *
 * You may obtain a copy of the License at
 *     https://spdx.org/licenses/OSL-3.0.txt
 */

package org.redlance.dima_dencep.mods.rrls.mixins.compat;

import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.renderer.PlayerSkinRenderCache;
import net.minecraft.client.renderer.block.BlockModelSet;
import net.minecraft.client.renderer.block.BlockStateModelSet;
import net.minecraft.client.renderer.block.FluidStateModelSet;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.client.resources.model.sprite.AtlasManager;
import org.jspecify.annotations.Nullable;
import org.redlance.dima_dencep.mods.rrls.utils.EmptyModel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Collections;

@Mixin(ModelManager.class)
public class ModelManagerMixin {
    @Shadow
    private @Nullable BlockStateModelSet blockStateModelSet;
    @Shadow
    private @Nullable BlockModelSet blockModelSet;
    @Shadow
    private @Nullable FluidStateModelSet fluidStateModelSet;

    @Inject(method = "<init>", at = @At("TAIL"))
    private void rrls$setEmptySets(BlockColors blockColors, AtlasManager atlasManager, PlayerSkinRenderCache playerSkinRenderCache, CallbackInfo ci) {
        this.blockStateModelSet = new BlockStateModelSet(Collections.emptyMap(), EmptyModel.EMPTY_BLOCK_STATE_MODEL);
        this.blockModelSet = new BlockModelSet(this.blockStateModelSet, Collections.emptyMap(), new BlockColors());
        this.fluidStateModelSet = new FluidStateModelSet(Collections.emptyMap(), EmptyModel.EMPTY_FLUID_MODEL);
    }
}
