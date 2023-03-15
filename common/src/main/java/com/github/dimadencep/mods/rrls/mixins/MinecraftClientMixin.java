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

            SystemToast.show(toastManager,
                    SystemToast.Type.PACK_LOAD_FAILURE,
                    Text.translatable("resourcePack.load_fail"),
                    resourceName == null ? Text.translatable("gui.all") : resourceName);
        });
    }
}