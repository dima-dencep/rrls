/*
 * Copyright 2023 - 2026 dima_dencep.
 *
 * Licensed under the Open Software License, Version 3.0 (the "License");
 * you may not use this file except in compliance with the License.
 *
 * You may obtain a copy of the License at
 *     https://spdx.org/licenses/OSL-3.0.txt
 */

package org.redlance.dima_dencep.mods.rrls.utils;

import net.minecraft.client.renderer.block.FluidModel;
import net.minecraft.client.renderer.block.dispatch.BlockStateModel;
import net.minecraft.client.renderer.block.dispatch.BlockStateModelPart;
import net.minecraft.client.renderer.chunk.ChunkSectionLayer;
import net.minecraft.client.resources.model.geometry.BakedQuad;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.util.RandomSource;
import org.jspecify.annotations.NonNull;

import java.util.List;

public final class EmptyModel implements BlockStateModel {
    private static final Material.Baked EMPTY_MATERIAL = new Material.Baked(null, false);

    public static final FluidModel EMPTY_FLUID_MODEL = new FluidModel(ChunkSectionLayer.TRANSLUCENT, EMPTY_MATERIAL, EMPTY_MATERIAL, EMPTY_MATERIAL, null);
    public static final EmptyModel EMPTY_BLOCK_STATE_MODEL = new EmptyModel();

    private EmptyModel() {
        // no-op
    }

    @Override
    public void collectParts(@NonNull RandomSource random, @NonNull List<BlockStateModelPart> output) {

    }

    @Override
    public Material.@NonNull Baked particleMaterial() {
        return EMPTY_MATERIAL;
    }

    @Override
    public @BakedQuad.MaterialFlags int materialFlags() {
        return 0;
    }
}
