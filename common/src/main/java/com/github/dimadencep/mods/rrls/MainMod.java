package com.github.dimadencep.mods.rrls;

import com.github.dimadencep.mods.rrls.config.ModConfig;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.Toml4jConfigSerializer;
import net.minecraft.client.MinecraftClient;

import java.util.concurrent.ThreadLocalRandom;

public class MainMod {
	public static HidedReloadHandler reloadHandler = new HidedReloadHandler();
	protected final MinecraftClient client = MinecraftClient.getInstance();
	public static ModConfig config;

	public void init() {
		config = AutoConfig.register(ModConfig.class, Toml4jConfigSerializer::new).get();
	}

	public static int getColor() {
		return ThreadLocalRandom.current().nextInt(0, 0xFFFFFF);
	}
}
