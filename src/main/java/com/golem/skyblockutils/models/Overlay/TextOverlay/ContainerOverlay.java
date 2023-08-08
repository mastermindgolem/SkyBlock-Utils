package com.golem.skyblockutils.models.Overlay.TextOverlay;

import com.golem.skyblockutils.Main;
import com.golem.skyblockutils.features.KuudraFight.Kuudra;
import com.golem.skyblockutils.models.gui.*;
import com.golem.skyblockutils.utils.TimeHelper;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.golem.skyblockutils.Main.configFile;
import static com.golem.skyblockutils.Main.mc;

public class ContainerOverlay {


    public static GuiElement element = new GuiElement("Container Overlay", 50, 10);
    private static final TimeHelper time = new TimeHelper();

    private static List<Long> hits = new ArrayList<>();
    private static int level = 0;

    public static int renderWidth(String text) {
        return mc.fontRendererObj.getStringWidth(text);
    }



    @SubscribeEvent
    public void onRenderOverlay(RenderGameOverlayEvent event) {
        if (event.type != RenderGameOverlayEvent.ElementType.TEXT) return;

        TextStyle textStyle = TextStyle.fromInt(1);

        if (mc.currentScreen instanceof MoveGui) {
            GlStateManager.pushMatrix();
            GlStateManager.translate(element.position.getX(), element.position.getY(), 500.0);
            GlStateManager.scale(element.position.getScale(), element.position.getScale(), 1.0);

            int max = 0;

            String string1 = EnumChatFormatting.YELLOW + "Total Value: " + EnumChatFormatting.GREEN + "125.1m";
            max = Math.max(renderWidth(string1), max);
            String string2 = "4x VET 5 Leggings: 28.0m";
            max = Math.max(renderWidth(string2), max);
            String string3 = "4x VET 5 Chestplate: 28.0m";
            max = Math.max(renderWidth(string3), max);
            String string4 = "4x VET 5 Boots: 28.0m";
            max = Math.max(renderWidth(string4), max);

            OverlayUtils.drawString(0, 0, string1, textStyle, Alignment.Left);
            OverlayUtils.drawString(0, 10, string2, textStyle, Alignment.Left);
            OverlayUtils.drawString(0, 20, string3, textStyle, Alignment.Left);
            OverlayUtils.drawString(0, 30, string4, textStyle, Alignment.Left);


            element.setWidth(max);
            element.setHeight(40);

            GlStateManager.popMatrix();
        }
    }
}
