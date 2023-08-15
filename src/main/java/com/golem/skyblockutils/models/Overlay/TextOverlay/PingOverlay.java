package com.golem.skyblockutils.models.Overlay.TextOverlay;

import com.golem.skyblockutils.Main;
import com.golem.skyblockutils.models.gui.*;
import logger.Logger;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.client.network.OldServerPinger;
import net.minecraft.network.play.client.C16PacketClientStatus;
import net.minecraft.network.play.server.S01PacketJoinGame;
import net.minecraft.network.play.server.S37PacketStatistics;

import java.text.DecimalFormat;
import java.util.UUID;

import static com.golem.skyblockutils.Main.*;

public class PingOverlay {

    public static GuiElement element = new GuiElement("Ping Overlay", 50, 10);
    private final DecimalFormat pingFormat = new DecimalFormat("0.0");

    public static int renderWidth(String text) {
        return mc.fontRendererObj.getStringWidth(text);
    }


    private int getPing() {
        NetHandlerPlayClient netHandler = Minecraft.getMinecraft().getNetHandler();
        EntityPlayerSP player = Minecraft.getMinecraft().thePlayer;

        if (netHandler == null || player == null) {
            return -2;
        }

        NetworkPlayerInfo info = netHandler.getPlayerInfo(player.getUniqueID());
        if (info != null) {
            return info.getResponseTime();
        }
        return -1;
    }


    @SubscribeEvent
    public void onRenderOverlay(RenderGameOverlayEvent event) {
        if (event.type != RenderGameOverlayEvent.ElementType.TEXT) return;

        TextStyle textStyle = TextStyle.fromInt(1);

        if (configFile.testGui && configFile.ping) {
            int currentPing = getPing();
            EnumChatFormatting pingColor = EnumChatFormatting.WHITE;
            String pingString = EnumChatFormatting.GOLD + "Ping: " + pingColor + pingFormat.format(currentPing);

            net.minecraft.client.renderer.GlStateManager.pushMatrix();
            net.minecraft.client.renderer.GlStateManager.translate(element.position.getX(), element.position.getY(), 500.0);
            net.minecraft.client.renderer.GlStateManager.scale(element.position.getScale(), element.position.getScale(), 1.0);

            OverlayUtils.drawString(0, 0, pingString, textStyle, Alignment.Left);

            element.setWidth(renderWidth(pingString));
            element.setHeight(10);

            net.minecraft.client.renderer.GlStateManager.popMatrix();
            return;
        }
        if (mc.currentScreen instanceof MoveGui) {
            net.minecraft.client.renderer.GlStateManager.pushMatrix();
            net.minecraft.client.renderer.GlStateManager.translate(element.position.getX(), element.position.getY(), 500.0);
            net.minecraft.client.renderer.GlStateManager.scale(element.position.getScale(), element.position.getScale(), 1.0);

            String string = EnumChatFormatting.GOLD + "Ping: " + EnumChatFormatting.WHITE + "0";
            OverlayUtils.drawString(0, 0, string, textStyle, Alignment.Left);

            element.setWidth(renderWidth(string));
            element.setHeight(10);

            net.minecraft.client.renderer.GlStateManager.popMatrix();
        }
    }
}