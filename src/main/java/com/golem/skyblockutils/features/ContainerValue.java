package com.golem.skyblockutils.features;

import com.golem.skyblockutils.Main;
import com.golem.skyblockutils.features.KuudraFight.Kuudra;
import com.golem.skyblockutils.init.KeybindsInit;
import com.golem.skyblockutils.injection.mixins.minecraft.client.AccessorGuiContainer;
import com.golem.skyblockutils.models.AttributePrice;
import com.golem.skyblockutils.models.DisplayString;
import com.golem.skyblockutils.models.Overlay.TextOverlay.ContainerOverlay;
import com.golem.skyblockutils.utils.RenderUtils;
import com.google.gson.JsonObject;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.client.gui.inventory.GuiContainer;
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
import org.lwjgl.input.Mouse;

import java.awt.*;
import java.math.BigInteger;
import java.util.List;
import java.util.*;

import static com.golem.skyblockutils.Main.*;

public class ContainerValue {
	public static boolean isActive = false;

	@SubscribeEvent
	public void guiDraw(GuiScreenEvent.BackgroundDrawnEvent event) {
		try {
			if (!(event.gui instanceof GuiContainer)) return;
			if (!isActive) return;
			if (configFile.container_value == 0) return;

			GuiChest gui = (GuiChest) event.gui;
			Container container = gui.inventorySlots;
			if (!(container instanceof ContainerChest)) return;
			String chestName = ((ContainerChest) container).getLowerChestInventory().getDisplayName().getUnformattedText();
				if (chestName.contains("Paid Chest") || chestName.contains("Free Chest"))  return;
			List<Slot> chestInventory = ((GuiChest) Minecraft.getMinecraft().currentScreen).inventorySlots.inventorySlots;
			LinkedHashMap<String, DisplayString> displayStrings = new LinkedHashMap<>();
			BigInteger totalValue = new BigInteger("0");


			int xSize = (int) (ContainerOverlay.element.position.getX() - 5);
			int guiLeft = 0;
			int guiTop = (int) (ContainerOverlay.element.position.getY() - 5);

			if (configFile.container_value == 1) {
				xSize = ((AccessorGuiContainer) gui).getXSize();
				guiLeft = ((AccessorGuiContainer) gui).getGuiLeft();
				guiTop = ((AccessorGuiContainer) gui).getGuiTop();
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
							totalValue = totalValue.add(valueData.get("value").getAsBigInteger());
							displayStrings.put(displayString, new DisplayString(displayStrings.getOrDefault(displayString, new DisplayString(0, 0)).quantity + 1, valueData.get("value").getAsLong(), 0, slot));
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

			if (configFile.dataSource == 0 && totalValue.compareTo(BigInteger.ONE) < 0)  return;
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

			int mouseX = Mouse.getX();
			int mouseY = Mouse.getY();
			boolean mouseClicked = Mouse.isButtonDown(0);



			for (String displayString : displayStrings.keySet()) {
				DisplayString display = displayStrings.get(displayString);
				int amount = display.quantity;
				long value = display.price;
				long median = display.median;
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

				Point mousePos = new Point(
						mouseX * event.gui.width / event.gui.mc.displayWidth,
						event.gui.height - mouseY * event.gui.height / event.gui.mc.displayHeight
				);

				StringBuilder sb = new StringBuilder();
				System.out.println("MouseX: " + mouseX + ", MouseY: " + mouseY + ", newMouseX: " + mousePos.x + ", newMouseY: " + mousePos.y + " , " + (guiLeft+xSize+5) + " , " + guiTop + 5 + 10*counter);
				sb.append(mouseX).append(" ").append(mouseY).append(" ").append(mouseClicked).append(" ").append(guiLeft).append(" ").append(xSize).append(" ").append(guiTop);
				Kuudra.addChatMessage(sb.toString());

				if (mouseX >= guiLeft + xSize + 5 &&
						mouseX <= guiLeft + xSize + 5 + mc.fontRendererObj.getStringWidth(displayString) &&
						mouseY >= guiTop + 5 + 10 * counter &&
						mouseY <= guiTop + 5 + 10 * counter + 10
				) {
					RenderUtils.highlight(Color.GREEN, (GuiContainer) event.gui, display.slot);
					if (mouseClicked) {
						Robot robot = new Robot();
						robot.mouseMove(display.slot.xDisplayPosition, display.slot.yDisplayPosition);
					}
				}

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

		// Sort the list using the custom comparator


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


