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
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.Util;
import org.redlance.dima_dencep.mods.rrls.Rrls;

public class TextureUtils {
    public static void reloadTextureSync(TextureManager manager, ReloadableTexture texture) {
        Rrls.LOGGER.warn("Force-reloading texture: '{}'!", texture.resourceId());

        try {
            texture.apply(manager.loadContentsSafe(texture.resourceId(), texture));
        } catch (Throwable th) {
            exceptionally(th);
        }
    }

    public static void reloadTexture(ResourceManager manager, Identifier rl, ReloadableTexture texture) {
        TextureManager.PendingReload reload = TextureManager.scheduleLoad(manager, rl, texture, Util.backgroundExecutor());
        Rrls.LOGGER.info("Reloading texture '{}'!", rl);

        reload.newContents().thenAcceptAsync(textureContents ->
                reload.texture().apply(textureContents), Minecraft.getInstance()
        ).exceptionally(TextureUtils::exceptionally);
    }

    private static Void exceptionally(Throwable th) {
        Rrls.LOGGER.error("Failed to force-reload texture!", th);
        return null;
    }
}
