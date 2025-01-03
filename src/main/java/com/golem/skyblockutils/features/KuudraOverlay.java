package com.golem.skyblockutils.features;

import com.golem.skyblockutils.Main;
import com.golem.skyblockutils.events.InventoryChangeEvent;
import com.golem.skyblockutils.features.KuudraFight.Kuudra;
import com.golem.skyblockutils.injection.mixins.minecraft.client.AccessorGuiContainer;
import com.golem.skyblockutils.models.AttributePrice;
import com.golem.skyblockutils.models.AttributeValueResult;
import com.golem.skyblockutils.models.Overlay.TextOverlay.ContainerOverlay;
import com.golem.skyblockutils.utils.InventoryData;
import com.golem.skyblockutils.utils.RenderUtils;
import com.golem.skyblockutils.utils.ToolTipListener;
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

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.golem.skyblockutils.Main.bazaar;
import static com.golem.skyblockutils.Main.configFile;
import static com.golem.skyblockutils.models.AttributePrice.LowestBin;

public class KuudraOverlay {

	private final Pattern ESSENCE_PATTERN = Pattern.compile("§d(.+) Essence §8x([\\d,]+)");
	public static int keyCost = 0;
	private int xSize = 0;
	private int guiLeft = 0;
	private int guiTop = 0;
	public static boolean usedKismet = false;

	private List<String> displayStrings = new ArrayList<>();
	private long totalValue;
	public static long totalProfit;
	public static ArrayList<Integer> expectedProfit = new ArrayList<>();


	@SubscribeEvent
	public void onInventoryChange(InventoryChangeEvent event) {
		try {
			if (!(event.event.gui instanceof GuiChest)) return;
			if (!configFile.kuudra_overlay) return;
			if (configFile.customProfitOverlay == 0) return;

			GuiChest gui = (GuiChest) event.event.gui;
			Container container = gui.inventorySlots;
			if (!(container instanceof ContainerChest)) return;
			String chestName = ((ContainerChest) container).getLowerChestInventory().getDisplayName().getUnformattedText();
			if (!chestName.contains("Paid Chest") && !chestName.contains("Free Chest")) return;
			List<Slot> fullChestInventory = InventoryData.containerSlots;
			displayStrings = new ArrayList<>();
			totalValue = 0;
			totalProfit = 0;

			if (configFile.customProfitOverlay == 1) {
				xSize = ((AccessorGuiContainer) gui).getXSize();
				guiLeft = ((AccessorGuiContainer) gui).getGuiLeft();
				guiTop = ((AccessorGuiContainer) gui).getGuiTop();
			} else if (configFile.customProfitOverlay == 2) {
				xSize = (int) (ContainerOverlay.element.position.getX());
				guiLeft = 0;
				guiTop = (int) (ContainerOverlay.element.position.getY());
			}

			List<Slot> chestInventory = fullChestInventory.subList(0, 33);

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
						} catch (Exception ignored) {
						}
						displayStrings.add(EnumChatFormatting.YELLOW + String.valueOf(amount) + "x " + EnumChatFormatting.LIGHT_PURPLE + matcher.group(1) + " Essence" + EnumChatFormatting.YELLOW + ": " + EnumChatFormatting.GREEN + Main.formatNumber(amount * (sell_price + buy_price) / 2F));
						totalValue += (long) amount * (buy_price + sell_price) / 2;
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
							} catch (Exception ignored) {
							}

