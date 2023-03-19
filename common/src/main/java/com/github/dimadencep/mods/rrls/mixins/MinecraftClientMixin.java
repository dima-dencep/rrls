package com.github.dimadencep.mods.rrls.mixins;

import com.github.dimadencep.mods.rrls.MainMod;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.toast.SystemToast;
import net.minecraft.client.toast.ToastManager;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.concurrent.CompletableFuture;

@Mixin(MinecraftClient.class)
public abstract class MinecraftClientMixin {
    @Shadow public abstract ToastManager getToastManager();

    @Shadow public abstract CompletableFuture<Void> reloadResources();

    @Inject(
            method = "method_31186",
            at = @At(
                    value = "HEAD"
            ),
            cancellable = true
    )
    public void method_31186(Throwable exception, Text resourceName, CallbackInfo ci) {
        if (!MainMod.config.resetResources) {
            this.reloadResources().thenRun(() -> {
                ToastManager toastManager = this.getToastManager();
                SystemToast.show(toastManager, SystemToast.Type.PACK_LOAD_FAILURE, new TranslatableText("resourcePack.load_fail"), resourceName == null ? new TranslatableText("gui.all") : resourceName);
            });

            ci.cancel();
        }
    }

    @Inject(
            method = "reloadResources",
            at = @At(
                    value = "HEAD"
            ),
            cancellable = true
    )
    public void reloadResources0(CallbackInfoReturnable<CompletableFuture<Void>> cir) {
        if (MainMod.reloadHandler.getReload() != null) {
            cir.setReturnValue(CompletableFuture.runAsync(() -> {
                ToastManager toastManager = this.getToastManager();
                SystemToast.show(toastManager, SystemToast.Type.PACK_LOAD_FAILURE, new TranslatableText("resourcePack.load_fail"), new TranslatableText("rrls.alreadyReloading"));
            }));
        }
    }
}