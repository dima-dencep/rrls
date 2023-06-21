package com.github.dimadencep.mods.rrls.mixins;

import com.github.dimadencep.mods.rrls.Rrls;
import com.github.dimadencep.mods.rrls.accessor.SplashAccessor;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Overlay;
import net.minecraft.client.gui.screen.SplashOverlay;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.resource.ResourceReload;
import net.minecraft.util.Util;
import net.minecraft.util.math.ColorHelper;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Consumer;

@Mixin(SplashOverlay.class)
public abstract class SplashOverlayMixin extends Overlay implements SplashAccessor {
    @Shadow
    @Final
    private ResourceReload reload;
    @Shadow
    private float progress;
    @Shadow private long reloadCompleteTime;
    @Shadow
    @Final
    private Consumer<Optional<Throwable>> exceptionHandler;
    @Shadow
    @Final private MinecraftClient client;

    @Shadow
    protected abstract void renderProgressBar(DrawContext drawContext, int minX, int minY, int maxX, int maxY, float opacity);

    @Shadow @Final private boolean reloading;
    @Shadow private long reloadStartTime;
    public boolean rrls_attach;

    @Inject(
            method = "<init>",
            at = @At(
                    value = "TAIL"
            )
    )
    private void init(MinecraftClient client, ResourceReload monitor, Consumer<Optional<Throwable>> exceptionHandler, boolean reloading, CallbackInfo ci) {
        this.rrls_attach = (reloading && Rrls.config.enabled) || (!reloading && Rrls.config.loadingScreenHide);
    }

    @Override
    public boolean isAttached() {
        return this.rrls_attach;
    }


    @Inject(
            method = "pausesGame",
            at = @At(
                    value = "TAIL"
            ),
            cancellable = true
    )
    public void pauses(CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(!this.rrls_attach);
    }

    @Override
    public void render(DrawContext context, boolean isGame) {
        if (!Rrls.config.showIn.canShow(isGame)) return;

        int i = context.getScaledWindowWidth();
        int j = context.getScaledWindowHeight();

        int s = (int) ((double) j * 0.8325);
        int r = (int) (Math.min(i * 0.75, j) * 0.5);

        this.renderProgressBar(context, i / 2 - r, s - 5, i / 2 + r, s + 5, 0.8F);
    }

    @Override
    public void reload() {
        long l = Util.getMeasuringTimeMs();
        if (this.reloading && this.reloadStartTime == -1L) {
            this.reloadStartTime = l;
        }
        float f = this.reloadCompleteTime > -1L ? (float)(l - this.reloadCompleteTime) / 1000.0f : -1.0f;

        float t = this.reload.getProgress();
        this.progress = MathHelper.clamp(this.progress * 0.95f + t * 0.050000012f, 0.0f, 1.0f);

        if (f >= 2.0f) {
            this.client.setOverlay(null);
        }

        if (this.reloadCompleteTime == -1L && this.reload.isComplete()) {
            try {
                this.reload.throwException();
                this.exceptionHandler.accept(Optional.empty());
            } catch (Throwable throwable) {
                this.exceptionHandler.accept(Optional.of(throwable));
            }

            this.reloadCompleteTime = Util.getMeasuringTimeMs();

            if (Rrls.config.reInitScreen && this.client.currentScreen != null) {
                this.client.currentScreen.init(this.client, this.client.getWindow().getScaledWidth(), this.client.getWindow().getScaledHeight());
            }
        }
    }


    @Inject(
            method = "render",
            at = @At(
                    value = "HEAD"
            ),
            cancellable = true
    )
    public void render(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        if (this.rrls_attach)
            ci.cancel();
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
    @Inject(method = "render", at = @At("HEAD"))
    private void onRender(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        if (Rrls.config.splashFade && this.reloadCompleteTime > 1) {
            this.client.setOverlay(null);
        }
    }
}