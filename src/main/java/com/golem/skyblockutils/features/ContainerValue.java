package com.golem.skyblockutils.features;

import com.golem.skyblockutils.Main;
import com.golem.skyblockutils.configs.overlays.ContainerConfig;
import com.golem.skyblockutils.events.InventoryChangeEvent;
import com.golem.skyblockutils.injection.mixins.minecraft.client.AccessorGuiContainer;
import com.golem.skyblockutils.models.AttributeValueResult;
import com.golem.skyblockutils.models.DisplayString;
import com.golem.skyblockutils.models.Overlay.TextOverlay.ContainerOverlay;
import com.golem.skyblockutils.models.gui.ButtonManager;
import com.golem.skyblockutils.utils.InventoryData;
import com.golem.skyblockutils.utils.RenderUtils;
import com.golem.skyblockutils.utils.rendering.Renderable;
import com.golem.skyblockutils.utils.rendering.RenderableString;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.inventory.Slot;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.awt.*;
import java.util.List;
import java.util.*;

import static com.golem.skyblockutils.Main.config;

public class ContainerValue {
	List<Renderable> renderStrings = new ArrayList<>();
	private int xSize;
	private int guiLeft;
	private int guiTop;
	private long totalValue;

	@SubscribeEvent
	public void onInventoryChange(InventoryChangeEvent event) {
		try {
			if (!(event.event.gui instanceof GuiChest)) return;
			if (config.getConfig().overlayCategory.containerValueConfig.containerValueOverlay == ContainerConfig.ContainerValuePosition.OFF) return;

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

			if (config.getConfig().overlayCategory.containerValueConfig.containerValueOverlay == ContainerConfig.ContainerValuePosition.NEXT_TO_GUI) {
				AccessorGuiContainer ac = (AccessorGuiContainer) gui;
				xSize = ac.getXSize();
				guiLeft = ac.getGuiLeft();
				guiTop = ac.getGuiTop();
			}

			for (Slot slot : InventoryData.containerSlots.subList(0, InventoryData.containerSlots.size() - 36)) {
				AttributeValueResult value = InventoryData.values.get(slot);
				if (value == null) continue;
				String displayString = value.display_string;
				totalValue += value.value;

				if (displayStrings.containsKey(displayString)) {
					displayStrings.get(displayString).quantity++;
				} else {
					displayStrings.put(displayString, new DisplayString(1, value.value, value.value, slot));
				}
			}

			displayStrings = sort(displayStrings);

			renderStrings.clear();

			for (Map.Entry<String, DisplayString> entry : displayStrings.entrySet()) {
				String displayString = entry.getKey();
				DisplayString display = entry.getValue();
				int amount = (int) display.quantity;
				long value = display.price;
				if (amount > 1) {
					displayString = amount + "x " + displayString;
				}

				String ds = displayString + EnumChatFormatting.YELLOW + ": " + EnumChatFormatting.GREEN + Main.formatNumber(value * amount);

				final Slot slot = display.slot;

				renderStrings.add(new RenderableString(ds, 0, 0)
						.onHover(() -> RenderUtils.highlight(Color.GREEN, gui, slot))
						.onClick(() -> clickSlot(slot, 0, 1))
				);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@SubscribeEvent
	public void guiDraw(GuiScreenEvent.BackgroundDrawnEvent event) {
		try {
			if (!(event.gui instanceof GuiChest)) return;
			if (config.getConfig().overlayCategory.containerValueConfig.containerValueOverlay == ContainerConfig.ContainerValuePosition.OFF) return;
			if (InventoryData.currentChestName.contains("Paid Chest") || InventoryData.currentChestName.contains("Free Chest")) return;

			if (!ButtonManager.isChecked("containerValue")) return;

			if (totalValue <= 0)  return;

			int counter = 1;

			Renderable totalValueRenderable = new RenderableString(
					EnumChatFormatting.YELLOW + "Total Value: " + EnumChatFormatting.GREEN + Main.formatNumber(totalValue),
					guiLeft + xSize + 5,
					guiTop + 5
			);

			GlStateManager.translate(0, 0, 5);

			totalValueRenderable.render(event);

			for (Renderable renderable : renderStrings) {
				renderable.setPosition(guiLeft + xSize + 5, guiTop + 5 + 10*counter);
				renderable.render(event);
				counter++;
			}


			GlStateManager.translate(0, 0, -5);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static LinkedHashMap<String, DisplayString> sort(LinkedHashMap<String, DisplayString> map) {
		// Convert the LinkedHashMap to a list of Map.Entry objects
		List<Map.Entry<String, DisplayString>> list = new ArrayList<>(map.entrySet());

		// Define a custom comparator to compare values in descending order
		Comparator<Map.Entry<String, DisplayString>> valueComparator;
		switch (config.getConfig().overlayCategory.containerValueConfig.sortBy) {
			case DESCENDING_PRICE:
				valueComparator = (entry1, entry2) -> {
					long product1 = (long) (entry1.getValue().quantity * entry1.getValue().price);
					long product2 = (long) (entry2.getValue().quantity * entry2.getValue().price);
					return Long.compare(product2, product1); // Sorting in descending order
				};
				list.sort(valueComparator);
				break;
			case ASCENDING_PRICE:
				valueComparator = (entry1, entry2) -> {
					long product1 = (long) (entry1.getValue().quantity * entry1.getValue().price);
					long product2 = (long) (entry2.getValue().quantity * entry2.getValue().price);
					return Long.compare(product1, product2); // Sorting in ascending order
				};
				list.sort(valueComparator);
				break;
			case ALPHABETICAL:
				valueComparator = Map.Entry.comparingByKey();
				list.sort(valueComparator);
				break;
			case ATTRIBUTE_TIER:
				valueComparator = (entry1, entry2) -> {
					int tier1 = entry1.getKey().split(" ")[1].matches("\\d+") ? Integer.parseInt(entry1.getKey().split(" ")[1]) : 0;
					int tier2 = entry2.getKey().split(" ")[1].matches("\\d+") ? Integer.parseInt(entry2.getKey().split(" ")[1]) : 0;
					return Integer.compare(tier2, tier1);
				};
				list.sort(valueComparator);
				break;
			case ITEM_TYPE:
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

	private void clickSlot(Slot slot, int type, int mode) {
		Main.mc.playerController.windowClick(Main.mc.thePlayer.openContainer.windowId, slot.slotNumber, type, mode, Main.mc.thePlayer);
	}

}


