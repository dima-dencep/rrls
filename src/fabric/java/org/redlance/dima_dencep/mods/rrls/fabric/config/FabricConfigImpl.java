/*
 * Copyright 2023 - 2026 dima_dencep.
 *
 * Licensed under the Open Software License, Version 3.0 (the "License");
 * you may not use this file except in compliance with the License.
 *
 * You may obtain a copy of the License at
 *     https://spdx.org/licenses/OSL-3.0.txt
 */

package org.redlance.dima_dencep.mods.rrls.fabric.config;

import fuzs.forgeconfigapiport.fabric.api.v5.client.ConfigScreenFactoryRegistry;
import net.fabricmc.loader.api.FabricLoader;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import org.redlance.dima_dencep.mods.rrls.Rrls;
import org.redlance.dima_dencep.mods.rrls.config.impl.ForgeLikeConfigImpl;
import org.redlance.dima_dencep.mods.rrls.screens.ConfigurationSectionScreenProxy;

public class FabricConfigImpl extends ForgeLikeConfigImpl {
    public FabricConfigImpl() {}

    @Override
    public boolean isActive() {
        return FabricLoader.getInstance().isModLoaded("forgeconfigapiport");
    }

    public void registerConfigScreen() {
        ConfigScreenFactoryRegistry.INSTANCE.register(Rrls.MOD_ID, (modContainer, screen) ->
                new ConfigurationScreen(modContainer, screen, ConfigurationSectionScreenProxy::new)
        );
    }
}
