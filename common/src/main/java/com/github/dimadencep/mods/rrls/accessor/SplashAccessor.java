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

import net.minecraft.client.gui.DrawContext;

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

    enum AttachType {
        DEFAULT,
        HIDE,
        WAIT
    }
}
