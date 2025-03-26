/*
 * Copyright 2023 - 2025 dima_dencep.
 *
 * Licensed under the Open Software License, Version 3.0 (the "License");
 * you may not use this file except in compliance with the License.
 *
 * You may obtain a copy of the License at
 *     https://spdx.org/licenses/OSL-3.0.txt
 */

package org.redlance.dima_dencep.mods.rrls.fabric;

import fuzs.forgeconfigapiport.fabric.api.v5.ConfigRegistry;
import fuzs.forgeconfigapiport.fabric.api.v5.client.ConfigScreenFactoryRegistry;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import org.redlance.dima_dencep.mods.rrls.Rrls;
import net.fabricmc.api.ClientModInitializer;
import org.redlance.dima_dencep.mods.rrls.RrlsConfig;
import org.redlance.dima_dencep.mods.rrls.screens.ConfigurationSectionScreenProxy;

public class RrlsFabric extends Rrls implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ConfigRegistry.INSTANCE.register(Rrls.MOD_ID, ModConfig.Type.CLIENT,
                RrlsConfig.CONFIG_SPEC_PAIR.getRight(), "rrls.toml"
        );
        ConfigScreenFactoryRegistry.INSTANCE.register(Rrls.MOD_ID, (modContainer, screen) ->
                new ConfigurationScreen(modContainer, screen, ConfigurationSectionScreenProxy::new)
        );

        // After loading config, we load the future stuff
        super.onInitializeClient();
    }
}
