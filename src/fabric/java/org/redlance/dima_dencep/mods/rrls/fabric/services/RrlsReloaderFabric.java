/*
 * Copyright 2023 - 2026 dima_dencep.
 *
 * Licensed under the Open Software License, Version 3.0 (the "License");
 * you may not use this file except in compliance with the License.
 *
 * You may obtain a copy of the License at
 *     https://spdx.org/licenses/OSL-3.0.txt
 */

package org.redlance.dima_dencep.mods.rrls.fabric.services;

import net.fabricmc.fabric.api.resource.v1.ResourceLoader;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.api.metadata.CustomValue;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.packs.resources.ResourceManager;
import org.redlance.dima_dencep.mods.rrls.Rrls;
import org.redlance.dima_dencep.mods.rrls.mixins.accessors.ResourceLoaderImplAccessor;
import org.redlance.dima_dencep.mods.rrls.services.RrlsReloaderService;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

public class RrlsReloaderFabric implements RrlsReloaderService {
    private static final Set<Identifier> FORCE_LOAD_IDENTIFIERS = new HashSet<>(1);

    static {
        FabricLoader.getInstance().getAllMods().forEach(RrlsReloaderFabric::parseModMetadata);
        Rrls.LOGGER.info("Forced early reloaders: {}", FORCE_LOAD_IDENTIFIERS);
    }

    @Override
    public void collectEarlyReloaders(ResourceManager manager, Consumer<PreparableReloadListener> consumer) {
        if (FORCE_LOAD_IDENTIFIERS.isEmpty()) return;

        if (ResourceLoader.get(PackType.CLIENT_RESOURCES) instanceof ResourceLoaderImplAccessor accessor) {
            Map<Identifier, PreparableReloadListener> reloaders = accessor.getAddedReloaders();

            for (Identifier id : FORCE_LOAD_IDENTIFIERS) {
                PreparableReloadListener listener = reloaders.get(id);
                if (listener != null) consumer.accept(listener);
            }
        }
    }

    @Override
    public boolean isServiceActive() {
        try {
            Class.forName("net.fabricmc.fabric.api.resource.v1.ResourceLoader");
            Class.forName("org.redlance.dima_dencep.mods.rrls.mixins.accessors.ResourceLoaderImplAccessor");
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    private static void parseModMetadata(ModContainer mod) {
        if (!mod.getMetadata().containsCustomValue(Rrls.MOD_ID)) return;

        try {
            CustomValue.CvObject rrlsObj = mod.getMetadata().getCustomValue(Rrls.MOD_ID).getAsObject();
            if (rrlsObj.containsKey("force_load")) {
                for (CustomValue id : rrlsObj.get("force_load").getAsArray()) {
                    RrlsReloaderFabric.FORCE_LOAD_IDENTIFIERS.add(Identifier.parse(id.getAsString()));
                }
            }
        } catch (Exception ex) {
            Rrls.LOGGER.warn("Failed to read rrls metadata from mod {}", mod.getMetadata().getId(), ex);
        }
    }
}
