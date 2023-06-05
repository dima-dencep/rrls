package com.github.dimadencep.mods.rrls;

import com.github.dimadencep.mods.rrls.config.ModConfig;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.Toml4jConfigSerializer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.SplashOverlay;
import net.minecraft.client.util.math.MatrixStack;

import java.util.Optional;

public class Rrls {
    public static SplashOverlay attachedOverlay;
    public static ModConfig config;
    protected final MinecraftClient client = MinecraftClient.getInstance();

    public void init() {
        config = AutoConfig.register(ModConfig.class, Toml4jConfigSerializer::new).get();
    }

    public void tickReload(MinecraftClient minecraft) {
        if (Rrls.attachedOverlay != null) {
            minecraft.setOverlay(null);

            if (Rrls.attachedOverlay.reload.isComplete()) {
                try {
                    Rrls.attachedOverlay.reload.throwException();
                    Rrls.attachedOverlay.exceptionHandler.accept(Optional.empty());
                } catch (Throwable var23) {
                    Rrls.attachedOverlay.exceptionHandler.accept(Optional.of(var23));
                }

                Rrls.attachedOverlay = null;

                if (Rrls.config.reInitScreen && minecraft.currentScreen != null) {
                    minecraft.currentScreen.init(minecraft, minecraft.getWindow().getScaledWidth(), minecraft.getWindow().getScaledHeight());
                }
            }
        }
    }

    public void renderText(MatrixStack stack, boolean isGame) {
        if (Rrls.attachedOverlay != null && Rrls.config.showIn.canShow(isGame)) {
            int i = this.client.getWindow().getScaledWidth();
            int s = (int) ((double) this.client.getWindow().getScaledHeight() * 0.8325);

            int r = (int) (Math.min(i * 0.75, this.client.getWindow().getScaledHeight()) * 0.5);

            Rrls.attachedOverlay.renderProgressBar(stack, i / 2 - r, s - 5, i / 2 + r, s + 5, 0.8F);
        }
    }
}