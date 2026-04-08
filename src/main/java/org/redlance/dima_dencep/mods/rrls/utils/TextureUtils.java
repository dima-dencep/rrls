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
import net.minecraft.client.renderer.texture.ReloadableTexture;
import net.minecraft.client.renderer.texture.TextureContents;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.Util;
import org.redlance.dima_dencep.mods.rrls.Rrls;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

public class TextureUtils {
    private static final Map<Identifier, CompletableFuture<TextureContents>> PENDING_TEXTURES = new ConcurrentHashMap<>();

    public static void reloadTextureSync(TextureManager manager, ReloadableTexture texture) {
        Identifier rl = texture.resourceId();
        Rrls.LOGGER.warn("Force-reloading texture: '{}'!", rl);

        CompletableFuture<TextureContents> future = reloadTexture(manager.resourceManager, rl, texture);
        Minecraft.getInstance().managedBlock(future::isDone);
    }

    public static CompletableFuture<TextureContents> reloadTexture(ResourceManager manager, Identifier rl, ReloadableTexture texture) {
        return PENDING_TEXTURES.computeIfAbsent(rl, _ -> reloadTextureInternal(manager, rl, texture));
    }

    private static CompletableFuture<TextureContents> reloadTextureInternal(ResourceManager manager, Identifier rl, ReloadableTexture texture) {
        TextureManager.PendingReload reload = TextureManager.scheduleLoad(manager, rl, texture, Util.backgroundExecutor());
        Rrls.LOGGER.info("Reloading texture '{}'!", rl);

        return reload.newContents().thenApplyAsync(textureContents -> {
            reload.texture().apply(textureContents);
            PENDING_TEXTURES.remove(rl);
            return textureContents;
        }, Minecraft.getInstance()).exceptionally(th -> {
            Rrls.LOGGER.error("Failed to force-reload texture!", th);
            PENDING_TEXTURES.remove(rl);
            return null;
        });
    }
}
