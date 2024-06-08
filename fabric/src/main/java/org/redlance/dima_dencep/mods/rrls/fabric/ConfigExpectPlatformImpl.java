/*
 * Copyright 2023 - 2024 dima_dencep.
 *
 * Licensed under the Open Software License, Version 3.0 (the "License");
 * you may not use this file except in compliance with the License.
 *
 * You may obtain a copy of the License at
 *     https://spdx.org/licenses/OSL-3.0.txt
 */

package org.redlance.dima_dencep.mods.rrls.fabric;

import org.redlance.dima_dencep.mods.rrls.Rrls;
import org.redlance.dima_dencep.mods.rrls.config.DoubleLoad;
import org.redlance.dima_dencep.mods.rrls.config.HideType;
import org.redlance.dima_dencep.mods.rrls.config.Type;
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
    public boolean blockOverlay = false;

    @ConfigEntry.Category("global")
    public boolean miniRender = true;

    @ConfigEntry.Category("global")
    @ConfigEntry.Gui.Tooltip
    public boolean enableScissor = false;

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

    public static HideType hideType() {
        return RrlsFabric.MOD_CONFIG.hideType;
    }

    public static boolean rgbProgress() {
        return RrlsFabric.MOD_CONFIG.rgbProgress;
    }

    public static boolean blockOverlay() {
        return RrlsFabric.MOD_CONFIG.blockOverlay;
    }

    public static boolean miniRender() {
        return RrlsFabric.MOD_CONFIG.miniRender;
    }

    public static boolean enableScissor() {
        return RrlsFabric.MOD_CONFIG.enableScissor;
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

    public static boolean skipForgeOverlay() {
        return false; // Fabric is forge?
    }
}
