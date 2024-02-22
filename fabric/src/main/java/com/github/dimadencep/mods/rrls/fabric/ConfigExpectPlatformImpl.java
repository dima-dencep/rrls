/*
 * Copyright 2023 dima_dencep.
 *
 * Licensed under the Open Software License, Version 3.0 (the "License");
 * you may not use this file except in compliance with the License.
 *
 * You may obtain a copy of the License at
 *     https://github.com/dima-dencep/rrls/blob/HEAD/LICENSE
 */

package com.github.dimadencep.mods.rrls.fabric;

import com.github.dimadencep.mods.rrls.Rrls;
import com.github.dimadencep.mods.rrls.config.AprilFool;
import com.github.dimadencep.mods.rrls.config.DoubleLoad;
import com.github.dimadencep.mods.rrls.config.HideType;
import com.github.dimadencep.mods.rrls.config.Type;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

@SuppressWarnings("unused")
@Config(name = Rrls.MOD_ID)
public class ConfigExpectPlatformImpl implements ConfigData {
    @ConfigEntry.Category("global")
    @ConfigEntry.Gui.EnumHandler(option = ConfigEntry.Gui.EnumHandler.EnumDisplayOption.BUTTON)
    public HideType hideType = HideType.RELOADING;

    @ConfigEntry.Category("splash")
    @ConfigEntry.Gui.Tooltip
    public boolean rgbProgress = false;

    @ConfigEntry.Category("global")
    @ConfigEntry.Gui.Tooltip
    public boolean forceClose = false;

    @ConfigEntry.Category("global")
    @ConfigEntry.Gui.Tooltip
    public boolean blockOverlay = false;

    @ConfigEntry.Category("global")
    public boolean miniRender = true;

    @ConfigEntry.Category("splash")
    @ConfigEntry.Gui.EnumHandler(option = ConfigEntry.Gui.EnumHandler.EnumDisplayOption.BUTTON)
    public Type type = Type.PROGRESS;

    @ConfigEntry.Category("splash")
    public String reloadText = "Edit in config!";

    @ConfigEntry.Category("other")
    public boolean resetResources = false;

    @ConfigEntry.Category("splash")
    @ConfigEntry.Gui.Tooltip
    public boolean reInitScreen = true;

    @ConfigEntry.Category("splash")
    @ConfigEntry.Gui.Tooltip
    public boolean removeOverlayAtEnd = true;

    @ConfigEntry.Category("other")
    @ConfigEntry.Gui.Tooltip(count = 2)
    public boolean earlyPackStatusSend = false;

    @ConfigEntry.Category("other")
    @ConfigEntry.Gui.EnumHandler(option = ConfigEntry.Gui.EnumHandler.EnumDisplayOption.BUTTON)
    public DoubleLoad doubleLoad = DoubleLoad.FORCE_LOAD;

    @ConfigEntry.Category("splash")
    @ConfigEntry.Gui.Tooltip
    public float animationSpeed = 1000.0F;

    @ConfigEntry.Category("splash")
    @ConfigEntry.Gui.Excluded
    public AprilFool aprilFool = AprilFool.ON_APRIL;

    public static HideType hideType() {
        return RrlsFabric.MOD_CONFIG.hideType;
    }

    public static boolean rgbProgress() {
        return RrlsFabric.MOD_CONFIG.rgbProgress;
    }

    public static boolean forceClose() {
        return RrlsFabric.MOD_CONFIG.forceClose;
    }

    public static boolean blockOverlay() {
        return RrlsFabric.MOD_CONFIG.blockOverlay;
    }

    public static boolean miniRender() {
        return RrlsFabric.MOD_CONFIG.miniRender;
    }

    public static Type type() {
        return RrlsFabric.MOD_CONFIG.type;
    }

    public static String reloadText() {
        return RrlsFabric.MOD_CONFIG.reloadText;
    }

    public static boolean resetResources() {
        return RrlsFabric.MOD_CONFIG.resetResources;
    }

    public static boolean reInitScreen() {
        return RrlsFabric.MOD_CONFIG.reInitScreen;
    }

    public static boolean removeOverlayAtEnd() {
        return RrlsFabric.MOD_CONFIG.removeOverlayAtEnd;
    }

    public static boolean earlyPackStatusSend() {
        return RrlsFabric.MOD_CONFIG.earlyPackStatusSend;
    }

    public static DoubleLoad doubleLoad() {
        return RrlsFabric.MOD_CONFIG.doubleLoad;
    }

    public static float animationSpeed() {
        return RrlsFabric.MOD_CONFIG.animationSpeed;
    }

    public static AprilFool aprilFool() {
        return RrlsFabric.MOD_CONFIG.aprilFool;
    }

    public static boolean skipForgeOverlay() {
        return false; // Fabric is forge?
    }
}
