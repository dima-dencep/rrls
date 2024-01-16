/*
 * Copyright 2023 dima_dencep.
 *
 * Licensed under the Open Software License, Version 3.0 (the "License");
 * you may not use this file except in compliance with the License.
 *
 * You may obtain a copy of the License at
 *     https://github.com/dima-dencep/rrls/blob/HEAD/LICENSE
 */

package com.github.dimadencep.mods.rrls.neoforge;

import com.github.dimadencep.mods.rrls.Rrls;
import com.github.dimadencep.mods.rrls.config.AprilFool;
import com.github.dimadencep.mods.rrls.config.HideType;
import com.github.dimadencep.mods.rrls.config.ShowIn;
import com.github.dimadencep.mods.rrls.config.Type;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModList;
import net.neoforged.fml.config.ConfigFileTypeHandler;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.loading.FMLPaths;
import net.neoforged.neoforge.common.ModConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

@SuppressWarnings("unused")
public class ConfigExpectPlatformImpl {
    public static final Pair<ConfigExpectPlatformImpl, ModConfigSpec> CONFIG_SPEC_PAIR = new ModConfigSpec.Builder()
            .configure(ConfigExpectPlatformImpl::new);
    public final ModConfigSpec.EnumValue<HideType> hideType;
    public final ModConfigSpec.BooleanValue rgbProgress;
    public final ModConfigSpec.BooleanValue forceClose;
    public final ModConfigSpec.EnumValue<ShowIn> showIn;
    public final ModConfigSpec.EnumValue<Type> type;
    public final ModConfigSpec.ConfigValue<String> reloadText;
    public final ModConfigSpec.BooleanValue resetResources;
    public final ModConfigSpec.BooleanValue reInitScreen;
    public final ModConfigSpec.BooleanValue earlyPackStatusSend;
    public final ModConfigSpec.ConfigValue<Double> animationSpeed;
    public final ModConfigSpec.EnumValue<AprilFool> aprilFool;

    public ConfigExpectPlatformImpl(ModConfigSpec.Builder builder) {
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

        showIn = builder
                .translation("text.autoconfig.rrls.option.showIn")
                .defineEnum("showIn", ShowIn.ALL);

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

        earlyPackStatusSend = builder
                .translation("text.autoconfig.rrls.option.earlyPackStatusSend")
                .comment("text.autoconfig.rrls.option.earlyPackStatusSend.@Tooltip[0]")
                .comment("text.autoconfig.rrls.option.earlyPackStatusSend.@Tooltip[1]")
                .define("earlyPackStatusSend", true);

        animationSpeed = builder
                .translation("text.autoconfig.rrls.option.animationSpeed")
                .comment("text.autoconfig.rrls.option.animationSpeed.@Tooltip")
                .define("animationSpeed", 1000.0);

        aprilFool = builder
                .translation("text.autoconfig.rrls.option.aprilFool")
                .defineEnum("aprilFool", AprilFool.ON_APRIL);
    }

    static { // Early loading for config
        ModContainer activeContainer = ModList.get().getModContainerById(Rrls.MOD_ID).orElseThrow();

        ModConfig modConfig = new ModConfig(ModConfig.Type.CLIENT, ConfigExpectPlatformImpl.CONFIG_SPEC_PAIR.getValue(), activeContainer, "rrls.toml");
        modConfig.getSpec().acceptConfig(ConfigFileTypeHandler.TOML.reader(FMLPaths.CONFIGDIR.get()).apply(modConfig));

        activeContainer.addConfig(modConfig);
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

    public static ShowIn showIn() {
        return ConfigExpectPlatformImpl.CONFIG_SPEC_PAIR.getKey().showIn.get();
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

    public static boolean earlyPackStatusSend() {
        return ConfigExpectPlatformImpl.CONFIG_SPEC_PAIR.getKey().earlyPackStatusSend.get();
    }

    public static float animationSpeed() {
        return ConfigExpectPlatformImpl.CONFIG_SPEC_PAIR.getKey().animationSpeed.get().floatValue();
    }

    public static AprilFool aprilFool() {
        return ConfigExpectPlatformImpl.CONFIG_SPEC_PAIR.getKey().aprilFool.get();
    }
}
