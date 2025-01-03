package com.golem.skyblockutils.utils;

import com.golem.skyblockutils.models.AttributeArmorType;
import com.golem.skyblockutils.models.AttributeItemType;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.golem.skyblockutils.models.AttributeItemType.*;
import static com.golem.skyblockutils.models.AttributeItemType.Boots;

public class AttributeUtils {
	private static List<String> all_attributes = Arrays.asList("vitality", "arachno", "attack_speed", "combo", "elite", "ignition", "lifeline", "breeze", "speed", "experience", "mana_pool", "life_regeneration", "blazing_resistance", "arachno_resistance", "undead_resistance", "blazing_fortune", "fishing_experience", "double_hook", "infection", "trophy_hunter", "fisherman", "hunter", "fishing_speed", "life_recovery", "midas_touch", "mana_regeneration", "veteran", "mending", "ender_resistance", "dominance", "mana_steal", "ender", "blazing", "undead", "warrior", "deadeye", "fortitude", "magic_find");

	public static String AttributeAliases (String input) {
		if (Objects.equals(input, "ll")) return "lifeline";
		if (all_attributes.contains(input)) return input;
		for (String attribute : all_attributes) {
			if (attribute.startsWith(input)) return (attribute.equals("vitality") ? "mending" : attribute);
			if (Objects.equals(Arrays.stream(attribute.split("_")).map(s -> s.substring(0, 1)).collect(Collectors.joining()), input)) return (attribute.equals("vitality") ? "mending" : attribute);
		}
		return input;
	}
	public static boolean isArmor(String item_id) {
		AttributeItemType type = getItemType(item_id);
		return type == Helmet || type == Chestplate || type == Leggings || type == Boots;
	}

	public static AttributeItemType getItemType(String item_id) {
		switch (item_id) {
			case "TAURUS_HELMET":
				return FishingHelmet;
			case "FLAMING_CHESTPLATE":
				return FishingChestplate;
			case "MOOGMA_LEGGINGS":
				return FishingLeggings;
			case "SLUG_BOOTS":
				return FishingBoots;
			case "MOLTEN_BELT":
				return MoltenBelt;
			case "MOLTEN_BRACELET":
				return MoltenBracelet;
			case "MOLTEN_NECKLACE":
				return MoltenNecklace;
			case "MOLTEN_CLOAK":
				return MoltenCloak;
			case "LAVA_SHELL_NECKLACE":
				return LavaShellNecklace;
			case "SCOURGE_CLOAK":
				return ScourgeCloak;
			case "ATTRIBUTE_SHARD":
				return Shard;
			case "MAGMA_NECKLACE":
				return MagmaNecklace;
			case "BLAZE_BELT":
				return BlazeBelt;
			case "GLOWSTONE_GAUNTLET":
				return GlowstoneGauntlet;
			case "GHAST_CLOAK":
				return GhastCloak;
			case "IMPLOSION_BELT":
				return ImplosionBelt;
			case "GAUNTLET_OF_CONTAGION":
				return GauntletOfContagion;
		}
		if (!item_id.contains("CRIMSON") && !item_id.contains("AURORA") && !item_id.contains("TERROR") && !item_id.contains("FERVOR") && !item_id.contains("HOLLOW")) return null;
		if (item_id.contains("HELMET")) return Helmet;
		if (item_id.contains("CHESTPLATE")) return Chestplate;
		if (item_id.contains("LEGGINGS")) return Leggings;
		if (item_id.contains("BOOTS")) return Boots;
		return null;
	}
	public static AttributeArmorType getArmorVariation(String item_id) {
		if (!isArmor(item_id)) return null;
		for (AttributeArmorType armorType : AttributeArmorType.values()) {
			if (item_id.contains(armorType.getID())) return armorType;
		}
		return null;
	}

	public static NBTTagCompound getExtraAttributes(String data) {
		NBTTagCompound item_tag;
		try {
			item_tag = CompressedStreamTools.readCompressed(new ByteArrayInputStream(Base64.getDecoder().decode(data)));
		} catch (IOException e) {
			return new NBTTagCompound();
		}
		NBTTagCompound ExtraAttributes = (NBTTagCompound) item_tag.getTagList("i", 10).get(0);
		return ExtraAttributes.getCompoundTag("tag").getCompoundTag("ExtraAttributes");
	}

}
