/*
 * Copyright 2023 - 2026 dima_dencep.
 *
 * Licensed under the Open Software License, Version 3.0 (the "License");
 * you may not use this file except in compliance with the License.
 *
 * You may obtain a copy of the License at
 *     https://spdx.org/licenses/OSL-3.0.txt
 */

package org.redlance.dima_dencep.mods.rrls.utils;

import net.minecraft.util.ARGB;
import net.minecraft.util.Mth;
import net.minecraft.util.Util;

public class RainbowUtils {
    private static float makeChannel(float speed, float shift, int offset) {
        return (1F + Mth.sin(speed * shift + offset)) * 0.5F;
    }

    public static int rainbowColor(int alpha) {
        return rainbowColor(ARGB.from8BitChannel(alpha));
    }

    public static int rainbowColor(float alpha) {
        float time = Util.getNanos() * 1E-9F;
        float speed = 0.5F; // TODO config

        return ARGB.colorFromFloat(alpha,
                makeChannel(speed, time, 0),
                makeChannel(speed, time, 2),
                makeChannel(speed, time, 4)
        );
    }
}
