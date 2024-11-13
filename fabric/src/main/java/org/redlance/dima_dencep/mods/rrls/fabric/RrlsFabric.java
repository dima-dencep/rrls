/*
 * Copyright 2023 - 2024 dima_dencep.
 *
 * Licensed under the Open Software License, Version 3.0 (the "License");
 * you may not use this file except in compliance with the License.
 *
 * You may obtain a copy of the License at
 *     https://spdx.org/licenses/OSL-3.0.txt
 */

package org.redlance.dima_dencep.mods.rrls.fabric;

import fuzs.forgeconfigapiport.fabric.api.neoforge.v4.NeoForgeConfigRegistry;
import fuzs.forgeconfigapiport.fabric.api.neoforge.v4.client.ConfigScreenFactoryRegistry;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import org.redlance.dima_dencep.mods.rrls.Rrls;
import net.fabricmc.api.ClientModInitializer;
import org.redlance.dima_dencep.mods.rrls.RrlsConfig;

public class RrlsFabric extends Rrls implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        NeoForgeConfigRegistry.INSTANCE.register(Rrls.MOD_ID, ModConfig.Type.CLIENT,
                RrlsConfig.CONFIG_SPEC_PAIR.getRight(), "rrls.toml"
        );
        ConfigScreenFactoryRegistry.INSTANCE.register(Rrls.MOD_ID, ConfigurationScreen::new);

        // After loading config, we load the future stuff
        super.onInitializeClient();
    }
}
