package com.github.dimadencep.mods.rrls.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

@Config(name = "rrls")
public class ModConfig implements ConfigData {
    public boolean enabled = true;
    @ConfigEntry.Gui.Tooltip
    public boolean rgbText = false;
    @ConfigEntry.Gui.Tooltip
    public boolean loadingScreenHide = false;
    public boolean worldLoadingHide = true;
    public boolean showInGui = true;
    public boolean showInGame = true;
    public boolean resetResources = false;
    @ConfigEntry.Gui.Tooltip
    public String reloadText = "rrls.reloadresources";
}