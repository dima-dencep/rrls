package org.redlance.dima_dencep.mods.rrls.mixins.workaround;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.ReloadableTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import org.redlance.dima_dencep.mods.rrls.Rrls;
import org.redlance.dima_dencep.mods.rrls.RrlsConfig;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(TextureManager.class)
public abstract class TextureManagerMixin {
    @Shadow
    @Final
    private ResourceManager resourceManager;

    @WrapOperation(
            method = "registerForNextReload",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/renderer/texture/TextureManager;register(Lnet/minecraft/resources/ResourceLocation;Lnet/minecraft/client/renderer/texture/AbstractTexture;)V"
            )
    )
    public void rrls$earlyRegister(TextureManager instance, ResourceLocation path, AbstractTexture texture, Operation<Void> original) {
        original.call(instance, path, texture);

        if (RrlsConfig.hideType().forceClose() && texture instanceof ReloadableTexture reloadableTexture) {
            TextureManager.PendingReload reload = TextureManager.scheduleLoad(
                    this.resourceManager, path, reloadableTexture, Util.backgroundExecutor()
            );
            Rrls.LOGGER.info("Reloading texture '{}'!", path);
            reload.newContents().thenAcceptAsync(textureContents -> reload.texture()
                    .apply(textureContents), Minecraft.getInstance());
        }
    }
}
