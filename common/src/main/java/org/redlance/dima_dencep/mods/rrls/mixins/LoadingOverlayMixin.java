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

import org.redlance.dima_dencep.mods.rrls.ConfigExpectPlatform;
import org.redlance.dima_dencep.mods.rrls.utils.DummyGuiGraphics;
import org.redlance.dima_dencep.mods.rrls.utils.OverlayHelper;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
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

    @Inject(
            method = "<init>",
            at = @At(
                    value = "TAIL"
            )
    )
    private void rrls$init(Minecraft client, ReloadInstance reload, Consumer<Optional<Throwable>> onFinish, boolean fadeIn, CallbackInfo ci) {
        rrls$setState(OverlayHelper.lookupState(client.screen, fadeIn));
    }

    @Override
    public void rrls$miniRender(GuiGraphics graphics) {
        int i = graphics.guiWidth();
        int j = graphics.guiHeight();

        switch (ConfigExpectPlatform.type()) {
            case PROGRESS -> {
                int s = (int) ((double) j * 0.8325);
                int r = (int) (Math.min(i * 0.75, j) * 0.5);

                this.drawProgressBar(graphics, i / 2 - r, s - 5, i / 2 + r, s + 5, 0.8F);
            }

            case TEXT -> graphics.drawCenteredString(
                    minecraft.font, ConfigExpectPlatform.reloadText(), i / 2, 70,
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
    public boolean rrls$infinityLoading(Minecraft instance, Overlay overlay) {
        return ConfigExpectPlatform.removeOverlayAtEnd();
    }

    @WrapOperation(
            method = "render",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/mojang/blaze3d/platform/GlStateManager;_clear(IZ)V",
                    remap = false
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
                    target = "Lcom/mojang/blaze3d/platform/GlStateManager;_clearColor(FFFF)V",
                    remap = false
            )
    )
    public void rrls$_clearColor(float red, float green, float blue, float alpha, Operation<Void> original) {
        if (!rrls$getState().isRendering()) {
            original.call(red, green, blue, alpha);
        }
    }

    @WrapOperation(
            method = "drawProgressBar",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/util/FastColor$ARGB32;color(IIII)I"
            )
    )
    public int rrls$rainbowProgress(int alpha, int red, int green, int blue, Operation<Integer> original) {
        if (ConfigExpectPlatform.rgbProgress() && rrls$getState() != OverlayHelper.State.DEFAULT) {
            return replaceAlpha(ThreadLocalRandom.current().nextInt(0, 0xFFFFFF), alpha);
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
