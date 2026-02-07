/*
 * Copyright 2023 - 2026 dima_dencep.
 *
 * Licensed under the Open Software License, Version 3.0 (the "License");
 * you may not use this file except in compliance with the License.
 *
 * You may obtain a copy of the License at
 *     https://spdx.org/licenses/OSL-3.0.txt
 */

package org.redlance.dima_dencep.mods.rrls;

import org.redlance.common.services.AdvancedService;
import org.redlance.common.services.ServiceUtils;
import org.redlance.dima_dencep.mods.rrls.config.DoubleLoad;
import org.redlance.dima_dencep.mods.rrls.config.HideType;
import org.redlance.dima_dencep.mods.rrls.config.Type;
import org.redlance.dima_dencep.mods.rrls.utils.Ease;

public interface RrlsConfig extends AdvancedService {
    RrlsConfig INSTANCE = ServiceUtils.loadService(RrlsConfig.class);

    HideType hideType();
    boolean blockOverlay();
    boolean miniRender();
    boolean enableScissor();
    Type type();
    boolean rgbProgress();
    String reloadText();
    boolean interpolateProgress();
    boolean interpolateAtEnd();
    Ease easing();
    Float easingArg();
    float animationSpeed();
    boolean resetResources();
    boolean reInitScreen();
    boolean earlyPackStatusSend();
    DoubleLoad doubleLoad();
    boolean skipForgeOverlay();
}
