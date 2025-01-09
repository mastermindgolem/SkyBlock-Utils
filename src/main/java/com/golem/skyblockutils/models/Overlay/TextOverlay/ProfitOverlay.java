package com.golem.skyblockutils.models.Overlay.TextOverlay;

import com.golem.skyblockutils.Main;
import com.golem.skyblockutils.events.SlotClickEvent;
import com.golem.skyblockutils.features.KuudraFight.Kuudra;
import com.golem.skyblockutils.features.KuudraOverlay;
import com.golem.skyblockutils.models.AttributePrice;
import com.golem.skyblockutils.models.gui.*;
import com.golem.skyblockutils.utils.RequestUtil;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import static com.golem.skyblockutils.Main.*;

public class ProfitOverlay {

    public static GuiElement element = new GuiElement("Profit Overlay", 50, 10);
    private static long totalProfit = 0;
    private static int chests = 0;
    public static long totalTime = 0;
    public static long totalTimeWithoutDowntime = 0;
    public static int totalRuns = 0;
    public static long runStartTime = 0;
    public static int numRerolls = 0;
    public static long runEndTime = 0;
    public static long lastChestOpen = 0;

    public static int renderWidth(String text) {
        return mc.fontRendererObj.getStringWidth(text);
    }

    @SubscribeEvent
    public void onMouseClick(SlotClickEvent event) {
        if (event.slotId != 31 && event.slotId != 50) return;
        if (time.getCurrentMS() - lastChestOpen < 60000) return;
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
        if (event.slotId == 50) {
            checkReroll(event);
            return;
        }
        lastChestOpen = time.getCurrentMS();
        totalProfit += KuudraOverlay.totalProfit;
        chests++;

        if (!configFile.sendProfitData) return;
        Container finalContainer = container;
        new Thread(() -> {
            try {
                JsonObject data = new JsonObject();
                data.addProperty("tier", Kuudra.tier);
                data.add("primary", new JsonParser().parse(finalContainer.getInventory().get(11).serializeNBT().getCompoundTag("tag").getCompoundTag("ExtraAttributes").toString()));
                data.add("secondary", new JsonParser().parse(finalContainer.getInventory().get(12).serializeNBT().getCompoundTag("tag").getCompoundTag("ExtraAttributes").toString()));
                data.add("primaryData", AttributePrice.AttributeValue(finalContainer.getInventory().get(11)).toJson());
                data.add("secondaryData", AttributePrice.AttributeValue(finalContainer.getInventory().get(12)).toJson());
                data.addProperty("profit", KuudraOverlay.totalProfit);
                data.addProperty("keyCost", KuudraOverlay.keyCost);
                new RequestUtil().sendPostRequest("https://mastermindgolem.pythonanywhere.com/?profit=a", data);
            } catch (Exception ignored) {}
        }).start();

    }

