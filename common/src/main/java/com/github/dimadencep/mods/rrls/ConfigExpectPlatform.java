package com.github.dimadencep.mods.rrls;

import com.github.dimadencep.mods.rrls.config.AprilFool;
import com.github.dimadencep.mods.rrls.config.HideType;
import com.github.dimadencep.mods.rrls.config.ShowIn;
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
    public static ShowIn showIn() {
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
    public static boolean earlyPackStatusSend() {
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
}
