/*
 * Copyright 2023 - 2024 dima_dencep.
 *
 * Licensed under the Open Software License, Version 3.0 (the "License");
 * you may not use this file except in compliance with the License.
 *
 * You may obtain a copy of the License at
 *     https://spdx.org/licenses/OSL-3.0.txt
 */

package com.github.dimadencep.mods.rrls.utils;

import com.mojang.blaze3d.vertex.VertexConsumer;
import java.util.List;
import java.util.Optional;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipPositioner;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.metadata.gui.GuiSpriteScaling;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings({"unused", "deprecation"})
public class DummyGuiGraphics extends GuiGraphics {
    public static final DummyGuiGraphics INSTANCE = new DummyGuiGraphics();

    private DummyGuiGraphics() {
        super(Minecraft.getInstance(), null);
    }

    @Override
    @Deprecated
    public void drawManaged(Runnable drawCallback) {
    }

    @Override
    @Deprecated
    public void flushIfUnmanaged() {
    }

    @Override
    @Deprecated
    public void flushIfManaged() {
    }

    @Override
    public void flush() {
    }

    @Override
    public void hLine(int x1, int x2, int y, int color) {
    }

    @Override
    public void hLine(RenderType layer, int x1, int x2, int y, int color) {
    }

    @Override
    public void vLine(int x, int y1, int y2, int color) {
    }

    @Override
    public void vLine(RenderType layer, int x, int y1, int y2, int color) {
    }

    @Override
    public void enableScissor(int x1, int y1, int x2, int y2) {
    }

    @Override
    public void disableScissor() {
    }

    @Override
    public boolean containsPointInScissor(int i, int j) {
        return false;
    }

    @Override
    public void applyScissor(@Nullable ScreenRectangle rect) {
    }

    @Override
    public void setColor(float red, float green, float blue, float alpha) {
    }

    @Override
    public void fill(int x1, int y1, int x2, int y2, int color) {
    }

    @Override
    public void fill(int x1, int y1, int x2, int y2, int z, int color) {
    }

    @Override
    public void fill(RenderType layer, int x1, int y1, int x2, int y2, int color) {
    }

    @Override
    public void fill(RenderType layer, int x1, int y1, int x2, int y2, int z, int color) {
    }

    @Override
    public void fillGradient(int startX, int startY, int endX, int endY, int colorStart, int colorEnd) {
    }

    @Override
    public void fillGradient(int startX, int startY, int endX, int endY, int z, int colorStart, int colorEnd) {
    }

    @Override
    public void fillGradient(RenderType layer, int startX, int startY, int endX, int endY, int colorStart, int colorEnd, int z) {
    }

    @Override
    public void fillGradient(VertexConsumer vertexConsumer, int startX, int startY, int endX, int endY, int z, int colorStart, int colorEnd) {
    }

    @Override
    public void fillRenderType(RenderType renderType, int i, int j, int k, int l, int m) {
    }

    @Override
    public void drawCenteredString(Font textRenderer, String text, int centerX, int y, int color) {
    }

    @Override
    public void drawCenteredString(Font textRenderer, Component text, int centerX, int y, int color) {
    }

    @Override
    public void drawCenteredString(Font textRenderer, FormattedCharSequence text, int centerX, int y, int color) {
    }

    @Override
    public int drawString(Font textRenderer, @Nullable String text, int x, int y, int color) {
        return -1;
    }

    @Override
    public int drawString(Font textRenderer, @Nullable String text, int x, int y, int color, boolean shadow) {
        return -1;
    }

    @Override
    public int drawString(Font textRenderer, FormattedCharSequence text, int x, int y, int color) {
        return -1;
    }

    @Override
    public int drawString(Font textRenderer, FormattedCharSequence text, int x, int y, int color, boolean shadow) {
        return -1;
    }

    @Override
    public int drawString(Font textRenderer, Component text, int x, int y, int color) {
        return -1;
    }

    @Override
    public int drawString(Font textRenderer, Component text, int x, int y, int color, boolean shadow) {
        return -1;
    }

    @Override
    public void drawWordWrap(Font textRenderer, FormattedText text, int x, int y, int width, int color) {
    }

    @Override
    public void blit(int x, int y, int z, int width, int height, TextureAtlasSprite sprite) {
    }

    @Override
    public void blit(int x, int y, int z, int width, int height, TextureAtlasSprite sprite, float red, float green, float blue, float alpha) {
    }

    @Override
    public void renderOutline(int x, int y, int width, int height, int color) {
    }

