package com.github.dimadencep.mods.rrls.accessor;

import net.minecraft.client.gui.DrawContext;

public interface SplashAccessor {

    default AttachType rrls$getAttachType() {
        return AttachType.DEFAULT;
    }

    default void rrls$render(DrawContext context, boolean isGame) {
    }

    default void rrls$reload() {
    }

    enum AttachType {
        DEFAULT,
        HIDE,
        WAIT
    }
}
