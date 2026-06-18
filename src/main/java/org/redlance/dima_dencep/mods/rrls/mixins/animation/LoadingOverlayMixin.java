package org.redlance.dima_dencep.mods.rrls.mixins.animation;

import net.minecraft.client.gui.screens.LoadingOverlay;
import net.minecraft.client.gui.screens.Overlay;
import org.redlance.dima_dencep.mods.rrls.RrlsConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(value = LoadingOverlay.class, priority = 999)
public abstract class LoadingOverlayMixin extends Overlay {
    @ModifyConstant(
            method = "extractRenderState",
            constant = {
                    @Constant(
                            floatValue = LoadingOverlay.FADE_OUT_TIME,
                            ordinal = 0
                    ),
                    @Constant(
                            floatValue = LoadingOverlay.FADE_IN_TIME,
                            ordinal = 0
                    )
            },
            require = 0
    )
    public float rrls$changeAnimationSpeed(float instance) {
        if (!rrls$getState().isRendering()) {
            return instance == LoadingOverlay.FADE_OUT_TIME ? RrlsConfig.INSTANCE.animationSpeed() : RrlsConfig.INSTANCE.animationSpeed() / 2;
        }

        return instance;
    }
}
