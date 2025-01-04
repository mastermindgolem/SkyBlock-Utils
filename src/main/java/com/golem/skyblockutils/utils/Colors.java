package com.golem.skyblockutils.utils;

import net.minecraft.util.EnumChatFormatting;

import java.util.HashMap;
import java.util.Map;

public class Colors {
	private static final HashMap<String, EnumChatFormatting> itemTypeMap = new HashMap<>();
	private static final HashMap<String, EnumChatFormatting> rarityMap = new HashMap<>();

	public static EnumChatFormatting getColourCode(String itemType) {
		itemType = itemType.toLowerCase();
		return itemTypeMap.getOrDefault(itemType, EnumChatFormatting.BLACK);
	}

	public static EnumChatFormatting getRarityCode(final String rarity) {
		return rarityMap.getOrDefault(rarity.toLowerCase(), null);
	}

	public static String cleanColour(String in) {
		return in.replaceAll("(?i)\\u00A7.", "");
	}

	public static String cleanDuplicateColourCodes(String line) {
		StringBuilder sb = new StringBuilder();
		char currentColourCode = 'r';
		boolean sectionSymbolLast = false;
		for (char c : line.toCharArray()) {
			if ((int) c > 50000) continue;

			if (c == '\u00a7') {
				sectionSymbolLast = true;
			} else {
				if (sectionSymbolLast) {
					if (currentColourCode != c) {
						sb.append('\u00a7');
						sb.append(c);
						currentColourCode = c;
					}
					sectionSymbolLast = false;
				} else {
					sb.append(c);
				}
			}
		}
		return sb.toString();
	}

	private static void addRarityCode() {
		rarityMap.put("common",				EnumChatFormatting.WHITE);
		rarityMap.put("uncommon", 		EnumChatFormatting.GREEN);
		rarityMap.put("rare", 				EnumChatFormatting.BLUE);
		rarityMap.put("epic", 				EnumChatFormatting.DARK_PURPLE);
		rarityMap.put("legendary", 		EnumChatFormatting.GOLD);
		rarityMap.put("mythic", 			EnumChatFormatting.LIGHT_PURPLE);
		rarityMap.put("supreme",			EnumChatFormatting.DARK_RED);
		rarityMap.put("special", 			EnumChatFormatting.RED);
		rarityMap.put("very_special",	EnumChatFormatting.RED);
		rarityMap.put("star", 				EnumChatFormatting.GOLD);
	}

	public static Map<String, EnumChatFormatting> getItemTypeMap() {
		return new HashMap<>(itemTypeMap);
	}

	public static Map<String, EnumChatFormatting> getRarityMap() {
		return new HashMap<>(rarityMap);
	}

	static {
		addRarityCode();
	}
}