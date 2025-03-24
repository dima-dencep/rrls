/*
 * Copyright 2023 - 2025 dima_dencep.
 *
 * Licensed under the Open Software License, Version 3.0 (the "License");
 * you may not use this file except in compliance with the License.
 *
 * You may obtain a copy of the License at
 *     https://spdx.org/licenses/OSL-3.0.txt
 */

package org.redlance.dima_dencep.mods.rrls.mixins.compat;

import net.minecraft.client.renderer.entity.ItemRenderer;
import org.spongepowered.asm.mixin.Mixin;

/**
 * fix for create
 */
@Mixin(ItemRenderer.class)
public class ItemRendererMixin {
    /*@Shadow
    @Final
    private ItemModelShaper itemModelShaper;

    @WrapOperation(
            method = "getModel",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/resources/model/BakedModel;getOverrides()Lnet/minecraft/client/renderer/block/model/ItemOverrides;"
            )
    )
    public ItemOverrides rrls$fixModelsInMenu(BakedModel instance, Operation<ItemOverrides> original, @Cancellable CallbackInfoReturnable<BakedModel> ci) {
        if (instance == null) {
            ci.setReturnValue(this.itemModelShaper.getModelManager().getMissingModel());
            return null;
        } else {
            return original.call(instance);
        }
    }*/
}
