/*
 * Copyright 2023 dima_dencep.
 *
 * Licensed under the Open Software License, Version 3.0 (the "License");
 * you may not use this file except in compliance with the License.
 *
 * You may obtain a copy of the License at
 *     https://github.com/dima-dencep/rrls/blob/HEAD/LICENSE
 */

package com.github.dimadencep.mods.rrls.fabric;

import com.github.dimadencep.mods.rrls.Rrls;
import com.github.dimadencep.mods.rrls.accessor.SplashAccessor;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;

public class RrlsFabric extends Rrls implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ScreenEvents.AFTER_INIT.register((client1, screen, scaledWidth, scaledHeight) ->
                ScreenEvents.afterRender(screen).register((screen1, drawContext, mouseX, mouseY, tickDelta) ->
                        getAccessor(SplashAccessor.AttachType.HIDE).ifPresent(splashAccessor -> splashAccessor.rrls$render(drawContext, false))
                )
        );

        HudRenderCallback.EVENT.register((drawContext, tickDelta) ->
                getAccessor(SplashAccessor.AttachType.HIDE).ifPresent(splashAccessor -> splashAccessor.rrls$render(drawContext, true))
        );
    }
}