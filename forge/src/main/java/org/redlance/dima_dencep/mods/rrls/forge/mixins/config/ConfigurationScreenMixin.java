package org.redlance.dima_dencep.mods.rrls.forge.mixins.config;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.neoforged.fml.ModContainer;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import org.redlance.dima_dencep.mods.rrls.Rrls;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = ConfigurationScreen.class, remap = false)
public class ConfigurationScreenMixin {
    @WrapOperation(
            method = "<init>(Lnet/neoforged/fml/ModContainer;Lnet/minecraft/client/gui/screens/Screen;Lnet/minecraft/data/models/blockstates/PropertyDispatch$QuadFunction;)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/neoforged/neoforge/client/gui/ConfigurationScreen$TranslationChecker;check(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;"
            )
    )
    private static String rrls$clothconfig(ConfigurationScreen.TranslationChecker instance, String translationKey, String fallback, Operation<String> original, @Local(argsOnly = true) ModContainer mod) {
        return original.call(instance, Rrls.MOD_ID.equals(mod.getModId()) ? "text.autoconfig.rrls.title" : translationKey, fallback);
    }
}
