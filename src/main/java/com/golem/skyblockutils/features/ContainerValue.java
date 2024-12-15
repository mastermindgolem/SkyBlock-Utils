package com.golem.skyblockutils.features;

import com.golem.skyblockutils.Main;
import com.golem.skyblockutils.init.KeybindsInit;
import com.golem.skyblockutils.injection.mixins.minecraft.client.AccessorGuiContainer;
import com.golem.skyblockutils.models.AttributePrice;
import com.golem.skyblockutils.models.DisplayString;
import com.golem.skyblockutils.models.Overlay.TextOverlay.ContainerOverlay;
import com.google.gson.JsonObject;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.math.BigDecimal;
import java.util.*;

import static com.golem.skyblockutils.Main.configFile;

public class ContainerValue {
	public static boolean isActive = false;

	@SubscribeEvent
	public void guiDraw(GuiScreenEvent.BackgroundDrawnEvent event) {
		try {

			if (!(event.gui instanceof GuiChest)) return;
			if (!isActive) return;
			if (configFile.container_value == 0) return;

			GuiChest gui = (GuiChest) event.gui;
			Container container = gui.inventorySlots;
			if (!(container instanceof ContainerChest)) return;
			String chestName = ((ContainerChest) container).getLowerChestInventory().getDisplayName().getUnformattedText();
			if (chestName.contains("Paid Chest") || chestName.contains("Free Chest"))  return;
			List<Slot> chestInventory = ((GuiChest) Minecraft.getMinecraft().currentScreen).inventorySlots.inventorySlots;
			LinkedHashMap<String, DisplayString> displayStrings = new LinkedHashMap<>();
			BigDecimal totalValue = new BigDecimal("0");


			int xSize = (int) (ContainerOverlay.element.position.getX() - 5);
			int guiLeft = 0;
			int guiTop = (int) (ContainerOverlay.element.position.getY() - 5);

			if (configFile.container_value == 1) {
			AccessorGuiContainer ac = (AccessorGuiContainer) gui;
				// Works for me for intellij and compiling! We needa fix ur intellij ngl
				xSize = ac.getXSize();
				guiLeft = ac.getGuiLeft();
				guiTop = ac.getGuiTop();
			}

			chestInventory = chestInventory.subList(0, chestInventory.size() - 36);
			switch (configFile.dataSource) {
				case 0:
					for (Slot slot : chestInventory) {
						try {
							if (!slot.getHasStack() || slot.getStack().getItem() == Item.getItemFromBlock(Blocks.stained_glass_pane))
								continue;
							JsonObject valueData = AttributePrice.AttributeValue(slot.getStack());
							if (valueData == null) continue;
							String displayString = valueData.get("display_string").getAsString();
							totalValue = totalValue.add(valueData.get("value").getAsBigDecimal());
							displayStrings.put(displayString, new DisplayString(displayStrings.getOrDefault(displayString, new DisplayString(0, 0)).quantity + 1, valueData.get("value").getAsLong()));
							//RenderUtils.highlight(Color.GREEN, (GuiContainer) event.gui, slot);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					break;
				case 1:
					displayStrings = DescriptionHandler.displayStrings;
					break;
			}

			displayStrings = sort(displayStrings);

			if (configFile.dataSource == 0 && totalValue.compareTo(BigDecimal.ONE) < 0)  return;
			long totalLbin = displayStrings.values().stream().mapToLong(displayString -> displayString.price * displayString.quantity).sum();
			long totalMedian = displayStrings.values().stream().mapToLong(displayString -> displayString.median * displayString.quantity).sum();
			if (configFile.dataSource == 1 && (totalLbin == 0 || totalMedian == 0))return;

			GlStateManager.disableLighting();
			GlStateManager.translate(0, 0, 200);
			int counter = 1;



			Minecraft.getMinecraft().fontRendererObj.drawStringWithShadow(
					(configFile.dataSource == 0 || !GameSettings.isKeyDown(KeybindsInit.getComboValue) ?
							EnumChatFormatting.YELLOW + "Total Value: " + EnumChatFormatting.GREEN + Main.formatNumber(totalLbin)
							: EnumChatFormatting.YELLOW + "Total Value: " + EnumChatFormatting.GREEN + Main.formatNumber(totalMedian)),
					guiLeft + xSize + 5,
					guiTop + 5,
					0xffffffff
			);


			for (String displayString : displayStrings.keySet()) {
				int amount = displayStrings.get(displayString).quantity;
				long value = displayStrings.get(displayString).price;
				long median = displayStrings.get(displayString).median;
				if (amount > 1) {
					displayString = amount + "x " + displayString;
				}

				displayString = displayString + EnumChatFormatting.YELLOW + ": " + EnumChatFormatting.GREEN + Main.formatNumber(GameSettings.isKeyDown(KeybindsInit.getComboValue) ? median * amount : value * amount);

				//displayString = displayString + EnumChatFormatting.YELLOW + ": " + EnumChatFormatting.GREEN + Main.formatNumber(value * amount);
				//if (median > 0) displayString = displayString + EnumChatFormatting.GOLD + " (" + Main.formatNumber(median * amount) + ")";
				Minecraft.getMinecraft().fontRendererObj.drawStringWithShadow(
						displayString,
						guiLeft + xSize + 5,
						guiTop + 5 + 10*counter,
						0xffffffff
				);
				counter++;
			}


			GlStateManager.translate(0, 0, -200);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static LinkedHashMap<String, DisplayString> sort(LinkedHashMap<String, DisplayString> map) {
		// Convert the LinkedHashMap to a list of Map.Entry objects
		List<Map.Entry<String, DisplayString>> list = new ArrayList<>(map.entrySet());

		// Define a custom comparator to compare values in descending order
		Comparator<Map.Entry<String, DisplayString>> valueComparator = null;
		switch (configFile.containerSorting) {
			case 0:
				valueComparator = (entry1, entry2) -> {
					long product1 = entry1.getValue().quantity * entry1.getValue().price;
					long product2 = entry2.getValue().quantity * entry2.getValue().price;
					return Long.compare(product2, product1); // Sorting in descending order
				};
				list.sort(valueComparator);
				break;
			case 1:
				valueComparator = (entry1, entry2) -> {
					long product1 = entry1.getValue().quantity * entry1.getValue().price;
					long product2 = entry2.getValue().quantity * entry2.getValue().price;
					return Long.compare(product1, product2); // Sorting in ascending order
				};
				list.sort(valueComparator);
				break;
			case 2:
				valueComparator = Map.Entry.comparingByKey();
				list.sort(valueComparator);
				break;
			case 3:
				valueComparator = (entry1, entry2) -> {
					int tier1 = entry1.getKey().split(" ")[1].matches("\\d+") ? Integer.parseInt(entry1.getKey().split(" ")[1]) : 0;
					int tier2 = entry2.getKey().split(" ")[1].matches("\\d+") ? Integer.parseInt(entry2.getKey().split(" ")[1]) : 0;
					return Integer.compare(tier2, tier1);
				};
				list.sort(valueComparator);
				break;
			case 4:
				valueComparator = Comparator.comparingInt(entry -> ItemType(entry.getKey()));
				list.sort(valueComparator);
				break;

		}

		// Create a new LinkedHashMap to hold the sorted entries
		LinkedHashMap<String, DisplayString> sortedMap = new LinkedHashMap<>();

		// Populate the new LinkedHashMap with sorted entries
		for (Map.Entry<String, DisplayString> entry : list) {
			sortedMap.put(entry.getKey(), entry.getValue());
		}

		return sortedMap;
	}

	private static int ItemType(String entry) {
		if (entry.contains("Helmet")) return 0;
		if (entry.contains("Chestplate")) return 1;
		if (entry.contains("Leggings")) return 2;
		if (entry.contains("Boots")) return 3;
		if (entry.contains("Necklace")) return 4;
		if (entry.contains("Cloak")) return 5;
		if (entry.contains("Belt")) return 6;
		if (entry.contains("Bracelet")) return 6;
		if (entry.contains("Attribute Shard")) return 7;
		if (entry.contains("Contagion")) return 8;
		if (entry.contains("Implosion Belt")) return 9;
		return 10;
	}

}