    private void checkReroll(SlotClickEvent event) {
        if (event.slot.getHasStack()) {
            String lore = event.slot.getStack().getTagCompound().getCompoundTag("display").getTagList("Lore", 8).toString();
            if (lore.contains("Click to reroll this chest!")) {
                numRerolls++;
                totalProfit -= bazaar.get("products").getAsJsonObject().get("KISMET_FEATHER").getAsJsonObject().get("buy_summary").getAsJsonArray().get(0).getAsJsonObject().get("pricePerUnit").getAsInt();
            }
        }
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
            String string2;
            if (configFile.includeDowntime) {
                string2 = EnumChatFormatting.GOLD + "Run Time: " + EnumChatFormatting.GREEN + SplitsOverlay.format((totalTime) / 60000F) + EnumChatFormatting.YELLOW + " / " + EnumChatFormatting.GREEN + SplitsOverlay.format((totalTimeWithoutDowntime) / 60000F);
            } else {
                string2 = EnumChatFormatting.GOLD + "Run Time: " + EnumChatFormatting.GREEN + SplitsOverlay.format((totalTime) / 60000F);
            }
            String string3 = EnumChatFormatting.GOLD + "Chests Opened: " + EnumChatFormatting.GREEN + chests;
            String string4 = EnumChatFormatting.GOLD + "Total Runs: " + EnumChatFormatting.GREEN + totalRuns;
            String string5 = EnumChatFormatting.GOLD + "Rerolls: " + EnumChatFormatting.GREEN + numRerolls;
            String string6 = EnumChatFormatting.GOLD + "Profit / Chest: " + EnumChatFormatting.GREEN + Main.formatNumber((chests > 0 ? (double) totalProfit / chests : 0));
            String string7 = EnumChatFormatting.GOLD + "Profit / Hour: " + EnumChatFormatting.GREEN + Main.formatNumber((totalTimeWithoutDowntime > 0 ? (double) totalProfit / (configFile.includeDowntime ? totalTime : totalTimeWithoutDowntime) * 3600000 : 0));

            OverlayUtils.drawString(0, 0, string1, textStyle, Alignment.Left);
            OverlayUtils.drawString(0, 10, string2, textStyle, Alignment.Left);
            OverlayUtils.drawString(0, 20, string3, textStyle, Alignment.Left);
            OverlayUtils.drawString(0, 30, string4, textStyle, Alignment.Left);
            OverlayUtils.drawString(0, 40, string5, textStyle, Alignment.Left);
            OverlayUtils.drawString(0, 50, string6, textStyle, Alignment.Left);
            OverlayUtils.drawString(0, 60, string7, textStyle, Alignment.Left);

            GlStateManager.popMatrix();
        }
        if (mc.currentScreen instanceof MoveGui) {
            GlStateManager.pushMatrix();
            GlStateManager.translate(element.position.getX(), element.position.getY(), 500.0);
            GlStateManager.scale(element.position.getScale(), element.position.getScale(), 1.0);

            int max = 0;
            String string1 = EnumChatFormatting.GOLD + "Total Profit: " + EnumChatFormatting.GREEN + Main.formatNumber(totalProfit);
            max = Math.max(renderWidth(string1), max);
            String string2;
            if (configFile.includeDowntime) {
                string2 = EnumChatFormatting.GOLD + "Run Time: " + EnumChatFormatting.GREEN + SplitsOverlay.format((totalTime) / 60000F) + EnumChatFormatting.YELLOW + " / " + EnumChatFormatting.GREEN + SplitsOverlay.format((totalTimeWithoutDowntime) / 60000F);
            } else {
                string2 = EnumChatFormatting.GOLD + "Run Time: " + EnumChatFormatting.GREEN + SplitsOverlay.format((totalTime) / 60000F);
            }
            max = Math.max(renderWidth(string2), max);
            String string3 = EnumChatFormatting.GOLD + "Chests Opened: " + EnumChatFormatting.GREEN + chests;
            max = Math.max(renderWidth(string3), max);
            String string4 = EnumChatFormatting.GOLD + "Rerolls: " + EnumChatFormatting.GREEN + numRerolls;
            max = Math.max(renderWidth(string3), max);
            String string5 = EnumChatFormatting.GOLD + "Total Runs: " + EnumChatFormatting.GREEN + totalRuns;
            max = Math.max(renderWidth(string4), max);
            String string6 = EnumChatFormatting.GOLD + "Profit / Chest: " + EnumChatFormatting.GREEN + Main.formatNumber((chests > 0 ? (double) totalProfit / chests : 0));
            max = Math.max(renderWidth(string5), max);
            String string7 = EnumChatFormatting.GOLD + "Profit / Hour: " + EnumChatFormatting.GREEN + Main.formatNumber((totalTimeWithoutDowntime > 0 ? (double) totalProfit / (configFile.includeDowntime ? totalTime : totalTimeWithoutDowntime) * 3600000 : 0));
            max = Math.max(renderWidth(string6), max);

            OverlayUtils.drawString(0, 0, string1, textStyle, Alignment.Left);
            OverlayUtils.drawString(0, 10, string2, textStyle, Alignment.Left);
            OverlayUtils.drawString(0, 20, string3, textStyle, Alignment.Left);
            OverlayUtils.drawString(0, 30, string4, textStyle, Alignment.Left);
            OverlayUtils.drawString(0, 40, string5, textStyle, Alignment.Left);
            OverlayUtils.drawString(0, 50, string6, textStyle, Alignment.Left);
            OverlayUtils.drawString(0, 60, string7, textStyle, Alignment.Left);

            element.setWidth(max);
            element.setHeight(60);

            GlStateManager.popMatrix();
        }
    }

    public static void reset() {
        totalProfit = 0;
        chests = 0;
        runStartTime = 0;
        runEndTime = 0;
        totalTime = 0;
        totalRuns = 0;
    }
}
