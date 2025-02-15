/*
 * Copyright 2023 - 2025 dima_dencep.
 *
 * Licensed under the Open Software License, Version 3.0 (the "License");
 * you may not use this file except in compliance with the License.
 *
 * You may obtain a copy of the License at
 *     https://spdx.org/licenses/OSL-3.0.txt
 */

package org.redlance.dima_dencep.mods.rrls.screens;

import com.electronwill.nightconfig.core.UnmodifiableConfig;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.redlance.dima_dencep.mods.rrls.Rrls;

public class ConfigurationSectionScreenProxy extends ConfigurationScreen.ConfigurationSectionScreen {
    public ConfigurationSectionScreenProxy(Screen parent, ModConfig.Type type, ModConfig modConfig, Component title) {
        super(parent, type, modConfig, title);
    }

    @Override
    protected @Nullable Element createSection(@NotNull String key, @NotNull UnmodifiableConfig subconfig, @NotNull UnmodifiableConfig subsection) {
        ConfigurationScreen.ConfigurationSectionScreen currentSection = this.sectionCache.get(key);
        if (currentSection instanceof ConfigurationSectionScreenWithOverlay) {
            return super.createSection(key, subconfig, subsection);
        }

        ConfigurationScreen.ConfigurationSectionScreen old = this.sectionCache.put(key, new ConfigurationSectionScreenWithOverlay(
                this.context, this, subconfig.valueMap(), key, subsection.entrySet(), Component.translatable(getTranslationKey(key))
        ).rebuild());
        if (old != null) {
            Rrls.LOGGER.warn("Removed section ({}) from config!", old);
        }

        return super.createSection(key, subconfig, subsection);
    }
}
