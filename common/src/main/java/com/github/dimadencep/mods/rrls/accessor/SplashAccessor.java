/*
 * Copyright 2023 dima_dencep.
 *
 * Licensed under the Open Software License, Version 3.0 (the "License");
 * you may not use this file except in compliance with the License.
 *
 * You may obtain a copy of the License at
 *     https://github.com/dima-dencep/rrls/blob/HEAD/LICENSE
 */

package com.github.dimadencep.mods.rrls.accessor;

import com.github.dimadencep.mods.rrls.ConfigExpectPlatform;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.MessageScreen;
import net.minecraft.client.gui.screen.Screen;

@SuppressWarnings("unused")
public interface SplashAccessor {

    default AttachType rrls$getAttachType() {
        return AttachType.DEFAULT;
    }

    default void rrls$setAttachType(AttachType type) {
    }

    default void rrls$render(DrawContext context, boolean isGame) {
    }

    default void rrls$reload() {
    }

    default void rrls$endhook() {
    }

    default void rrls$progress(float progress) {
    }

    default AttachType rrls$filterAttachType(Screen screen, boolean reloading) {
        if (!ConfigExpectPlatform.hideType().canHide(reloading))
            return AttachType.DEFAULT;

        if (reloading || ConfigExpectPlatform.forceClose())
            return AttachType.HIDE;

        if (screen instanceof MessageScreen msg) // Loading Minecraft
            return AttachType.WAIT;

        return screen != null ? AttachType.HIDE : AttachType.WAIT;
    }

    enum AttachType {
        DEFAULT,
        HIDE,
        WAIT
    }
}
