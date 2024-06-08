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

public enum HideType {
    ALL,
    LOADING,
    RELOADING,
    NONE;

    public boolean canHide(boolean reloading) {
        if (this == NONE) return false;

        if (this == ALL) return true;

        if (reloading) {
            return this == RELOADING;
        } else {
            return this == LOADING;
        }
    }

    public boolean forceClose() {
        return this == LOADING || this == ALL;
    }
}
