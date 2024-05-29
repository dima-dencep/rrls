/*
 * Copyright 2023 - 2024 dima_dencep.
 *
 * Licensed under the Open Software License, Version 3.0 (the "License");
 * you may not use this file except in compliance with the License.
 *
 * You may obtain a copy of the License at
 *     https://spdx.org/licenses/OSL-3.0.txt
 */

package org.redlance.dima_dencep.mods.rrls.forge;

import org.redlance.dima_dencep.mods.rrls.config.DoubleLoad;
import org.redlance.dima_dencep.mods.rrls.config.HideType;
import org.redlance.dima_dencep.mods.rrls.config.Type;
import org.redlance.dima_dencep.mods.rrls.Rrls;
import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Optional;

@SuppressWarnings("unused")
public class ConfigExpectPlatformImpl { // TODO categorize
    public static final Pair<ConfigExpectPlatformImpl, ForgeConfigSpec> CONFIG_SPEC_PAIR = new ForgeConfigSpec.Builder()
            .configure(ConfigExpectPlatformImpl::new);
    public final ForgeConfigSpec.EnumValue<HideType> hideType;
    public final ForgeConfigSpec.BooleanValue rgbProgress;
    public final ForgeConfigSpec.BooleanValue forceClose;
    public final ForgeConfigSpec.BooleanValue blockOverlay;
    public final ForgeConfigSpec.BooleanValue miniRender;
    public final ForgeConfigSpec.EnumValue<Type> type;
    public final ForgeConfigSpec.ConfigValue<String> reloadText;
    public final ForgeConfigSpec.BooleanValue resetResources;
    public final ForgeConfigSpec.BooleanValue reInitScreen;
    public final ForgeConfigSpec.BooleanValue removeOverlayAtEnd;
    public final ForgeConfigSpec.BooleanValue earlyPackStatusSend;
    public final ForgeConfigSpec.EnumValue<DoubleLoad> doubleLoad;
    public final ForgeConfigSpec.ConfigValue<Double> animationSpeed;
    public final ForgeConfigSpec.BooleanValue skipForgeOverlay;

    public ConfigExpectPlatformImpl(ForgeConfigSpec.Builder builder) {
        hideType = builder
                .translation("text.autoconfig.rrls.option.hideType")
                .defineEnum("hideType", HideType.RELOADING);

        rgbProgress = builder
                .translation("text.autoconfig.rrls.option.rgbProgress")
                .comment("text.autoconfig.rrls.option.rgbProgress.@Tooltip")
                .define("rgbProgress", false);

        forceClose = builder
                .translation("text.autoconfig.rrls.option.forceClose")
                .comment("text.autoconfig.rrls.option.forceClose.@Tooltip")
                .define("forceClose", false);

        blockOverlay = builder
                .translation("text.autoconfig.rrls.option.blockOverlay")
                .comment("text.autoconfig.rrls.option.blockOverlay.@Tooltip")
                .define("blockOverlay", false);

        miniRender = builder
                .translation("text.autoconfig.rrls.option.miniRender")
                .define("miniRender", true);

        type = builder
                .translation("text.autoconfig.rrls.option.type")
                .defineEnum("type", Type.PROGRESS);

        reloadText = builder
                .translation("text.autoconfig.rrls.option.reloadText")
                .define("reloadText", "Edit in config!");

        resetResources = builder
                .translation("text.autoconfig.rrls.option.resetResources")
                .define("resetResources", false);

        reInitScreen = builder
                .translation("text.autoconfig.rrls.option.reInitScreen")
                .comment("text.autoconfig.rrls.option.reInitScreen.@Tooltip")
                .define("reInitScreen", true);

        removeOverlayAtEnd = builder
                .translation("text.autoconfig.rrls.option.removeOverlayAtEnd")
                .comment("text.autoconfig.rrls.option.removeOverlayAtEnd.@Tooltip")
                .define("removeOverlayAtEnd", true);

        earlyPackStatusSend = builder
                .translation("text.autoconfig.rrls.option.earlyPackStatusSend")
                .comment("text.autoconfig.rrls.option.earlyPackStatusSend.@Tooltip[0]")
                .comment("text.autoconfig.rrls.option.earlyPackStatusSend.@Tooltip[1]")
                .define("earlyPackStatusSend", false);

        doubleLoad = builder
                .translation("text.autoconfig.rrls.option.doubleLoad")
                .defineEnum("doubleLoad", DoubleLoad.FORCE_LOAD);

        animationSpeed = builder
                .translation("text.autoconfig.rrls.option.animationSpeed")
                .comment("text.autoconfig.rrls.option.animationSpeed.@Tooltip")
                .define("animationSpeed", 1000.0);

        skipForgeOverlay = builder
                .translation("text.autoconfig.rrls.option.skipForgeOverlay")
                .define("skipForgeOverlay", false);
    }

