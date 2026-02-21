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
import net.minecraft.client.gui.components.Renderable;
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
import net.minecraft.client.resources.model.SpriteId;
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

@SuppressWarnings("all")
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
    public void hLine(int x0, int x1, int y, int col) {
    }

    @Override
    public void vLine(int x, int y0, int y1, int col) {
    }

    @Override
    public void enableScissor(int x0, int y0, int x1, int y1) {
    }

    @Override
    public void disableScissor() {
    }

    @Override
    public boolean containsPointInScissor(int x, int y) {
        return false;
    }

    @Override
    public void fill(int x0, int y0, int x1, int y1, int col) {
    }

    @Override
    public void fill(RenderPipeline pipeline, int x0, int y0, int x1, int y1, int col) {
    }

    @Override
    public void fillGradient(int x0, int y0, int x1, int y1, int col1, int col2) {
    }

    @Override
    public void fill(RenderPipeline renderPipeline, TextureSetup textureSetup, int x0, int y0, int x1, int y1) {
    }

    @Override
    public void submitColoredRectangle(RenderPipeline renderPipeline, TextureSetup textureSetup, int x0, int y0, int x1, int y1, int color1, Integer color2) {
    }

    @Override
    public void textHighlight(int x0, int y0, int x1, int y1, boolean invertText) {
    }

    @Override
    public void drawCenteredString(Font font, String str, int x, int y, int color) {
    }

    @Override
    public void drawCenteredString(Font font, Component text, int x, int y, int color) {
    }

    @Override
    public void drawCenteredString(Font font, FormattedCharSequence text, int x, int y, int color) {
    }

    @Override
    public void drawString(Font font, String str, int x, int y, int color) {
    }

    @Override
    public void drawString(Font font, String str, int x, int y, int color, boolean dropShadow) {
    }

    @Override
    public void drawString(Font font, FormattedCharSequence str, int x, int y, int color) {
    }

    @Override
    public void drawString(Font font, FormattedCharSequence str, int x, int y, int color, boolean dropShadow) {
    }

    @Override
    public void drawString(Font font, Component str, int x, int y, int color) {
    }

    @Override
    public void drawString(Font font, Component str, int x, int y, int color, boolean dropShadow) {
    }

    @Override
    public void drawWordWrap(Font font, FormattedText string, int x, int y, int w, int col) {
    }

    @Override
    public void drawWordWrap(Font font, FormattedText string, int x, int y, int w, int col, boolean dropShadow) {
    }

    @Override
    public void drawStringWithBackdrop(Font font, Component str, int textX, int textY, int textWidth, int textColor) {
    }

    @Override
    public void renderOutline(int x, int y, int width, int height, int color) {
    }

    @Override
    public void blitSprite(RenderPipeline renderPipeline, Identifier location, int x, int y, int width, int height) {
    }

    @Override
    public void blitSprite(RenderPipeline renderPipeline, Identifier location, int x, int y, int width, int height, float alpha) {
    }

    @Override
    public void blitSprite(RenderPipeline renderPipeline, Identifier location, int x, int y, int width, int height, int color) {
    }

    @Override
    public void blitSprite(RenderPipeline renderPipeline, Identifier location, int spriteWidth, int spriteHeight, int textureX, int textureY, int x, int y, int width, int height) {
    }

    @Override
    public void blitSprite(RenderPipeline renderPipeline, Identifier location, int spriteWidth, int spriteHeight, int textureX, int textureY, int x, int y, int width, int height, int color) {
    }

    @Override
    public void blitSprite(RenderPipeline renderPipeline, TextureAtlasSprite sprite, int x, int y, int width, int height) {
    }

    @Override
    public void blitSprite(RenderPipeline renderPipeline, TextureAtlasSprite sprite, int x, int y, int width, int height, int color) {
    }

    @Override
    public void blitSprite(RenderPipeline renderPipeline, TextureAtlasSprite sprite, int spriteWidth, int spriteHeight, int textureX, int textureY, int x, int y, int width, int height, int color) {
    }

    @Override
    public void blitNineSlicedSprite(RenderPipeline renderPipeline, TextureAtlasSprite sprite, GuiSpriteScaling.NineSlice nineSlice, int x, int y, int width, int height, int color) {
    }

    @Override
    public void blitNineSliceInnerSegment(RenderPipeline renderPipeline, GuiSpriteScaling.NineSlice nineSlice, TextureAtlasSprite sprite, int x, int y, int width, int height, int textureX, int textureY, int textureWidth, int textureHeight, int spriteWidth, int spriteHeight, int color) {
    }

    @Override
    public void blitTiledSprite(RenderPipeline renderPipeline, TextureAtlasSprite sprite, int x, int y, int width, int height, int textureX, int textureY, int tileWidth, int tileHeight, int spriteWidth, int spriteHeight, int color) {
    }

    @Override
    public void blit(RenderPipeline renderPipeline, Identifier texture, int x, int y, float u, float v, int width, int height, int textureWidth, int textureHeight, int color) {
    }

    @Override
    public void blit(RenderPipeline renderPipeline, Identifier texture, int x, int y, float u, float v, int width, int height, int textureWidth, int textureHeight) {
    }

    @Override
    public void blit(RenderPipeline renderPipeline, Identifier texture, int x, int y, float u, float v, int width, int height, int srcWidth, int srcHeight, int textureWidth, int textureHeight) {
    }

    @Override
    public void blit(RenderPipeline renderPipeline, Identifier texture, int x, int y, float u, float v, int width, int height, int srcWidth, int srcHeight, int textureWidth, int textureHeight, int color) {
    }

    @Override
    public void blit(Identifier location, int x0, int y0, int x1, int y1, float u0, float u1, float v0, float v1) {
    }

    @Override
    public void blit(GpuTextureView textureView, GpuSampler sampler, int x0, int y0, int x1, int y1, float u0, float u1, float v0, float v1) {
    }

    @Override
    public void innerBlit(RenderPipeline renderPipeline, Identifier location, int x0, int x1, int y0, int y1, float u0, float u1, float v0, float v1, int color) {
    }

    @Override
    public void submitBlit(RenderPipeline pipeline, GpuTextureView textureView, GpuSampler sampler, int x0, int y0, int x1, int y1, float u0, float u1, float v0, float v1, int color) {
    }

    @Override
    public void submitTiledBlit(RenderPipeline pipeline, GpuTextureView textureView, GpuSampler sampler, int tileWidth, int tileHeight, int x0, int y0, int x1, int y1, float u0, float u1, float v0, float v1, int color) {
    }

    @Override
    public void renderItem(ItemStack itemStack, int x, int y) {
    }

    @Override
    public void renderItem(ItemStack itemStack, int x, int y, int seed) {
    }

    @Override
    public void renderFakeItem(ItemStack itemStack, int x, int y) {
    }

    @Override
    public void renderFakeItem(ItemStack itemStack, int x, int y, int seed) {
    }

    @Override
    public void renderItem(LivingEntity owner, ItemStack itemStack, int x, int y, int seed) {
    }

    @Override
    public void renderItem(LivingEntity owner, Level level, ItemStack itemStack, int x, int y, int seed) {
    }

    @Override
    public void renderItemDecorations(Font font, ItemStack itemStack, int x, int y) {
    }

    @Override
    public void renderItemDecorations(Font font, ItemStack itemStack, int x, int y, String countText) {
    }

    @Override
    public void setTooltipForNextFrame(Component component, int x, int y) {
    }

    @Override
    public void setTooltipForNextFrame(List<FormattedCharSequence> formattedCharSequences, int x, int y) {
    }

    @Override
    public void setTooltipForNextFrame(Font font, ItemStack itemStack, int xo, int yo) {
    }

    @Override
    public void setTooltipForNextFrame(Font font, List<Component> texts, Optional<TooltipComponent> optionalImage, int xo, int yo) {
    }

    @Override
    public void setTooltipForNextFrame(Font font, List<Component> texts, Optional<TooltipComponent> optionalImage, int xo, int yo, Identifier style) {
    }

    @Override
    public void setTooltipForNextFrame(Font font, List<FormattedCharSequence> tooltip, Optional<TooltipComponent> component, ClientTooltipPositioner positioner, int xo, int yo, boolean replaceExisting, Identifier style) {
    }

    @Override
    public void setTooltipForNextFrame(Font font, Component text, int xo, int yo) {
    }

    @Override
    public void setTooltipForNextFrame(Font font, Component text, int xo, int yo, Identifier style) {
    }

    @Override
    public void setComponentTooltipForNextFrame(Font font, List<Component> lines, int xo, int yo) {
    }

    @Override
    public void setComponentTooltipForNextFrame(Font font, List<Component> lines, int xo, int yo, Identifier style) {
    }

    @Override
    public void setTooltipForNextFrame(Font font, List<? extends FormattedCharSequence> lines, int xo, int yo) {
    }

    @Override
    public void setTooltipForNextFrame(Font font, List<? extends FormattedCharSequence> lines, int xo, int yo, Identifier style) {
    }

    @Override
    public void setTooltipForNextFrame(Font font, List<FormattedCharSequence> tooltip, ClientTooltipPositioner positioner, int xo, int yo, boolean replaceExisting) {
    }

    @Override
    public void setTooltipForNextFrameInternal(Font font, List<ClientTooltipComponent> lines, int xo, int yo, ClientTooltipPositioner positioner, Identifier style, boolean replaceExisting) {
    }

    @Override
    public void setPreeditOverlay(Renderable preeditOverlay) {
    }

    @Override
    public void renderTooltip(Font font, List<ClientTooltipComponent> lines, int xo, int yo, ClientTooltipPositioner positioner, Identifier style) {
    }

    @Override
    public void renderDeferredElements(int mouseX, int mouseY, float a) {
    }

    @Override
    public void renderItemBar(ItemStack itemStack, int x, int y) {
    }

    @Override
    public void renderItemCount(Font font, ItemStack itemStack, int x, int y, String countText) {
    }

    @Override
    public void renderItemCooldown(ItemStack itemStack, int x, int y) {
    }

    @Override
    public void renderComponentHoverEffect(Font font, Style hoveredStyle, int xMouse, int yMouse) {
    }

    @Override
    public void submitMapRenderState(MapRenderState mapRenderState) {
    }

    @Override
    public void submitEntityRenderState(EntityRenderState renderState, float scale, Vector3f translation, Quaternionf rotation, Quaternionf overrideCameraAngle, int x0, int y0, int x1, int y1) {
    }

    @Override
    public void submitSkinRenderState(PlayerModel playerModel, Identifier texture, float scale, float rotationX, float rotationY, float pivotY, int x0, int y0, int x1, int y1) {
    }

    @Override
    public void submitBookModelRenderState(BookModel bookModel, Identifier texture, float scale, float open, float flip, int x0, int y0, int x1, int y1) {
    }

    @Override
    public void submitBannerPatternRenderState(BannerFlagModel flag, DyeColor baseColor, BannerPatternLayers resultBannerPatterns, int x0, int y0, int x1, int y1) {
    }

    @Override
    public void submitSignRenderState(Model.Simple signModel, float scale, WoodType woodType, int x0, int y0, int x1, int y1) {
    }

    @Override
    public void submitProfilerChartRenderState(List<ResultField> chartData, int x0, int y0, int x1, int y1) {
    }

    @Override
    public TextureAtlasSprite getSprite(SpriteId sprite) {
        return null;
    }

    @Override
    public ActiveTextCollector textRendererForWidget(AbstractWidget owner, GuiGraphics.HoveredTextEffects hoveredTextEffects) {
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
    public ActiveTextCollector textRenderer(GuiGraphics.HoveredTextEffects hoveredTextEffects, Consumer<Style> additionalHoverStyleConsumer) {
        return null;
    }

    @Override
    public ActiveTextCollector.Parameters createDefaultTextParameters(float opacity) {
        return null;
    }
}
