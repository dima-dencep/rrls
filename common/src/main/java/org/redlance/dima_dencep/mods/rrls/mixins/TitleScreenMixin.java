/*
 * Copyright 2023 - 2024 dima_dencep.
 *
 * Licensed under the Open Software License, Version 3.0 (the "License");
 * you may not use this file except in compliance with the License.
 *
 * You may obtain a copy of the License at
 *     https://spdx.org/licenses/OSL-3.0.txt
 */

package org.redlance.dima_dencep.mods.rrls.mixins;

import net.minecraft.client.gui.screens.TitleScreen;
import org.redlance.dima_dencep.mods.rrls.RrlsConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(TitleScreen.class)
public class TitleScreenMixin {
    @ModifyConstant(
            method = "render",
            constant = @Constant(
                    floatValue = 2000.0F,
                    ordinal = 0
            ),
            require = 0
    )
    public float rrls$changeAnimationSpeed(float instance) {
        return RrlsConfig.animationSpeed() * 2;
    }
}
