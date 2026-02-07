/*
 * Copyright 2023 - 2026 dima_dencep.
 *
 * Licensed under the Open Software License, Version 3.0 (the "License");
 * you may not use this file except in compliance with the License.
 *
 * You may obtain a copy of the License at
 *     https://spdx.org/licenses/OSL-3.0.txt
 */

package org.redlance.dima_dencep.mods.rrls.config.impl;

import org.redlance.common.services.ServiceUtils;
import org.redlance.dima_dencep.mods.rrls.RrlsConfig;
import org.redlance.dima_dencep.mods.rrls.config.DoubleLoad;
import org.redlance.dima_dencep.mods.rrls.config.HideType;
import org.redlance.dima_dencep.mods.rrls.config.Type;
import org.redlance.dima_dencep.mods.rrls.utils.Ease;

public class FallbackConfigImpl implements RrlsConfig {
    @Override
    public HideType hideType() {
        return HideType.ALL;
    }

    @Override
    public boolean blockOverlay() {
        return false;
    }

    @Override
    public boolean miniRender() {
        return true;
    }

    @Override
    public boolean enableScissor() {
        return false;
    }

    @Override
    public Type type() {
        return Type.PROGRESS;
    }

    @Override
    public boolean rgbProgress() {
        return false;
    }

    @Override
    public String reloadText() {
        return "Edit in config!";
    }

    @Override
    public boolean interpolateProgress() {
        return false;
    }

    @Override
    public boolean interpolateAtEnd() {
        return true;
    }

    @Override
    public Ease easing() {
        return Ease.INOUTQUINT;
    }

    @Override
    public Float easingArg() {
        return Float.NaN;
    }

    @Override
    public float animationSpeed() {
        return 1000.0F;
    }

    @Override
    public boolean resetResources() {
        return true;
    }

    @Override
    public boolean reInitScreen() {
        return true;
    }

    @Override
    public boolean earlyPackStatusSend() {
        return false;
    }

    @Override
    public DoubleLoad doubleLoad() {
        return DoubleLoad.FORCE_LOAD;
    }

    @Override
    public boolean skipForgeOverlay() {
        return false;
    }

    @Override
    public boolean isActive() {
        return true;
    }

    @Override
    public int getPriority() {
        return ServiceUtils.LOWEST_PRIORITY;
    }
}
