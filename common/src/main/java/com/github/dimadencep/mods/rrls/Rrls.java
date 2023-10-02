package com.github.dimadencep.mods.rrls;

import com.github.dimadencep.mods.rrls.accessor.SplashAccessor;
import com.github.dimadencep.mods.rrls.config.ModConfig;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.Toml4jConfigSerializer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Overlay;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class Rrls {
    public static final ModConfig MOD_CONFIG = AutoConfig.register(ModConfig.class, Toml4jConfigSerializer::new).get();
    public static final Logger LOGGER = LogManager.getLogger("rrls");

    @Nullable
    public static Overlay tryGetOverlay() {
        return Rrls.getAccessor(SplashAccessor.AttachType.DEFAULT).orElse(Rrls.getAccessor(SplashAccessor.AttachType.WAIT).orElse(null));
    }

    public static Optional<Overlay> getAccessor(SplashAccessor.AttachType type) {
        return Optional.ofNullable(MinecraftClient.getInstance().overlay)
                .filter(accessor -> accessor.rrls$getAttachType() == type);
    }
}