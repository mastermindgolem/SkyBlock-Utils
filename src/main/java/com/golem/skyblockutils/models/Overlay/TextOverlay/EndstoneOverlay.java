package com.golem.skyblockutils.models.Overlay.TextOverlay;

import com.golem.skyblockutils.Main;
import com.golem.skyblockutils.features.KuudraFight.Kuudra;
import com.golem.skyblockutils.models.gui.*;
import com.golem.skyblockutils.utils.RenderUtils;
import com.golem.skyblockutils.utils.TimeHelper;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.text.DecimalFormat;

import static com.golem.skyblockutils.Main.configFile;
import static com.golem.skyblockutils.Main.mc;

public class EndstoneOverlay {

    public static GuiElement element = new GuiElement("Endstone Overlay", 50, 20);

    private static final TimeHelper time = new TimeHelper();

    private final DecimalFormat formatter = new DecimalFormat("0.00");
    private static long lastUse = 0;

    public static int renderWidth(String text) {
        return mc.fontRendererObj.getStringWidth(text);
    }


    @SubscribeEvent
    public void onChat(ClientChatReceivedEvent event) {
        String message = event.message.getUnformattedText().replaceAll("§.", "");
        if (message.startsWith("Used Extreme Focus! (")) lastUse = time.getCurrentMS();
    }

    @SubscribeEvent
    public void onRenderOverlay(RenderGameOverlayEvent event) {
        if (event.type != RenderGameOverlayEvent.ElementType.TEXT) return;

        TextStyle textStyle = TextStyle.fromInt(1);

        if (configFile.testGui && (configFile.endstoneTimer == 1 || (configFile.endstoneTimer == 2 && Kuudra.currentPhase > 0) || (configFile.endstoneTimer == 3 && Kuudra.currentPhase == 4))) {
            GlStateManager.pushMatrix();
            GlStateManager.translate(element.position.getX(), element.position.getY(), 500.0);
            GlStateManager.scale(element.position.getScale(), element.position.getScale(), 1.0);

            double buffLeft = lastUse + 5000 - time.getCurrentMS();
            String string1;
            if (buffLeft < 0) {
                string1 = EnumChatFormatting.DARK_RED + "Endstone Buff: Inactive";
            } else {
                string1 = EnumChatFormatting.YELLOW + "Endstone Buff: " + EnumChatFormatting.GREEN + formatter.format(buffLeft/1000) + "s";
            }
            
            OverlayUtils.drawString(0, 0, string1, textStyle, Alignment.Left);

            element.setWidth(renderWidth(string1));
            element.setHeight(10);

            GlStateManager.popMatrix();
        } else if (mc.currentScreen instanceof MoveGui) {
            GlStateManager.pushMatrix();
            GlStateManager.translate(element.position.getX(), element.position.getY(), 500.0);
            GlStateManager.scale(element.position.getScale(), element.position.getScale(), 1.0);

            String string1 = EnumChatFormatting.DARK_RED + "Endstone Buff: Inactive";

            OverlayUtils.drawString(0, 0, string1, textStyle, Alignment.Left);

            element.setWidth(renderWidth(string1));
            element.setHeight(10);

            GlStateManager.popMatrix();
        }
    }

}
