package com.golem.skyblockutils.models.Overlay.TextOverlay;

import com.golem.skyblockutils.Main;
import com.golem.skyblockutils.features.BrokenHyp;
import com.golem.skyblockutils.models.gui.*;
import com.golem.skyblockutils.utils.TimeHelper;
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

    public static String text = "";

    public static int renderWidth(String text) {
        return mc.fontRendererObj.getStringWidth(text);
    }



    @SubscribeEvent
    public void onRenderOverlay(RenderGameOverlayEvent event) {
        if (event.type != RenderGameOverlayEvent.ElementType.TEXT || Objects.equals(text, "")) return;

        TextStyle textStyle = TextStyle.fromInt(1);

        if (configFile.testGui) {
            GlStateManager.pushMatrix();
            GlStateManager.translate(element.position.getX(), element.position.getY(), 500.0);
            GlStateManager.scale(element.position.getScale(), element.position.getScale(), 1.0);
            OverlayUtils.drawString(0, 0, text, textStyle, Alignment.Left);

            element.setHeight(10);

            GlStateManager.popMatrix();

        } else if (mc.currentScreen instanceof MoveGui) {
            GlStateManager.pushMatrix();
            GlStateManager.translate(element.position.getX(), element.position.getY(), 500.0);
            GlStateManager.scale(element.position.getScale(), element.position.getScale(), 1.0);

            String string = EnumChatFormatting.DARK_RED + "ALERT";

            OverlayUtils.drawString(0, 0, string, textStyle, Alignment.Left);

            element.setWidth(renderWidth(string));
            element.setHeight(10);

            GlStateManager.popMatrix();
        }
    }

}


