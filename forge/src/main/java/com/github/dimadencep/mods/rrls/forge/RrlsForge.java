package com.github.dimadencep.mods.rrls.forge;

import com.github.dimadencep.mods.rrls.Rrls;
import com.github.dimadencep.mods.rrls.accessor.SplashAccessor;
import com.github.dimadencep.mods.rrls.config.ModConfig;
import me.shedaniel.autoconfig.AutoConfig;
import net.minecraftforge.client.ConfigScreenHandler;
import net.minecraftforge.client.event.RenderGuiEvent;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.IExtensionPoint;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.NetworkConstants;

@Mod("rrls")
public class RrlsForge extends Rrls {
    public RrlsForge() {
        ModLoadingContext.get().registerExtensionPoint(
                IExtensionPoint.DisplayTest.class,
                () -> new IExtensionPoint.DisplayTest(
                        () -> NetworkConstants.IGNORESERVERONLY,
                        (a, b) -> true
                )
        );

        MinecraftForge.EVENT_BUS.register(this);

        ModLoadingContext.get().registerExtensionPoint(
                ConfigScreenHandler.ConfigScreenFactory.class,
                () -> new ConfigScreenHandler.ConfigScreenFactory(
                        (mc, screen) -> AutoConfig.getConfigScreen(ModConfig.class, screen).get()
                )
        );
    }

    @SubscribeEvent
    public void onRenderGui(RenderGuiEvent.Pre event) {
        if (this.client.overlay instanceof SplashAccessor accessor && accessor.isAttached())
            accessor.render(event.getPoseStack(), true);
    }

    @SubscribeEvent
    public void onScreenRender(ScreenEvent.Render event) {
        if (this.client.overlay instanceof SplashAccessor accessor && accessor.isAttached())
            accessor.render(event.getPoseStack(), false);
    }

    @SubscribeEvent
    public void onTick(TickEvent.RenderTickEvent event) {
        if (event.phase == TickEvent.Phase.END && this.client.overlay instanceof SplashAccessor accessor && accessor.isAttached())
            accessor.reload();
    }
}
