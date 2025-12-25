/*
 * Copyright 2023 - 2025 dima_dencep.
 *
 * Licensed under the Open Software License, Version 3.0 (the "License");
 * you may not use this file except in compliance with the License.
 *
 * You may obtain a copy of the License at
 *     https://spdx.org/licenses/OSL-3.0.txt
 */

package org.redlance.dima_dencep.mods.rrls.mixins;

import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.systems.CommandEncoder;
import com.mojang.blaze3d.textures.GpuTexture;
import net.minecraft.client.gui.components.FocusableTextWidget;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.util.ARGB;
import net.minecraft.util.Mth;
import net.minecraft.util.Util;
import org.redlance.dima_dencep.mods.rrls.Rrls;
import org.redlance.dima_dencep.mods.rrls.RrlsConfig;
import org.redlance.dima_dencep.mods.rrls.config.Type;
import org.redlance.dima_dencep.mods.rrls.utils.DummyGuiGraphics;
import org.redlance.dima_dencep.mods.rrls.utils.OverlayHelper;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import org.redlance.dima_dencep.mods.rrls.utils.RainbowUtils;
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
import java.util.function.Consumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.LoadingOverlay;
import net.minecraft.client.gui.screens.Overlay;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.server.packs.resources.ReloadInstance;

@Mixin(LoadingOverlay.class)
public abstract class LoadingOverlayMixin extends Overlay {
    @Shadow
    @Final
    private Minecraft minecraft;
    @Shadow
    public float currentProgress;
    @Shadow
    private long fadeOutStart;
    @Shadow
    private long fadeInStart;
    @Shadow
    public abstract void drawProgressBar(GuiGraphics guiGraphics, int minX, int minY, int maxX, int maxY, float partialTick);

    @Unique
    private FocusableTextWidget rrls$textWidget;
    @Unique
    private long rrls$atEndStart = -1L;
    @Unique
    private boolean rrls$isFinished;

    @Inject(
            method = "<init>",
            at = @At(
                    value = "TAIL"
            )
    )
    private void rrls$init(Minecraft client, ReloadInstance reload, Consumer<Optional<Throwable>> onFinish, boolean fadeIn, CallbackInfo ci) {
        rrls$setState(OverlayHelper.lookupState(client.screen, fadeIn));

        if (RrlsConfig.type() == Type.TEXT_WITH_BACKGROUND) {
            this.rrls$textWidget = FocusableTextWidget.builder(Component.literal(RrlsConfig.reloadText()), minecraft.font)
                    .backgroundFill(FocusableTextWidget.BackgroundFill.ALWAYS)
                    .build();
        }
    }

    @Override
    public void rrls$miniRender(GuiGraphics graphics, float partialTick) {
        int i = graphics.guiWidth();
        int j = graphics.guiHeight();

        long fadeTime = RrlsConfig.interpolateAtEnd() ? this.rrls$atEndStart : this.fadeOutStart;

        float ease = 1.0F;
        if (RrlsConfig.interpolateProgress()) {
            ease -= RrlsConfig.easing().invoke(this.currentProgress, RrlsConfig.easingArg());

        } else if (fadeTime > -1L) {
            float f = (float)(Util.getMillis() - fadeTime) / RrlsConfig.animationSpeed();
            ease -= RrlsConfig.easing().invoke(Mth.clamp(f, 0.0F, 1.0F), RrlsConfig.easingArg());
        }

        if (ease <= 0.0F) this.rrls$isFinished = true;

        int easeAlpha = Mth.ceil(Mth.lerp(ease, 4.0F /* Fuck Font#adjustColor */, 255.0F));
        int easeColor = ARGB.color(easeAlpha, 255, 255, 255);

        switch (RrlsConfig.type()) {
            case Type.PROGRESS -> {
                int s = (int) ((double) j * 0.8325);
                int r = (int) (Math.min(i * 0.75, j) * 0.5);

                this.drawProgressBar(graphics, i / 2 - r, s - 5, i / 2 + r, s + 5, ease);
            }

            case Type.TEXT -> graphics.drawCenteredString(
                    minecraft.font, RrlsConfig.reloadText(), i / 2, 70,
                    RrlsConfig.rgbProgress() ? RainbowUtils.rainbowColor(easeAlpha) : easeColor
            );

            case Type.TEXT_WITH_BACKGROUND -> {
                if (rrls$textWidget != null) {
                    rrls$textWidget.setMaxWidth(i);
                    rrls$textWidget.setX(i / 2 - rrls$textWidget.getWidth() / 2);
                    rrls$textWidget.setY(j - j / 3);
                    rrls$textWidget.setAlpha(ARGB.from8BitChannel(easeAlpha));

                    if (rrls$textWidget.getMessage() instanceof MutableComponent mutable && RrlsConfig.rgbProgress()) {
                        mutable.withColor(RainbowUtils.rainbowColor(easeAlpha));
                    }

                    rrls$textWidget.render(graphics, 0, 0, partialTick);
                }
            }
            case NONE -> {}
        }
    }

