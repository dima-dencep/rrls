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

    @ConfigEntry.Gui.Tooltip
    public boolean ignoreScreen = false;

    @ConfigEntry.Gui.EnumHandler(option = ConfigEntry.Gui.EnumHandler.EnumDisplayOption.BUTTON)
    public ShowIn showIn = ShowIn.ALL;

    @ConfigEntry.Gui.EnumHandler(option = ConfigEntry.Gui.EnumHandler.EnumDisplayOption.BUTTON)
    public Type type = Type.PROGRESS;

    public String reloadText = "Edit in config!";

    public boolean resetResources = false;

    @ConfigEntry.Gui.Tooltip
    public boolean reInitScreen = true;

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
}