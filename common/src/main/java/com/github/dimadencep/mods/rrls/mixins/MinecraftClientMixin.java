package com.github.dimadencep.mods.rrls.mixins;

import com.github.dimadencep.mods.rrls.MainMod;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.resource.ResourceReloadLogger;
import net.minecraft.resource.ResourcePackManager;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Collections;
import java.util.concurrent.CompletableFuture;

@Mixin(MinecraftClient.class)
public abstract class MinecraftClientMixin {
    @Shadow @Final private static Logger LOGGER;

    @Shadow protected abstract CompletableFuture<Void> reloadResources(boolean force);

    @Shadow @Final public GameOptions options;

    @Shadow @Final private ResourceReloadLogger resourceReloadLogger;

    @Shadow @Final private ResourcePackManager resourcePackManager;

    @Shadow protected abstract void showResourceReloadFailureToast(@Nullable Text description);

    /**
     * @author dima_dencep
     * @reason fix on failed load resources
     */
    @Overwrite
    public void onResourceReloadFailure(Throwable exception, @Nullable Text resourceName) {
        LOGGER.info("Caught error loading resourcepacks, removing all selected resourcepacks", exception);

        if (MainMod.config.resetResources) {
            this.resourceReloadLogger.recover(exception);
            this.resourcePackManager.setEnabledProfiles(Collections.emptyList());
            this.options.resourcePacks.clear();
            this.options.incompatibleResourcePacks.clear();
            this.options.write();
        }

        this.reloadResources(true).thenRun(() -> this.showResourceReloadFailureToast(resourceName == null ? Text.translatable("gui.all") : resourceName));
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