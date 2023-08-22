package com.github.dimadencep.mods.rrls.mixins;

import com.github.dimadencep.mods.rrls.Rrls;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Overlay;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.concurrent.CompletableFuture;

@Mixin(MinecraftClient.class)
public abstract class MinecraftClientMixin {
    @Shadow
    protected abstract void showResourceReloadFailureToast(@Nullable Text description);

    @Shadow
    protected abstract CompletableFuture<Void> reloadResources(boolean force);

    @Inject(
            method = "onResourceReloadFailure",
            at = @At(
                    value = "HEAD"
            ),
            cancellable = true
    )
    public void onResourceReloadFailure(Throwable exception, Text resourceName, CallbackInfo ci) {
        if (!Rrls.MOD_CONFIG.resetResources) {
            Rrls.LOGGER.error("Caught error loading resourcepacks!", exception);

            this.reloadResources(true).thenRun(() -> this.showResourceReloadFailureToast(resourceName));

            ci.cancel();
        }
    }

    @Redirect(
            method = {
                    "tick",
                    "handleInputEvents"
            },
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/client/MinecraftClient;overlay:Lnet/minecraft/client/gui/screen/Overlay;"
            )
    )
    public Overlay fixOverlayUsage(MinecraftClient instance) {
        return Rrls.tryGetOverlay(instance.overlay);
    }
}