    static { // Early loading for config
        Optional<? extends ModContainer> activeContainer = ModList.get().getModContainerById(Rrls.MOD_ID);
        ForgeConfigSpec configSpec = ConfigExpectPlatformImpl.CONFIG_SPEC_PAIR.getValue();

        final ModConfig modConfig = new ModConfig(ModConfig.Type.CLIENT, configSpec, activeContainer.orElse(null), "rrls.toml");
        activeContainer.ifPresentOrElse(
                container -> container.addConfig(modConfig), // Configs with the STARTUP type are loaded here
                () -> Rrls.LOGGER.error("Unable to find ModContainer, this can cause issues!")
        );

        if (!configSpec.isLoaded()) { // Fallback
            Rrls.LOGGER.warn("Config is not loaded?");

            configSpec.acceptConfig(
                    modConfig.getHandler().reader(FMLPaths.CONFIGDIR.get())
                            .apply(modConfig)
            );
        }
    }

    public static HideType hideType() {
        return ConfigExpectPlatformImpl.CONFIG_SPEC_PAIR.getKey().hideType.get();
    }

    public static boolean rgbProgress() {
        return ConfigExpectPlatformImpl.CONFIG_SPEC_PAIR.getKey().rgbProgress.get();
    }

    public static boolean forceClose() {
        return ConfigExpectPlatformImpl.CONFIG_SPEC_PAIR.getKey().forceClose.get();
    }

    public static boolean blockOverlay() {
        return ConfigExpectPlatformImpl.CONFIG_SPEC_PAIR.getKey().blockOverlay.get();
    }

    public static boolean miniRender() {
        return ConfigExpectPlatformImpl.CONFIG_SPEC_PAIR.getKey().miniRender.get();
    }

    public static Type type() {
        return ConfigExpectPlatformImpl.CONFIG_SPEC_PAIR.getKey().type.get();
    }

    public static String reloadText() {
        return ConfigExpectPlatformImpl.CONFIG_SPEC_PAIR.getKey().reloadText.get();
    }

    public static boolean resetResources() {
        return ConfigExpectPlatformImpl.CONFIG_SPEC_PAIR.getKey().resetResources.get();
    }

    public static boolean reInitScreen() {
        return ConfigExpectPlatformImpl.CONFIG_SPEC_PAIR.getKey().reInitScreen.get();
    }

    public static boolean removeOverlayAtEnd() {
        return ConfigExpectPlatformImpl.CONFIG_SPEC_PAIR.getKey().removeOverlayAtEnd.get();
    }

    public static boolean earlyPackStatusSend() {
        return ConfigExpectPlatformImpl.CONFIG_SPEC_PAIR.getKey().earlyPackStatusSend.get();
    }

    public static DoubleLoad doubleLoad() {
        return ConfigExpectPlatformImpl.CONFIG_SPEC_PAIR.getKey().doubleLoad.get();
    }

    public static float animationSpeed() {
        return ConfigExpectPlatformImpl.CONFIG_SPEC_PAIR.getKey().animationSpeed.get().floatValue();
    }

    public static boolean skipForgeOverlay() {
        return ConfigExpectPlatformImpl.CONFIG_SPEC_PAIR.getKey().skipForgeOverlay.get();
    }
}
