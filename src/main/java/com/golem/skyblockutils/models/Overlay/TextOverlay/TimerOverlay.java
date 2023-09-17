package com.golem.skyblockutils.models.Overlay.TextOverlay;

import com.golem.skyblockutils.Main;
import com.golem.skyblockutils.features.KuudraFight.Kuudra;
import com.golem.skyblockutils.models.gui.*;
import com.golem.skyblockutils.utils.TimeHelper;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.text.DecimalFormat;
import java.util.*;

import static com.golem.skyblockutils.Main.configFile;
import static com.golem.skyblockutils.Main.mc;

public class TimerOverlay {

    public static GuiElement element = new GuiElement("Timer Overlay", 50, 10);
    private static final TimeHelper time = new TimeHelper();
    public static long timer = 0;
    public static boolean active = false;

    public static int renderWidth(String text) {
        return mc.fontRendererObj.getStringWidth(text);
    }


    @SubscribeEvent
    public void onRenderOverlay(RenderGameOverlayEvent event) {
        if (event.type != RenderGameOverlayEvent.ElementType.TEXT || configFile.timerAmount == 0) return;

        TextStyle textStyle = TextStyle.fromInt(1);

        if (configFile.testGui && active) {
            GlStateManager.pushMatrix();
            GlStateManager.translate(element.position.getX(), element.position.getY(), 500.0);
            GlStateManager.scale(element.position.getScale(), element.position.getScale(), 1.0);

            EnumChatFormatting prefix = EnumChatFormatting.YELLOW;
            if (time.getCurrentMS() - timer > configFile.timerAmount * 1000L) prefix = EnumChatFormatting.RED;
            if (time.getCurrentMS() - timer > configFile.timerAmount * 1000L && time.getCurrentMS() - timer < (configFile.timerAmount + 1) * 1000L) AlertOverlay.newAlert(EnumChatFormatting.RED + "TIMER ENDED", 40);
            OverlayUtils.drawString(0, 0, prefix + SplitsOverlay.format((time.getCurrentMS() - timer)/60000f), textStyle, Alignment.Center);

            GlStateManager.popMatrix();

        }
        if (mc.currentScreen instanceof MoveGui) {
            GlStateManager.pushMatrix();
            GlStateManager.translate(element.position.getX(), element.position.getY(), 500.0);
            GlStateManager.scale(element.position.getScale(), element.position.getScale(), 1.0);

            OverlayUtils.drawString(0, 0, EnumChatFormatting.YELLOW + "4m20s", textStyle, Alignment.Center);

            element.setWidth(renderWidth("4m20s"));
            element.setHeight(10);

            GlStateManager.popMatrix();
        }
    }

}
