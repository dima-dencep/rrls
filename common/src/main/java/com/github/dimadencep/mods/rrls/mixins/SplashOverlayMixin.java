package com.github.dimadencep.mods.rrls.mixins;

import com.github.dimadencep.mods.rrls.Rrls;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Overlay;
import net.minecraft.client.gui.screen.SplashOverlay;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.resource.ResourceReload;
import net.minecraft.util.math.ColorHelper;
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
    public boolean rrls_attach;

    @Inject(at = @At("TAIL"), method = "<init>")
    private void init(MinecraftClient client, ResourceReload monitor, Consumer<Optional<Throwable>> exceptionHandler, boolean reloading, CallbackInfo ci) {
        if ((reloading && Rrls.config.enabled) || (!reloading && Rrls.config.loadingScreenHide)) {
            SplashOverlay oldSplash = Rrls.attachedOverlay.getAndSet((SplashOverlay) (Object) this);

            this.rrls_attach = true;

            if (oldSplash != null && oldSplash != (Object) this) {
                oldSplash.reload.whenComplete().cancel(true);
            }
        }
    }

    @Inject(at = @At("HEAD"), method = "render", cancellable = true)
    public void render(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        if (this.rrls_attach)
            ci.cancel();
    }

    @Inject(at = @At("HEAD"), method = "renderProgressBar")
    public void fixProgress(DrawContext drawContext, int minX, int minY, int maxX, int maxY, float opacity, CallbackInfo ci) {
        if (this.rrls_attach) {
            this.progress = this.reload.getProgress();
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
        if (Rrls.config.rgbProgress && this.rrls_attach) {
            int baseColor = ThreadLocalRandom.current().nextInt(0, 0xFFFFFF);

            return ColorHelper.Argb.getArgb(alpha, ColorHelper.Argb.getRed(baseColor), ColorHelper.Argb.getGreen(baseColor), ColorHelper.Argb.getBlue(baseColor));
        }

        return ColorHelper.Argb.getArgb(alpha, red, green, blue);
    }
}