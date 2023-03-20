package com.github.dimadencep.mods.rrls.mixins;

import com.github.dimadencep.mods.rrls.MainMod;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.DownloadingTerrainScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.concurrent.CompletableFuture;

@Mixin(MinecraftClient.class)
public abstract class MinecraftClientMixin {
    @Shadow protected abstract void showResourceReloadFailureToast(@Nullable Text description);
    @Shadow protected abstract CompletableFuture<Void> reloadResources(boolean force);

    @Shadow public abstract void setScreen(@Nullable Screen screen);

    @Inject(
            method = "onResourceReloadFailure",
            at = @At(
                    value = "HEAD"
            ),
            cancellable = true
    )
    public void onResourceReloadFailure(Throwable exception, Text resourceName, CallbackInfo ci) {
        if (!MainMod.config.resetResources) {
            this.reloadResources(true).thenRun(() -> this.showResourceReloadFailureToast(resourceName));

            ci.cancel();
        }
    }

    @ModifyArg(
            method = "showResourceReloadFailureToast",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/toast/SystemToast;show(Lnet/minecraft/client/toast/ToastManager;Lnet/minecraft/client/toast/SystemToast$Type;Lnet/minecraft/text/Text;Lnet/minecraft/text/Text;)V"
            ),
            index = 3
    )
    public Text createDisplay(Text resourceName) {
        return resourceName == null ? Text.translatable("gui.all") : resourceName;
    }

    @Inject(
            method = "setScreen",
            at = @At(
                    value = "HEAD"
            ),
            cancellable = true
    )
    public void setScreen(Screen screen, CallbackInfo ci) {
        if (MainMod.config.worldLoadingHide && screen instanceof DownloadingTerrainScreen) {
            setScreen(null);

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
            cir.setReturnValue(CompletableFuture.runAsync(() -> this.showResourceReloadFailureToast(Text.translatable("rrls.alreadyReloading"))));
        }
    }
}
