/*
 * Copyright 2023 - 2024 dima_dencep.
 *
 * Licensed under the Open Software License, Version 3.0 (the "License");
 * you may not use this file except in compliance with the License.
 *
 * You may obtain a copy of the License at
 *     https://spdx.org/licenses/OSL-3.0.txt
 */

package org.redlance.dima_dencep.mods.rrls.utils;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipPositioner;
import net.minecraft.client.renderer.MultiBufferSource;
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

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

@SuppressWarnings("unused")
public class DummyGuiGraphics extends GuiGraphics {
    public static final DummyGuiGraphics INSTANCE = new DummyGuiGraphics();

    private DummyGuiGraphics() {
        super(Minecraft.getInstance(), null);
    }

    @Override
    public void flush() {
    }

    @Override
    public void hLine(int minX, int maxX, int y, int color) {
    }

    @Override
    public void hLine(RenderType renderType, int minX, int maxX, int y, int color) {
    }

    @Override
    public void vLine(int x, int minY, int maxY, int color) {
    }

    @Override
    public void vLine(RenderType renderType, int x, int minY, int maxY, int color) {
    }

    @Override
    public void enableScissor(int minX, int minY, int maxX, int maxY) {
    }

    @Override
    public void disableScissor() {
    }

    @Override
    public boolean containsPointInScissor(int x, int y) {
        return false;
    }

    @Override
    public void applyScissor(@Nullable ScreenRectangle rectangle) {
    }

    @Override
    public void fill(int minX, int minY, int maxX, int maxY, int color) {
    }

    @Override
    public void fill(int minX, int minY, int maxX, int maxY, int z, int color) {
    }

    @Override
    public void fill(RenderType renderType, int minX, int minY, int maxX, int maxY, int color) {
    }

    @Override
    public void fill(RenderType renderType, int minX, int minY, int maxX, int maxY, int z, int color) {
    }

    @Override
    public void fillGradient(int x1, int y1, int x2, int y2, int colorFrom, int colorTo) {
    }

    @Override
    public void fillGradient(int x1, int y1, int x2, int y2, int z, int colorFrom, int colorTo) {
    }

    @Override
    public void fillGradient(RenderType renderType, int x1, int y1, int x2, int y2, int colorFrom, int colorTo, int z) {
    }

    @Override
    public void fillGradient(VertexConsumer consumer, int x1, int y1, int x2, int y2, int z, int colorFrom, int colorTo) {
    }

    @Override
    public void fillRenderType(RenderType renderType, int x1, int y1, int x2, int y2, int z) {
    }

    @Override
    public void drawCenteredString(Font font, String text, int x, int y, int color) {
    }

    @Override
    public void drawCenteredString(Font font, Component text, int x, int y, int color) {
    }

    @Override
    public void drawCenteredString(Font font, FormattedCharSequence text, int x, int y, int color) {
    }

    @Override
    public int drawString(Font font, @Nullable String text, int x, int y, int color) {
        return -1;
    }

    @Override
    public int drawString(Font font, @Nullable String text, int x, int y, int color, boolean dropShadow) {
        return -1;
    }

    @Override
    public int drawString(Font font, FormattedCharSequence text, int x, int y, int color) {
        return -1;
    }

    @Override
    public int drawString(Font font, FormattedCharSequence text, int x, int y, int color, boolean dropShadow) {
        return -1;
    }

    @Override
    public int drawString(Font font, Component text, int x, int y, int color) {
        return -1;
    }

    @Override
    public int drawString(Font font, Component text, int x, int y, int color, boolean dropShadow) {
        return -1;
    }

    @Override
    public void drawWordWrap(Font font, FormattedText text, int x, int y, int lineWidth, int color) {
    }

    @Override
    public int drawStringWithBackdrop(Font font, Component text, int x, int y, int xOffset, int color) {
        return -1;
    }

    @Override
    public void renderOutline(int x, int y, int width, int height, int color) {
    }

    @Override
    public void blitSprite(Function<ResourceLocation, RenderType> function, ResourceLocation resourceLocation, int i, int j, int k, int l) {
    }

    @Override
    public void blitSprite(Function<ResourceLocation, RenderType> function, ResourceLocation resourceLocation, int i, int j, int k, int l, int m) {
    }

    @Override
    public void blitSprite(Function<ResourceLocation, RenderType> function, ResourceLocation resourceLocation, int i, int j, int k, int l, int m, int n, int o, int p) {
    }

    @Override
    public void blitSprite(Function<ResourceLocation, RenderType> function, TextureAtlasSprite textureAtlasSprite, int i, int j, int k, int l) {
    }

    @Override
    public void blitSprite(Function<ResourceLocation, RenderType> function, TextureAtlasSprite textureAtlasSprite, int i, int j, int k, int l, int m) {
    }

