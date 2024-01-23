/*
 * Copyright 2023 dima_dencep.
 *
 * Licensed under the Open Software License, Version 3.0 (the "License");
 * you may not use this file except in compliance with the License.
 *
 * You may obtain a copy of the License at
 *     https://github.com/dima-dencep/rrls/blob/HEAD/LICENSE
 */

package com.github.dimadencep.mods.rrls.mixins.compat;

import com.github.dimadencep.mods.rrls.duck.OverlayExtender;
import com.github.dimadencep.mods.rrls.utils.OverlayHelper;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.client.gui.screen.Overlay;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Overlay.class)
public class OverlayMixin implements OverlayExtender {
    @Unique
    public OverlayHelper.State rrls$state = OverlayHelper.State.DEFAULT;

    @Override
    public OverlayHelper.State rrls$getState() {
        return rrls$state;
    }

    @Override
    public void rrls$setState(OverlayHelper.State state) {
        this.rrls$state = state;
    }

    @ModifyReturnValue(
            method = "pausesGame",
            at = @At(
                    value = "RETURN"
            )
    )
    public boolean rrls$pausesGame(boolean original) {
        return !rrls$getState().isRendering() && original;
    }
}
