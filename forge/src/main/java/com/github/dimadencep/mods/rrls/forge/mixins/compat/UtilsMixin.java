/*
 * Copyright 2023 dima_dencep.
 *
 * Licensed under the Open Software License, Version 3.0 (the "License");
 * you may not use this file except in compliance with the License.
 *
 * You may obtain a copy of the License at
 *     https://github.com/dima-dencep/rrls/blob/HEAD/LICENSE
 */

package com.github.dimadencep.mods.rrls.forge.mixins.compat;

import me.shedaniel.autoconfig.util.Utils;
import net.neoforged.fml.loading.FMLPaths;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import java.nio.file.Path;

@Mixin(Utils.class)
public class UtilsMixin {
    /**
     * @author dima_dencep
     * @reason temp fix for <a href="https://discord.com/channels/792699517631594506/1175160193584205945">Bug</a> (via <a href="https://discord.architectury.dev/">Discord</a>)
     */
    @Overwrite
    public static Path getConfigFolder() {
        return FMLPaths.CONFIGDIR.get();
    }
}