    @Override
    public void blitSprite(Function<ResourceLocation, RenderType> function, TextureAtlasSprite textureAtlasSprite, int i, int j, int k, int l, int m, int n, int o, int p, int q) {
    }

    @Override
    public void blitNineSlicedSprite(Function<ResourceLocation, RenderType> function, TextureAtlasSprite textureAtlasSprite, GuiSpriteScaling.NineSlice nineSlice, int i, int j, int k, int l, int m) {
    }

    @Override
    public void blitNineSliceInnerSegment(Function<ResourceLocation, RenderType> function, GuiSpriteScaling.NineSlice nineSlice, TextureAtlasSprite textureAtlasSprite, int i, int j, int k, int l, int m, int n, int o, int p, int q, int r, int s) {
    }

    @Override
    public void blitTiledSprite(Function<ResourceLocation, RenderType> function, TextureAtlasSprite textureAtlasSprite, int i, int j, int k, int l, int m, int n, int o, int p, int q, int r, int s) {
    }

    @Override
    public void blit(Function<ResourceLocation, RenderType> function, ResourceLocation resourceLocation, int i, int j, float f, float g, int k, int l, int m, int n, int o) {
    }

    @Override
    public void blit(Function<ResourceLocation, RenderType> function, ResourceLocation resourceLocation, int i, int j, float f, float g, int k, int l, int m, int n) {
    }

    @Override
    public void blit(Function<ResourceLocation, RenderType> function, ResourceLocation resourceLocation, int i, int j, float f, float g, int k, int l, int m, int n, int o, int p) {
    }

    @Override
    public void blit(Function<ResourceLocation, RenderType> function, ResourceLocation resourceLocation, int i, int j, float f, float g, int k, int l, int m, int n, int o, int p, int q) {
    }

    @Override
    public void innerBlit(Function<ResourceLocation, RenderType> function, ResourceLocation resourceLocation, int i, int j, int k, int l, float f, float g, float h, float m, int n) {
    }

    @Override
    public void renderItem(ItemStack stack, int x, int y) {
    }

    @Override
    public void renderItem(ItemStack stack, int x, int y, int seed) {
    }

    @Override
    public void renderItem(ItemStack stack, int x, int y, int seed, int guiOffset) {
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
    public void renderItem(@Nullable LivingEntity entity, @Nullable Level level, ItemStack stack, int x, int y, int seed) {
    }

    @Override
    public void renderItem(@Nullable LivingEntity entity, @Nullable Level level, ItemStack stack, int x, int y, int seed, int guiOffset) {
    }

    @Override
    public void renderItemDecorations(Font font, ItemStack stack, int x, int y) {
    }

    @Override
    public void renderItemDecorations(Font font, ItemStack stack, int x, int y, @Nullable String text) {
    }

    @Override
    public void renderTooltip(Font font, ItemStack stack, int mouseX, int mouseY) {
    }

    @Override
    public void renderTooltip(Font font, List<Component> tooltipLines, Optional<TooltipComponent> visualTooltipComponent, int mouseX, int mouseY) {
    }

    @Override
    public void renderTooltip(Font font, List<Component> list, Optional<TooltipComponent> optional, int i, int j, @Nullable ResourceLocation resourceLocation) {
    }

    @Override
    public void renderTooltip(Font font, Component text, int mouseX, int mouseY) {
    }

    @Override
    public void renderTooltip(Font font, Component component, int i, int j, @Nullable ResourceLocation resourceLocation) {
    }

    @Override
    public void renderComponentTooltip(Font font, List<Component> tooltipLines, int mouseX, int mouseY) {
    }

    @Override
    public void renderComponentTooltip(Font font, List<Component> list, int i, int j, @Nullable ResourceLocation resourceLocation) {
    }

    @Override
    public void renderTooltip(Font font, List<? extends FormattedCharSequence> tooltipLines, int mouseX, int mouseY) {
    }

    @Override
    public void renderTooltip(Font font, List<? extends FormattedCharSequence> list, int i, int j, @Nullable ResourceLocation resourceLocation) {
    }

    @Override
    public void renderTooltip(Font font, List<FormattedCharSequence> tooltipLines, ClientTooltipPositioner tooltipPositioner, int mouseX, int mouseY) {
    }

    @Override
    public void renderTooltipInternal(Font font, List<ClientTooltipComponent> list, int i, int j, ClientTooltipPositioner clientTooltipPositioner, @Nullable ResourceLocation resourceLocation) {
    }

    @Override
    public void renderItemBar(ItemStack itemStack, int i, int j) {
    }

    @Override
    public void renderItemCount(Font font, ItemStack itemStack, int i, int j, @Nullable String string) {
    }

    @Override
    public void renderItemCooldown(ItemStack itemStack, int i, int j) {
    }

    @Override
    public void renderComponentHoverEffect(Font font, @Nullable Style style, int mouseX, int mouseY) {
    }

    @Override
    public void drawSpecial(Consumer<MultiBufferSource> consumer) {
    }
}
