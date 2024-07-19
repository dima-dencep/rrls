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

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
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

import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;

@Mixin(value = ConfigurationScreen.ConfigurationSectionScreen.class, remap = false)
public abstract class ConfigurationSectionScreenMixin {
    @Shadow
    @Final
    protected ConfigurationScreen.ConfigurationSectionScreen.Context context;

    @Shadow
    protected abstract ConfigurationScreen.ConfigurationSectionScreen.Element createBooleanValue(String key, ModConfigSpec.ValueSpec spec, Supplier<Boolean> source, Consumer<Boolean> target);
    @Shadow
    protected abstract ConfigurationScreen.ConfigurationSectionScreen.Element createIntegerValue(String key, ModConfigSpec.ValueSpec spec, Supplier<Integer> source, Consumer<Integer> target);
    @Shadow
    protected abstract ConfigurationScreen.ConfigurationSectionScreen.Element createLongValue(String key, ModConfigSpec.ValueSpec spec, Supplier<Long> source, Consumer<Long> target);
    @Shadow
    protected abstract ConfigurationScreen.ConfigurationSectionScreen.Element createDoubleValue(String key, ModConfigSpec.ValueSpec spec, Supplier<Double> source, Consumer<Double> target);
    @Shadow
    protected abstract ConfigurationScreen.ConfigurationSectionScreen.Element createStringValue(String key, Predicate<String> tester, Supplier<String> source, Consumer<String> target);

    @Shadow
    protected ConfigurationScreen.RestartType needsRestart;

    @Inject(
            method = "<init>(Lnet/minecraft/client/gui/screens/Screen;Lnet/neoforged/fml/config/ModConfig$Type;Lnet/neoforged/fml/config/ModConfig;Lnet/minecraft/network/chat/Component;)V",
            at = @At(
                    value = "TAIL"
            )
    )
    public void rrls$fixStartupConfigs(Screen parent, ModConfig.Type type, ModConfig modConfig, Component title, CallbackInfo ci) {
        if (Rrls.MOD_ID.equals(context.modId())) {
            this.needsRestart = ConfigurationScreen.RestartType.NONE;
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

    @WrapOperation(
            method = "rebuild",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/neoforged/neoforge/client/gui/ConfigurationScreen$ConfigurationSectionScreen;createOtherValue(Ljava/lang/String;Lnet/neoforged/neoforge/common/ModConfigSpec$ConfigValue;)Lnet/neoforged/neoforge/client/gui/ConfigurationScreen$ConfigurationSectionScreen$Element;"
            )
    )
    public ConfigurationScreen.ConfigurationSectionScreen.Element rrls$fixValues(ConfigurationScreen.ConfigurationSectionScreen instance, String key, ModConfigSpec.ConfigValue value, Operation<ConfigurationScreen.ConfigurationSectionScreen.Element> original, @Local(ordinal = 0) ModConfigSpec.ValueSpec valueSpec) {
        if (Rrls.MOD_ID.equals(this.context.modId())) {
            return switch (valueSpec.getDefault()) {
                case Boolean b -> createBooleanValue(key, valueSpec, value, value::set);
                case Integer i -> createIntegerValue(key, valueSpec, value, value::set);
                case Long l -> createLongValue(key, valueSpec, value, value::set);
                case Double d -> createDoubleValue(key, valueSpec, value, value::set);
                case String s -> createStringValue(key, valueSpec::test, value, value::set);
                default -> original.call(instance, key, value);
            };
        }

        return original.call(instance, key, value);
    }
}
