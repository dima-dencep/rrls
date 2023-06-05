package com.github.dimadencep.mods.rrls;

import com.github.dimadencep.mods.rrls.config.ModConfig;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.Toml4jConfigSerializer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.SplashOverlay;
import net.minecraft.client.util.math.MatrixStack;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

public class Rrls {
    public static AtomicReference<SplashOverlay> attachedOverlay = new AtomicReference<>();
    protected final MinecraftClient client = MinecraftClient.getInstance();
    public static ModConfig config;

    public void init() {
        config = AutoConfig.register(ModConfig.class, Toml4jConfigSerializer::new).get();
    }

    public void tickReload(MinecraftClient minecraft) {
        SplashOverlay overlay = Rrls.attachedOverlay.get();

        if (overlay != null) {
            minecraft.setOverlay(null);

            if (overlay.reload.isComplete()) {
                try {
                    overlay.reload.throwException();
                    overlay.exceptionHandler.accept(Optional.empty());
                } catch (Throwable var23) {
                    overlay.exceptionHandler.accept(Optional.of(var23));
                }

                Rrls.attachedOverlay.set(null);

                if (Rrls.config.reInitScreen && minecraft.currentScreen != null) {
                    minecraft.currentScreen.init(minecraft, minecraft.getWindow().getScaledWidth(), minecraft.getWindow().getScaledHeight());
                }
            }
        }
    }

    public void renderText(MatrixStack stack, boolean isGame) {
        if (!Rrls.config.showIn.canShow(isGame)) return;

        SplashOverlay overlay = Rrls.attachedOverlay.get();

        if (overlay != null) {
            int i = this.client.getWindow().getScaledWidth();
            int s = (int) ((double) this.client.getWindow().getScaledHeight() * 0.8325);

            int r = (int) (Math.min(i * 0.75, this.client.getWindow().getScaledHeight()) * 0.5);

            overlay.renderProgressBar(stack, i / 2 - r, s - 5, i / 2 + r, s + 5, 0.8F);
        }
    }
}