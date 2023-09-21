package com.github.dimadencep.mods.rrls.accessor;

import net.minecraft.client.gui.DrawContext;

public interface SplashAccessor {

    boolean rrls$isAttached();

    void rrls$render(DrawContext context, boolean isGame);

    void rrls$reload();
}
