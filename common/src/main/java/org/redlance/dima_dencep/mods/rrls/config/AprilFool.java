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

import java.util.Calendar;

@SuppressWarnings("unused")
public enum AprilFool {
    ON_APRIL,
    ALWAYS,
    DISABLED;

    private static Calendar calendar;

    public boolean canUes() {
        return switch (this) {
            case ON_APRIL -> {
                if (calendar == null) {
                    calendar = Calendar.getInstance();
                    calendar.setTimeInMillis(System.currentTimeMillis());
                }

                yield calendar.get(Calendar.MONTH) == Calendar.APRIL && calendar.get(Calendar.DAY_OF_MONTH) == 1;
            }
            case ALWAYS -> true;
            case DISABLED -> false;
        };
    }
}
