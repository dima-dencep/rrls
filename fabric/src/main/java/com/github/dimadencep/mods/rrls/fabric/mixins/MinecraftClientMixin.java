package com.github.dimadencep.mods.rrls.fabric.mixins;

import com.github.dimadencep.mods.rrls.accessor.SplashAccessor;
import com.github.dimadencep.mods.rrls.fabric.RrlsFabric;
import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {
    @Inject(
            method = "render",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/render/GameRenderer;render(FJZ)V"
            )
    )
    public void handleReload(boolean tick, CallbackInfo ci) {
        RrlsFabric.getAccessor().ifPresent(SplashAccessor::rrls$reload);
    }
}
