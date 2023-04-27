package com.github.dimadencep.mods.rrls.fabric;

import com.github.dimadencep.mods.rrls.Rrls;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.util.math.MatrixStack;

import java.util.function.Consumer;

public class RrlsFabric extends Rrls implements ClientModInitializer {
    public static Consumer<MatrixStack> renderInGame;

    @Override
    public void onInitializeClient() {
        RrlsFabric.renderInGame = (stack) -> this.renderText(stack, this.client, false);

        this.init();

        HudRenderCallback.EVENT.register((matrixStack, tickDelta) -> this.renderText(matrixStack, this.client, true));

        ClientTickEvents.START_CLIENT_TICK.register(this::tickReload);
    }
}