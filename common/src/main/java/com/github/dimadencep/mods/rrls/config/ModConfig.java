/*
 * Copyright 2023 dima_dencep.
 *
 * Licensed under the Open Software License, Version 3.0 (the "License");
 * you may not use this file except in compliance with the License.
 *
 * You may obtain a copy of the License at
 *     https://github.com/dima-dencep/rrls/blob/HEAD/LICENSE
 */

package com.github.dimadencep.mods.rrls.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

@Config(name = "rrls")
public class ModConfig implements ConfigData {
    @ConfigEntry.Gui.EnumHandler(option = ConfigEntry.Gui.EnumHandler.EnumDisplayOption.BUTTON)
    public HideType hideType = HideType.RELOADING;

    @ConfigEntry.Gui.Tooltip
    public boolean rgbProgress = false;

    @ConfigEntry.Gui.EnumHandler(option = ConfigEntry.Gui.EnumHandler.EnumDisplayOption.BUTTON)
    public ShowIn showIn = ShowIn.ALL;

    @ConfigEntry.Gui.EnumHandler(option = ConfigEntry.Gui.EnumHandler.EnumDisplayOption.BUTTON)
    public Type type = Type.PROGRESS;

    public String reloadText = "Edit in config!";

    public boolean resetResources = false;

    @ConfigEntry.Gui.Tooltip
    public boolean reInitScreen = true;

    @ConfigEntry.Gui.Tooltip(count = 2)
    @ConfigEntry.Gui.EnumHandler(option = ConfigEntry.Gui.EnumHandler.EnumDisplayOption.BUTTON)
    public PackStatus earlyPackStatus = PackStatus.SEND_DENY;

    @ConfigEntry.Gui.Tooltip
    public float animationSpeed = 1000.0F;

    public enum ShowIn {
        DISABLED,
        ONLY_GAME,
        ONLY_GUI,
        ALL;

        public boolean canShow(boolean isGame) {
            if (this == DISABLED) return false;

            if (this == ALL) return true;

            if (isGame) {
                return this == ONLY_GAME;
            } else {
                return this == ONLY_GUI;
            }
        }
    }

    public enum HideType {
        ALL,
        LOADING,
        RELOADING,
        NONE;

        public boolean canHide(boolean reloading) {
            if (this == NONE) return false;

            if (this == ALL) return true;

            if (reloading) {
                return this == RELOADING;
            } else {
                return this == LOADING;
            }
        }
    }

    public enum Type {
        PROGRESS,
        TEXT
    }

    public enum PackStatus {
        DISABLED,
        SEND,
        SEND_DENY;

        public boolean earlySend() {
            return this == SEND || this == SEND_DENY;
        }
    }
}
