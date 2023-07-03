package com.golem.skyblockutils.features;

import com.golem.skyblockutils.Main;
import com.golem.skyblockutils.injection.mixins.minecraft.client.AccessorGuiContainer;
import com.golem.skyblockutils.models.AttributePrice;
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

	@SubscribeEvent
	public void guiDraw(GuiScreenEvent.BackgroundDrawnEvent event) {
		try {
			if (!(event.gui instanceof GuiChest)) return;
			if (!configFile.kuudra_overlay) return;

			GuiChest gui = (GuiChest) event.gui;
			Container container = gui.inventorySlots;
			if (!(container instanceof ContainerChest)) return;
			String chestName = ((ContainerChest) container).getLowerChestInventory().getDisplayName().getUnformattedText();
			if (!chestName.contains("Paid Chest") && !chestName.contains("Free Chest")) return;
			List<Slot> chestInventory = ((GuiChest) Minecraft.getMinecraft().currentScreen).inventorySlots.inventorySlots;
			List<String> displayStrings = new ArrayList<>();
			BigInteger totalValue = new BigInteger("0");
			new BigInteger("0");
			BigInteger totalProfit;


			List<String> excludeAttributes = Arrays.asList(Main.configFile.attributesToExclude.split(", "));
			List<String> priorityAttributes = Arrays.asList(Main.configFile.priorityAttributes.split(", "));

			int xSize = ((AccessorGuiContainer) gui).getXSize();
			int guiLeft = ((AccessorGuiContainer) gui).getGuiLeft();
			int guiTop = ((AccessorGuiContainer) gui).getGuiTop();

			chestInventory = chestInventory.subList(0, 33);
			for (Slot slot : chestInventory) {
				try {
					if (!slot.getHasStack() || slot.getStack().getItem() == Item.getItemFromBlock(Blocks.stained_glass_pane))
						continue;
					Matcher matcher = ESSENCE_PATTERN.matcher(slot.getStack().getDisplayName());
					if (matcher.matches() && configFile.considerEssenceValue) {
						int buy_price = bazaar.get("products").getAsJsonObject().get("ESSENCE_CRIMSON").getAsJsonObject().get("sell_summary").getAsJsonArray().get(0).getAsJsonObject().get("pricePerUnit").getAsInt();
						int sell_price = bazaar.get("products").getAsJsonObject().get("ESSENCE_CRIMSON").getAsJsonObject().get("buy_summary").getAsJsonArray().get(0).getAsJsonObject().get("pricePerUnit").getAsInt();
						int amount = KuudraPetEssenceBonus(Integer.parseInt(matcher.group(2)));
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

					NBTTagCompound nbt = slot.getStack().serializeNBT().getCompoundTag("tag").getCompoundTag("ExtraAttributes").getCompoundTag("attributes");
					String item_id = slot.getStack().serializeNBT().getCompoundTag("tag").getCompoundTag("ExtraAttributes").getString("id");
					switch (item_id) {
						case "BURNING_KUUDRA_CORE":
						case "WHEEL_OF_FATE":
						case "RUNIC_STAFF":
						case "HOLLOW_WAND": {
							int price = LowestBin.get(item_id);
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
						default:
							for (String key : all_kuudra_categories) {
								if (!item_id.contains(key)) continue;
								if ((key.equals("HELMET") || key.equals("CHESTPLATE") || key.equals("LEGGINGS") || key.equals("BOOTS")) &&
										!item_id.contains("AURORA") && !item_id.contains("CRIMSON") && !item_id.contains("TERROR") && !item_id.contains("FERVOR") && !item_id.contains("HOLLOW")
								) continue;

								String best_attribute = "";
								int best_tier = 0;
								int best_value = 0;
								int added_value = 0;

								for (String key2 : nbt.getKeySet()) {
									if (excludeAttributes.contains(key2)) continue;
									ArrayList<JsonObject> items = AttributePrices.get(key).get(key2);
									if (items == null || items.size() == 0) {
										continue;
									}
									items = items.stream().filter(i -> i.get(key2).getAsInt() >= configFile.min_tier).collect(Collectors.toCollection(ArrayList::new));
									if (items.size() == 0) {
										continue;
									}
									items.sort(Comparator.comparingDouble((JsonObject o) -> o.get("price_per_tier").getAsDouble()));
									int value = items.get(0).get("price_per_tier").getAsInt();
									added_value += (value * Math.pow(2, nbt.getInteger(key2) - 1));
									if (priorityAttributes.contains(best_attribute) && !priorityAttributes.contains(key2) && !Objects.equals(best_attribute, ""))
										continue;
									if (!priorityAttributes.contains(best_attribute) && priorityAttributes.contains(key2))
										best_value = 0;
									if (value * Math.pow(2, nbt.getInteger(key2) - 1) > best_value) {
										best_value = (int) (value * Math.pow(2, nbt.getInteger(key2) - 1));
										best_attribute = key2;
										best_tier = nbt.getInteger(key2);
									}
								}
								JsonObject comboitem;
								comboitem = AttributePrice.getComboValue(item_id, new ArrayList<>(nbt.getKeySet()));
								if (best_tier == 0) continue;
								if (best_tier > 5 && comboitem != null && comboitem.get("starting_bid").getAsInt() > Main.configFile.min_godroll_price * 1000000) {
									displayStrings.add(AttributePrice.ShortenedAttribute(new ArrayList<>(nbt.getKeySet()).get(0)) + " " + nbt.getInteger(new ArrayList<>(nbt.getKeySet()).get(0)) + " " + AttributePrice.ShortenedAttribute(new ArrayList<>(nbt.getKeySet()).get(1)) + " " + nbt.getInteger(new ArrayList<>(nbt.getKeySet()).get(1)) + " " + slot.getStack().getDisplayName() + EnumChatFormatting.YELLOW + ": " + EnumChatFormatting.GREEN + Main.formatNumber(comboitem.get("starting_bid").getAsDouble() + added_value));
									totalValue = totalValue.add(new BigInteger(comboitem.get("starting_bid").getAsString())).add(new BigInteger(String.valueOf(added_value)));
								} else if (comboitem != null && comboitem.get("starting_bid").getAsInt() > Math.max(best_value, Main.configFile.min_godroll_price * 1000000)) {
									displayStrings.add(AttributePrice.ShortenedAttribute(new ArrayList<>(nbt.getKeySet()).get(0)) + " " + AttributePrice.ShortenedAttribute(new ArrayList<>(nbt.getKeySet()).get(1)) + " " + slot.getStack().getDisplayName() + EnumChatFormatting.YELLOW + ": " + EnumChatFormatting.GREEN + Main.formatNumber(comboitem.get("starting_bid").getAsDouble()));
									totalValue = totalValue.add(new BigInteger(comboitem.get("starting_bid").getAsString()));
								} else if (best_value > LowestBin.get(item_id)) {
									displayStrings.add(AttributePrice.ShortenedAttribute(best_attribute) + " " + best_tier + " " + slot.getStack().getDisplayName() + EnumChatFormatting.YELLOW + ": " + EnumChatFormatting.GREEN + Main.formatNumber(best_value));
									totalValue = totalValue.add(new BigInteger(String.valueOf(best_value)));
								} else {
									displayStrings.add("LBIN " + slot.getStack().getDisplayName() + EnumChatFormatting.YELLOW + ": " + EnumChatFormatting.GREEN + Main.formatNumber(LowestBin.get(item_id)));
									totalValue = totalValue.add(new BigInteger(String.valueOf(LowestBin.get(item_id))));
								}
							}
							break;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			int keyCost = 0;
			int itemCost = 0;
			String item = (configFile.faction == 0 ? "ENCHANTED_MYCELIUM" : "ENCHANTED_RED_SAND");
			try {
				itemCost = bazaar.get("products").getAsJsonObject().get(item).getAsJsonObject().get("buy_summary").getAsJsonArray().get(0).getAsJsonObject().get("pricePerUnit").getAsInt();
			} catch (Exception ignored) {}
			if (chestName.contains("Paid")) {
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
			}


			GlStateManager.disableLighting();
			GlStateManager.translate(0, 0, 200);
			for (int i = 0; i < displayStrings.size(); i++) {
				Minecraft.getMinecraft().fontRendererObj.drawStringWithShadow(
						displayStrings.get(i),
						guiLeft + xSize + 5,
						guiTop + 5 + 10*i,
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

			Minecraft.getMinecraft().fontRendererObj.drawStringWithShadow(
					(totalProfit.signum() > 0 ? EnumChatFormatting.DARK_GREEN + "Profit: " + EnumChatFormatting.GREEN : EnumChatFormatting.DARK_RED + "Loss: " + EnumChatFormatting.RED) + Main.formatNumber(totalProfit),
					guiLeft + xSize + 5,
					guiTop + 25 + 10 * displayStrings.size(),
					0xffffffff
			);


			GlStateManager.translate(0, 0, -200);

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
