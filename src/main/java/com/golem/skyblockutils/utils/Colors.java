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

	private static void addColourCode() {
		itemTypeMap.put("alert",				EnumChatFormatting.DARK_GREEN);
		itemTypeMap.put("white", 				EnumChatFormatting.WHITE);
		itemTypeMap.put("default", 			EnumChatFormatting.BLACK);
		itemTypeMap.put("potential", 		EnumChatFormatting.BLACK);
		itemTypeMap.put("crystal", 			EnumChatFormatting.AQUA);
		itemTypeMap.put("fairy", 				EnumChatFormatting.LIGHT_PURPLE);
		itemTypeMap.put("og_fairy",			EnumChatFormatting.DARK_PURPLE);
		itemTypeMap.put("exotic", 			EnumChatFormatting.GREEN);
		itemTypeMap.put("original", 		EnumChatFormatting.DARK_GRAY);
		itemTypeMap.put("undyed", 			EnumChatFormatting.GRAY);
		itemTypeMap.put("divan", 				EnumChatFormatting.WHITE);
		itemTypeMap.put("flip", 				EnumChatFormatting.DARK_RED);
		itemTypeMap.put("huge_flip",		EnumChatFormatting.RED);
		itemTypeMap.put("ending_soon",	EnumChatFormatting.DARK_AQUA);
		itemTypeMap.put("attribute",		EnumChatFormatting.BLUE);
		itemTypeMap.put("dye",					EnumChatFormatting.DARK_GREEN);
		itemTypeMap.put("glitched",			EnumChatFormatting.BLACK);
		itemTypeMap.put("kuudra",				EnumChatFormatting.DARK_BLUE);
		itemTypeMap.put("god_roll",			EnumChatFormatting.DARK_RED);
		itemTypeMap.put("impact",				EnumChatFormatting.DARK_RED);
		itemTypeMap.put("gem",					EnumChatFormatting.LIGHT_PURPLE);
		itemTypeMap.put("farming",			EnumChatFormatting.GOLD);
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
		addColourCode();
		addRarityCode();
	}
}