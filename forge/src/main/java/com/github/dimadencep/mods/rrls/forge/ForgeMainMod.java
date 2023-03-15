package com.github.dimadencep.mods.rrls.forge;

import com.github.dimadencep.mods.rrls.MainMod;
import com.github.dimadencep.mods.rrls.config.ModConfig;
import me.shedaniel.autoconfig.AutoConfig;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.resource.language.I18n;
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
public class ForgeMainMod extends MainMod {
    public ForgeMainMod() {
        ModLoadingContext.get().registerExtensionPoint(IExtensionPoint.DisplayTest.class, () -> new IExtensionPoint.DisplayTest(() -> NetworkConstants.IGNORESERVERONLY, (a, b) -> true));

        this.init();

        MinecraftForge.EVENT_BUS.register(this);

        ModLoadingContext.get().registerExtensionPoint(ConfigScreenHandler.ConfigScreenFactory.class, () -> new ConfigScreenHandler.ConfigScreenFactory((mc, screen) -> AutoConfig.getConfigScreen(ModConfig.class, screen).get()));
    }

    @SubscribeEvent
    public void onRenderGui(RenderGuiEvent.Pre event) {
        if (reloadHandler.getReload() != null && config.showInGame)
            DrawableHelper.drawCenteredTextWithShadow(event.getPoseStack(), this.client.textRenderer, I18n.translate(config.reloadText), event.getWindow().getScaledWidth() / 2, 70, config.rgbText ? getColor() : -1);
    }

    @SubscribeEvent
    public void onScreenRender(ScreenEvent.Render event) {
        if (reloadHandler.getReload() != null && config.showInGui)
            DrawableHelper.drawCenteredTextWithShadow(event.getPoseStack(), this.client.textRenderer, I18n.translate(config.reloadText), event.getScreen().width / 2, 70, config.rgbText ? getColor() : -1);
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.START)
            reloadHandler.tick(this.client);
    }
}
