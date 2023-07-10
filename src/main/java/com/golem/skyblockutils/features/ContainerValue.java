package com.golem.skyblockutils.features;

import com.golem.skyblockutils.Main;
import com.golem.skyblockutils.injection.mixins.minecraft.client.AccessorGuiContainer;
import com.golem.skyblockutils.models.AttributePrice;
import com.google.gson.JsonObject;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.math.BigInteger;
import java.util.*;
import java.util.stream.Collectors;

import static com.golem.skyblockutils.Main.configFile;
import static com.golem.skyblockutils.models.AttributePrice.*;

public class ContainerValue {
	public static boolean isActive = false;

	@SubscribeEvent
	public void guiDraw(GuiScreenEvent.BackgroundDrawnEvent event) {
		try {
			if (!(event.gui instanceof GuiChest)) return;
			if (!isActive) return;
			if (!configFile.container_value) return;

			GuiChest gui = (GuiChest) event.gui;
			Container container = gui.inventorySlots;
			if (!(container instanceof ContainerChest)) return;
			String chestName = ((ContainerChest) container).getLowerChestInventory().getDisplayName().getUnformattedText();
			if (chestName.contains("Paid Chest") || chestName.contains("Free Chest")) return;
			List<Slot> chestInventory = ((GuiChest) Minecraft.getMinecraft().currentScreen).inventorySlots.inventorySlots;
			HashMap<String, Integer> displayStrings = new HashMap<>();
			BigInteger totalValue = new BigInteger("0");

			List<String> excludeAttributes = Arrays.asList(Main.configFile.attributesToExclude.split(", "));
			List<String> priorityAttributes = Arrays.asList(Main.configFile.priorityAttributes.split(", "));

			int xSize = ((AccessorGuiContainer) gui).getXSize();
			int guiLeft = ((AccessorGuiContainer) gui).getGuiLeft();
			int guiTop = ((AccessorGuiContainer) gui).getGuiTop();

			chestInventory = chestInventory.subList(0, chestInventory.size() - 36);
			for (Slot slot : chestInventory) {
				try {
					if (!slot.getHasStack() || slot.getStack().getItem() == Item.getItemFromBlock(Blocks.stained_glass_pane))
						continue;
					JsonObject valueData = AttributePrice.AttributeValue(slot.getStack());
					if (valueData == null) continue;
					String displayString = valueData.get("display_string").getAsString();
					totalValue = totalValue.add(valueData.get("value").getAsBigInteger());
					displayStrings.put(displayString, displayStrings.getOrDefault(displayString, 0) + 1);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}


			GlStateManager.disableLighting();
			GlStateManager.translate(0, 0, 200);
			int counter = 0;
			for (String displayString : displayStrings.keySet()) {
				int amount = displayStrings.get(displayString);
				if (amount > 1) {
					displayString = amount + "x " + displayString;
				}
				Minecraft.getMinecraft().fontRendererObj.drawStringWithShadow(
						displayString,
						guiLeft + xSize + 5,
						guiTop + 5 + 10*counter,
						0xffffffff
				);
				counter++;
			}

			Minecraft.getMinecraft().fontRendererObj.drawStringWithShadow(
					EnumChatFormatting.YELLOW + "Total Value: " + EnumChatFormatting.GREEN + Main.formatNumber(totalValue),
					guiLeft + xSize + 5,
					guiTop + 5 + 10 * displayStrings.size(),
					0xffffffff
			);

			GlStateManager.translate(0, 0, -200);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}