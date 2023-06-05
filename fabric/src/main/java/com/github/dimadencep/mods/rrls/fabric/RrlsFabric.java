package com.github.dimadencep.mods.rrls.fabric;

import com.github.dimadencep.mods.rrls.Rrls;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;

public class RrlsFabric extends Rrls implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        super.init();

        ScreenEvents.AFTER_INIT.register((client1, screen, scaledWidth, scaledHeight) -> ScreenEvents.afterRender(screen).register((screen1, matrices, mouseX, mouseY, tickDelta) -> this.renderText(matrices, false)));

        HudRenderCallback.EVENT.register((matrixStack, tickDelta) -> this.renderText(matrixStack, true));

        ClientTickEvents.START_CLIENT_TICK.register(this::tickReload);
    }
}