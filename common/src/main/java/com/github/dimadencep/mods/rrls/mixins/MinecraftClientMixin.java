package com.github.dimadencep.mods.rrls.mixins;

import com.github.dimadencep.mods.rrls.MainMod;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.toast.SystemToast;
import net.minecraft.client.toast.ToastManager;
import net.minecraft.resource.ResourcePackManager;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Collections;
import java.util.concurrent.CompletableFuture;

@Mixin(MinecraftClient.class)
public abstract class MinecraftClientMixin {

    @Shadow public abstract CompletableFuture<Void> reloadResources();

    @Shadow public abstract ToastManager getToastManager();

    @Shadow @Final private static Logger LOGGER;

    @Shadow @Final private ResourcePackManager resourcePackManager;

    @Shadow @Final public GameOptions options;

    /**
     * @author dima_dencep
     * @reason fix on failed load resources
     */
    @Overwrite
    public void method_31186(Throwable exception, @Nullable Text resourceName) {
        LOGGER.info("Caught error loading resourcepacks, removing all selected resourcepacks", exception);

        if (MainMod.config.resetResources) {
            this.resourcePackManager.setEnabledProfiles(Collections.emptyList());
            this.options.resourcePacks.clear();
            this.options.incompatibleResourcePacks.clear();
            this.options.write();
        }

        this.reloadResources().thenRun(() -> {
            ToastManager toastManager = this.getToastManager();

            SystemToast.show(toastManager,
                    SystemToast.Type.PACK_LOAD_FAILURE,
                    new TranslatableText("resourcePack.load_fail"),
                    resourceName == null ? new TranslatableText("gui.all") : resourceName);
        });
    }
}