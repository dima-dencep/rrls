package com.github.dimadencep.mods.rrls.utils;

import com.github.dimadencep.mods.rrls.ConfigExpectPlatform;
import net.minecraft.client.gui.screen.MessageScreen;
import net.minecraft.client.gui.screen.Overlay;
import net.minecraft.client.gui.screen.Screen;

public class SplashHelper {
    public static State lookupState(Screen screen, boolean reloading) {
        if (!ConfigExpectPlatform.hideType().canHide(reloading))
            return State.DEFAULT;

        if (reloading || ConfigExpectPlatform.forceClose())
            return State.HIDE;

        if (screen instanceof MessageScreen) // Loading Minecraft
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
