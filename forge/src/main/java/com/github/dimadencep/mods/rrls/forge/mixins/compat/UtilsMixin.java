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
