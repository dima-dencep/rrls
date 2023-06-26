package com.github.dimadencep.mods.rrls.mixins;

import com.github.dimadencep.mods.rrls.Rrls;
import com.github.dimadencep.mods.rrls.accessor.SplashAccessor;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Overlay;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.client.toast.SystemToast;
import net.minecraft.client.toast.ToastManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.concurrent.CompletableFuture;

@Mixin(MinecraftClient.class)
public abstract class MinecraftClientMixin {
    @Shadow
    public abstract ToastManager getToastManager();

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
        if (!Rrls.config.resetResources) {
            Rrls.logger.error("Caught error loading resourcepacks!", exception);

            this.reloadResources(true).thenRun(() -> SystemToast.show(getToastManager(), SystemToast.Type.PACK_LOAD_FAILURE, new TranslatableText("resourcePack.load_fail"), resourceName));

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

    @Inject(
            method = "getOverlay",
            at = @At(
                    value = "HEAD"
            )
    )
    public void getOverlay(CallbackInfoReturnable<Overlay> cir) { // TODO rewrite
        if (cir.getReturnValue() instanceof SplashAccessor accessor && accessor.isAttached()) {
            Class<?> callerClass = Rrls.classWalker.getCallerClass();

            Rrls.logger.warn("Illegal getOverlay access: {}", callerClass.getName());
        }
    }
}
