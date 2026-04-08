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

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.SpriteLoader;
import net.minecraft.client.resources.model.sprite.AtlasManager;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.Util;
import org.redlance.dima_dencep.mods.rrls.Rrls;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

public class AtlasUtils {
    private static final Map<Identifier, CompletableFuture<SpriteLoader.Preparations>> PENDING = new ConcurrentHashMap<>();

    public static void reloadAtlasSync(AtlasManager atlasManager, ResourceManager resourceManager, AtlasManager.AtlasEntry entry) {
        Rrls.LOGGER.warn("Force-reloading atlas: '{}'!", entry.config().definitionLocation());

        CompletableFuture<SpriteLoader.Preparations> future = reloadAtlas(atlasManager, resourceManager, entry);
        Minecraft.getInstance().managedBlock(future::isDone);
    }

    public static CompletableFuture<SpriteLoader.Preparations> reloadAtlas(AtlasManager atlasManager, ResourceManager resourceManager, AtlasManager.AtlasEntry entry) {
        return PENDING.computeIfAbsent(entry.config().definitionLocation(), _ -> {
            Rrls.LOGGER.info("Scheduling atlas reload: '{}'!", entry.config().definitionLocation());

            return entry.scheduleLoad(resourceManager, Util.backgroundExecutor(), atlasManager.maxMipmapLevels)
                    .thenCompose(preps -> preps.readyForUpload().thenApply(_ -> preps))
                    .thenApplyAsync(preps -> {
                        entry.atlas().upload(preps);
                        PENDING.remove(entry.config().definitionLocation());
                        return preps;
                    }, Minecraft.getInstance())
                    .exceptionally(th -> {
                        Rrls.LOGGER.error("Failed to force-reload atlas '{}'!", entry.config().definitionLocation(), th);
                        PENDING.remove(entry.config().definitionLocation());
                        return null;
                    });
        });
    }
}
