package com.golem.skyblockutils.models.Overlay.TextOverlay;

import com.golem.skyblockutils.Main;
import com.golem.skyblockutils.features.BrokenHyp;
import com.golem.skyblockutils.init.KeybindsInit;
import com.golem.skyblockutils.models.gui.*;
import com.golem.skyblockutils.utils.RenderUtils;
import com.golem.skyblockutils.utils.TimeHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.text.DecimalFormat;
import java.util.Objects;

import static com.golem.skyblockutils.Main.configFile;
import static com.golem.skyblockutils.Main.mc;
import static com.golem.skyblockutils.features.BrokenHyp.currentXP;
import static com.golem.skyblockutils.features.BrokenHyp.gainedXP;

public class AlertOverlay {
    public static GuiElement element = new GuiElement("Alert Overlay", 50, 10);
    private static final TimeHelper time = new TimeHelper();
    private static long endTime = 0;

    public static String text = "";

    public static int renderWidth(String text) {
        return mc.fontRendererObj.getStringWidth(text);
    }



    public static void newAlert(String string, int ticks) {
        text = string;
        endTime = time.getCurrentMS() + 50L*ticks;
    }


    @SubscribeEvent
    public void onRenderOverlay(RenderGameOverlayEvent event) {
        if (event.type != RenderGameOverlayEvent.ElementType.TEXT || Objects.equals(text, "")) return;

        TextStyle textStyle = TextStyle.fromInt(1);

        if (time.getCurrentMS() >= endTime) text = "";
        ScaledResolution scaledResolution = new ScaledResolution(Minecraft.getMinecraft());
        int screenWidth = scaledResolution.getScaledWidth();
        int screenHeight = scaledResolution.getScaledHeight();

        // Calculate the game viewport size and position within the window
        int gameWidth = Main.mc.displayWidth * Main.mc.gameSettings.guiScale;
        int gameHeight = Main.mc.displayWidth * Main.mc.gameSettings.guiScale;
        int offsetX = (screenWidth - gameWidth) / 2;
        int offsetY = (screenHeight - gameHeight) / 2;

        // Calculate the center of the game viewport
        int x = offsetX + gameWidth / 2;
        int y = offsetY + gameHeight / 2;

        GlStateManager.pushMatrix();
        if (configFile.mainAlert) {
            GlStateManager.translate(element.position.getX(), element.position.getY(), 500.0);
            GlStateManager.scale(element.position.getScale(), element.position.getScale(), 1.0);
        } else {
            GlStateManager.translate(x, y, 500.0);
            GlStateManager.scale(4.0, 4.0, 1.0);
        }
        OverlayUtils.drawString(0, 0, text, textStyle, Alignment.Center);

        element.setHeight(10);

        GlStateManager.popMatrix();
    }

}


