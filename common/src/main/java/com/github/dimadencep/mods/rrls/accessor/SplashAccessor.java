package com.github.dimadencep.mods.rrls.accessor;

import net.minecraft.client.gui.DrawContext;

public interface SplashAccessor {
    boolean isAttached();

    void render(DrawContext context, boolean isGame);

    void reload();
}
