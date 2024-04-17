/*
 * Copyright 2023 - 2024 dima_dencep.
 *
 * Licensed under the Open Software License, Version 3.0 (the "License");
 * you may not use this file except in compliance with the License.
 *
 * You may obtain a copy of the License at
 *     https://spdx.org/licenses/OSL-3.0.txt
 */

package com.github.dimadencep.mods.rrls.config;

import com.github.dimadencep.mods.rrls.Rrls;
import com.github.dimadencep.mods.rrls.config.enums.DoubleLoad;
import com.github.dimadencep.mods.rrls.config.enums.HideType;
import com.github.dimadencep.mods.rrls.config.enums.Type;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModList;
import net.neoforged.fml.config.ConfigFileTypeHandler;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.loading.FMLPaths;
import net.neoforged.neoforge.common.ModConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

public class RrlsConfig {
    public static final Pair<RrlsConfig, ModConfigSpec> CONFIG_SPEC_PAIR = new ModConfigSpec.Builder()
            .configure(RrlsConfig::new);
    public static final RrlsConfig INSTANCE = RrlsConfig.CONFIG_SPEC_PAIR.getKey();

    public final ModConfigSpec.EnumValue<HideType> hideType;
    public final ModConfigSpec.BooleanValue rgbProgress;
    public final ModConfigSpec.BooleanValue forceClose;
    public final ModConfigSpec.BooleanValue blockOverlay;
    public final ModConfigSpec.BooleanValue miniRender;
    public final ModConfigSpec.EnumValue<Type> type;
    public final ModConfigSpec.ConfigValue<String> reloadText;
    public final ModConfigSpec.BooleanValue resetResources;
    public final ModConfigSpec.BooleanValue reInitScreen;
    public final ModConfigSpec.BooleanValue removeOverlayAtEnd;
    public final ModConfigSpec.BooleanValue earlyPackStatusSend;
    public final ModConfigSpec.EnumValue<DoubleLoad> doubleLoad;
    public final ModConfigSpec.ConfigValue<Double> animationSpeed;
    public final ModConfigSpec.BooleanValue skipForgeOverlay;

    public RrlsConfig(ModConfigSpec.Builder builder) {
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
        ModContainer activeContainer = ModList.get().getModContainerById(Rrls.MOD_ID).orElseThrow();
        ModConfigSpec configSpec = RrlsConfig.CONFIG_SPEC_PAIR.getValue();

        ModConfig modConfig = new ModConfig(ModConfig.Type.CLIENT, configSpec, activeContainer, "rrls.toml");
        activeContainer.addConfig(modConfig);

        if (!configSpec.isLoaded()) {
            Rrls.LOGGER.warn("Config is not loaded?");

            configSpec.acceptConfig(
                    ConfigFileTypeHandler.TOML.reader(FMLPaths.CONFIGDIR.get())
                            .apply(modConfig)
            );
        }
    }
}
