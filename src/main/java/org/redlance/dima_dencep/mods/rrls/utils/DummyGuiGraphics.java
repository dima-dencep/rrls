/*
 * Copyright 2023 - 2026 dima_dencep.
 *
 * Licensed under the Open Software License, Version 3.0 (the "License");
 * you may not use this file except in compliance with the License.
 *
 * You may obtain a copy of the License at
 *     https://spdx.org/licenses/OSL-3.0.txt
 */

package org.redlance.dima_dencep.mods.rrls.utils;

import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.platform.cursor.CursorType;
import com.mojang.blaze3d.textures.GpuSampler;
import com.mojang.blaze3d.textures.GpuTextureView;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ActiveTextCollector;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.render.TextureSetup;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipPositioner;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.object.banner.BannerFlagModel;
import net.minecraft.client.model.object.book.BookModel;
import net.minecraft.client.model.player.PlayerModel;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.state.MapRenderState;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.metadata.gui.GuiSpriteScaling;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.Identifier;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.profiling.ResultField;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BannerPatternLayers;
import net.minecraft.world.level.block.state.properties.WoodType;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.jspecify.annotations.Nullable;

public class DummyGuiGraphics extends GuiGraphics {
    public static final DummyGuiGraphics INSTANCE = new DummyGuiGraphics();

    private DummyGuiGraphics() {
        super(Minecraft.getInstance(), null, 0, 0);
    }

    @Override
    public void requestCursor(CursorType cursorType) {
    }

    @Override
    public void applyCursor(Window window) {
    }

    @Override
    public void nextStratum() {
    }

    @Override
    public void blurBeforeThisStratum() {
    }

    @Override
    public void hLine(int minX, int maxX, int y, int color) {
    }

