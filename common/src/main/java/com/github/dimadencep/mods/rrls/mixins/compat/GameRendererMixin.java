package com.github.dimadencep.mods.rrls.mixins.compat;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.GameRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(GameRenderer.class)
public class GameRendererMixin {
    @Redirect(
            method = "render",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/MinecraftClient;isFinishedLoading()Z"
            )
    )
    public boolean fix2(MinecraftClient instance) {
        if (instance.currentScreen == null) {
            instance.setScreen(null);
        }

        return true;
    }
}
