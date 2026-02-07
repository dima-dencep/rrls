package org.redlance.dima_dencep.mods.rrls.mixins.workaround;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.mojang.blaze3d.systems.CommandEncoder;
import org.redlance.dima_dencep.mods.rrls.utils.OverlayHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(CommandEncoder.class)
public class CommandEncoderMixin {
    @ModifyExpressionValue(
            method = {
                    "writeToTexture(Lcom/mojang/blaze3d/textures/GpuTexture;Lcom/mojang/blaze3d/platform/NativeImage;IIIIIIII)V",
                    "writeToTexture(Lcom/mojang/blaze3d/textures/GpuTexture;Ljava/nio/ByteBuffer;Lcom/mojang/blaze3d/platform/NativeImage$Format;IIIIII)V"
            },
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/mojang/blaze3d/systems/CommandEncoderBackend;isInRenderPass()Z"
            )
    )
    private boolean rrls$exitRenderPass(boolean original) {
        return !OverlayHelper.isCurrentRenderingState() && original;
    }
}
