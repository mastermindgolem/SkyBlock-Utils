package com.golem.skyblockutils.models.Overlay.TextOverlay;


import com.golem.skyblockutils.models.gui.*;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.text.DecimalFormat;
import java.util.LinkedList;
import java.util.Objects;
import java.util.Queue;

import static com.golem.skyblockutils.Main.configFile;
import static com.golem.skyblockutils.Main.mc;

public class FPSOverlay {

    public static GuiElement element = new GuiElement("FPS Overlay", 50, 10);

    private final Queue<Integer> fpsValues = new LinkedList<>();

    public static int renderWidth(String text) {
        return mc.fontRendererObj.getStringWidth(text);
    }




    @SubscribeEvent
    public void onRenderOverlay(RenderGameOverlayEvent event) {
        if (event.type != RenderGameOverlayEvent.ElementType.TEXT) return;

        TextStyle textStyle = TextStyle.fromInt(1);
        if (configFile.testGui && configFile.fps) {


            String fpsString = EnumChatFormatting.GOLD + "FPS: " + EnumChatFormatting.WHITE + Minecraft.getDebugFPS();

            GlStateManager.pushMatrix();
            GlStateManager.translate(element.position.getX(), element.position.getY(), 500.0);
            GlStateManager.scale(element.position.getScale(), element.position.getScale(), 1.0);


            OverlayUtils.drawString(0, 0, fpsString, textStyle, Alignment.Left);

            element.setWidth(renderWidth(fpsString));
            element.setHeight(10);

            GlStateManager.popMatrix();
            return;
        }
        if (mc.currentScreen instanceof MoveGui) {

            String fpsString = EnumChatFormatting.GOLD + "FPS: " + EnumChatFormatting.WHITE + 60 + " "
                    + EnumChatFormatting.GRAY + "[" + 60 + " " + 60 + "]";

            GlStateManager.pushMatrix();
            GlStateManager.translate(element.position.getX(), element.position.getY(), 500.0);
            GlStateManager.scale(element.position.getScale(), element.position.getScale(), 1.0);


            OverlayUtils.drawString(0, 0, fpsString, textStyle, Alignment.Left);

            element.setWidth(renderWidth(fpsString));
            element.setHeight(10);

            GlStateManager.popMatrix();
            return;
        }
    }

}
