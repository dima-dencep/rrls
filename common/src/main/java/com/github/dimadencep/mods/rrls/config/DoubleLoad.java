package com.github.dimadencep.mods.rrls.config;

public enum DoubleLoad {
    FORCE_LOAD,
    LOAD,
    NONE;

    public boolean isLoad() {
        return this == FORCE_LOAD || this == LOAD;
    }
}
