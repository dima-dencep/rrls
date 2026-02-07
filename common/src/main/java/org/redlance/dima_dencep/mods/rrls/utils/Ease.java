/*
 * Copyright 2023 - 2026 dima_dencep.
 *
 * Licensed under the Open Software License, Version 3.0 (the "License");
 * you may not use this file except in compliance with the License.
 *
 * You may obtain a copy of the License at
 *     https://spdx.org/licenses/OSL-3.0.txt
 */

package org.redlance.dima_dencep.mods.rrls.utils;

import java.util.function.Function;

/**
 * Easings form <a href="https://easings.net/">easings.net</a><br>
 * + constant + linear
 */
public enum Ease {
    LINEAR(0, arg -> easeIn(f -> f)),
    CONSTANT(1, arg -> easeIn(f -> 0f)),

    // Sine
    INSINE(6, arg -> easeIn(Easing::sine)),
    OUTSINE(7, arg -> easeOut(Easing::sine)),
    INOUTSINE(8, arg -> easeInOut(Easing::sine)),

    // Cubic
    INCUBIC(9, arg -> easeIn(Easing::cubic)),
    OUTCUBIC(10, arg -> easeOut(Easing::cubic)),
    INOUTCUBIC(11, arg -> easeInOut(Easing::cubic)),

    // Quadratic
    INQUAD(12, arg -> easeIn(Easing::quadratic)),
    OUTQUAD(13, arg -> easeOut(Easing::quadratic)),
    INOUTQUAD(14, arg -> easeInOut(Easing::quadratic)),

    // Quart
    INQUART(15, arg -> easeIn(Easing.pow(4))),
    OUTQUART(16, arg -> easeOut(Easing.pow(4))),
    INOUTQUART(17, arg -> easeInOut(Easing.pow(4))),

    // Quint
    INQUINT(18, arg -> easeIn(Easing.pow(5))),
    OUTQUINT(19, arg -> easeOut(Easing.pow(5))),
    INOUTQUINT(20, arg -> easeInOut(Easing.pow(5))),

    // Expo
    INEXPO(21, arg -> easeIn(Easing::exp)),
    OUTEXPO(22, arg -> easeOut(Easing::exp)),
    INOUTEXPO(23, arg -> easeInOut(Easing::exp)),

    // Cricle
    INCIRC(24, arg -> easeIn(Easing::circle)),
    OUTCIRC(25, arg -> easeOut(Easing::circle)),
    INOUTCIRC(26, arg -> easeInOut(Easing::circle)),

    // Back
    INBACK(27, arg -> easeIn(Easing.back(arg))),
    OUTBACK(28, arg -> easeOut(Easing.back(arg))),
    INOUTBACK(29, arg -> easeInOut(Easing.back(arg))),

    // Elastic
    INELASTIC(30, arg -> easeIn(Easing.elastic(arg))),
    OUTELASTIC(31, arg -> easeOut(Easing.elastic(arg))),
    INOUTELASTIC(32, arg -> easeInOut(Easing.elastic(arg))),

    // Bounce
    INBOUNCE(33, arg -> easeIn(Easing.bounce(arg))),
    OUTBOUNCE(34, arg -> easeOut(Easing.bounce(arg))),
    INOUTBOUNCE(35, arg -> easeInOut(Easing.bounce(arg))),

    CATMULLROM(36, arg -> easeInOut(Easing::catmullRom)),
    STEP(37, arg -> easeIn(Easing.step(arg)));

    final byte id;
    private final Function<Float, Function<Float, Float>> impl;

    /**
     * @param id   id
     * @param impl implementation
     */
    Ease(byte id, Function<Float, Function<Float, Float>> impl) {
        this.id = id;
        this.impl = impl;
    }

    /**
     * @param id   id
     * @param impl implementation
     */
    Ease(int id, Function<Float, Function<Float, Float>> impl) {
        this((byte) id, impl);
    }

    /**
     * Run the easing
     * @param t float between 0 and 1
     * @param n float easing argument
     * @return ease(t, n)
     */
    public float invoke(float t, Float n) {
        return this.impl.apply(n).apply(t);
    }

    public static Function<Float, Float> easeIn(Function<Float, Float> function) {
        return function;
    }

    public static Function<Float, Float> easeOut(Function<Float, Float> function) {
        return time -> 1 - function.apply(1 - time);
    }

    public static Function<Float, Float> easeInOut(Function<Float, Float> function) {
        return time -> {
            if (time < 0.5F) {
                return function.apply(time * 2F) / 2F;
            }

            return 1 - function.apply((1 - time) * 2F) / 2F;
        };
    }
}