							displayStrings.add(ToolTipListener.TitleCase(enchant + " " + enchants.getInteger(enchant)) + EnumChatFormatting.YELLOW + ": " + EnumChatFormatting.GREEN + Main.formatNumber(price));
							totalValue += price;

						}

					}

					String item_id = slot.getStack().serializeNBT().getCompoundTag("tag").getCompoundTag("ExtraAttributes").getString("id");
					if (Objects.equals(item_id, "ATTRIBUTE_SHARD") && !configFile.valueShards) continue;
					switch (item_id) {
						case "BURNING_KUUDRA_CORE":
						case "WHEEL_OF_FATE":
						case "RUNIC_STAFF":
						case "HOLLOW_WAND":
						case "TENTACLE_DYE":
						case "ENRAGER": {
							int price = LowestBin.getOrDefault(item_id, 0);
							displayStrings.add(slot.getStack().getDisplayName() + ": " + EnumChatFormatting.GREEN + Main.formatNumber(price));
							totalValue += price;
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
							totalValue += price;
							break;
						}
						default: {
							AttributeValueResult valueData = AttributePrice.AttributeValue(slot.getStack());
							if (valueData == null) break;
							String displayString = valueData.display_string + EnumChatFormatting.YELLOW + ": " + EnumChatFormatting.GREEN + Main.formatNumber(valueData.value);
							totalValue += valueData.value;
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

			try {
				int myceliumCost = bazaar.get("products").getAsJsonObject().get("ENCHANTED_MYCELIUM").getAsJsonObject().get("buy_summary").getAsJsonArray().get(0).getAsJsonObject().get("pricePerUnit").getAsInt();
				int redSandCost = bazaar.get("products").getAsJsonObject().get("ENCHANTED_RED_SAND").getAsJsonObject().get("buy_summary").getAsJsonArray().get(0).getAsJsonObject().get("pricePerUnit").getAsInt();

				switch (configFile.faction) {
					case 0: // Mage
						itemCost = myceliumCost;
						break;
					case 1: // Barbarian
						itemCost = redSandCost;
						break;
					case 2: // Cheapest
						itemCost = Math.min(myceliumCost, redSandCost);
						break;
				}
			} catch (Exception ignored) {
			}

			if (chestName.contains("Paid")) {
				try {
					String keySlotLore = chestInventory.get(31).getStack().getTagCompound().getCompoundTag("display").getTagList("Lore", 8).toString();
					keyCost += bazaar.get("products").getAsJsonObject().get("CORRUPTED_NETHER_STAR").getAsJsonObject().get("buy_summary").getAsJsonArray().get(0).getAsJsonObject().get("pricePerUnit").getAsInt() * 2;
					if (keySlotLore.contains("Infernal")) {
						keyCost += 2400000;
						keyCost += itemCost * 80;
					} else if (keySlotLore.contains("Fiery")) {
						keyCost += 1200000;
						keyCost += itemCost * 40;
					} else if (keySlotLore.contains("Burning")) {
						keyCost += 600000;
						keyCost += itemCost * 16;
					} else if (keySlotLore.contains("Hot")) {
						keyCost += 320000;
						keyCost += itemCost * 4;
					} else {
						keyCost += 160000;
						keyCost += itemCost * 2;
					}
					usedKismet = false;
					Slot kismetSlot = (fullChestInventory.get(50));
					if (kismetSlot.getHasStack()) {
						String lore = kismetSlot.getStack().getTagCompound().getCompoundTag("display").getTagList("Lore", 8).toString();
						if (lore.contains("You already rerolled this chest!")) {
							usedKismet = true;
						}
					}
					totalProfit = totalValue - keyCost;

				} catch (Exception ignored) {
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

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

			if (expectedProfit.size() == 5) {
				if (totalValue < expectedProfit.get(Kuudra.tier - 1) - bazaar.get("products").getAsJsonObject().get("KISMET_FEATHER").getAsJsonObject().get("buy_summary").getAsJsonArray().get(0).getAsJsonObject().get("pricePerUnit").getAsInt()) {
					RenderUtils.highlight(Color.GREEN, gui, InventoryData.containerSlots.get(50));
				} else {
					RenderUtils.highlight(Color.GREEN, gui, InventoryData.containerSlots.get(31));
				}
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

			Minecraft.getMinecraft().fontRendererObj.drawStringWithShadow(
					(totalProfit > 0 ? EnumChatFormatting.DARK_GREEN + "Profit: " + EnumChatFormatting.GREEN : EnumChatFormatting.DARK_RED + "Loss: " + EnumChatFormatting.RED) + Main.formatNumber(totalProfit),
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
