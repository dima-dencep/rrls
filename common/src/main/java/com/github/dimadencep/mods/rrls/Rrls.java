package com.github.dimadencep.mods.rrls;

import com.github.dimadencep.mods.rrls.config.ModConfig;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.Toml4jConfigSerializer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.screen.SplashOverlay;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.client.util.math.MatrixStack;

import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

public class Rrls {
	protected final MinecraftClient client = MinecraftClient.getInstance();
	public static SplashOverlay attachedOverlay;
	public static ModConfig config;

	public void init() {
		config = AutoConfig.register(ModConfig.class, Toml4jConfigSerializer::new).get();
	}

	public static int getColor() {
		return ThreadLocalRandom.current().nextInt(0, 0xFFFFFF);
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

	public void renderText(MatrixStack stack, MinecraftClient minecraft, boolean isGame) { // TODO rewrite
		if (Rrls.attachedOverlay != null && Rrls.config.showIn.canShow(isGame)) {
			DrawableHelper.drawCenteredTextWithShadow(stack, minecraft.textRenderer, I18n.translate(config.reloadText), minecraft.getWindow().getScaledWidth() / 2, 70, config.rgbText ? getColor() : -1);

			if (Rrls.config.renderProgressBar) {

				int i = this.client.getWindow().getScaledWidth();
				int s = (int) ((double) this.client.getWindow().getScaledHeight() * 0.8325);

				int r = (int) (Math.min(i * 0.75, this.client.getWindow().getScaledHeight()) * 0.5);

				Rrls.attachedOverlay.renderProgressBar(stack, i / 2 - r, s - 5, i / 2 + r, s + 5, 0.8F);
			}
		}
	}
}