package com.golem.skyblockutils.utils;

import com.golem.skyblockutils.Main;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import static com.golem.skyblockutils.Main.configFile;

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
		if (!configFile.showValueInLore) return;
		ItemStack item = event.itemStack;
		InventoryData.values.forEach((key, value) -> {
            if (!key.getHasStack()) return;
            if (value == null) return;
            if (value.value == 0) return;
            if (key.getStack().equals(item)) {
                event.toolTip.add(EnumChatFormatting.GOLD + "SBU Value: " + EnumChatFormatting.GREEN + Main.formatNumber(value.value));
            }
        });
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