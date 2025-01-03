package com.golem.skyblockutils.utils;

import com.golem.skyblockutils.init.KeybindsInit;
import com.golem.skyblockutils.models.AttributeItemType;
import com.golem.skyblockutils.models.AttributePrice;
import com.golem.skyblockutils.models.AuctionAttributeItem;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Arrays;

import static com.golem.skyblockutils.Main.configFile;
import static com.golem.skyblockutils.models.AttributePrice.LowestAttributePrices;
import static com.golem.skyblockutils.models.AttributePrice.all_attributes;

public class ToolTipListener {
	private long comboprice = -1;
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
				AuctionAttributeItem comboitem = AttributePrice.getComboValue(AttributeUtils.getItemType(name), shards.getKeySet());
				if (comboitem == null) {
					comboprice = 0;
				} else {
					comboprice = comboitem.price;
				}
				previousItemSearched = name;
				previousAttributesSearched = s;
			}
			StringBuilder newToolTip = new StringBuilder();
			if (shards.getKeySet().size() > 1) {
				String ToolTipString = EnumChatFormatting.GOLD + "Combo Value: " + EnumChatFormatting.GREEN + String.format("%,d", comboprice);
				newToolTip.append(ToolTipString);
			}
			long attributeprice;
			AttributeItemType key = AttributeUtils.getItemType(name);
			if (key == null) {
				event.toolTip.add(newToolTip.toString());
				return;
			}
			try {
				for (String attribute : all_attributes) {
					if (!shards.getKeySet().contains(attribute)) continue;
					if (!LowestAttributePrices.get(key).containsKey(attribute)) continue;
					int min_tier = (key == AttributeItemType.Shard ? configFile.minShardTier : configFile.minArmorTier);
					attributeprice = LowestAttributePrices.get(key).get(attribute).get(min_tier) << (shards.getInteger(attribute) - 1);
					String ToolTipString1 = EnumChatFormatting.GOLD + TitleCase(attribute) + " " + shards.getInteger(attribute) + ": " + EnumChatFormatting.GREEN + String.format("%,d", attributeprice);
					if (newToolTip.length() == 0) newToolTip = new StringBuilder(ToolTipString1);
					else newToolTip.append("\n").append(ToolTipString1);

				}
			} catch (NullPointerException e) {
				e.printStackTrace();
			}
			if (itemNbt.hasKey("boss_tier")) {
				String obtainedString = EnumChatFormatting.GOLD + "Obtained in ";
				switch (itemNbt.getInteger("boss_tier")) {
					case 1:
						newToolTip.append("\n").append(obtainedString).append(EnumChatFormatting.YELLOW).append("Basic");
						break;
					case 2:
						newToolTip.append("\n").append(obtainedString).append(EnumChatFormatting.YELLOW).append("Hot");
						break;
					case 3:
						newToolTip.append("\n").append(obtainedString).append(EnumChatFormatting.YELLOW).append("Burning");
						break;
					case 4:
						newToolTip.append("\n").append(obtainedString).append(EnumChatFormatting.YELLOW).append("Fiery");
						break;
					case 5:
						newToolTip.append("\n").append(obtainedString).append(EnumChatFormatting.YELLOW).append("Infernal");
						break;
				}
			}
			event.toolTip.add(newToolTip.toString());
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