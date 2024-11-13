/*
 * Copyright 2023 - 2024 dima_dencep.
 *
 * Licensed under the Open Software License, Version 3.0 (the "License");
 * you may not use this file except in compliance with the License.
 *
 * You may obtain a copy of the License at
 *     https://spdx.org/licenses/OSL-3.0.txt
 */

package org.redlance.dima_dencep.mods.rrls.mixins.workaround;

import com.google.common.collect.Lists;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiSpriteManager;
import net.minecraft.client.gui.font.FontManager;
import net.minecraft.client.renderer.ShaderManager;
import net.minecraft.client.resources.SplashManager;
import net.minecraft.client.resources.language.LanguageManager;
import net.minecraft.server.packs.PackResources;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.CloseableResourceManager;
import net.minecraft.server.packs.resources.MultiPackResourceManager;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.packs.resources.ReloadInstance;
import net.minecraft.server.packs.resources.ReloadableResourceManager;
import net.minecraft.util.Unit;
import org.redlance.dima_dencep.mods.rrls.Rrls;
import org.redlance.dima_dencep.mods.rrls.RrlsConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
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
    private final List<PreparableReloadListener> rrls$listeners = Lists.newArrayList();

    @Inject(
            method = "registerReloadListener",
            at = @At(
                    value = "TAIL"
            )
    )
    public void rrls$initReloader(PreparableReloadListener listener, CallbackInfo ci) {
        if (!RrlsConfig.hideType().forceClose()) {
            return;
        }

        if (listener instanceof FontManager fontManager &&
                !fontManager.fontSets.containsKey(Minecraft.DEFAULT_FONT)
        ) {
            rrls$reloadListener(fontManager, RRLS$MINECRAFT,
                    (unused, throwable) -> rrls$refreshScreen()
            );
        }

        if (listener instanceof LanguageManager languageManager &&
                languageManager.getLanguages().size() <= 1
        ) {
            rrls$reloadListener(languageManager, Util.backgroundExecutor(), (unused, throwable) -> {});
        }

        if (listener instanceof SplashManager splashManager &&
                splashManager.splashes.isEmpty()
        ) {
            rrls$reloadListener(splashManager, Util.backgroundExecutor(),
                    (unused, throwable) -> rrls$refreshScreen()
            );
        }

        if (listener instanceof GuiSpriteManager spriteManager &&
                spriteManager.textureAtlas.texturesByName.isEmpty()
        ) {
            rrls$reloadListener(spriteManager, RRLS$MINECRAFT, (unused, throwable) -> {});
        }

        if (listener instanceof ShaderManager shaderManager &&
                shaderManager.compilationCache.configs == ShaderManager.Configs.EMPTY
        ) {
            rrls$reloadListener(shaderManager, RRLS$MINECRAFT, (unused, throwable) -> {});
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
            rrls$reloadListener(listener, RRLS$MINECRAFT, (unused, throwable) -> {});
        }
        this.rrls$listeners.clear();
    }

    @Unique
    private void rrls$reloadListener(PreparableReloadListener listener, Executor gameExecutor, BiConsumer<Void, Throwable> action) {
        try {
            if (this.resources.getNamespaces().isEmpty() || this.resources.getNamespaces().size() < 2 /* EBE workaround */) {
                this.resources.close();

                Rrls.LOGGER.info("Creating new resource manager!");
                this.resources = new MultiPackResourceManager(PackType.CLIENT_RESOURCES,
                        RRLS$MINECRAFT.getResourcePackRepository().openAllSelected()
                );
            }

            Rrls.LOGGER.info("Quick reload listener '{}'", listener.getName());

            listener.reload(
                    CompletableFuture::completedFuture, (ReloadableResourceManager) (Object) this, Util.backgroundExecutor(), gameExecutor
            ).whenCompleteAsync(action, Util.backgroundExecutor());

        } catch (Throwable th) {
            this.rrls$listeners.add(listener);
            Rrls.LOGGER.warn("Failed to reload {}!", listener.getName(), th);
        }
    }

    @Unique
    private void rrls$refreshScreen() {
        if (RrlsConfig.reInitScreen() && RRLS$MINECRAFT.screen != null) {
            RRLS$MINECRAFT.screen.init(RRLS$MINECRAFT,
                    RRLS$MINECRAFT.getWindow().getGuiScaledWidth(), RRLS$MINECRAFT.getWindow().getGuiScaledHeight()
            );
        }
    }
}
