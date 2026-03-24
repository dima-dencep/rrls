/*
 * Copyright 2023 - 2026 dima_dencep.
 *
 * Licensed under the Open Software License, Version 3.0 (the "License");
 * you may not use this file except in compliance with the License.
 *
 * You may obtain a copy of the License at
 *     https://spdx.org/licenses/OSL-3.0.txt
 */

package org.redlance.dima_dencep.mods.rrls.fabric;

import fuzs.forgeconfigapiport.fabric.api.v5.ConfigRegistry;
import net.neoforged.fml.config.ModConfig;
import org.redlance.dima_dencep.mods.rrls.Rrls;
import net.fabricmc.api.ClientModInitializer;
import org.redlance.dima_dencep.mods.rrls.RrlsConfig;
import org.redlance.dima_dencep.mods.rrls.fabric.config.FabricConfigImpl;

public class RrlsFabric extends Rrls implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        if (RrlsConfig.INSTANCE instanceof FabricConfigImpl impl) {
            ConfigRegistry.INSTANCE.register(Rrls.MOD_ID, ModConfig.Type.CLIENT,
                    impl.configSpec, "rrls.toml"
            );
            impl.registerConfigScreen();
        }

        // After loading config, we load the future stuff
        super.onInitializeClient();
    }
}
