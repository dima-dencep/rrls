/*
 * Copyright 2023 dima_dencep.
 *
 * Licensed under the Open Software License, Version 3.0 (the "License");
 * you may not use this file except in compliance with the License.
 *
 * You may obtain a copy of the License at
 *     https://github.com/dima-dencep/rrls/blob/HEAD/LICENSE
 */

package com.github.dimadencep.mods.rrls.utils;

import java.util.List;
import java.util.Optional;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.ScreenRect;
import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.client.gui.tooltip.TooltipPositioner;
import net.minecraft.client.item.TooltipData;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.texture.Scaling;
import net.minecraft.client.texture.Sprite;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.OrderedText;
import net.minecraft.text.StringVisitable;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings({"unused", "deprecation"})
public class DummyDrawContext extends DrawContext {
    public static final DummyDrawContext INSTANCE = new DummyDrawContext();

    private DummyDrawContext() {
        super(MinecraftClient.getInstance(), null);
    }

    @Override
    @Deprecated
    public void draw(Runnable drawCallback) {
    }

    @Override
    @Deprecated
    public void tryDraw() {
    }

    @Override
    @Deprecated
    public void drawIfRunning() {
    }

    @Override
    public void draw() {
    }

    @Override
    public void drawHorizontalLine(int x1, int x2, int y, int color) {
    }

    @Override
    public void drawHorizontalLine(RenderLayer layer, int x1, int x2, int y, int color) {
    }

    @Override
    public void drawVerticalLine(int x, int y1, int y2, int color) {
    }

    @Override
    public void drawVerticalLine(RenderLayer layer, int x, int y1, int y2, int color) {
    }

    @Override
    public void enableScissor(int x1, int y1, int x2, int y2) {
    }

    @Override
    public void disableScissor() {
    }

    @Override
    public void setScissor(@Nullable ScreenRect rect) {
    }

    @Override
    public void setShaderColor(float red, float green, float blue, float alpha) {
    }

    @Override
    public void fill(int x1, int y1, int x2, int y2, int color) {
    }

    @Override
    public void fill(int x1, int y1, int x2, int y2, int z, int color) {
    }

    @Override
    public void fill(RenderLayer layer, int x1, int y1, int x2, int y2, int color) {
    }

    @Override
    public void fill(RenderLayer layer, int x1, int y1, int x2, int y2, int z, int color) {
    }

    @Override
    public void fillGradient(int startX, int startY, int endX, int endY, int colorStart, int colorEnd) {
    }

    @Override
    public void fillGradient(int startX, int startY, int endX, int endY, int z, int colorStart, int colorEnd) {
    }

    @Override
    public void fillGradient(RenderLayer layer, int startX, int startY, int endX, int endY, int colorStart, int colorEnd, int z) {
    }

    @Override
    public void fillGradient(VertexConsumer vertexConsumer, int startX, int startY, int endX, int endY, int z, int colorStart, int colorEnd) {
    }

    @Override
    public void drawCenteredTextWithShadow(TextRenderer textRenderer, String text, int centerX, int y, int color) {
    }

    @Override
    public void drawCenteredTextWithShadow(TextRenderer textRenderer, Text text, int centerX, int y, int color) {
    }

    @Override
    public void drawCenteredTextWithShadow(TextRenderer textRenderer, OrderedText text, int centerX, int y, int color) {
    }

    @Override
    public int drawTextWithShadow(TextRenderer textRenderer, @Nullable String text, int x, int y, int color) {
        return -1;
    }

    @Override
    public int drawText(TextRenderer textRenderer, @Nullable String text, int x, int y, int color, boolean shadow) {
        return -1;
    }

    @Override
    public int drawTextWithShadow(TextRenderer textRenderer, OrderedText text, int x, int y, int color) {
        return -1;
    }

    @Override
    public int drawText(TextRenderer textRenderer, OrderedText text, int x, int y, int color, boolean shadow) {
        return -1;
    }

    @Override
    public int drawTextWithShadow(TextRenderer textRenderer, Text text, int x, int y, int color) {
        return -1;
    }

    @Override
    public int drawText(TextRenderer textRenderer, Text text, int x, int y, int color, boolean shadow) {
        return -1;
    }

    @Override
    public void drawTextWrapped(TextRenderer textRenderer, StringVisitable text, int x, int y, int width, int color) {
    }

    @Override
    public void drawSprite(int x, int y, int z, int width, int height, Sprite sprite) {
    }

    @Override
    public void drawSprite(int x, int y, int z, int width, int height, Sprite sprite, float red, float green, float blue, float alpha) {
    }

