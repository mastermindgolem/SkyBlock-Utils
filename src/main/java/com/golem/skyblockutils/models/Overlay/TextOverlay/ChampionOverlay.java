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

import static com.golem.skyblockutils.Main.configFile;
import static com.golem.skyblockutils.Main.mc;
import static com.golem.skyblockutils.features.BrokenHyp.currentXP;
import static com.golem.skyblockutils.features.BrokenHyp.gainedXP;

public class ChampionOverlay {
    public static GuiElement element = new GuiElement("Champion Overlay", 50, 10);
    private static final TimeHelper time = new TimeHelper();
    private final DecimalFormat formatter = new DecimalFormat("00,000,000,000");

    public static int renderWidth(String text) {
        return mc.fontRendererObj.getStringWidth(text);
    }



    @SubscribeEvent
    public void onRenderOverlay(RenderGameOverlayEvent event) {
        if (event.type != RenderGameOverlayEvent.ElementType.TEXT  || !configFile.brokenHyp) return;

        TextStyle textStyle = TextStyle.fromInt(1);

        if (configFile.testGui) {
            if (System.currentTimeMillis() - BrokenHyp.lastKill < 60000) {
                GlStateManager.pushMatrix();
                GlStateManager.translate(element.position.getX(), element.position.getY(), 500.0);
                GlStateManager.scale(element.position.getScale(), element.position.getScale(), 1.0);

                String string = EnumChatFormatting.GOLD + "Champion XP: " + EnumChatFormatting.YELLOW + currentXP + " (+" + gainedXP + ")";
                OverlayUtils.drawString(0, 0, string, textStyle, Alignment.Left);

                element.setWidth(renderWidth(string));
                element.setHeight(10);


                GlStateManager.popMatrix();
            }
        } else if (mc.currentScreen instanceof MoveGui) {
            GlStateManager.pushMatrix();
            GlStateManager.translate(element.position.getX(), element.position.getY(), 500.0);
            GlStateManager.scale(element.position.getScale(), element.position.getScale(), 1.0);

            String string = EnumChatFormatting.GOLD + "Champion XP: " + EnumChatFormatting.YELLOW + "1,234,567,890" + " (+312)";

            OverlayUtils.drawString(0, 0, string, textStyle, Alignment.Left);

            element.setWidth(renderWidth(string));
            element.setHeight(10);

            GlStateManager.popMatrix();
        }
    }

}

