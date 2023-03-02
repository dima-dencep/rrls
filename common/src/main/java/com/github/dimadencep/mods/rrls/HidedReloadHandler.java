package com.github.dimadencep.mods.rrls;

import net.minecraft.client.MinecraftClient;
import net.minecraft.resource.ResourceReload;

import java.util.Optional;
import java.util.function.Consumer;

public class HidedReloadHandler {
    private Consumer<Optional<Throwable>> exceptionHandler;
    private ResourceReload reload;

    public void setReload(ResourceReload reload) {
        this.reload = reload;
    }

    public ResourceReload getReload() {
        return this.reload;
    }

    public void setExceptionHandler(Consumer<Optional<Throwable>> exceptionHandler) {
        this.exceptionHandler = exceptionHandler;
    }

    public void tick(MinecraftClient minecraft) {
        if (this.reload != null) {
            minecraft.setOverlay(null);

            if (this.reload.isComplete()) {
                try {
                    this.reload.throwException();
                    this.exceptionHandler.accept(Optional.empty());
                } catch (Throwable var23) {
                    this.exceptionHandler.accept(Optional.of(var23));
                }

                this.exceptionHandler = null;
                this.reload = null;

                if (minecraft.currentScreen != null) {
                    minecraft.currentScreen.init(minecraft, minecraft.getWindow().getScaledWidth(), minecraft.getWindow().getScaledHeight());
                }
            }
        }
    }
}