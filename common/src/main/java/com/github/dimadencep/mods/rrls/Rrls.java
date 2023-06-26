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


public class Rrls {
    public static final StackWalker classWalker = StackWalker.getInstance(StackWalker.Option.RETAIN_CLASS_REFERENCE);
    public static final ModConfig config = AutoConfig.register(ModConfig.class, Toml4jConfigSerializer::new).get();
    public static final Logger logger = LogManager.getLogger("RRLS");

    protected final MinecraftClient client = MinecraftClient.getInstance();

    @Nullable
    public static Overlay tryGetOverlay(Overlay original) {
        if (original instanceof SplashAccessor accessor && accessor.isAttached()) {
            return null;
        }

        return original;
    }
}