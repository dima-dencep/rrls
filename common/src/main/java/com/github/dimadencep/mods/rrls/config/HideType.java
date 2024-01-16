package com.github.dimadencep.mods.rrls.config;

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
