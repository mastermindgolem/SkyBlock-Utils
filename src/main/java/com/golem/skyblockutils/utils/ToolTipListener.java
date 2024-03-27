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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

import static com.golem.skyblockutils.Main.configFile;
import static com.golem.skyblockutils.models.AttributePrice.LowestAttributePrices;
import static com.golem.skyblockutils.models.AttributePrice.all_attributes;

public class ToolTipListener {
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

		if (!itemNbt.hasKey("attributes")) return;

		String name = getItemId(itemNbt);

		NBTTagCompound shards;
		try {
			shards = itemNbt.getCompoundTag("attributes");
		} catch (NullPointerException e) {
			return;
		}
		if (GameSettings.isKeyDown(KeybindsInit.getComboValue) && !event.toolTip.isEmpty() && !shards.getKeySet().isEmpty()) {
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
			String newToolTip = "";
			if (shards.getKeySet().size() > 1) {
				String ToolTipString = EnumChatFormatting.GOLD + "Combo Value: " + EnumChatFormatting.GREEN + String.format("%,d", comboprice);
				newToolTip += ToolTipString;
			}
			int attributeprice;
			String key = "none";
			for (String k : LowestAttributePrices.keySet()) if (name.contains(k)) key = k;
			if (Objects.equals(key, "none")) {
				event.toolTip.add(newToolTip);
				return;
			}
			try {
				for (String attribute : all_attributes) {
					if (!shards.getKeySet().contains(attribute)) continue;
					if (!LowestAttributePrices.get(key).containsKey(attribute)) continue;
					int min_tier = (key.equals("SHARD") ? configFile.minShardTier : configFile.minArmorTier);
					attributeprice = LowestAttributePrices.get(key).get(attribute).get(min_tier) << (shards.getInteger(attribute) - 1);
					String ToolTipString1 = EnumChatFormatting.GOLD + TitleCase(attribute) + " " + shards.getInteger(attribute) + ": " + EnumChatFormatting.GREEN + String.format("%,d", attributeprice);
					if (newToolTip == "") newToolTip = ToolTipString1;
					else newToolTip += "\n" + ToolTipString1;

				}
			} catch (NullPointerException e) {
				e.printStackTrace();
			}
			if (itemNbt.hasKey("boss_tier")) {
				String obtainedString = EnumChatFormatting.GOLD + "Obtained in ";
				switch (itemNbt.getInteger("boss_tier")) {
					case 1:
						newToolTip += "\n" + obtainedString + EnumChatFormatting.YELLOW + "Basic";
						break;
					case 2:
						newToolTip += "\n" + obtainedString + EnumChatFormatting.YELLOW + "Hot";
						break;
					case 3:
						newToolTip += "\n" + obtainedString + EnumChatFormatting.YELLOW + "Burning";
						break;
					case 4:
						newToolTip += "\n" + obtainedString + EnumChatFormatting.YELLOW + "Fiery";
						break;
					case 5:
						newToolTip += "\n" + obtainedString + EnumChatFormatting.YELLOW + "Infernal";
						break;
				}
			}
			event.toolTip.add(newToolTip);
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