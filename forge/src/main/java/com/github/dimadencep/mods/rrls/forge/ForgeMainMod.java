package com.github.dimadencep.mods.rrls.forge;

import com.github.dimadencep.mods.rrls.MainMod;
import com.github.dimadencep.mods.rrls.config.ModConfig;
import me.shedaniel.autoconfig.AutoConfig;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.resource.language.I18n;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ExtensionPoint;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.network.FMLNetworkConstants;
import org.apache.commons.lang3.tuple.Pair;

@Mod("rrls")
public class ForgeMainMod extends MainMod {
    public ForgeMainMod() {
        ModLoadingContext.get().registerExtensionPoint(ExtensionPoint.DISPLAYTEST, () -> Pair.of(() -> FMLNetworkConstants.IGNORESERVERONLY, (a, b) -> true));

        this.init();

        MinecraftForge.EVENT_BUS.register(this);

        ModLoadingContext.get().registerExtensionPoint(ExtensionPoint.CONFIGGUIFACTORY, () -> (mc, screen) -> AutoConfig.getConfigScreen(ModConfig.class, screen).get());
    }

    @SubscribeEvent
    public void onRenderGui(RenderGameOverlayEvent.Post event) {
        if (reloadHandler.getReload() != null && config.showInGame)
            DrawableHelper.drawCenteredText(event.getMatrixStack(), this.client.textRenderer, I18n.translate(config.reloadText), event.getWindow().getScaledWidth() / 2, 70, config.rgbText ? getColor() : -1);
    }

    @SubscribeEvent
    public void onScreenRender(GuiScreenEvent.DrawScreenEvent event) {
        if (reloadHandler.getReload() != null && config.showInGui)
            DrawableHelper.drawCenteredText(event.getMatrixStack(), this.client.textRenderer, I18n.translate(config.reloadText), event.getGui().width / 2, 70, config.rgbText ? getColor() : -1);
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.START)
            reloadHandler.tick(this.client);
    }
}
