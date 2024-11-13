package org.redlance.dima_dencep.mods.rrls;

import net.neoforged.neoforge.common.ModConfigSpec;
import org.apache.commons.lang3.tuple.Pair;
import org.redlance.dima_dencep.mods.rrls.config.DoubleLoad;
import org.redlance.dima_dencep.mods.rrls.config.HideType;
import org.redlance.dima_dencep.mods.rrls.config.Type;

public class RrlsConfig {
    public static final Pair<RrlsConfig, ModConfigSpec> CONFIG_SPEC_PAIR = new ModConfigSpec.Builder()
            .configure(RrlsConfig::new);

    // Global
    public final ModConfigSpec.EnumValue<HideType> hideType;
    public final ModConfigSpec.BooleanValue blockOverlay;
    public final ModConfigSpec.BooleanValue miniRender;
    public final ModConfigSpec.BooleanValue enableScissor;

    // Splash
    public final ModConfigSpec.EnumValue<Type> type;
    public final ModConfigSpec.BooleanValue rgbProgress;
    public final ModConfigSpec.ConfigValue<String> reloadText;
    public final ModConfigSpec.ConfigValue<Double> animationSpeed;

    // Other
    public final ModConfigSpec.BooleanValue resetResources;
    public final ModConfigSpec.BooleanValue reInitScreen;
    public final ModConfigSpec.BooleanValue earlyPackStatusSend;
    public final ModConfigSpec.EnumValue<DoubleLoad> doubleLoad;

    // Platform-specific
    public final ModConfigSpec.BooleanValue skipForgeOverlay;

    protected RrlsConfig(ModConfigSpec.Builder builder) {
        builder.push("global");
        this.hideType = builder.defineEnum("hideOverlays", HideType.ALL);
        this.blockOverlay = builder.define("blockOverlay", false);
        this.miniRender = builder.define("miniRender", true);
        this.enableScissor = builder.define("enableScissor", false);
        builder.pop();

        builder.push("splash");
        this.type = builder.defineEnum("type", Type.PROGRESS);
        this.rgbProgress = builder.define("rgbProgress", false);
        this.reloadText = builder.define("reloadText", "Edit in config!");
        this.animationSpeed = builder.define("animationSpeed", 1000.0);
        builder.pop();

        builder.push("other");
        this.resetResources = builder.define("resetResources", true);
        this.reInitScreen = builder.define("reInitScreen", true);
        this.earlyPackStatusSend = builder.define("earlyPackStatusSend", false);
        this.doubleLoad = builder.defineEnum("doubleLoad", DoubleLoad.FORCE_LOAD);
        builder.pop();

        builder.push("platform");
        this.skipForgeOverlay = builder.define("skipForgeOverlay", false);
        builder.pop();
    }

    public static HideType hideType() {
        return CONFIG_SPEC_PAIR.getKey().hideType.get();
    }

    public static boolean blockOverlay() {
        return CONFIG_SPEC_PAIR.getKey().blockOverlay.get();
    }

    public static boolean miniRender() {
        return CONFIG_SPEC_PAIR.getKey().miniRender.get();
    }

    public static boolean enableScissor() {
        return CONFIG_SPEC_PAIR.getKey().enableScissor.get();
    }

    public static Type type() {
        return CONFIG_SPEC_PAIR.getKey().type.get();
    }

    public static boolean rgbProgress() {
        return CONFIG_SPEC_PAIR.getKey().rgbProgress.get();
    }

    public static String reloadText() {
        return CONFIG_SPEC_PAIR.getKey().reloadText.get();
    }

    public static float animationSpeed() {
        return CONFIG_SPEC_PAIR.getKey().animationSpeed.get()
                .floatValue();
    }

    public static boolean resetResources() {
        return CONFIG_SPEC_PAIR.getKey().resetResources.get();
    }

    public static boolean reInitScreen() {
        return CONFIG_SPEC_PAIR.getKey().reInitScreen.get();
    }

    public static boolean earlyPackStatusSend() {
        return CONFIG_SPEC_PAIR.getKey().earlyPackStatusSend.get();
    }

    public static DoubleLoad doubleLoad() {
        return CONFIG_SPEC_PAIR.getKey().doubleLoad.get();
    }

    public static boolean skipForgeOverlay() {
        return CONFIG_SPEC_PAIR.getKey().skipForgeOverlay.get();
    }
}
