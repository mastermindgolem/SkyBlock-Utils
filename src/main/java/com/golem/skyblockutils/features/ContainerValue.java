package com.golem.skyblockutils.features;

import com.golem.skyblockutils.Main;
import com.golem.skyblockutils.events.InventoryChangeEvent;
import com.golem.skyblockutils.injection.mixins.minecraft.client.AccessorGuiContainer;
import com.golem.skyblockutils.models.DisplayString;
import com.golem.skyblockutils.models.Overlay.TextOverlay.ContainerOverlay;
import com.golem.skyblockutils.utils.InventoryData;
import com.google.gson.JsonObject;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.inventory.Slot;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.client.config.GuiCheckBox;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Mouse;

import java.util.*;

import static com.golem.skyblockutils.Main.configFile;

public class ContainerValue {
	List<String> renderStrings = new ArrayList<>();
	private int xSize;
	private int guiLeft;
	private int guiTop;
	private long totalValue;

	private GuiCheckBox active;

	@SubscribeEvent
	public void onInventoryChange(InventoryChangeEvent event) {
		try {
			if (!(event.event.gui instanceof GuiChest)) return;
			if (configFile.container_value == 0) return;

			GuiChest gui = (GuiChest) event.event.gui;
			Container container = gui.inventorySlots;
			if (!(container instanceof ContainerChest)) return;
			String chestName = ((ContainerChest) container).getLowerChestInventory().getDisplayName().getUnformattedText();
			if (chestName.contains("Paid Chest") || chestName.contains("Free Chest")) return;
			LinkedHashMap<String, DisplayString> displayStrings = new LinkedHashMap<>();
			totalValue = 0;

			xSize = (int) (ContainerOverlay.element.position.getX() - 5);
			guiLeft = 0;
			guiTop = (int) (ContainerOverlay.element.position.getY() - 5);

			if (configFile.container_value == 1) {
				AccessorGuiContainer ac = (AccessorGuiContainer) gui;
				xSize = ac.getXSize();
				guiLeft = ac.getGuiLeft();
				guiTop = ac.getGuiTop();
			}

			for (Slot slot : InventoryData.containerSlots.subList(0, InventoryData.containerSlots.size() - 36)) {
				JsonObject value = InventoryData.values.get(slot);
				if (value == null) continue;
				String displayString = value.get("display_string").getAsString();
				totalValue += value.get("value").getAsLong();
				displayStrings.put(displayString, new DisplayString(displayStrings.getOrDefault(displayString, new DisplayString(0, 0)).quantity + 1, value.get("value").getAsLong()));
			}

			displayStrings = sort(displayStrings);

			for (String displayString : displayStrings.keySet()) {
				int amount = displayStrings.get(displayString).quantity;
				long value = displayStrings.get(displayString).price;
				if (amount > 1) {
					displayString = amount + "x " + displayString;
				}

				renderStrings.add(displayString + EnumChatFormatting.YELLOW + ": " + EnumChatFormatting.GREEN + Main.formatNumber(value * amount));
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@SubscribeEvent
	public void guiDraw(GuiScreenEvent.BackgroundDrawnEvent event) {
		try {
			if (!(event.gui instanceof GuiChest)) return;
			if (configFile.container_value == 0) return;

			GuiChest gui = (GuiChest) event.gui;
			Container container = gui.inventorySlots;
			if (!(container instanceof ContainerChest)) return;
			String chestName = ((ContainerChest) container).getLowerChestInventory().getDisplayName().getUnformattedText();
			if (chestName.contains("Paid Chest") || chestName.contains("Free Chest")) return;

			if (active == null) {
				active = new GuiCheckBox(999, 5, event.gui.height - 25, "SBU Container Value", false);
			}
			active.yPosition = event.gui.height - 25;
			active.drawButton(Main.mc, 0, 0);

			if (active == null || !active.isChecked()) return;

			if (totalValue <= 0)  return;

			GlStateManager.disableLighting();
			GlStateManager.translate(0, 0, 200);
			int counter = 1;

			Minecraft.getMinecraft().fontRendererObj.drawStringWithShadow(
					EnumChatFormatting.YELLOW + "Total Value: " + EnumChatFormatting.GREEN + Main.formatNumber(totalValue),
					guiLeft + xSize + 5,
					guiTop + 5,
					0xffffffff
			);

			for (String displayString : renderStrings) {
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

	@SubscribeEvent
	public void onGuiClick(GuiScreenEvent.MouseInputEvent.Pre event) {
		if (active != null && Mouse.getEventButtonState()) {
			int mouseX = Mouse.getEventX() * event.gui.width / Minecraft.getMinecraft().displayWidth;
			int mouseY = event.gui.height - Mouse.getEventY() * event.gui.height / Minecraft.getMinecraft().displayHeight - 1;

			active.mousePressed(Main.mc, mouseX, mouseY);

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


