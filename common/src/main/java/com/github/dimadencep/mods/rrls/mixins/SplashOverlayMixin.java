/*
 * Copyright 2023 - 2024 dima_dencep.
 *
 * Licensed under the Open Software License, Version 3.0 (the "License");
 * you may not use this file except in compliance with the License.
 *
 * You may obtain a copy of the License at
 *     https://spdx.org/licenses/OSL-3.0.txt
 */

package com.github.dimadencep.mods.rrls.mixins;

import com.github.dimadencep.mods.rrls.ConfigExpectPlatform;
import com.github.dimadencep.mods.rrls.utils.DummyDrawContext;
import com.github.dimadencep.mods.rrls.utils.OverlayHelper;
import com.llamalad7.mixinextras.injector.WrapWithCondition;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Consumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.LoadingOverlay;
import net.minecraft.client.gui.screens.Overlay;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.server.packs.resources.ReloadInstance;

@Mixin(LoadingOverlay.class)
public abstract class SplashOverlayMixin extends Overlay {
    @Shadow
    public float progress;
    @Shadow
    @Final
    private Minecraft client;

    @Shadow
    protected abstract void renderProgressBar(GuiGraphics drawContext, int minX, int minY, int maxX, int maxY, float opacity);
    @Shadow
    private static int withAlpha(int color, int alpha) {
        return 0;
    }

    @Inject(
            method = "<init>",
            at = @At(
                    value = "TAIL"
            )
    )
    private void rrls$init(Minecraft client, ReloadInstance monitor, Consumer<Optional<Throwable>> exceptionHandler, boolean reloading, CallbackInfo ci) {
        rrls$setState(OverlayHelper.lookupState(client.screen, reloading));
    }

    @Override
    public void rrls$miniRender(GuiGraphics context) {
        int i = context.guiWidth();
        int j = context.guiHeight();

        switch (ConfigExpectPlatform.type()) {
            case PROGRESS -> {
                int s = (int) ((double) j * 0.8325);
                int r = (int) (Math.min(i * 0.75, j) * 0.5);

                this.renderProgressBar(context, i / 2 - r, s - 5, i / 2 + r, s + 5, 0.8F);
            }

            case TEXT -> context.drawCenteredString(
                    client.font, ConfigExpectPlatform.reloadText(), i / 2, 70,
                    ConfigExpectPlatform.rgbProgress() ? ThreadLocalRandom.current().nextInt(0, 0xFFFFFF) : -1
            );
        }
    }

    @Inject(
            method = "render",
            at = @At(
                    value = "HEAD"
            )
    )
    public void rrls$render(GuiGraphics context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        if (rrls$getState() != OverlayHelper.State.DEFAULT) // Update attach (Optifine ❤️)
            rrls$setState(OverlayHelper.lookupState(client.screen, rrls$getState() != OverlayHelper.State.WAIT));
    }

    @WrapWithCondition(
            method = "render",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/screen/Screen;render(Lnet/minecraft/client/gui/DrawContext;IIF)V"
            )
    )
    public boolean rrls$screenrender(Screen instance, GuiGraphics context, int mouseX, int mouseY, float delta) {
        return !(context instanceof DummyDrawContext);
    }

    @WrapWithCondition(
            method = "render",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/MinecraftClient;setOverlay(Lnet/minecraft/client/gui/screen/Overlay;)V"
            )
    )
    public boolean rrls$infinityLoading(Minecraft instance, Overlay overlay) {
        return ConfigExpectPlatform.removeOverlayAtEnd();
    }

    @WrapOperation(
            method = "render",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/mojang/blaze3d/platform/GlStateManager;_clear(IZ)V"
            )
    )
    public void rrls$_clear(int mask, boolean getError, Operation<Void> original) {
        if (!rrls$getState().isRendering()) {
            original.call(mask, getError);
        }
    }

    @WrapOperation(
            method = "render",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/mojang/blaze3d/platform/GlStateManager;_clearColor(FFFF)V"
            )
    )
    public void rrls$_clearColor(float red, float green, float blue, float alpha, Operation<Void> original) {
        if (!rrls$getState().isRendering()) {
            original.call(red, green, blue, alpha);
        }
    }

    @Unique
    private static float rrls$dvd$x = 0;
    @Unique
    private static float rrls$dvd$y = 0;
    @Unique
    private static int rrls$dvd$xdir = 1;
    @Unique
    private static int rrls$dvd$ydir = 1;
    @Unique
    private static int rrls$dvd$color = -1;

    @Inject(
            method = "renderProgressBar",
            at = @At(
                    value = "HEAD"
            )
    )
    public void rrls$dvdStart(GuiGraphics drawContext, int minX, int minY, int maxX, int maxY, float opacity, CallbackInfo ci) {
        if (!ConfigExpectPlatform.aprilFool().canUes())
            return;

        int sx = (drawContext.guiWidth() * 2) - (maxX - minX) * 2;
        float mul = 1f / sx;

        int sy = (drawContext.guiHeight() * 2) - (maxY - minY) * 2;
        float ymul = 1f / sy;

        drawContext.pose().pushPose();
        drawContext.pose().translate(rrls$dvd$x * sx - minX, rrls$dvd$y * sy - minY, 0.0F);

        rrls$dvd$x += mul * (progress * 5) * rrls$dvd$xdir;
        rrls$dvd$y += ymul * (progress * 5) * rrls$dvd$ydir;

        if (rrls$dvd$x > 0.5f) rrls$dvd$xdir = -1;
        if (rrls$dvd$y > 0.5f) rrls$dvd$ydir = -1;
        if (rrls$dvd$x < 0f) rrls$dvd$xdir = 1;
        if (rrls$dvd$y < 0f) rrls$dvd$ydir = 1;

        if (rrls$dvd$y < 0f || rrls$dvd$x < 0f || rrls$dvd$y > 0.5f || rrls$dvd$x > 0.5f)
            rrls$dvd$color = ThreadLocalRandom.current().nextInt(0, 0xFFFFFF);
    }

    @Inject(
            method = "renderProgressBar",
            at = @At(
                    value = "RETURN"
            )
    )
    public void rrls$dvdStop(GuiGraphics drawContext, int minX, int minY, int maxX, int maxY, float opacity, CallbackInfo ci) {
        if (ConfigExpectPlatform.aprilFool().canUes())
            drawContext.pose().popPose();
    }

    @WrapOperation(
            method = "renderProgressBar",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/util/math/ColorHelper$Argb;getArgb(IIII)I"
            )
    )
    public int rrls$rainbowProgress(int alpha, int red, int green, int blue, Operation<Integer> original) {
        if (ConfigExpectPlatform.aprilFool().canUes() && rrls$dvd$color != -1) {
            return withAlpha(rrls$dvd$color, alpha);
        }

        if (ConfigExpectPlatform.rgbProgress() && rrls$getState() != OverlayHelper.State.DEFAULT) {
            return withAlpha(ThreadLocalRandom.current().nextInt(0, 0xFFFFFF), alpha);
        }

        return original.call(alpha, red, green, blue);
    }

    @ModifyConstant(
            method = "render",
            constant = @Constant(
                    floatValue = 1000.0F,
                    ordinal = 0
            ),
            require = 0
    )
    public float rrls$changeAnimationSpeed(float instance) {
        return ConfigExpectPlatform.animationSpeed();
    }

    @Override // YAY Conflicts!!!
    public boolean isPauseScreen() {
        return super.isPauseScreen();
    }
}