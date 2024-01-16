package com.github.dimadencep.mods.rrls.config;

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
