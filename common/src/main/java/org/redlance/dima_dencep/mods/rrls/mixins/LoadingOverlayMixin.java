/*
 * Copyright 2023 - 2024 dima_dencep.
 *
 * Licensed under the Open Software License, Version 3.0 (the "License");
 * you may not use this file except in compliance with the License.
 *
 * You may obtain a copy of the License at
 *     https://spdx.org/licenses/OSL-3.0.txt
 */

package org.redlance.dima_dencep.mods.rrls.mixins;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.gui.components.FocusableTextWidget;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
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
    protected abstract void drawProgressBar(GuiGraphics guiGraphics, int minX, int minY, int maxX, int maxY, float partialTick);
    @Shadow
    private static int replaceAlpha(int color, int alpha) {
        return 0;
    }

    @Unique
    private FocusableTextWidget rrls$textWidget;
    @Unique
    private float rrls$fadeOutTime;

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
    public void rrls$miniRender(GuiGraphics graphics) {
        int i = graphics.guiWidth();
        int j = graphics.guiHeight();

        switch (RrlsConfig.type()) {
            case Type.PROGRESS -> {
                int s = (int) ((double) j * 0.8325);
                int r = (int) (Math.min(i * 0.75, j) * 0.5);

                this.drawProgressBar(graphics, i / 2 - r, s - 5, i / 2 + r, s + 5, this.rrls$fadeOutTime);
            }

            case Type.TEXT -> graphics.drawCenteredString(
                    minecraft.font, RrlsConfig.reloadText(), i / 2, 70,
                    RrlsConfig.rgbProgress() ? RainbowUtils.rainbowColor() : -1
            );

            case Type.TEXT_WITH_BACKGROUND -> {
                if (rrls$textWidget != null) {
                    rrls$textWidget.setMaxWidth(i);
                    rrls$textWidget.setX(i / 2 - rrls$textWidget.getWidth() / 2);
                    rrls$textWidget.setY(j - j / 3);

                    if (RrlsConfig.rgbProgress())
                        rrls$textWidget.setColor(RainbowUtils.rainbowColor());

                    // This will make sure the widget is rendered above other widgets in Pause screen
                    graphics.pose().pushPose();
                    graphics.pose().translate(0, 0,255);

                    rrls$textWidget.render(graphics, 0, 0, this.rrls$fadeOutTime);

                    graphics.pose().popPose();
                }
            }
            case NONE -> {}
        }
    }

    @Inject(
            method = "render",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/Minecraft;setOverlay(Lnet/minecraft/client/gui/screens/Overlay;)V"
            )
    )
    public void rrls$onLoadDone(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick, CallbackInfo ci) {
        this.rrls$textWidget = null;
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

    @Inject(
            method = "render",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/client/gui/screens/LoadingOverlay;fadeInStart:J",
                    ordinal = 2
            )
    )
    public void rrls$hookPartialTick(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick, CallbackInfo ci, @Local(ordinal = 1) float f) {
        this.rrls$fadeOutTime = 1.0F - Mth.clamp(f, 0.0F, 1.0F);
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
                    target = "Lcom/mojang/blaze3d/platform/GlStateManager;_clear(I)V",
                    remap = false
            )
    )
    public void rrls$_clear(int i, Operation<Void> original, @Local(argsOnly = true) GuiGraphics graphics) {
        if (graphics instanceof DummyGuiGraphics) {
            return;
        }
        original.call(i);
    }

    @WrapOperation(
            method = "render",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/mojang/blaze3d/platform/GlStateManager;_clearColor(FFFF)V",
                    remap = false
            )
    )
    public void rrls$_clearColor(float red, float green, float blue, float alpha, Operation<Void> original, @Local(argsOnly = true) GuiGraphics graphics) {
        if (graphics instanceof DummyGuiGraphics) {
            return;
        }
        original.call(red, green, blue, alpha);
    }

    @WrapOperation(
            method = "drawProgressBar",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/util/ARGB;color(IIII)I"
            )
    )
    public int rrls$rainbowProgress(int alpha, int red, int green, int blue, Operation<Integer> original) {
        if (RrlsConfig.rgbProgress() && rrls$getState() != OverlayHelper.State.DEFAULT) {
            return replaceAlpha(RainbowUtils.rainbowColor(), alpha);
        }

        return original.call(alpha, red, green, blue);
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
