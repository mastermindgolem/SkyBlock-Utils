package com.golem.skyblockutils.models.Overlay.TextOverlay;

import com.golem.skyblockutils.Main;
import com.golem.skyblockutils.events.SlotClickEvent;
import com.golem.skyblockutils.features.GuiEvent;
import com.golem.skyblockutils.features.KuudraFight.Kuudra;
import com.golem.skyblockutils.features.KuudraOverlay;
import com.golem.skyblockutils.models.AttributePrice;
import com.golem.skyblockutils.models.gui.*;
import com.golem.skyblockutils.utils.RequestUtil;
import com.golem.skyblockutils.utils.TimeHelper;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C0EPacketClickWindow;
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

public class ProfitOverlay {

    public static GuiElement element = new GuiElement("Profit Overlay", 50, 10);
    private static final TimeHelper time = new TimeHelper();
    private static long totalProfit = 0;
    private static int chests = 0;
    public static long totalTime = 0;
    public static int totalRuns = 0;
    public static long start = 0;
    public static long end = 0;

    public static int renderWidth(String text) {
        return mc.fontRendererObj.getStringWidth(text);
    }

    @SubscribeEvent
    public void onMouseClick(SlotClickEvent event) {
        if (event.slotId != 31) return;
        String chestName = "";
        Container container = null;
        try {
            GuiScreen gui = Main.mc.currentScreen;
            if (!(gui instanceof GuiChest)) return;
            container = ((GuiChest) gui).inventorySlots;
            if (!(container instanceof ContainerChest)) return;
            chestName = ((ContainerChest) container).getLowerChestInventory().getDisplayName().getUnformattedText();
        } catch (Exception ignored) {}
        if (!event.slot.getHasStack() || !chestName.contains("Paid Chest")) return;
        totalProfit += KuudraOverlay.profit;
        chests++;
        if (!configFile.sendProfitData) return;
        try {
            JsonObject data = new JsonObject();
            data.addProperty("tier", Kuudra.tier);
            data.add("primary", new JsonParser().parse(container.getInventory().get(11).serializeNBT().getCompoundTag("tag").getCompoundTag("ExtraAttributes").toString()));
            data.add("secondary", new JsonParser().parse(container.getInventory().get(12).serializeNBT().getCompoundTag("tag").getCompoundTag("ExtraAttributes").toString()));
            data.add("primaryData", AttributePrice.AttributeValue(container.getInventory().get(11)));
            data.add("secondaryData", AttributePrice.AttributeValue(container.getInventory().get(12)));
            data.addProperty("profit", KuudraOverlay.profit);
            data.addProperty("keyCost", KuudraOverlay.keyCost);
            new RequestUtil().sendPostRequest("https://mastermindgolem.pythonanywhere.com/?profit=a", data);
        } catch (Exception ignored) {}


    }


    @SubscribeEvent
    public void onRenderOverlay(RenderGameOverlayEvent event) {
        if (event.type != RenderGameOverlayEvent.ElementType.TEXT) return;

        TextStyle textStyle = TextStyle.fromInt(1);

        if (configFile.testGui && (configFile.profitOverlay == 1 || (configFile.profitOverlay == 2 && Kuudra.currentPhase > 0) || (configFile.profitOverlay == 3 && Kuudra.currentPhase == 5))) {
            GlStateManager.pushMatrix();
            GlStateManager.translate(element.position.getX(), element.position.getY(), 500.0);
            GlStateManager.scale(element.position.getScale(), element.position.getScale(), 1.0);

            String string1 = EnumChatFormatting.GOLD + "Total Profit: " + EnumChatFormatting.GREEN + Main.formatNumber(totalProfit);
            String string2 = EnumChatFormatting.GOLD + "Run Time: " + EnumChatFormatting.GREEN + SplitsOverlay.format((totalTime) / 60000F);
            String string3 = EnumChatFormatting.GOLD + "Chests Opened: " + EnumChatFormatting.GREEN + chests;
            String string4 = EnumChatFormatting.GOLD + "Total Runs: " + EnumChatFormatting.GREEN + totalRuns;
            String string5 = EnumChatFormatting.GOLD + "Profit / Chest: " + EnumChatFormatting.GREEN + Main.formatNumber((chests > 0 ? (double) totalProfit / chests : 0));
            String string6 = EnumChatFormatting.GOLD + "Profit / Hour: " + EnumChatFormatting.GREEN + Main.formatNumber((totalTime > 0 ? (double) totalProfit / totalTime * 3600000 : 0));

            OverlayUtils.drawString(0, 0, string1, textStyle, Alignment.Left);
            OverlayUtils.drawString(0, 10, string2, textStyle, Alignment.Left);
            OverlayUtils.drawString(0, 20, string3, textStyle, Alignment.Left);
            OverlayUtils.drawString(0, 30, string4, textStyle, Alignment.Left);
            OverlayUtils.drawString(0, 40, string5, textStyle, Alignment.Left);
            OverlayUtils.drawString(0, 50, string6, textStyle, Alignment.Left);

            GlStateManager.popMatrix();
        }
        if (mc.currentScreen instanceof MoveGui) {
            GlStateManager.pushMatrix();
            GlStateManager.translate(element.position.getX(), element.position.getY(), 500.0);
            GlStateManager.scale(element.position.getScale(), element.position.getScale(), 1.0);

            int max = 0;
            String string1 = EnumChatFormatting.GOLD + "Total Profit: " + EnumChatFormatting.GREEN + Main.formatNumber(totalProfit);
            max = Math.max(renderWidth(string1), max);
            String string2 = EnumChatFormatting.GOLD + "Run Time: " + EnumChatFormatting.GREEN + SplitsOverlay.format((totalTime) / 60000F);
            max = Math.max(renderWidth(string2), max);
            String string3 = EnumChatFormatting.GOLD + "Chests Opened: " + EnumChatFormatting.GREEN + chests;
            max = Math.max(renderWidth(string3), max);
            String string4 = EnumChatFormatting.GOLD + "Total Runs: " + EnumChatFormatting.GREEN + totalRuns;
            max = Math.max(renderWidth(string4), max);
            String string5 = EnumChatFormatting.GOLD + "Profit / Chest: " + EnumChatFormatting.GREEN + Main.formatNumber((chests > 0 ? (double) totalProfit / chests : 0));
            max = Math.max(renderWidth(string5), max);
            String string6 = EnumChatFormatting.GOLD + "Profit / Hour: " + EnumChatFormatting.GREEN + Main.formatNumber((double) (totalProfit / ((totalTime)/3600000 + 1)));
            max = Math.max(renderWidth(string6), max);

            OverlayUtils.drawString(0, 0, string1, textStyle, Alignment.Left);
            OverlayUtils.drawString(0, 10, string2, textStyle, Alignment.Left);
            OverlayUtils.drawString(0, 20, string3, textStyle, Alignment.Left);
            OverlayUtils.drawString(0, 30, string4, textStyle, Alignment.Left);
            OverlayUtils.drawString(0, 40, string5, textStyle, Alignment.Left);

            element.setWidth(max);
            element.setHeight(60);

            GlStateManager.popMatrix();
        }
    }

    public static void reset() {
        totalProfit = 0;
        chests = 0;
        start = 0;
        end = 0;
        totalTime = 0;
        totalRuns = 0;
    }
}
