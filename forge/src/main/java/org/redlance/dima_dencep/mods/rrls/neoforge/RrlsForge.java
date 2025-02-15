/*
 * Copyright 2023 - 2025 dima_dencep.
 *
 * Licensed under the Open Software License, Version 3.0 (the "License");
 * you may not use this file except in compliance with the License.
 *
 * You may obtain a copy of the License at
 *     https://spdx.org/licenses/OSL-3.0.txt
 */

package org.redlance.dima_dencep.mods.rrls.neoforge;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import org.redlance.dima_dencep.mods.rrls.Rrls;
import net.neoforged.fml.common.Mod;
import org.redlance.dima_dencep.mods.rrls.RrlsConfig;
import org.redlance.dima_dencep.mods.rrls.screens.ConfigurationSectionScreenProxy;

@Mod(value = Rrls.MOD_ID, dist = Dist.CLIENT)
public class RrlsForge extends Rrls {
    public RrlsForge(ModContainer container) {
        container.registerConfig(ModConfig.Type.STARTUP, RrlsConfig.CONFIG_SPEC_PAIR.getValue(), "rrls.toml");
        container.registerExtensionPoint(IConfigScreenFactory.class, (modContainer, screen) ->
                new ConfigurationScreen(modContainer, screen, ConfigurationSectionScreenProxy::new)
        );

        // After loading config, we load the future stuff
        super.onInitializeClient();
    }
}
