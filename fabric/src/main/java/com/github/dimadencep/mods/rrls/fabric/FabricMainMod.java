package com.github.dimadencep.mods.rrls.fabric;

import com.github.dimadencep.mods.rrls.MainMod;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.resource.language.I18n;

public class FabricMainMod extends MainMod implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        this.init();

        HudRenderCallback.EVENT.register((matrixStack, tickDelta) -> {
            if (reloadHandler.getReload() != null && config.showInGame)
                DrawableHelper.drawCenteredText(matrixStack, this.client.textRenderer, I18n.translate(config.reloadText), this.client.getWindow().getScaledWidth() / 2, 70, config.rgbText ? getColor() : -1);
        });

        ScreenEvents.BEFORE_INIT.register((client, screen, scaledWidth, scaledHeight) -> {
            ScreenEvents.afterRender(screen).register((screen2, pose, mouseX, mouseY, delta) -> {
                if (reloadHandler.getReload() != null && config.showInGui)
                    DrawableHelper.drawCenteredText(pose, this.client.textRenderer, I18n.translate(config.reloadText), screen2.width / 2, 70, config.rgbText ? getColor() : -1);
            });
        });

        ClientTickEvents.START_CLIENT_TICK.register(reloadHandler::tick);
    }
}