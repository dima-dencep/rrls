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

public enum HideType implements TranslatableEnum {
    ALL(Component.translatable("rrls.configuration.hide.all")),
    LOADING(Component.translatable("rrls.configuration.hide.loading")),
    RELOADING(Component.translatable("rrls.configuration.hide.reloading")),
    NONE(Component.translatable("rrls.configuration.hide_doubleload.none"));

    private final Component translatedName;

    HideType(Component translatedName) {
        this.translatedName = translatedName;
    }

    public boolean canHide(boolean reloading) {
        return switch (this) {
            case ALL -> true;
            case LOADING -> !reloading;
            case RELOADING -> reloading;
            case NONE -> false;
        };
    }

    public boolean forceClose() {
        return this == LOADING || this == ALL;
    }

    @Override
    public Component getTranslatedName() {
        return this.translatedName;
    }
}
