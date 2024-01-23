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

import com.github.dimadencep.mods.rrls.utils.SplashHelper;
import net.minecraft.client.gui.DrawContext;

@SuppressWarnings("unused")
public interface SplashAccessor {
    default SplashHelper.State rrls$getState() {
        return SplashHelper.State.DEFAULT;
    }

    default void rrls$setState(SplashHelper.State state) {
    }

    default void rrls$miniRender(DrawContext context) {
    }
}
