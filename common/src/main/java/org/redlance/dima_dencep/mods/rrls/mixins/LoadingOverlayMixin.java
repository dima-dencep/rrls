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
import net.minecraft.Util;
import net.minecraft.client.gui.components.FocusableTextWidget;
import net.minecraft.network.chat.Component;
import net.minecraft.util.ARGB;
import net.minecraft.util.Mth;
import org.objectweb.asm.Opcodes;
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
    private boolean rrls$isFinished;

    @Inject(
            method = "<init>",
            at = @At(
                    value = "TAIL"
            )
    )
    private void rrls$init(Minecraft client, ReloadInstance reload, Consumer<Optional<Throwable>> onFinish, boolean fadeIn, CallbackInfo ci) {
        rrls$setState(OverlayHelper.lookupState(client.screen, fadeIn));

        if (RrlsConfig.type() == Type.TEXT_WITH_BACKGROUND)
            rrls$textWidget = new FocusableTextWidget(1, Component.literal(RrlsConfig.reloadText()), minecraft.font, 12);
    }

    @Override
    public void rrls$miniRender(GuiGraphics graphics, float partialTick) {
        int i = graphics.guiWidth();
        int j = graphics.guiHeight();

        float ease = 1.0F;
        if (RrlsConfig.interpolateProgress()) {
            ease -= RrlsConfig.easing().invoke(this.currentProgress, RrlsConfig.easingArg());

        } else if (this.fadeOutStart > -1L) {
            float f = (float)(Util.getMillis() - this.fadeOutStart) / RrlsConfig.animationSpeed();
            ease -= RrlsConfig.easing().invoke(Mth.clamp(f, 0.0F, 1.0F), RrlsConfig.easingArg());
        }

        if (this.rrls$isFinished && ease <= 0.0F) {
            this.minecraft.setOverlay(null);
        }

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
                    rrls$textWidget.setColor(easeColor);

                    if (RrlsConfig.rgbProgress())
                        rrls$textWidget.setColor(RainbowUtils.rainbowColor(easeAlpha));

                    // This will make sure the widget is rendered above other widgets in Pause screen
                    graphics.pose().pushPose();
                    graphics.pose().translate(0, 0, 255);

                    rrls$textWidget.render(graphics, 0, 0, partialTick);

                    graphics.pose().popPose();
                }
            }
            case NONE -> {}
        }
    }

    @Override
    public void rrls$resetProgress() {
        this.currentProgress = 0;
        this.fadeOutStart = -1L;
        this.fadeInStart = -1L;
    }

    @Inject(
            method = "render",
            at = @At(
                    value = "HEAD"
            )
    )
    public void rrls$render(GuiGraphics context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        if (rrls$getState() != OverlayHelper.State.DEFAULT) // Update attach (Optifine ❤️)
            rrls$setState(OverlayHelper.lookupState(minecraft.screen, rrls$getState() != OverlayHelper.State.WAIT));
    }

    @WrapWithCondition(
            method = "render",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/screens/Screen;render(Lnet/minecraft/client/gui/GuiGraphics;IIF)V"
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
        return rrls$getState() == OverlayHelper.State.DEFAULT || this.currentProgress >= 0.999F;
    }

    @WrapWithCondition(
            method = "render",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/screens/Screen;init(Lnet/minecraft/client/Minecraft;II)V"
            )
    )
    public boolean rrls$reinitScreen(Screen instance, Minecraft minecraft, int width, int height) {
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

    @WrapOperation(
            method = "render",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/client/gui/screens/LoadingOverlay;fadeOutStart:J",
                    ordinal = 2
            )
    )
    public long rrls$interpolateAtEnd(LoadingOverlay instance, Operation<Long> original) {
        if (rrls$getState() == OverlayHelper.State.DEFAULT) return original.call(instance);
        if (this.fadeOutStart == -1L && this.currentProgress >= 0.999F) {
            this.fadeOutStart = Util.getMillis();
        }
        if (RrlsConfig.interpolateAtEnd()) {
            return this.rrls$isFinished ? 1L : -1L;
        }
        return original.call(instance);
    }

    @WrapOperation(
            method = "render",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/client/gui/screens/LoadingOverlay;fadeOutStart:J",
                    opcode = Opcodes.PUTFIELD
            )
    )
    public void rrls$(LoadingOverlay instance, long value, Operation<Void> original) {
        this.rrls$isFinished = true;
        if (rrls$getState() == OverlayHelper.State.DEFAULT || !RrlsConfig.interpolateAtEnd()) {
            original.call(instance, value);
        }
    }

    @ModifyConstant(
            method = "render",
            constant = {
                    @Constant(
                            floatValue = 1000.0F,
                            ordinal = 0
                    ),
                    @Constant(
                            floatValue = 500.0F,
                            ordinal = 0
                    )
            },
            require = 0
    )
    public float rrls$changeAnimationSpeed(float instance) {
        if (!rrls$getState().isRendering()) {
            return instance == 1000.0F ? RrlsConfig.animationSpeed() : RrlsConfig.animationSpeed() / 2;
        }

        return instance;
    }

    @Override // YAY Conflicts!!!
    public boolean isPauseScreen() {
        return super.isPauseScreen();
    }
}
