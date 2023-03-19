package com.github.dimadencep.mods.rrls.mixins;

import com.github.dimadencep.mods.rrls.MainMod;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.toast.SystemToast;
import net.minecraft.client.toast.ToastManager;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.concurrent.CompletableFuture;

@Mixin(MinecraftClient.class)
public abstract class MinecraftClientMixin {
    @Shadow protected abstract CompletableFuture<Void> reloadResources(boolean force);

    @Shadow public abstract ToastManager getToastManager();

    @Inject(
            method = "onResourceReloadFailure",
            at = @At(
                    value = "HEAD"
            ),
            cancellable = true
    )
    public void onResourceReloadFailure(Throwable exception, Text resourceName, CallbackInfo ci) {
        if (!MainMod.config.resetResources) {
            this.reloadResources(true).thenRun(() -> {
                ToastManager toastManager = this.getToastManager();
                SystemToast.show(toastManager, SystemToast.Type.PACK_LOAD_FAILURE, Text.translatable("resourcePack.load_fail"), resourceName == null ? Text.translatable("gui.all") : resourceName);
            });

            ci.cancel();
        }
    }

    @Inject(
            method = "reloadResources(Z)Ljava/util/concurrent/CompletableFuture;",
            at = @At(
                    value = "HEAD"
            ),
            cancellable = true
    )
    public void reloadResources(CallbackInfoReturnable<CompletableFuture<Void>> cir) {
        if (MainMod.reloadHandler.getReload() != null) {
            cir.setReturnValue(CompletableFuture.runAsync(() -> {
                ToastManager toastManager = this.getToastManager();
                SystemToast.show(toastManager, SystemToast.Type.PACK_LOAD_FAILURE, Text.translatable("resourcePack.load_fail"), Text.translatable("rrls.alreadyReloading"));
            }));
        }
    }
}