package com.golem.skyblockutils.features;

import com.golem.skyblockutils.Main;
import com.golem.skyblockutils.injection.mixins.minecraft.client.AccessorGuiContainer;
import com.golem.skyblockutils.models.AttributePrice;
import com.golem.skyblockutils.models.Overlay.TextOverlay.ContainerOverlay;
import com.golem.skyblockutils.utils.AuctionHouse;
import com.golem.skyblockutils.utils.ToolTipListener;
import com.google.gson.JsonObject;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.golem.skyblockutils.Main.bazaar;
import static com.golem.skyblockutils.Main.configFile;
import static com.golem.skyblockutils.models.AttributePrice.*;

public class KuudraOverlay {

	private final Pattern ESSENCE_PATTERN = Pattern.compile("§d(.+) Essence §8x([\\d,]+)");
	public static int profit = 0;
	public static int keyCost = 0;
	public int xSize = 0;
	public int guiLeft = 0;
	public int guiTop = 0;

	@SubscribeEvent
	public void guiDraw(GuiScreenEvent.BackgroundDrawnEvent event) {
		try {
			if (!(event.gui instanceof GuiChest)) return;
			if (!configFile.kuudra_overlay) return;
			if (configFile.customProfitOverlay == 0) return;

			GuiChest gui = (GuiChest) event.gui;
			Container container = gui.inventorySlots;
			if (!(container instanceof ContainerChest)) return;
			String chestName = ((ContainerChest) container).getLowerChestInventory().getDisplayName().getUnformattedText();
			if (!chestName.contains("Paid Chest") && !chestName.contains("Free Chest")) return;
			List<Slot> chestInventory = ((GuiChest) Minecraft.getMinecraft().currentScreen).inventorySlots.inventorySlots;
			List<String> displayStrings = new ArrayList<>();
			BigInteger totalValue = new BigInteger("0");
			BigInteger totalProfit;



			if (configFile.customProfitOverlay == 1) {
				xSize = ((AccessorGuiContainer) gui).getXSize();
				guiLeft = ((AccessorGuiContainer) gui).getGuiLeft();
				guiTop = ((AccessorGuiContainer) gui).getGuiTop();
			}
			else if (configFile.customProfitOverlay == 2) {
				xSize = (int) (ContainerOverlay.element.position.getX());
				guiLeft = 0;
				guiTop = (int) (ContainerOverlay.element.position.getY());
			}




			chestInventory = chestInventory.subList(0, 33);
			for (Slot slot : chestInventory) {
				try {
					if (!slot.getHasStack() || slot.getStack().getItem() == Item.getItemFromBlock(Blocks.stained_glass_pane))
						continue;
					Matcher matcher = ESSENCE_PATTERN.matcher(slot.getStack().getDisplayName());
					if (matcher.matches() && configFile.considerEssenceValue) {
						int buy_price = 1000;
						int sell_price = 1000;
						int amount = 1;
						try {
							buy_price = bazaar.get("products").getAsJsonObject().get("ESSENCE_CRIMSON").getAsJsonObject().get("sell_summary").getAsJsonArray().get(0).getAsJsonObject().get("pricePerUnit").getAsInt();
							sell_price = bazaar.get("products").getAsJsonObject().get("ESSENCE_CRIMSON").getAsJsonObject().get("buy_summary").getAsJsonArray().get(0).getAsJsonObject().get("pricePerUnit").getAsInt();
							amount = KuudraPetEssenceBonus(Integer.parseInt(matcher.group(2)));
						} catch (Exception ignored) {}
						displayStrings.add(EnumChatFormatting.YELLOW + String.valueOf(amount) + "x " + EnumChatFormatting.LIGHT_PURPLE + matcher.group(1) + " Essence" + EnumChatFormatting.YELLOW + ": " + EnumChatFormatting.GREEN + Main.formatNumber(amount * (sell_price + buy_price) / 2F));
						totalValue = totalValue.add(new BigInteger(String.valueOf(amount * (buy_price + sell_price) / 2)));
					}
					if (Objects.equals(slot.getStack().getItem().getRegistryName(), Items.enchanted_book.getRegistryName())) {
						NBTTagCompound enchants = slot.getStack().serializeNBT().getCompoundTag("tag").getCompoundTag("ExtraAttributes").getCompoundTag("enchantments");
						for (String enchant : enchants.getKeySet()) {
							String sell_type = (configFile.book_sell_method == 0 ? "sell_summary" : "buy_summary");
							if (Objects.equals(enchant, "hardened_mana")) sell_type = "sell_summary";
							int price = 0;
							String enchantString = "ENCHANTMENT_" + enchant.toUpperCase() + "_" + enchants.getInteger(enchant);
							try {
								price = bazaar.get("products").getAsJsonObject().get(enchantString).getAsJsonObject().get(sell_type).getAsJsonArray().get(0).getAsJsonObject().get("pricePerUnit").getAsInt();
							} catch (Exception ignored) {}

							displayStrings.add(ToolTipListener.TitleCase(enchant + " " + enchants.getInteger(enchant)) + EnumChatFormatting.YELLOW + ": " + EnumChatFormatting.GREEN + Main.formatNumber(price));
							totalValue = totalValue.add(new BigInteger(String.valueOf(price)));

						}

					}

					String item_id = slot.getStack().serializeNBT().getCompoundTag("tag").getCompoundTag("ExtraAttributes").getString("id");
					switch (item_id) {
						case "BURNING_KUUDRA_CORE":
						case "WHEEL_OF_FATE":
						case "RUNIC_STAFF":
						case "HOLLOW_WAND": {
							int price = LowestBin.getOrDefault(item_id, 0);
							displayStrings.add(slot.getStack().getDisplayName() + ": " + EnumChatFormatting.GREEN + Main.formatNumber(price));
							totalValue = totalValue.add(new BigInteger(String.valueOf(price)));
							break;
						}
						case "MANDRAA":
						case "KUUDRA_MANDIBLE": {
							String sell_type = (configFile.book_sell_method == 0 ? "sell_summary" : "buy_summary");
							int price = 0;
							try {
								price = bazaar.get("products").getAsJsonObject().get(item_id).getAsJsonObject().get(sell_type).getAsJsonArray().get(0).getAsJsonObject().get("pricePerUnit").getAsInt();
							} catch (Exception ignored) {
							}
							displayStrings.add(slot.getStack().getDisplayName() + ": " + EnumChatFormatting.GREEN + Main.formatNumber(price));
							totalValue = totalValue.add(new BigInteger(String.valueOf(price)));
							break;
						}
						default: {
							JsonObject valueData = AttributePrice.AttributeValue(slot.getStack());
							if (valueData == null) break;
							String displayString = valueData.get("display_string").getAsString() + EnumChatFormatting.YELLOW + ": " + EnumChatFormatting.GREEN + Main.formatNumber(valueData.get("value").getAsBigInteger());
							totalValue = totalValue.add(valueData.get("value").getAsBigInteger());
							displayStrings.add(displayString);
							break;
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			keyCost = 0;
			int itemCost = 0;
			String item = (configFile.faction == 0 ? "ENCHANTED_MYCELIUM" : "ENCHANTED_RED_SAND");
			try {
				itemCost = bazaar.get("products").getAsJsonObject().get(item).getAsJsonObject().get("buy_summary").getAsJsonArray().get(0).getAsJsonObject().get("pricePerUnit").getAsInt();
			} catch (Exception ignored) {}
			if (chestName.contains("Paid")) {
				try {
					String keySlotLore = chestInventory.get(31).getStack().getTagCompound().getCompoundTag("display").getTagList("Lore", 8).toString();
					if (keySlotLore.contains("Infernal")) {
						keyCost += 3000000;
						keyCost += itemCost * 120;
					} else if (keySlotLore.contains("Fiery")) {
						keyCost += 1500000;
						keyCost += itemCost * 60;
					} else if (keySlotLore.contains("Burning")) {
						keyCost += 750000;
						keyCost += itemCost * 20;
					} else if (keySlotLore.contains("Hot")) {
						keyCost += 400000;
						keyCost += itemCost * 60;
					} else {
						keyCost += 200000;
						keyCost += itemCost * 2;
					}
				} catch (Exception ignored) {}
			}


			GlStateManager.disableLighting();

			GlStateManager.translate(0, 0, 200);

			for (int i = 0; i < displayStrings.size(); i++) {
				Minecraft.getMinecraft().fontRendererObj.drawStringWithShadow(
						displayStrings.get(i),
						guiLeft + xSize + 5,
						guiTop + 5 + 10 * i,
						0xffffffff
				);
			}

			Minecraft.getMinecraft().fontRendererObj.drawStringWithShadow(
					EnumChatFormatting.YELLOW + "Total Value: " + EnumChatFormatting.GREEN + Main.formatNumber(totalValue),
					guiLeft + xSize + 5,
					guiTop + 5 + 10 * displayStrings.size(),
					0xffffffff
			);

			Minecraft.getMinecraft().fontRendererObj.drawStringWithShadow(
					EnumChatFormatting.YELLOW + "Key Cost: " + EnumChatFormatting.RED + Main.formatNumber(keyCost),
					guiLeft + xSize + 5,
					guiTop + 15 + 10 * displayStrings.size(),
					0xffffffff
			);

			totalProfit = totalValue.subtract(new BigInteger(String.valueOf(keyCost)));

			profit = totalProfit.intValue();

			Minecraft.getMinecraft().fontRendererObj.drawStringWithShadow(
					(totalProfit.signum() > 0 ? EnumChatFormatting.DARK_GREEN + "Profit: " + EnumChatFormatting.GREEN : EnumChatFormatting.DARK_RED + "Loss: " + EnumChatFormatting.RED) + Main.formatNumber(totalProfit),
					guiLeft + xSize + 5,
					guiTop + 25 + 10 * displayStrings.size(),
					0xffffffff
			);

			GlStateManager.translate(0,0,-200);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	public static int KuudraPetEssenceBonus (int amount) {
		switch (configFile.kuudraPetRarity) {
			case 0:
				return (int) (amount * (1+configFile.kuudraPetLevel * 0.001));
			case 1:
			case 2:
				return (int) (amount * (1+configFile.kuudraPetLevel * 0.0015));
			case 3:
			case 4:
				return (int) (amount * (1+configFile.kuudraPetLevel * 0.002));
			default:
				return amount;
		}
	}

}
