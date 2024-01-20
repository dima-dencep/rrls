/*
 * Copyright 2023 dima_dencep.
 *
 * Licensed under the Open Software License, Version 3.0 (the "License");
 * you may not use this file except in compliance with the License.
 *
 * You may obtain a copy of the License at
 *     https://github.com/dima-dencep/rrls/blob/HEAD/LICENSE
 */

package com.github.dimadencep.mods.rrls.fabric;

import com.github.dimadencep.mods.rrls.Rrls;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.Toml4jConfigSerializer;
import net.fabricmc.api.ClientModInitializer;

public class RrlsFabric extends Rrls implements ClientModInitializer {
    public static final ConfigExpectPlatformImpl MOD_CONFIG = AutoConfig.register(ConfigExpectPlatformImpl.class, Toml4jConfigSerializer::new).get();

    @Override
    public void onInitializeClient() {
        // no-op
    }
}