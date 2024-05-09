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

import org.redlance.dima_dencep.mods.rrls.Rrls;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.Toml4jConfigSerializer;
import net.fabricmc.api.ClientModInitializer;

public class RrlsFabric extends Rrls implements ClientModInitializer {
    public static final ConfigExpectPlatformImpl MOD_CONFIG = AutoConfig.register(
            ConfigExpectPlatformImpl.class, Toml4jConfigSerializer::new
    ).get();

    @Override
    public void onInitializeClient() {
        // no-op
    }
}