    @Override
    public void rrls$resetProgress() {
        this.rrls$isFinished = false;
        this.currentProgress = 0;
        this.rrls$atEndStart = -1L;
        this.fadeOutStart = -1L;
        this.fadeInStart = -1L;
    }

    /*@Inject(
            method = "render",
            at = @At(
                    value = "HEAD"
            )
    )
    public void rrls$render(GuiGraphics context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        if (rrls$getState() != OverlayHelper.State.DEFAULT) { // Update attach (Optifine ❤️)
            rrls$setState(OverlayHelper.lookupState(minecraft.screen, rrls$getState() != OverlayHelper.State.WAIT));
        }
    }*/

    @WrapWithCondition(
            method = "render",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/screens/Screen;renderWithTooltipAndSubtitles(Lnet/minecraft/client/gui/GuiGraphics;IIF)V"
            )
    )
    public boolean rrls$screenrender(Screen instance, GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        return !(graphics instanceof DummyGuiGraphics);
    }

    @WrapWithCondition(
            method = "render",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/Minecraft;setOverlay(Lnet/minecraft/client/gui/screens/Overlay;)V"
            )
    )
    public boolean rrls$reinitScreen(Minecraft instance, Overlay loadingGui) {
        boolean isRemoved = rrls$getState() == OverlayHelper.State.DEFAULT || this.rrls$isFinished;
        if (isRemoved) Rrls.LOGGER.info("Overlay is removed!");

        return isRemoved;
    }

    @WrapWithCondition(
            method = "tick",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/screens/Screen;init(II)V"
            )
    )
    public boolean rrls$reinitScreen(Screen instance, int width, int height) {
        return RrlsConfig.reInitScreen();
    }

    @WrapOperation(
            method = "render",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/mojang/blaze3d/systems/CommandEncoder;clearColorTexture(Lcom/mojang/blaze3d/textures/GpuTexture;I)V",
                    remap = false
            )
    )
    public void rrls$_clearColor(CommandEncoder instance, GpuTexture gpuTexture, int i, Operation<Void> original, @Local(argsOnly = true) GuiGraphics graphics) {
        if (graphics instanceof DummyGuiGraphics) return;
        original.call(instance, gpuTexture, i);
    }

    @WrapOperation(
            method = "drawProgressBar",
            at = @At(
                    value = "INVOKE",
                    target = "Ljava/lang/Math;round(F)I"
            )
    )
    public int rrls$lerp(float i, Operation<Integer> original, @Local(argsOnly = true) float partialTick) {
        if (rrls$getState() != OverlayHelper.State.DEFAULT) {
            return original.call(Mth.lerp(partialTick, 0.0F, 255.0F));
        }

        return original.call(i);
    }

    @WrapOperation(
            method = "drawProgressBar",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/util/ARGB;color(IIII)I"
            )
    )
    public int rrls$rainbowProgress(int alpha, int red, int green, int blue, Operation<Integer> original, @Local(argsOnly = true) float partialTick) {
        if (RrlsConfig.rgbProgress() && rrls$getState() != OverlayHelper.State.DEFAULT) {
            return RainbowUtils.rainbowColor(partialTick);
        }

        return original.call(alpha, red, green, blue);
    }

    @Inject(
            method = "tick",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/client/gui/screens/LoadingOverlay;fadeOutStart:J",
                    ordinal = 0
            )
    )
    public void rrls$interpolateAtEnd(CallbackInfo ci) {
        if (this.rrls$atEndStart == -1L && this.currentProgress >= 0.99999F) {
            this.rrls$atEndStart = Util.getMillis();
        }
    }

    @ModifyConstant(
            method = "render",
            constant = {
                    @Constant(
                            floatValue = LoadingOverlay.FADE_OUT_TIME,
                            ordinal = 0
                    ),
                    @Constant(
                            floatValue = LoadingOverlay.FADE_IN_TIME,
                            ordinal = 0
                    )
            },
            require = 0
    )
    public float rrls$changeAnimationSpeed(float instance) {
        if (!rrls$getState().isRendering()) {
            return instance == LoadingOverlay.FADE_OUT_TIME ? RrlsConfig.animationSpeed() : RrlsConfig.animationSpeed() / 2;
        }

        return instance;
    }

    @Override // YAY Conflicts!!!
    public boolean isPauseScreen() {
        return super.isPauseScreen();
    }
}
