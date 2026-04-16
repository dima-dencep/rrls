/*
 * Copyright 2023 - 2026 dima_dencep.
 *
 * Licensed under the Open Software License, Version 3.0 (the "License");
 * you may not use this file except in compliance with the License.
 *
 * You may obtain a copy of the License at
 *     https://spdx.org/licenses/OSL-3.0.txt
 */

package org.redlance.dima_dencep.mods.rrls.mixins.workaround;

import com.google.common.collect.Sets;
import com.mojang.blaze3d.platform.Window;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.font.FontManager;
import net.minecraft.client.renderer.ShaderManager;
import net.minecraft.client.resources.SplashManager;
import net.minecraft.client.resources.language.LanguageManager;
import net.minecraft.client.resources.model.sprite.AtlasManager;
import net.minecraft.data.AtlasIds;
import net.minecraft.server.packs.PackResources;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.CloseableResourceManager;
import net.minecraft.server.packs.resources.MultiPackResourceManager;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.packs.resources.ReloadInstance;
import net.minecraft.server.packs.resources.ReloadableResourceManager;
import net.minecraft.util.Unit;
import net.minecraft.util.Util;
import org.redlance.dima_dencep.mods.rrls.Rrls;
import org.redlance.dima_dencep.mods.rrls.RrlsConfig;
import org.redlance.dima_dencep.mods.rrls.services.RrlsReloaderService;
import org.redlance.dima_dencep.mods.rrls.utils.AtlasUtils;
import org.redlance.dima_dencep.mods.rrls.utils.OverlayHelper;
import org.redlance.dima_dencep.mods.rrls.utils.WaitingSharedState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.BiConsumer;

@Mixin(ReloadableResourceManager.class)
public class ReloadableResourceManagerMixin {
    @Shadow
    private CloseableResourceManager resources;

    @Unique
    private static final Minecraft RRLS$MINECRAFT = Minecraft.getInstance();

    @Unique
    private final Set<PreparableReloadListener> rrls$listeners = Sets.newConcurrentHashSet();

    @Inject(
            method = "registerReloadListener",
            at = @At(
                    value = "TAIL"
            )
    )
    public void rrls$initReloader(PreparableReloadListener listener, CallbackInfo ci) {
        if (!OverlayHelper.isCurrentRenderingState()) {
            return;
        }

        if (this.resources.getNamespaces().isEmpty() || this.resources.getNamespaces().size() < 2 /* EBE workaround */) {
            this.resources.close();

            Rrls.LOGGER.info("Creating new resource manager!");
            this.resources = new MultiPackResourceManager(PackType.CLIENT_RESOURCES,
                    RRLS$MINECRAFT.getResourcePackRepository().openAllSelected()
            );

            List<CompletableFuture<Void>> futures = new ArrayList<>();

            RrlsReloaderService.INSTANCE.collectEarlyReloaders((ReloadableResourceManager) (Object) this, modListener -> {
                CompletableFuture<Void> future = new CompletableFuture<>();
                futures.add(future);

                rrls$reloadListener(modListener, Util.backgroundExecutor(), (_, throwable) -> {
                    if (throwable != null) {
                        future.completeExceptionally(throwable);
                    } else {
                        future.complete(null);
                    }
                });
            });

            if (!futures.isEmpty()) {
                Rrls.MOD_RELOADERS_FUTURE.set(CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])));
            }
        }

        if (listener instanceof FontManager fontManager &&
                !fontManager.fontSets.containsKey(Minecraft.DEFAULT_FONT)
        ) {
            rrls$reloadListener(fontManager, RRLS$MINECRAFT,
                    (_, _) -> rrls$refreshScreen()
            );
        }

        if (listener instanceof LanguageManager languageManager &&
                languageManager.getLanguages().size() <= 1
        ) {
            rrls$reloadListener(languageManager, Util.backgroundExecutor(), (_, _) -> {});
        }

        if (listener instanceof SplashManager splashManager &&
                splashManager.splashes.isEmpty()
        ) {
            rrls$reloadListener(splashManager, Util.backgroundExecutor(),
                    (_, _) -> rrls$refreshScreen()
            );
        }

        if (listener instanceof AtlasManager atlasManager &&
                atlasManager.spriteLookup.isEmpty()
        ) {
            AtlasUtils.reloadAtlas(atlasManager, (ReloadableResourceManager) (Object) this, atlasManager.atlasById.get(AtlasIds.GUI));
        }

        if (listener instanceof ShaderManager shaderManager &&
                shaderManager.compilationCache.configs == ShaderManager.Configs.EMPTY
        ) {
            rrls$reloadListener(shaderManager, RRLS$MINECRAFT, (_, _) -> {});
        }
    }

    @Inject(
            method = "createReload",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/packs/resources/SimpleReloadInstance;create(Lnet/minecraft/server/packs/resources/ResourceManager;Ljava/util/List;Ljava/util/concurrent/Executor;Ljava/util/concurrent/Executor;Ljava/util/concurrent/CompletableFuture;Z)Lnet/minecraft/server/packs/resources/ReloadInstance;"
            )
    )
    private void rrls$initReloaders(Executor backgroundExecutor, Executor gameExecutor, CompletableFuture<Unit> waitingFor, List<PackResources> resourcePacks, CallbackInfoReturnable<ReloadInstance> cir) {
        for (PreparableReloadListener listener : this.rrls$listeners) {
            rrls$reloadListener(listener, RRLS$MINECRAFT, (_, _) -> {});
        }
        this.rrls$listeners.clear();
    }

    @Unique
    @SuppressWarnings("ConstantConditions")
    private void rrls$reloadListener(PreparableReloadListener listener, Executor gameExecutor, BiConsumer<Void, Throwable> action) {
        try {
            Rrls.LOGGER.info("Quick reload listener '{}'", listener.getName());

            PreparableReloadListener.SharedState sharedState = new WaitingSharedState((ReloadableResourceManager) (Object) this);
            listener.prepareSharedState(sharedState);

            listener.reload(sharedState, Util.backgroundExecutor(), CompletableFuture::completedFuture, gameExecutor)
                    .whenCompleteAsync(action, RRLS$MINECRAFT);
        } catch (Throwable th) {
            this.rrls$listeners.add(listener);
            Rrls.LOGGER.warn("Failed to reload {}!", listener.getName(), th);
        }
    }

    @Unique
    private void rrls$refreshScreen() {
        if (RrlsConfig.INSTANCE.reInitScreen() && RRLS$MINECRAFT.screen != null) {
            Window window = RRLS$MINECRAFT.getWindow();
            RRLS$MINECRAFT.screen.init(window.getGuiScaledWidth(), window.getGuiScaledHeight());
        }
    }
}
