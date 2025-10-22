/*
 * Copyright 2023 - 2025 dima_dencep.
 *
 * Licensed under the Open Software License, Version 3.0 (the "License");
 * you may not use this file except in compliance with the License.
 *
 * You may obtain a copy of the License at
 *     https://spdx.org/licenses/OSL-3.0.txt
 */

package org.redlance.dima_dencep.mods.rrls.utils;

import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.ReloadableTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import org.redlance.dima_dencep.mods.rrls.Rrls;

public class TextureUtils {
    public static void reloadTexture(ResourceManager manager, ResourceLocation rl, ReloadableTexture texture) {
        TextureManager.PendingReload reload = TextureManager.scheduleLoad(manager, rl, texture, Util.backgroundExecutor());
        Rrls.LOGGER.info("Reloading texture '{}'!", rl);

        reload.newContents().thenAcceptAsync(textureContents -> reload.texture()
                .apply(textureContents),Minecraft.getInstance());
    }
}
