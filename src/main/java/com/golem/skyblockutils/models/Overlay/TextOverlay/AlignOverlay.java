package com.golem.skyblockutils.models.Overlay.TextOverlay;

import com.golem.skyblockutils.configs.OverlayCategory;
import com.golem.skyblockutils.models.gui.*;
import com.golem.skyblockutils.utils.TimeHelper;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.text.DecimalFormat;

import static com.golem.skyblockutils.Main.config;
import static com.golem.skyblockutils.Main.mc;

public class    AlignOverlay {
    public static GuiElement element = new GuiElement("Align Overlay", 50, 20);

    private static final TimeHelper time = new TimeHelper();
    private final DecimalFormat formatter = new DecimalFormat("0.00");
    private static long lastAlign = 0;
    private static long lastSelfAlign = 0;
    public static int renderWidth(String text) {
        return mc.fontRendererObj.getStringWidth(text);
    }


    @SubscribeEvent
    public void onChat(ClientChatReceivedEvent event) {
        String message = event.message.getUnformattedText().replaceAll("§.", "");
        if ((message.startsWith("You aligned") && (message.contains("other player")) || message.endsWith("yourself!")) || (message.endsWith("casted Cells Alignment on you!") && message.split(" ").length == 6)) {
            lastAlign = time.getCurrentMS();
            if (message.startsWith("You aligned")) lastSelfAlign = time.getCurrentMS();
        }

    }

    @SubscribeEvent
    public void onRenderOverlay(RenderGameOverlayEvent event) {
        if (event.type != RenderGameOverlayEvent.ElementType.TEXT) return;

        TextStyle textStyle = TextStyle.fromInt(1);

        if (OverlayCategory.isOverlayOn(config.getConfig().overlayCategory.alignConfig.alignOverlay)) {
            GlStateManager.pushMatrix();
            GlStateManager.translate(element.position.getX(), element.position.getY(), 500.0);
            GlStateManager.scale(element.position.getScale(), element.position.getScale(), 1.0);

            double timeLeft = lastAlign + 6000 - time.getCurrentMS();
            String timeString;
            if (timeLeft < 0) {
                timeString = EnumChatFormatting.DARK_RED + "NO ALIGN";
                //if (Objects.equals(AlertOverlay.text, EnumChatFormatting.DARK_RED + "ALIGN NOW")) AlertOverlay.text = "";
            } else if (timeLeft <= 1000) {
                timeString = EnumChatFormatting.YELLOW + "ALIGN: " + EnumChatFormatting.RED + formatter.format(timeLeft/1000) + "s";
                if (time.getCurrentMS() - lastSelfAlign >= 10000) AlertOverlay.newAlert(EnumChatFormatting.DARK_RED + "ALIGN NOW", 1);
            } else {
                timeString = EnumChatFormatting.YELLOW + "ALIGN: " + EnumChatFormatting.GREEN + formatter.format(timeLeft/1000) + "s";
                //if (Objects.equals(AlertOverlay.text, EnumChatFormatting.DARK_RED + "ALIGN NOW")) AlertOverlay.text = "";
            }

            OverlayUtils.drawString(0, 0, timeString, textStyle, Alignment.Left);

            element.setWidth(renderWidth(timeString));
            element.setHeight(10);

            GlStateManager.popMatrix();
            return;
        }
        if (mc.currentScreen instanceof MoveGui) {
            GlStateManager.pushMatrix();
            GlStateManager.translate(element.position.getX(), element.position.getY(), 500.0);
            GlStateManager.scale(element.position.getScale(), element.position.getScale(), 1.0);

            String string = EnumChatFormatting.YELLOW + "ALIGN: " + EnumChatFormatting.GREEN + "6.00s";
            OverlayUtils.drawString(0, 0, string, textStyle, Alignment.Left);

            element.setWidth(renderWidth(string));
            element.setHeight(10);

            GlStateManager.popMatrix();
        }
    }
}
