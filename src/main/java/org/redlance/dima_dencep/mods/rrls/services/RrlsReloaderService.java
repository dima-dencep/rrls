/*
 * Copyright 2023 - 2026 dima_dencep.
 *
 * Licensed under the Open Software License, Version 3.0 (the "License");
 * you may not use this file except in compliance with the License.
 *
 * You may obtain a copy of the License at
 *     https://spdx.org/licenses/OSL-3.0.txt
 */

package org.redlance.dima_dencep.mods.rrls.services;

import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.packs.resources.ResourceManager;
import org.redlance.common.services.AdvancedService;
import org.redlance.common.services.ServiceUtils;

import java.util.function.Consumer;

public interface RrlsReloaderService extends AdvancedService {
    RrlsReloaderService INSTANCE = ServiceUtils.loadService(RrlsReloaderService.class, () -> new RrlsReloaderService() {
        @Override
        public void collectEarlyReloaders(ResourceManager manager, Consumer<PreparableReloadListener> consumer) {
            // no-op
        }

        @Override
        public boolean isServiceActive() {
            return true;
        }
    });

    void collectEarlyReloaders(ResourceManager manager, Consumer<PreparableReloadListener> consumer);
}
