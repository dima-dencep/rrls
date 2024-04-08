/*
 * Copyright 2023 - 2024 dima_dencep.
 *
 * Licensed under the Open Software License, Version 3.0 (the "License");
 * you may not use this file except in compliance with the License.
 *
 * You may obtain a copy of the License at
 *     https://spdx.org/licenses/OSL-3.0.txt
 */

package com.github.dimadencep.mods.rrls;

import com.github.dimadencep.mods.rrls.config.AprilFool;
import com.github.dimadencep.mods.rrls.config.DoubleLoad;
import com.github.dimadencep.mods.rrls.config.HideType;
import com.github.dimadencep.mods.rrls.config.Type;
import dev.architectury.injectables.annotations.ExpectPlatform;

public class ConfigExpectPlatform {
    @ExpectPlatform
    public static HideType hideType() {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static boolean rgbProgress() {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static boolean forceClose() {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static boolean blockOverlay() {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static boolean miniRender() {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static Type type() {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static String reloadText() {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static boolean resetResources() {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static boolean reInitScreen() {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static boolean removeOverlayAtEnd() {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static boolean earlyPackStatusSend() {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static DoubleLoad doubleLoad() {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static float animationSpeed() {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static AprilFool aprilFool() {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static boolean skipForgeOverlay() {
        throw new AssertionError();
    }
}