    @Override
    public void blitSprite(ResourceLocation texture, int x, int y, int width, int height) {
    }

    @Override
    public void blitSprite(ResourceLocation texture, int x, int y, int z, int width, int height) {
    }

    @Override
    public void blitSprite(ResourceLocation texture, int i, int j, int k, int l, int x, int y, int width, int height) {
    }

    @Override
    public void blitSprite(ResourceLocation texture, int i, int j, int k, int l, int x, int y, int z, int width, int height) {
    }

    @Override
    public void blitSprite(TextureAtlasSprite sprite, int i, int j, int k, int l, int x, int y, int z, int width, int height) {
    }

    @Override
    public void blitSprite(TextureAtlasSprite sprite, int x, int y, int z, int width, int height) {
    }

    @Override
    public void blit(ResourceLocation texture, int x, int y, int u, int v, int width, int height) {
    }

    @Override
    public void blit(ResourceLocation texture, int x, int y, int z, float u, float v, int width, int height, int textureWidth, int textureHeight) {
    }

    @Override
    public void blit(ResourceLocation texture, int x, int y, int width, int height, float u, float v, int regionWidth, int regionHeight, int textureWidth, int textureHeight) {
    }

    @Override
    public void blit(ResourceLocation texture, int x, int y, float u, float v, int width, int height, int textureWidth, int textureHeight) {
    }

    @Override
    public void blit(ResourceLocation texture, int x1, int x2, int y1, int y2, int z, int regionWidth, int regionHeight, float u, float v, int textureWidth, int textureHeight) {
    }

    @Override
    public void innerBlit(ResourceLocation texture, int x1, int x2, int y1, int y2, int z, float u1, float u2, float v1, float v2) {
    }

    @Override
    public void innerBlit(ResourceLocation texture, int x1, int x2, int y1, int y2, int z, float u1, float u2, float v1, float v2, float red, float green, float blue, float alpha) {
    }

    @Override
    public void blitNineSlicedSprite(TextureAtlasSprite sprite, GuiSpriteScaling.NineSlice nineSlice, int x, int y, int z, int width, int height) {
    }

    @Override
    public void blitTiledSprite(TextureAtlasSprite sprite, int x, int y, int z, int width, int height, int i, int j, int tileWidth, int tileHeight, int k, int l) {
    }

    @Override
    public void renderItem(ItemStack item, int x, int y) {
    }

    @Override
    public void renderItem(ItemStack stack, int x, int y, int seed) {
    }

    @Override
    public void renderItem(ItemStack stack, int x, int y, int seed, int z) {
    }

    @Override
    public void renderFakeItem(ItemStack stack, int x, int y) {
    }

    @Override
    public void renderFakeItem(ItemStack stack, int x, int y, int seed) {
    }

    @Override
    public void renderItem(LivingEntity entity, ItemStack stack, int x, int y, int seed) {
    }

    @Override
    public void renderItem(@Nullable LivingEntity entity, @Nullable Level world, ItemStack stack, int x, int y, int seed) {
    }

    @Override
    public void renderItem(@Nullable LivingEntity entity, @Nullable Level world, ItemStack stack, int x, int y, int seed, int z) {
    }

    @Override
    public void renderItemDecorations(Font textRenderer, ItemStack stack, int x, int y) {
    }

    @Override
    public void renderItemDecorations(Font textRenderer, ItemStack stack, int x, int y, @Nullable String countOverride) {
    }

    @Override
    public void renderTooltip(Font textRenderer, ItemStack stack, int x, int y) {
    }

    @Override
    public void renderTooltip(Font textRenderer, List<Component> text, Optional<TooltipComponent> data, int x, int y) {
    }

    @Override
    public void renderTooltip(Font textRenderer, Component text, int x, int y) {
    }

    @Override
    public void renderComponentTooltip(Font textRenderer, List<Component> text, int x, int y) {
    }

    @Override
    public void renderTooltip(Font textRenderer, List<? extends FormattedCharSequence> text, int x, int y) {
    }

    @Override
    public void renderTooltip(Font textRenderer, List<FormattedCharSequence> text, ClientTooltipPositioner positioner, int x, int y) {
    }

    @Override
    public void renderTooltipInternal(Font textRenderer, List<ClientTooltipComponent> components, int x, int y, ClientTooltipPositioner positioner) {
    }

    @Override
    public void renderComponentHoverEffect(Font textRenderer, @Nullable Style style, int x, int y) {
    }
}
