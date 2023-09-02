package com.github.dimadencep.mods.rrls.accessor;

import net.minecraft.client.gui.DrawContext;

public interface SplashAccessor {

    @Deprecated(forRemoval = true, since = "2.2.0")
    default boolean isAttached() {
        return rrls$isAttached();
    }

    boolean rrls$isAttached();

    void rrls$render(DrawContext context, boolean isGame);

    void rrls$reload();
}
