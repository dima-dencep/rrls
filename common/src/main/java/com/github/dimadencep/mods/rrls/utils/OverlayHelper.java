/*
 * Copyright 2023 - 2024 dima_dencep.
 *
 * Licensed under the Open Software License, Version 3.0 (the "License");
 * you may not use this file except in compliance with the License.
 *
 * You may obtain a copy of the License at
 *     https://spdx.org/licenses/OSL-3.0.txt
 */

package com.github.dimadencep.mods.rrls.utils;

import com.github.dimadencep.mods.rrls.ConfigExpectPlatform;
import net.minecraft.client.gui.screens.GenericMessageScreen;
import net.minecraft.client.gui.screens.Overlay;
import net.minecraft.client.gui.screens.Screen;

public class OverlayHelper {
    public static State lookupState(Screen screen, boolean reloading) {
        if (!ConfigExpectPlatform.hideType().canHide(reloading))
            return State.DEFAULT;

        if (reloading || ConfigExpectPlatform.forceClose())
            return State.HIDE;

        if (screen instanceof GenericMessageScreen) // Loading Minecraft
            return State.WAIT;

        return screen != null ? State.HIDE : State.WAIT;
    }

    public static boolean isRenderingState(Overlay overlay) {
        return overlay != null && overlay.rrls$getState().isRendering();
    }

    public enum State {
        DEFAULT(false),
        HIDE(true),
        WAIT(false);

        private final boolean render;

        State(boolean r) {
            this.render = r;
        }

        public boolean isRendering() {
            return render;
        }
    }
}
