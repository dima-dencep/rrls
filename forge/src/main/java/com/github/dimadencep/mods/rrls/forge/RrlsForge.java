/*
 * Copyright 2023 dima_dencep.
 *
 * Licensed under the Open Software License, Version 3.0 (the "License");
 * you may not use this file except in compliance with the License.
 *
 * You may obtain a copy of the License at
 *     https://github.com/dima-dencep/rrls/blob/HEAD/LICENSE
 */

package com.github.dimadencep.mods.rrls.forge;

import com.github.dimadencep.mods.rrls.Rrls;
import com.github.dimadencep.mods.rrls.accessor.SplashAccessor;
import com.github.dimadencep.mods.rrls.config.ModConfig;
import me.shedaniel.autoconfig.AutoConfig;
import net.neoforged.neoforge.client.ConfigScreenHandler;
import net.neoforged.neoforge.client.event.RenderGuiEvent;
import net.neoforged.neoforge.client.event.ScreenEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.TickEvent;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.IExtensionPoint;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.network.NetworkConstants;

@Mod("rrls")
public class RrlsForge extends Rrls {
    public RrlsForge() {
        ModLoadingContext.get().registerExtensionPoint(
                IExtensionPoint.DisplayTest.class,
                () -> new IExtensionPoint.DisplayTest(
                        () -> NetworkConstants.IGNORESERVERONLY,
                        (a, b) -> true
                )
        );

        NeoForge.EVENT_BUS.register(this);

        ModLoadingContext.get().registerExtensionPoint(
                ConfigScreenHandler.ConfigScreenFactory.class,
                () -> new ConfigScreenHandler.ConfigScreenFactory(
                        (mc, screen) -> AutoConfig.getConfigScreen(ModConfig.class, screen).get()
                )
        );
    }

    @SubscribeEvent
    public void onRenderGui(RenderGuiEvent.Pre event) {
        getAccessor(SplashAccessor.AttachType.HIDE)
                .ifPresent(splashAccessor -> splashAccessor.rrls$render(event.getGuiGraphics(), true));
    }

    @SubscribeEvent
    public void onScreenRender(ScreenEvent.Render.Post event) {
        getAccessor(SplashAccessor.AttachType.HIDE)
                .ifPresent(splashAccessor -> splashAccessor.rrls$render(event.getGuiGraphics(), false));
    }

    @SubscribeEvent
    public void onTick(TickEvent.RenderTickEvent event) {
        if (event.phase == TickEvent.Phase.START)
            getAccessor(SplashAccessor.AttachType.HIDE)
                    .ifPresent(SplashAccessor::rrls$reload);
    }
}