    @Override
    public void drawBorder(int x, int y, int width, int height, int color) {
    }

    @Override
    public void drawGuiTexture(Identifier texture, int x, int y, int width, int height) {
    }

    @Override
    public void drawGuiTexture(Identifier texture, int x, int y, int z, int width, int height) {
    }

    @Override
    public void drawGuiTexture(Identifier texture, int i, int j, int k, int l, int x, int y, int width, int height) {
    }

    @Override
    public void drawGuiTexture(Identifier texture, int i, int j, int k, int l, int x, int y, int z, int width, int height) {
    }

    @Override
    public void drawSprite(Sprite sprite, int i, int j, int k, int l, int x, int y, int z, int width, int height) {
    }

    @Override
    public void drawSprite(Sprite sprite, int x, int y, int z, int width, int height) {
    }

    @Override
    public void drawTexture(Identifier texture, int x, int y, int u, int v, int width, int height) {
    }

    @Override
    public void drawTexture(Identifier texture, int x, int y, int z, float u, float v, int width, int height, int textureWidth, int textureHeight) {
    }

    @Override
    public void drawTexture(Identifier texture, int x, int y, int width, int height, float u, float v, int regionWidth, int regionHeight, int textureWidth, int textureHeight) {
    }

    @Override
    public void drawTexture(Identifier texture, int x, int y, float u, float v, int width, int height, int textureWidth, int textureHeight) {
    }

    @Override
    public void drawTexture(Identifier texture, int x1, int x2, int y1, int y2, int z, int regionWidth, int regionHeight, float u, float v, int textureWidth, int textureHeight) {
    }

    @Override
    public void drawTexturedQuad(Identifier texture, int x1, int x2, int y1, int y2, int z, float u1, float u2, float v1, float v2) {
    }

    @Override
    public void drawTexturedQuad(Identifier texture, int x1, int x2, int y1, int y2, int z, float u1, float u2, float v1, float v2, float red, float green, float blue, float alpha) {
    }

    @Override
    public void drawSprite(Sprite sprite, Scaling.NineSlice nineSlice, int x, int y, int z, int width, int height) {
    }

    @Override
    public void drawSpriteTiled(Sprite sprite, int x, int y, int z, int width, int height, int i, int j, int tileWidth, int tileHeight, int k, int l) {
    }

    @Override
    public void drawItem(ItemStack item, int x, int y) {
    }

    @Override
    public void drawItem(ItemStack stack, int x, int y, int seed) {
    }

    @Override
    public void drawItem(ItemStack stack, int x, int y, int seed, int z) {
    }

    @Override
    public void drawItemWithoutEntity(ItemStack stack, int x, int y) {
    }

    @Override
    public void drawItemWithoutEntity(ItemStack stack, int x, int y, int seed) {
    }

    @Override
    public void drawItem(LivingEntity entity, ItemStack stack, int x, int y, int seed) {
    }

    @Override
    public void drawItem(@Nullable LivingEntity entity, @Nullable World world, ItemStack stack, int x, int y, int seed) {
    }

    @Override
    public void drawItem(@Nullable LivingEntity entity, @Nullable World world, ItemStack stack, int x, int y, int seed, int z) {
    }

    @Override
    public void drawItemInSlot(TextRenderer textRenderer, ItemStack stack, int x, int y) {
    }

    @Override
    public void drawItemInSlot(TextRenderer textRenderer, ItemStack stack, int x, int y, @Nullable String countOverride) {
    }

    @Override
    public void drawItemTooltip(TextRenderer textRenderer, ItemStack stack, int x, int y) {
    }

    @Override
    public void drawTooltip(TextRenderer textRenderer, List<Text> text, Optional<TooltipData> data, int x, int y) {
    }

    @Override
    public void drawTooltip(TextRenderer textRenderer, Text text, int x, int y) {
    }

    @Override
    public void drawTooltip(TextRenderer textRenderer, List<Text> text, int x, int y) {
    }

    @Override
    public void drawOrderedTooltip(TextRenderer textRenderer, List<? extends OrderedText> text, int x, int y) {
    }

    @Override
    public void drawTooltip(TextRenderer textRenderer, List<OrderedText> text, TooltipPositioner positioner, int x, int y) {
    }

    @Override
    public void drawTooltip(TextRenderer textRenderer, List<TooltipComponent> components, int x, int y, TooltipPositioner positioner) {
    }

    @Override
    public void drawHoverEvent(TextRenderer textRenderer, @Nullable Style style, int x, int y) {
    }
}
