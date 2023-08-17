package com.golem.skyblockutils.models.Overlay.TextOverlay;

import com.golem.skyblockutils.models.gui.*;
import com.golem.skyblockutils.utils.TimeHelper;
import com.golem.skyblockutils.events.PacketEvent;

import com.typesafe.config.ConfigException;
import jline.internal.Log;
import logger.Logger;
import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.network.play.client.C16PacketClientStatus;
import net.minecraft.network.play.server.S37PacketStatistics;

import java.text.DecimalFormat;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import static com.golem.skyblockutils.Main.*;

public class PingOverlay {

    public static GuiElement element = new GuiElement("Ping Overlay", 50, 10);

    private long ping = 0;
    private long lastPing = 0;
    private static final TimeHelper time = new TimeHelper();
    public static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    private int scheledurStatus = 0; // 0 off 1 on
    private     ScheduledFuture<?> scheduledTask;


    public static int renderWidth(String text) {
        return mc.fontRendererObj.getStringWidth(text);
    }

    public void sendPing() {
        if (lastPing == 0) {
            Minecraft.getMinecraft().thePlayer.sendQueue.getNetworkManager().sendPacket(new C16PacketClientStatus(C16PacketClientStatus.EnumState.REQUEST_STATS));
            lastPing = time.getCurrentMS();
        }
    }

    private void calculatePing() {
        if (lastPing != 0) {
            ping = Math.abs(time.getCurrentMS() - lastPing);
            lastPing = 0;
        }
    }

    @SubscribeEvent
    public void onPacket(PacketEvent.ReceiveEvent event) {
        if (event.getPacket() instanceof S37PacketStatistics) {
            calculatePing();
        }
    }

    @SubscribeEvent
    public void onRenderOverlay(RenderGameOverlayEvent event) {
        if (event.type != RenderGameOverlayEvent.ElementType.TEXT) return;
        if (!configFile.ping && scheledurStatus == 1) stopScheduler();
        if (configFile.ping && scheledurStatus == 0) startScheduler();
        TextStyle textStyle = TextStyle.fromInt(1);

        if (configFile.testGui && configFile.ping) {
            EnumChatFormatting pingColor = EnumChatFormatting.WHITE;
            String pingString = EnumChatFormatting.GOLD + "Ping: " + pingColor + ping + " ms";

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

    public void stopScheduler() {
        scheledurStatus = 0;
        if (scheduledTask != null) {
            scheduledTask.cancel(false);
            scheduledTask = null;
            Logger.debug("Scheduler task cancelled");
        }
    }
    public void startScheduler() {
        Logger.debug("Starting scheduler");
        scheledurStatus = 1;
        scheduledTask = scheduler.scheduleAtFixedRate(() -> {
            try {
                sendPing();
            } catch (NullPointerException ignored) {
                Logger.error("No clue why this is null", ignored.getMessage());
            }
        }, 0, 1, TimeUnit.SECONDS);
    }
}
