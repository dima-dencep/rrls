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

import java.util.Calendar;

public enum AprilFool {
    ON_APRIL,
    ALWAYS,
    DISABLED;

    private static Calendar calendar;

    public boolean canUes() {
        if (this == ALWAYS)
            return true;

        try {
            if (this == ON_APRIL) {
                if (calendar == null) {
                    calendar = Calendar.getInstance();
                    calendar.setTimeInMillis(System.currentTimeMillis());
                }

                return calendar.get(Calendar.MONTH) == Calendar.APRIL && calendar.get(Calendar.DAY_OF_MONTH) == 1;
            }
        } catch (Throwable ignored) {
        }

        return false;
    }
}
