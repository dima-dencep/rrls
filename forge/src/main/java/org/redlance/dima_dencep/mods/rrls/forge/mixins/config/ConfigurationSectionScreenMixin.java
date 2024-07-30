/*
 * Copyright 2023 - 2024 dima_dencep.
 *
 * Licensed under the Open Software License, Version 3.0 (the "License");
 * you may not use this file except in compliance with the License.
 *
 * You may obtain a copy of the License at
 *     https://spdx.org/licenses/OSL-3.0.txt
 */

package org.redlance.dima_dencep.mods.rrls.forge.mixins.config;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.common.ModConfigSpec;
import org.redlance.dima_dencep.mods.rrls.Rrls;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = ConfigurationScreen.ConfigurationSectionScreen.class, remap = false)
public abstract class ConfigurationSectionScreenMixin {
    @Shadow
    @Final
    protected ConfigurationScreen.ConfigurationSectionScreen.Context context;
    @Shadow
    protected ModConfigSpec.RestartType needsRestart;

    @Inject(
            method = "<init>(Lnet/minecraft/client/gui/screens/Screen;Lnet/neoforged/fml/config/ModConfig$Type;Lnet/neoforged/fml/config/ModConfig;Lnet/minecraft/network/chat/Component;Lnet/neoforged/neoforge/client/gui/ConfigurationScreen$ConfigurationSectionScreen$Filter;)V",
            at = @At(
                    value = "TAIL"
            )
    )
    public void rrls$fixStartupConfigs(Screen parent, ModConfig.Type type, ModConfig modConfig, Component title, ConfigurationScreen.ConfigurationSectionScreen.Filter filter, CallbackInfo ci) {
        if (Rrls.MOD_ID.equals(context.modId())) {
            this.needsRestart = ModConfigSpec.RestartType.NONE;
        }
    }

    @ModifyVariable(
            method = "getTooltipComponent",
            at = @At(
                    value = "STORE"
            ),
            ordinal = 1
    )
    public String rrls$clothconfig(String value) {
        if (Rrls.MOD_ID.equals(this.context.modId())) {
            return value.replace(".tooltip", ".@Tooltip");
        }

        return value;
    }
}
