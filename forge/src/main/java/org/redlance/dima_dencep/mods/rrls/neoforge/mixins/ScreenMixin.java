/*
 * Copyright 2023 - 2025 dima_dencep.
 *
 * Licensed under the Open Software License, Version 3.0 (the "License");
 * you may not use this file except in compliance with the License.
 *
 * You may obtain a copy of the License at
 *     https://spdx.org/licenses/OSL-3.0.txt
 */

package org.redlance.dima_dencep.mods.rrls.neoforge.mixins;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.neoforged.bus.api.Event;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.loading.FMLLoader;
import org.redlance.dima_dencep.mods.rrls.Rrls;
import org.redlance.dima_dencep.mods.rrls.utils.OverlayHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Screen.class)
public class ScreenMixin {
    @Unique
    private static final boolean HAS_TWFOREST = FMLLoader.getCurrent().getLoadingModList().getModFileById("twilightforest") != null;

    @WrapOperation(
            method = "init(Lnet/minecraft/client/Minecraft;II)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/neoforged/bus/api/IEventBus;post(Lnet/neoforged/bus/api/Event;)Lnet/neoforged/bus/api/Event;",
                    ordinal = 1
            )
    )
    private Event rrls$fixTWForest(IEventBus instance, Event t, Operation<Event> original, @Local(argsOnly = true) Minecraft mc) {
        if (HAS_TWFOREST && OverlayHelper.isRenderingState(mc.overlay)) {
            Rrls.LOGGER.warn("Canceling '{}' firing because twilightforest is present!", t.getClass().getSimpleName());
            return t;
        }
        return original.call(instance, t);
    }
}
