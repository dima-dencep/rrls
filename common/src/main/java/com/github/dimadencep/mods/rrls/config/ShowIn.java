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

public enum ShowIn {
    DISABLED,
    ONLY_GAME,
    ONLY_GUI,
    ALL;

    public boolean canShow(boolean isGame) {
        if (this == DISABLED) return false;

        if (this == ALL) return true;

        if (isGame) {
            return this == ONLY_GAME;
        } else {
            return this == ONLY_GUI;
        }
    }
}
