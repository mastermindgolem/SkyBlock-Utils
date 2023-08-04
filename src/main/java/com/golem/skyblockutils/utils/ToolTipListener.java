package com.golem.skyblockutils.utils;

import com.golem.skyblockutils.init.KeybindsInit;
import com.golem.skyblockutils.models.AttributePrice;
import com.google.gson.JsonObject;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.*;
import java.util.stream.Collectors;

import static com.golem.skyblockutils.Main.configFile;
import static com.golem.skyblockutils.models.AttributePrice.AttributePrices;
import static com.golem.skyblockutils.models.AttributePrice.all_attributes;

public class ToolTipListener {
	public static JsonObject attribute_prices = new JsonObject();
	private int comboprice = -1;
	private String previousItemSearched = "";

	private String[] previousAttributesSearched = new String[0];

	public String getItemId(NBTTagCompound extraAttributes) {
		String itemId = extraAttributes.getString("id");
		itemId = itemId.split(":")[0];
		return itemId;
	}

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void onItemToolTipEvent(ItemTooltipEvent event) {
		ItemStack item = event.itemStack;
		NBTTagCompound itemNbt;
		try {
			itemNbt = item.serializeNBT().getCompoundTag("tag").getCompoundTag("ExtraAttributes");
		} catch (NullPointerException e) {
			// Possible bugs where items don't have nbt, ignore the item.
			return;
		}
		String name = getItemId(itemNbt);

		NBTTagCompound shards;
		try {
			shards = itemNbt.getCompoundTag("attributes");
		} catch (NullPointerException e) {
			return;
		}
		if (GameSettings.isKeyDown(KeybindsInit.getComboValue) && event.toolTip.size() > 0 && shards.getKeySet().size() > 0) {
			String[] s = shards.getKeySet().toArray(new String[0]);
			if (comboprice == -1 || !name.equals(previousItemSearched) || !Arrays.equals(s, previousAttributesSearched) && shards.getKeySet().size() > 1) {
				JsonObject comboitem = AttributePrice.getComboValue(name, new ArrayList<>(shards.getKeySet()));
				if (comboitem == null) {
					comboprice = 0;
				} else {
					comboprice = comboitem.get("starting_bid").getAsInt();
				}
				previousItemSearched = name;
				previousAttributesSearched = s;
			}
			if (shards.getKeySet().size() > 1) {
				String ToolTipString = EnumChatFormatting.GOLD + "Combo Value: " + EnumChatFormatting.GREEN + String.format("%,d", comboprice);
				event.toolTip.add(event.toolTip.size(), ToolTipString);
			}
			int attributeprice;
			String key = "none";
			for (String k : AttributePrices.keySet()) if (name.contains(k)) key = k;
			if (Objects.equals(key, "none")) return;
			try {
				for (String attribute : all_attributes) {
					if (!shards.getKeySet().contains(attribute)) continue;
					if (!AttributePrices.get(key).containsKey(attribute)) continue;
					ArrayList<JsonObject> items = AttributePrices.get(key).get(attribute);
					System.out.println(items.get(0));
					if ((key.equals("SHARD") ? configFile.minShardTier : configFile.minArmorTier) > 0) {
						String finalKey = key;
						items = items.stream().filter(i -> i.get(attribute).getAsInt() >= (finalKey.equals("SHARD") ? configFile.minShardTier : configFile.minArmorTier)).collect(Collectors.toCollection(ArrayList::new));
					} else {
						items = items.stream().filter(i -> i.get(attribute).getAsInt() == shards.getInteger(attribute)).collect(Collectors.toCollection(ArrayList::new));
					}
					items.sort(Comparator.comparingDouble((JsonObject o) -> o.get("price_per_tier").getAsDouble()));
					if (items.size() == 0) {
						attributeprice = 0;
					} else {
						attributeprice = (int) (items.get(0).get("price_per_tier").getAsDouble() * Math.pow(2,shards.getInteger(attribute)-1));
					}
					String ToolTipString1 = EnumChatFormatting.GOLD + TitleCase(attribute) + " " + shards.getInteger(attribute) + ": " + EnumChatFormatting.GREEN + String.format("%,d", attributeprice);
					event.toolTip.add(event.toolTip.size(), ToolTipString1);

				}
			} catch (NullPointerException e) {
				e.printStackTrace();
			}
		}



	}
	public static String TitleCase(String text) {
		text = text.replace("_"," ");
		if (text.isEmpty()) {
			return text;
		}

		StringBuilder converted = new StringBuilder();

		boolean convertNext = true;
		for (char ch : text.toCharArray()) {
			if (Character.isSpaceChar(ch)) {
				convertNext = true;
			} else if (convertNext) {
				ch = Character.toTitleCase(ch);
				convertNext = false;
			} else {
				ch = Character.toLowerCase(ch);
			}
			converted.append(ch);
		}

		return converted.toString();
	}
}