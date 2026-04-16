/*
 * Copyright 2023 - 2026 dima_dencep.
 *
 * Licensed under the Open Software License, Version 3.0 (the "License");
 * you may not use this file except in compliance with the License.
 *
 * You may obtain a copy of the License at
 *     https://spdx.org/licenses/OSL-3.0.txt
 */

package org.redlance.dima_dencep.mods.rrls.neoforge.services;

import com.electronwill.nightconfig.core.CommentedConfig;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.packs.resources.ReloadableResourceManager;
import net.minecraft.server.packs.resources.ResourceManager;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModList;
import net.neoforged.fml.loading.FMLLoader;
import net.neoforged.fml.loading.moddiscovery.ModInfo;
import net.neoforged.neoforge.client.event.AddClientReloadListenersEvent;
import org.redlance.dima_dencep.mods.rrls.Rrls;
import org.redlance.dima_dencep.mods.rrls.services.RrlsReloaderService;

import java.util.*;
import java.util.function.Consumer;

public class RrlsReloaderNeo implements RrlsReloaderService {
    private static final Map<String, Set<Identifier>> FORCE_LOAD_IDENTIFIERS = new HashMap<>(1);

    static {
        FMLLoader.getCurrent().getLoadingModList().getMods().forEach(RrlsReloaderNeo::parseModMetadata);
        Rrls.LOGGER.info("Forced early reloaders: {}", FORCE_LOAD_IDENTIFIERS);
    }

    @Override
    @SuppressWarnings("UnstableApiUsage")
    public void collectEarlyReloaders(ResourceManager manager, Consumer<PreparableReloadListener> consumer) {
        if (FORCE_LOAD_IDENTIFIERS.isEmpty()) return;

        for (Map.Entry<String, Set<Identifier>> reload : FORCE_LOAD_IDENTIFIERS.entrySet()) {
            AddClientReloadListenersEvent rlEvent = new AddClientReloadListenersEvent((ReloadableResourceManager) manager);

            try {
                ModContainer container = ModList.get().getModContainerById(reload.getKey()).orElseThrow();
                container.acceptEvent(rlEvent);

                for (Identifier id : reload.getValue()) {
                    PreparableReloadListener listener = rlEvent.getRegistry().get(id);
                    if (listener != null) consumer.accept(listener);
                }
            } catch (Exception ex) {
                Rrls.LOGGER.warn("Failed to collect early listeners on neoforge!", ex);
            }
        }
    }

    @Override
    public boolean isServiceActive() {
        try {
            Class.forName("net.neoforged.fml.loading.moddiscovery.ModInfo");
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    private static void parseModMetadata(ModInfo mod) {
        if (!mod.getModProperties().containsKey(Rrls.MOD_ID)) return;

        try {
            CommentedConfig rrlsObj = ((CommentedConfig) mod.getModProperties().get(Rrls.MOD_ID));

            if (rrlsObj.contains("force_load")) {
                for (String id : rrlsObj.<List<String>>get("force_load")) {
                    RrlsReloaderNeo.FORCE_LOAD_IDENTIFIERS.computeIfAbsent(
                            mod.getModId(), _ -> new HashSet<>(1)
                    ).add(Identifier.parse(id));
                }
            }
        } catch (Exception ex) {
            Rrls.LOGGER.warn("Failed to read rrls metadata from mod {}", mod.getModId(), ex);
        }
    }
}