    @Override
    public void vLine(int x, int minY, int maxY, int color) {
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
    public void fill(int minX, int minY, int maxX, int maxY, int color) {
    }

    @Override
    public void fill(RenderPipeline renderPipeline, int i, int j, int k, int l, int m) {
    }

    @Override
    public void fillGradient(int x1, int y1, int x2, int y2, int colorFrom, int colorTo) {
    }

    @Override
    public void fill(RenderPipeline renderPipeline, TextureSetup textureSetup, int i, int j, int k, int l) {
    }

    @Override
    public void submitColoredRectangle(RenderPipeline renderPipeline, TextureSetup textureSetup, int i, int j, int k, int l, int m, @Nullable Integer integer) {
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
    public void drawString(Font font, @Nullable String string, int i, int j, int k) {
    }

    @Override
    public void drawString(Font font, @Nullable String string, int i, int j, int k, boolean bl) {
    }

    @Override
    public void drawString(Font font, FormattedCharSequence formattedCharSequence, int i, int j, int k) {
    }

    @Override
    public void drawString(Font font, FormattedCharSequence formattedCharSequence, int i, int j, int k, boolean bl) {
    }

    @Override
    public void drawString(Font font, Component component, int i, int j, int k) {
    }

    @Override
    public void drawString(Font font, Component component, int i, int j, int k, boolean bl) {
    }

    @Override
    public void drawWordWrap(Font font, FormattedText text, int x, int y, int lineWidth, int color) {
    }

    @Override
    public void drawWordWrap(Font font, FormattedText text, int x, int y, int lineWidth, int color, boolean dropShadow) {
    }

    @Override
    public void drawStringWithBackdrop(Font font, Component component, int i, int j, int k, int l) {
    }

    @Override
    public void blitSprite(RenderPipeline renderPipeline, Identifier resourceLocation, int i, int j, int k, int l) {
    }

    @Override
    public void blitSprite(RenderPipeline renderPipeline, Identifier resourceLocation, int i, int j, int k, int l, float f) {
    }

    @Override
    public void blitSprite(RenderPipeline renderPipeline, Identifier resourceLocation, int i, int j, int k, int l, int m) {
    }

    @Override
    public void blitSprite(RenderPipeline renderPipeline, Identifier resourceLocation, int i, int j, int k, int l, int m, int n, int o, int p) {
    }

    @Override
    public void blitSprite(RenderPipeline renderPipeline, Identifier resourceLocation, int i, int j, int k, int l, int m, int n, int o, int p, int q) {
    }

    @Override
    public void blitSprite(RenderPipeline renderPipeline, TextureAtlasSprite textureAtlasSprite, int i, int j, int k, int l) {
    }

    @Override
    public void blitSprite(RenderPipeline renderPipeline, TextureAtlasSprite textureAtlasSprite, int i, int j, int k, int l, int m) {
    }

    @Override
    public void blitSprite(RenderPipeline renderPipeline, TextureAtlasSprite textureAtlasSprite, int i, int j, int k, int l, int m, int n, int o, int p, int q) {
    }

    @Override
    public void blitNineSlicedSprite(RenderPipeline renderPipeline, TextureAtlasSprite textureAtlasSprite, GuiSpriteScaling.NineSlice nineSlice, int i, int j, int k, int l, int m) {
    }

    @Override
    public void blitNineSliceInnerSegment(RenderPipeline renderPipeline, GuiSpriteScaling.NineSlice nineSlice, TextureAtlasSprite textureAtlasSprite, int i, int j, int k, int l, int m, int n, int o, int p, int q, int r, int s) {
    }

    @Override
    public void blitTiledSprite(RenderPipeline renderPipeline, TextureAtlasSprite textureAtlasSprite, int i, int j, int k, int l, int m, int n, int o, int p, int q, int r, int s) {
    }

    @Override
    public void blit(RenderPipeline renderPipeline, Identifier resourceLocation, int i, int j, float f, float g, int k, int l, int m, int n, int o) {
    }

    @Override
    public void blit(RenderPipeline renderPipeline, Identifier resourceLocation, int i, int j, float f, float g, int k, int l, int m, int n) {
    }

    @Override
    public void blit(RenderPipeline renderPipeline, Identifier resourceLocation, int i, int j, float f, float g, int k, int l, int m, int n, int o, int p) {
    }

    @Override
    public void blit(RenderPipeline renderPipeline, Identifier resourceLocation, int i, int j, float f, float g, int k, int l, int m, int n, int o, int p, int q) {
    }

    @Override
    public void blit(Identifier resourceLocation, int i, int j, int k, int l, float f, float g, float h, float m) {
    }

    @Override
    public void renderItem(ItemStack stack, int x, int y) {
    }

    @Override
    public void renderItem(ItemStack stack, int x, int y, int seed) {
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
    public void renderItemDecorations(Font font, ItemStack stack, int x, int y) {
    }

    @Override
    public void renderItemDecorations(Font font, ItemStack stack, int x, int y, @Nullable String text) {
    }

    @Override
    public void setTooltipForNextFrame(Component component, int i, int j) {
    }

    @Override
    public void setTooltipForNextFrame(List<FormattedCharSequence> list, int i, int j) {
    }

    @Override
    public void setTooltipForNextFrame(Font font, ItemStack itemStack, int i, int j) {
    }

    @Override
    public void setTooltipForNextFrame(Font font, List<Component> list, Optional<TooltipComponent> optional, int i, int j) {
    }

    @Override
    public void setTooltipForNextFrame(Font font, List<Component> list, Optional<TooltipComponent> optional, int i, int j, @Nullable Identifier resourceLocation) {
    }

    @Override
    public void setTooltipForNextFrame(Font font, Component component, int i, int j) {
    }

    @Override
    public void setTooltipForNextFrame(Font font, Component component, int i, int j, @Nullable Identifier resourceLocation) {
    }

    @Override
    public void setComponentTooltipForNextFrame(Font font, List<Component> list, int i, int j) {
    }

    @Override
    public void setComponentTooltipForNextFrame(Font font, List<Component> list, int i, int j, @Nullable Identifier resourceLocation) {
    }

    @Override
    public void setTooltipForNextFrame(Font font, List<? extends FormattedCharSequence> list, int i, int j) {
    }

    @Override
    public void setTooltipForNextFrame(Font font, List<? extends FormattedCharSequence> list, int i, int j, @Nullable Identifier resourceLocation) {
    }

    @Override
    public void setTooltipForNextFrame(Font font, List<FormattedCharSequence> list, ClientTooltipPositioner clientTooltipPositioner, int i, int j, boolean bl) {
    }

    @Override
    public void renderTooltip(Font font, List<ClientTooltipComponent> list, int i, int j, ClientTooltipPositioner clientTooltipPositioner, @Nullable Identifier resourceLocation) {
    }

    @Override
    public void renderItemBar(ItemStack stack, int x, int y) {
    }

    @Override
    public void renderItemCount(Font font, ItemStack stack, int x, int y, @Nullable String text) {
    }

    @Override
    public void renderItemCooldown(ItemStack stack, int x, int y) {
    }

    @Override
    public void renderComponentHoverEffect(Font font, @Nullable Style style, int mouseX, int mouseY) {
    }

    @Override
    public void submitMapRenderState(MapRenderState mapRenderState) {
    }

    @Override
    public void submitEntityRenderState(EntityRenderState entityRenderState, float f, Vector3f vector3f, Quaternionf quaternionf, @Nullable Quaternionf quaternionf2, int i, int j, int k, int l) {
    }

    @Override
    public void submitProfilerChartRenderState(List<ResultField> list, int i, int j, int k, int l) {
    }

    @Override
    public void renderDeferredElements() {
    }

    @Override
    public void submitSignRenderState(Model.Simple simple, float f, WoodType woodType, int i, int j, int k, int l) {
    }

    @Override
    public void textHighlight(int i, int j, int k, int l, boolean bl) {
    }

    @Override
    public void renderOutline(int i, int j, int k, int l, int m) {
    }

    @Override
    public void innerBlit(RenderPipeline renderPipeline, Identifier identifier, int i, int j, int k, int l, float f, float g, float h, float m, int n) {
    }

    @Override
    public void submitBlit(RenderPipeline renderPipeline, GpuTextureView gpuTextureView, GpuSampler gpuSampler, int i, int j, int k, int l, float f, float g, float h, float m, int n) {
    }

    @Override
    public void submitTiledBlit(RenderPipeline renderPipeline, GpuTextureView gpuTextureView, GpuSampler gpuSampler, int i, int j, int k, int l, int m, int n, float f, float g, float h, float o, int p) {
    }

    @Override
    public void setTooltipForNextFrameInternal(Font font, List<ClientTooltipComponent> list, int i, int j, ClientTooltipPositioner clientTooltipPositioner, @Nullable Identifier identifier, boolean bl) {
    }

    @Override
    public void submitSkinRenderState(PlayerModel playerModel, Identifier identifier, float f, float g, float h, float i, int j, int k, int l, int m) {
    }

    @Override
    public void submitBookModelRenderState(BookModel bookModel, Identifier identifier, float f, float g, float h, int i, int j, int k, int l) {
    }

    @Override
    public void submitBannerPatternRenderState(BannerFlagModel bannerFlagModel, DyeColor dyeColor, BannerPatternLayers bannerPatternLayers, int i, int j, int k, int l) {
    }

    @Override
    public ActiveTextCollector textRendererForWidget(AbstractWidget abstractWidget, GuiGraphics.HoveredTextEffects hoveredTextEffects) {
        return null;
    }

    @Override
    public ActiveTextCollector textRenderer() {
        return null;
    }

    @Override
    public ActiveTextCollector textRenderer(GuiGraphics.HoveredTextEffects hoveredTextEffects) {
        return null;
    }

    @Override
    public ActiveTextCollector textRenderer(GuiGraphics.HoveredTextEffects hoveredTextEffects, @Nullable Consumer<Style> consumer) {
        return null;
    }

    @Override
    public ActiveTextCollector.Parameters createDefaultTextParameters(float f) {
        return null;
    }

    @Override
    public void blit(final GpuTextureView textureView, final GpuSampler sampler, final int x0, final int y0, final int x1, final int y1, final float u0, final float u1, final float v0, final float v1) {
    }

    @Override
    public void setTooltipForNextFrame(final Font font, final List<FormattedCharSequence> tooltip, final Optional<TooltipComponent> component, final ClientTooltipPositioner positioner, final int xo, final int yo, final boolean replaceExisting, final @Nullable Identifier style) {
    }
}
