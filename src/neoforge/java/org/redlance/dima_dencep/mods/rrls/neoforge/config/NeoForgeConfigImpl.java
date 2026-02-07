/*
 * Copyright 2023 - 2026 dima_dencep.
 *
 * Licensed under the Open Software License, Version 3.0 (the "License");
 * you may not use this file except in compliance with the License.
 *
 * You may obtain a copy of the License at
 *     https://spdx.org/licenses/OSL-3.0.txt
 */

package org.redlance.dima_dencep.mods.rrls.neoforge.config;

import org.redlance.dima_dencep.mods.rrls.config.impl.ForgeLikeConfigImpl;

public class NeoForgeConfigImpl extends ForgeLikeConfigImpl {
    public NeoForgeConfigImpl() {}

    @Override
    public boolean isActive() {
        return true;
    }
}
