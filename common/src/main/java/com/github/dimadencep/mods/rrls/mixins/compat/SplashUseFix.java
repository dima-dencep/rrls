package com.github.dimadencep.mods.rrls.mixins.compat;

import com.github.dimadencep.mods.rrls.Rrls;
import net.minecraft.client.Keyboard;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.Mouse;
import net.minecraft.client.gui.screen.Overlay;
import net.minecraft.client.render.GameRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = {
        GameRenderer.class,
        Keyboard.class,
        Mouse.class
})
public class SplashUseFix {
    @Redirect(
            method = "*",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/MinecraftClient;getOverlay()Lnet/minecraft/client/gui/screen/Overlay;"
            )
    )
    public Overlay fix(MinecraftClient instance) {
        return Rrls.tryGetOverlay(instance.overlay);
    }
}
