/*
 * Copyright 2023 - 2026 dima_dencep.
 *
 * Licensed under the Open Software License, Version 3.0 (the "License");
 * you may not use this file except in compliance with the License.
 *
 * You may obtain a copy of the License at
 *     https://spdx.org/licenses/OSL-3.0.txt
 */

package org.redlance.dima_dencep.mods.rrls.config;

import net.minecraft.network.chat.Component;

public enum HideType {
    ALL(Component.translatable("rrls.configuration.hide.all"), true),
    LOADING(Component.translatable("rrls.configuration.hide.loading"), true),
    RELOADING(Component.translatable("rrls.configuration.hide.reloading"), false),
    NONE(Component.translatable("rrls.configuration.hide_doubleload.none"), false);

    private final Component translatedName;
    public final boolean affectInitial;

    HideType(Component translatedName, boolean affectInitial) {
        this.translatedName = translatedName;
        this.affectInitial = affectInitial;
    }

    public boolean canHide(boolean reloading) {
        return switch (this) {
            case ALL -> true;
            case LOADING -> !reloading;
            case RELOADING -> reloading;
            case NONE -> false;
        };
    }

    public Component getTranslatedName() {
        return this.translatedName;
    }
}
