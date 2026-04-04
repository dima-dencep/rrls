/*
 * Copyright 2023 - 2026 dima_dencep.
 *
 * Licensed under the Open Software License, Version 3.0 (the "License");
 * you may not use this file except in compliance with the License.
 *
 * You may obtain a copy of the License at
 *     https://spdx.org/licenses/OSL-3.0.txt
 */

package org.redlance.dima_dencep.mods.rrls.config.impl;

import net.neoforged.fml.config.IConfigSpec;
import net.neoforged.neoforge.common.ModConfigSpec;
import org.redlance.common.services.ServiceUtils;
import org.redlance.dima_dencep.mods.rrls.config.DoubleLoad;
import org.redlance.dima_dencep.mods.rrls.config.HideType;
import org.redlance.dima_dencep.mods.rrls.config.Type;
import org.redlance.dima_dencep.mods.rrls.utils.Ease;

public abstract class ForgeLikeConfigImpl extends FallbackConfigImpl {
    public final IConfigSpec configSpec;

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

    // Interpolation
    public final ModConfigSpec.BooleanValue interpolateProgress;
    public final ModConfigSpec.BooleanValue interpolateAtEnd;
    public final ModConfigSpec.EnumValue<Ease> ease;
    public final ModConfigSpec.ConfigValue<Double> easingArg;

    // Other
    public final ModConfigSpec.BooleanValue resetResources;
    public final ModConfigSpec.BooleanValue reInitScreen;
    public final ModConfigSpec.BooleanValue earlyPackStatusSend;
    public final ModConfigSpec.EnumValue<DoubleLoad> doubleLoad;

    // Platform-specific
    public final ModConfigSpec.BooleanValue skipForgeOverlay;

    protected ForgeLikeConfigImpl() {
        this(new ModConfigSpec.Builder());
    }

    protected ForgeLikeConfigImpl(ModConfigSpec.Builder builder) {
        builder.push("global");
        this.hideType = builder.defineEnum("hideOverlays", super.hideType());
        this.blockOverlay = builder.define("blockOverlay", super.blockOverlay());
        this.miniRender = builder.define("miniRender", super.miniRender());
        this.enableScissor = builder.define("enableScissor", super.enableScissor());
        builder.pop();

        builder.push("splash");
        this.type = builder.defineEnum("type", super.type());
        this.rgbProgress = builder.define("rgbProgress", super.rgbProgress());
        this.reloadText = builder.define("reloadText", super.reloadText());
        this.animationSpeed = builder.define("animationSpeed", (double) super.animationSpeed());
        builder.pop();

        builder.push("interpolation");
        this.interpolateProgress = builder.define("interpolateProgress", super.interpolateProgress());
        this.interpolateAtEnd = builder.define("interpolateAtEnd", super.interpolateAtEnd());
        this.ease = builder.defineEnum("ease", super.easing());
        this.easingArg = builder.define("easingArg", super.easingArg().doubleValue(), ForgeLikeConfigImpl::isFloatLike);
        builder.pop();

        builder.push("other");
        this.resetResources = builder.define("resetResources", super.resetResources());
        this.reInitScreen = builder.define("reInitScreen", super.reInitScreen());
        this.earlyPackStatusSend = builder.define("earlyPackStatusSend", super.earlyPackStatusSend());
        this.doubleLoad = builder.defineEnum("doubleLoad", super.doubleLoad());
        builder.pop();

        builder.push("platform");
        this.skipForgeOverlay = builder.define("skipForgeOverlay", super.skipForgeOverlay());
        builder.pop();

        this.configSpec = builder.build();
    }

    @Override
    public HideType hideType() {
        return this.hideType.get();
    }

    @Override
    public boolean blockOverlay() {
        return this.blockOverlay.get();
    }

    @Override
    public boolean miniRender() {
        return this.miniRender.get();
    }

    @Override
    public boolean enableScissor() {
        return this.enableScissor.get();
    }

    @Override
    public Type type() {
        return this.type.get();
    }

    @Override
    public boolean rgbProgress() {
        return this.rgbProgress.get();
    }

    @Override
    public String reloadText() {
        return this.reloadText.get();
    }

    @Override
    public boolean interpolateProgress() {
        return this.interpolateProgress.get();
    }

    @Override
    public boolean interpolateAtEnd() {
        return this.interpolateAtEnd.get();
    }

    @Override
    public Ease easing() {
        return this.ease.get();
    }

    @Override
    public Float easingArg() {
        float easingArg = this.easingArg.get().floatValue();
        if (Float.isNaN(easingArg)) {
            return null;
        }

        return easingArg;
    }

    @Override
    public float animationSpeed() {
        return this.animationSpeed.get().floatValue();
    }

    @Override
    public boolean resetResources() {
        return this.resetResources.get();
    }

    @Override
    public boolean reInitScreen() {
        return this.reInitScreen.get();
    }

    @Override
    public boolean earlyPackStatusSend() {
        return this.earlyPackStatusSend.get();
    }

    @Override
    public DoubleLoad doubleLoad() {
        return this.doubleLoad.get();
    }

    @Override
    public boolean skipForgeOverlay() {
        return this.skipForgeOverlay.get();
    }

    private static boolean isFloatLike(Object obj) {
        if (obj == null) {
            return false;
        }

        return switch (obj) {
            case Float ignored -> true;
            case Double ignored -> true;
            case String str -> {
                try {
                    Float.valueOf(str);
                    yield true;
                } catch (Throwable th) {
                    yield false;
                }
            }
            default -> false;
        };
    }

    @Override
    public abstract boolean isServiceActive();
    @Override
    public int getPriority() {
        return ServiceUtils.HIGHEST_PRIORITY;
    }
}
