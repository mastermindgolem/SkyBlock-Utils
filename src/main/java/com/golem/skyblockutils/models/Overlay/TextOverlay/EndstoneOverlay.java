package com.golem.skyblockutils.models.Overlay.TextOverlay;

import com.golem.skyblockutils.configs.OverlayCategory;
import com.golem.skyblockutils.models.gui.GuiElement;
import com.golem.skyblockutils.models.gui.MoveGui;
import com.golem.skyblockutils.utils.TimeHelper;
import com.golem.skyblockutils.utils.rendering.RenderableString;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.text.DecimalFormat;

import static com.golem.skyblockutils.Main.config;
import static com.golem.skyblockutils.Main.mc;

public class EndstoneOverlay {
    public static GuiElement element = new GuiElement("Endstone Overlay", 50, 20);
    private static final TimeHelper time = new TimeHelper();
    private final DecimalFormat formatter = new DecimalFormat("0.00");
    private static long lastUse = 0;
    private final RenderableString display;

    public EndstoneOverlay() {
        display = new RenderableString("", 0, 0);
    }

    @SubscribeEvent
    public void onChat(ClientChatReceivedEvent event) {
        String message = event.message.getUnformattedText().replaceAll("§.", "");
        if (message.startsWith("Used Extreme Focus! (")) lastUse = time.getCurrentMS();
    }

    @SubscribeEvent
    public void onRenderOverlay(RenderGameOverlayEvent event) {
        if (event.type != RenderGameOverlayEvent.ElementType.TEXT) return;

        if (OverlayCategory.isOverlayOn(config.getConfig().overlayCategory.endStoneConfig.endstoneOverlay)) {

            double buffLeft = lastUse + 5000 - time.getCurrentMS();
            String displayText;
            if (buffLeft < 0) {
                displayText = EnumChatFormatting.DARK_RED + "Endstone Buff: Inactive";
            } else {
                displayText = EnumChatFormatting.YELLOW + "Endstone Buff: " +
                        EnumChatFormatting.GREEN + formatter.format(buffLeft/1000) + "s";
            }

            display.setText(displayText)
                    .setScale(element.position.getScale());
            display.render();

            element.setWidth(display.getWidth());
            element.setHeight(display.getHeight());

        } else if (mc.currentScreen instanceof MoveGui) {

            display.setText(EnumChatFormatting.DARK_RED + "Endstone Buff: Inactive")
                    .setScale(element.position.getScale());
            display.render();

            element.setWidth(display.getWidth());
            element.setHeight(display.getHeight());

        }
    }
}
