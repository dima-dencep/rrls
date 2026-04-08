/*
 * Copyright 2023 - 2026 dima_dencep.
 *
 * Licensed under the Open Software License, Version 3.0 (the "License");
 * you may not use this file except in compliance with the License.
 *
 * You may obtain a copy of the License at
 *     https://spdx.org/licenses/OSL-3.0.txt
 */

package org.redlance.dima_dencep.mods.rrls.utils;

import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.packs.resources.ResourceManager;
import org.jspecify.annotations.NonNull;

import java.util.concurrent.CompletableFuture;

@SuppressWarnings("unchecked")
public class WaitingSharedState extends PreparableReloadListener.SharedState {
    public WaitingSharedState(ResourceManager manager) {
        super(manager);
    }

    @Override
    public <T> @NonNull T get(PreparableReloadListener.@NonNull StateKey<T> key) {
        try {
            return super.get(key);
        } catch (Throwable th) {
            try {
                return (T) CompletableFuture.failedFuture(th);
            } catch (ClassCastException e) {
                throw th;
            }
        }
    }
}

