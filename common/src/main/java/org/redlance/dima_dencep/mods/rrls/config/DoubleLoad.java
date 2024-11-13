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

public enum DoubleLoad implements TranslatableEnum {
    FORCE_LOAD(Component.translatable("rrls.configuration.doubleload.force")),
    LOAD(Component.translatable("rrls.configuration.doubleload.load")),
    NONE(Component.translatable("rrls.configuration.hide_doubleload.none"));

    private final Component translatedName;

    DoubleLoad(Component translatedName) {
        this.translatedName = translatedName;
    }

    public boolean isLoad() {
        return switch (this) {
            case FORCE_LOAD, LOAD -> true;
            case NONE -> false;
        };
    }

    @Override
    public Component getTranslatedName() {
        return this.translatedName;
    }
}
