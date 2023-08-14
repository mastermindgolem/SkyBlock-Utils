package com.golem.skyblockutils.models.Overlay.TextOverlay;

import com.golem.skyblockutils.Main;
import com.golem.skyblockutils.NoteForDecompilers;
import com.golem.skyblockutils.models.gui.*;
import com.golem.skyblockutils.utils.TimeHelper;
import logger.Logger;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.text.DecimalFormat;
import java.util.LinkedList;
import java.util.Queue;

import static com.golem.skyblockutils.Main.*;

public class TPSOverlay {

    public static GuiElement element = new GuiElement("TPS Overlay", 50, 10);

    private int passedTicks = 0;
    private long startTime = 0;
    private float tps = 0.0f;
    private final DecimalFormat tpsFormat = new DecimalFormat("0.0");

    private static final TimeHelper time = new TimeHelper();

    public static int renderWidth(String text) {
        return mc.fontRendererObj.getStringWidth(text);
    }



    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event){


        if (passedTicks == 20) {
            long timeTaken = time.getCurrentMS() - startTime;
            tps = Math.min(20, 20f * (1000 / timeTaken));
            passedTicks = 0;
            startTime = time.getCurrentMS();
        }
        passedTicks++;
    }

    @SubscribeEvent
    public void onRenderOverlay(RenderGameOverlayEvent event) {
        if (event.type != RenderGameOverlayEvent.ElementType.TEXT) return;

        TextStyle textStyle = TextStyle.fromInt(1);

        if (configFile.testGui && configFile.tps) {
            String tpsString = EnumChatFormatting.GOLD + "TPS: " + EnumChatFormatting.WHITE + tpsFormat.format(tps);

            GlStateManager.pushMatrix();
            GlStateManager.translate(element.position.getX(), element.position.getY(), 500.0);
            GlStateManager.scale(element.position.getScale(), element.position.getScale(), 1.0);

            OverlayUtils.drawString(0, 0, tpsString, textStyle, Alignment.Left);

            element.setWidth(renderWidth(tpsString));
            element.setHeight(10);

            GlStateManager.popMatrix();
            return;
        }
        if (mc.currentScreen instanceof MoveGui) {
            GlStateManager.pushMatrix();
            GlStateManager.translate(element.position.getX(), element.position.getY(), 500.0);
            GlStateManager.scale(element.position.getScale(), element.position.getScale(), 1.0);

            String string = EnumChatFormatting.YELLOW + "TPS: " + EnumChatFormatting.WHITE + "20.0";
            OverlayUtils.drawString(0, 0, string, textStyle, Alignment.Left);

            element.setWidth(renderWidth(string));
            element.setHeight(10);

            GlStateManager.popMatrix();
        }
    }
}
