/*
 * Copyright 2023 - 2024 dima_dencep.
 *
 * Licensed under the Open Software License, Version 3.0 (the "License");
 * you may not use this file except in compliance with the License.
 *
 * You may obtain a copy of the License at
 *     https://spdx.org/licenses/OSL-3.0.txt
 */

package com.github.dimadencep.mods.rrls.config.enums;

public enum DoubleLoad {
    FORCE_LOAD,
    LOAD,
    NONE;

    public boolean isLoad() {
        return this == FORCE_LOAD || this == LOAD;
    }
}
