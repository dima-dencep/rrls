/*
 * Copyright 2023 - 2024 dima_dencep.
 *
 * Licensed under the Open Software License, Version 3.0 (the "License");
 * you may not use this file except in compliance with the License.
 *
 * You may obtain a copy of the License at
 *     https://spdx.org/licenses/OSL-3.0.txt
 */

package org.redlance.dima_dencep.mods.rrls.config;

import net.minecraft.network.chat.Component;
import net.neoforged.neoforge.common.TranslatableEnum;

public enum Type implements TranslatableEnum {
    PROGRESS(Component.translatable("rrls.configuration.type.progress")),
    TEXT(Component.translatable("rrls.configuration.type.text")),
    TEXT_WITH_BACKGROUND(Component.translatable("rrls.configuration.type.textbg"));

    private final Component translatedName;

    Type(Component translatedName) {
        this.translatedName = translatedName;
    }

    @Override
    public Component getTranslatedName() {
        return this.translatedName;
    }
}
