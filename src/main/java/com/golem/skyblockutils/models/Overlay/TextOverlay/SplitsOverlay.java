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
import static com.golem.skyblockutils.features.KuudraFight.Kuudra.splits;

public class SplitsOverlay {
    public static GuiElement element = new GuiElement("Splits Overlay", 50, 10);
    private static final DecimalFormat formatter = new DecimalFormat("0m00s");
    private static final TimeHelper time = new TimeHelper();

    public static String text = "";

    public static int renderWidth(String text) {
        return mc.fontRendererObj.getStringWidth(text);
    }



    @SubscribeEvent
    public void onRenderOverlay(RenderGameOverlayEvent event) {
        if (event.type != RenderGameOverlayEvent.ElementType.TEXT || !configFile.splitsOverlay) return;

        TextStyle textStyle = TextStyle.fromInt(1);

        if (configFile.testGui) {
            GlStateManager.pushMatrix();
            GlStateManager.translate(element.position.getX(), element.position.getY(), 500.0);
            GlStateManager.scale(element.position.getScale(), element.position.getScale(), 1.0);


            OverlayUtils.drawString(0, 0, EnumChatFormatting.AQUA + "Supplies: " + EnumChatFormatting.RESET + format(splits[2]/60000F - splits[1]/60000F), textStyle, Alignment.Left);
            OverlayUtils.drawString(0, 10, EnumChatFormatting.AQUA + "Build: " + EnumChatFormatting.RESET + format(splits[3]/60000F - splits[2]/60000F), textStyle, Alignment.Left);
            OverlayUtils.drawString(0, 20, EnumChatFormatting.AQUA + "Fuel/Stun: " + EnumChatFormatting.RESET + format(splits[4]/60000F - splits[3]/60000F), textStyle, Alignment.Left);
            OverlayUtils.drawString(0, 30, EnumChatFormatting.AQUA + "Kuudra Kill: " + EnumChatFormatting.RESET + format(splits[5]/60000F - splits[4]/60000F), textStyle, Alignment.Left);

            element.setHeight(40);

            GlStateManager.popMatrix();

        } else if (mc.currentScreen instanceof MoveGui) {
            GlStateManager.pushMatrix();
            GlStateManager.translate(element.position.getX(), element.position.getY(), 500.0);
            GlStateManager.scale(element.position.getScale(), element.position.getScale(), 1.0);

            OverlayUtils.drawString(0, 0, EnumChatFormatting.AQUA + "Supplies: " + EnumChatFormatting.RESET + format(1.11), textStyle, Alignment.Left);
            OverlayUtils.drawString(0, 10, EnumChatFormatting.AQUA + "Build: " + EnumChatFormatting.RESET + format(2.22), textStyle, Alignment.Left);
            OverlayUtils.drawString(0, 20, EnumChatFormatting.AQUA + "Fuel/Stun: " + EnumChatFormatting.RESET + format(3.33), textStyle, Alignment.Left);
            OverlayUtils.drawString(0, 30, EnumChatFormatting.AQUA + "Kuudra Kill: " + EnumChatFormatting.RESET + format(4.44), textStyle, Alignment.Left);


            element.setWidth(renderWidth(EnumChatFormatting.AQUA + "Kuudra Kill: " + EnumChatFormatting.RESET + format(4.44)));
            element.setHeight(40);

            GlStateManager.popMatrix();
        }
    }
    
    
    public static String format(double time) {
        if (time < 1) {
            return (int) time * 60 + "s";
        }
        return (int) time + "m" + (int) ((time - (int) time) * 60) + "s";
    }

}


