/*
 * Copyright 2023 - 2026 dima_dencep.
 *
 * Licensed under the Open Software License, Version 3.0 (the "License");
 * you may not use this file except in compliance with the License.
 *
 * You may obtain a copy of the License at
 *     https://spdx.org/licenses/OSL-3.0.txt
 */

package net.neoforged.neoforge.common;

import net.minecraft.network.chat.Component;

/**
 * An enum value that can be be translated.
 */
public interface TranslatableEnum {
    /**
     * {@return the translated name of this value}
     * Defaults to a {@linkplain Component#literal(String) literal component} with the {@link Enum#name() enum name};
     */
    default Component getTranslatedName() {
        return Component.literal(((Enum<?>) this).name());
    }
}
