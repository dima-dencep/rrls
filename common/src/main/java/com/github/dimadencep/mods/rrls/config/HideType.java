/*
 * Copyright 2023 dima_dencep.
 *
 * Licensed under the Open Software License, Version 3.0 (the "License");
 * you may not use this file except in compliance with the License.
 *
 * You may obtain a copy of the License at
 *     https://github.com/dima-dencep/rrls/blob/HEAD/LICENSE
 */

package com.github.dimadencep.mods.rrls.config;

public enum HideType { // TODO dehardcode
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
}
