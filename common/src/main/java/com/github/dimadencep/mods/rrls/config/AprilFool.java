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
