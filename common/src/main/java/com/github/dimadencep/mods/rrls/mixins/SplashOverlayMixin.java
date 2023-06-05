package com.github.dimadencep.mods.rrls.mixins;

import com.github.dimadencep.mods.rrls.Rrls;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Overlay;
import net.minecraft.client.gui.screen.SplashOverlay;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.resource.ResourceReload;
import net.minecraft.util.math.ColorHelper;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Consumer;

@Mixin(SplashOverlay.class)
public abstract class SplashOverlayMixin extends Overlay {

    @Shadow
    @Final
    public ResourceReload reload;
    @Shadow
    private float progress;

    @Inject(at = @At("TAIL"), method = "<init>")
    private void init(MinecraftClient client, ResourceReload monitor, Consumer<Optional<Throwable>> exceptionHandler, boolean reloading, CallbackInfo ci) { // TODO rewrite
        if (Rrls.attachedOverlay != null && Rrls.attachedOverlay != (Object) this) {
            Rrls.attachedOverlay.reload.whenComplete().cancel(true);
        }

        if ((reloading && Rrls.config.enabled) || (!reloading && Rrls.config.loadingScreenHide))
            Rrls.attachedOverlay = (SplashOverlay) (Object) this;
    }

    @Inject(at = @At("HEAD"), method = "render", cancellable = true)
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        if (Rrls.attachedOverlay == (Object) this)
            ci.cancel();
    }

    @Inject(at = @At("HEAD"), method = "renderProgressBar")
    public void fixProgress(MatrixStack matrices, int minX, int minY, int maxX, int maxY, float opacity, CallbackInfo ci) {
        if (Rrls.attachedOverlay == (Object) this) {
            float t = this.reload.getProgress();
            this.progress = MathHelper.clamp(this.progress * 0.95f + t * 0.050000012f, 0.0f, 1.0f);
        }
    }

    @Redirect(
            method = "renderProgressBar",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/util/math/ColorHelper$Argb;getArgb(IIII)I"
            )
    )
    public int rainbowProgress(int alpha, int red, int green, int blue) {
        if (Rrls.config.rgbProgress && Rrls.attachedOverlay == (Object) this) {
            int baseColor = ThreadLocalRandom.current().nextInt(0, 0xFFFFFF);

            return ColorHelper.Argb.getArgb(alpha, ColorHelper.Argb.getRed(baseColor), ColorHelper.Argb.getGreen(baseColor), ColorHelper.Argb.getBlue(baseColor));
        }

        return ColorHelper.Argb.getArgb(alpha, red, green, blue);
    }
}