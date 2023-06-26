package com.github.dimadencep.mods.rrls.accessor;

import net.minecraft.client.util.math.MatrixStack;

public interface SplashAccessor {
    boolean isAttached();

    void render(MatrixStack context, boolean isGame);

    void reload();
}
