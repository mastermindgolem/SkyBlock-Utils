package com.golem.skyblockutils.models.Overlay.TextOverlay;

import com.golem.skyblockutils.models.gui.*;
import com.golem.skyblockutils.utils.TimeHelper;
import com.golem.skyblockutils.utils.rendering.RenderableString;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.text.DecimalFormat;

import static com.golem.skyblockutils.Main.configFile;
import static com.golem.skyblockutils.Main.mc;

public class DropshipOverlay {
    public static GuiElement element = new GuiElement("Dropship Overlay", 50, 20);
    private static final TimeHelper time = new TimeHelper();
    private final DecimalFormat formatter = new DecimalFormat("0.00");
    private static long lastAlign = 0;
    private final RenderableString display;

    public DropshipOverlay() {
        display = new RenderableString("", 0, 0);
    }

    @SubscribeEvent
    public void onChat(ClientChatReceivedEvent event) {
        String message = event.message.getUnformattedText().replaceAll("§.", "");
        if ((message.startsWith("You aligned") && (message.contains("other player")) ||
                message.endsWith("yourself!")) ||
                (message.endsWith("casted Cells Alignment on you!") && message.split(" ").length == 6)) {
            lastAlign = time.getCurrentMS();
        }
    }

    @SubscribeEvent
    public void onRenderOverlay(RenderGameOverlayEvent event) {
        if (event.type != RenderGameOverlayEvent.ElementType.TEXT) return;

        if (configFile.testGui) {

            double timeLeft = lastAlign + 6000 - time.getCurrentMS();
            String displayText;
            if (timeLeft < 0) {
                displayText = EnumChatFormatting.DARK_RED + "NO ALIGN";
            } else if (timeLeft <= 1000) {
                displayText = EnumChatFormatting.YELLOW + "ALIGN: " +
                        EnumChatFormatting.RED + formatter.format(timeLeft/1000) + "s";
            } else {
                displayText = EnumChatFormatting.YELLOW + "ALIGN: " +
                        EnumChatFormatting.GREEN + formatter.format(timeLeft/1000) + "s";
            }

            display.setText(displayText)
                    .setScale(element.position.getScale());
            display.render();

            element.setWidth(display.getWidth());
            element.setHeight((int) (display.getHeight() * element.position.getScale()));

        } else if (mc.currentScreen instanceof MoveGui) {

            display.setText(EnumChatFormatting.YELLOW + "ALIGN: " +
                            EnumChatFormatting.GREEN + "6.00s")
                    .setScale(element.position.getScale());
            display.render();

            element.setWidth(display.getWidth());
            element.setHeight((int) (display.getHeight() * element.position.getScale()));

            GlStateManager.popMatrix();
        }
    }
}