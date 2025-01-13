package com.golem.skyblockutils.models;

import com.golem.skyblockutils.Main;
import com.golem.skyblockutils.features.KuudraFight.Kuudra;
import com.golem.skyblockutils.utils.AttributeUtils;
import com.golem.skyblockutils.utils.AuctionHouse;
import com.golem.skyblockutils.utils.ChatUtils;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import logger.Logger;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;

import java.util.List;
import java.util.*;
import java.util.stream.Collectors;

import static com.golem.skyblockutils.Main.*;
import static com.golem.skyblockutils.models.AttributeItemType.Shard;

public class AttributePrice {

	public static final String[] all_attributes = new String[]{"arachno", "attack_speed", "combo", "elite", "ignition", "lifeline", "breeze", "speed", "experience", "mana_pool", "life_regeneration", "blazing_resistance", "arachno_resistance", "undead_resistance", "blazing_fortune", "fishing_experience", "double_hook", "infection", "trophy_hunter", "fisherman", "hunter", "fishing_speed", "life_recovery", "midas_touch", "mana_regeneration", "veteran", "mending", "ender_resistance", "dominance", "mana_steal", "ender", "blazing", "undead", "warrior", "deadeye", "fortitude", "magic_find"};
	public static final AttributeItemType[] all_kuudra_categories = AttributeItemType.values();
	private static HashMap<String, AuctionAttributeItem> AllCombos = new HashMap<>();
	public static HashMap<String, Long> LowestBin = new HashMap<>();
	public static HashMap<AttributeItemType, HashMap<String, ArrayList<AuctionAttributeItem>>> AttributePrices = new HashMap<>();
	public static HashMap<AttributeItemType, HashMap<String, ArrayList<Long>>> LowestAttributePrices = new HashMap<>();
	private static HashMap<String, Long> skyHelperPrices = new HashMap<>();
	static final IChatComponent ErrorMessage = new ChatComponentText(EnumChatFormatting.RED + "Auctions not checked yet. If you have logged in more than 5 minutes ago, contact golem. Run /sbu refresh");
	public static List<String> equipmentExcludeAttributes;
	public static List<String> armorExcludeAttributes;
	public static List<String> priorityAttributes;
	public static Set<String> expensiveAttributes = new HashSet<>();


	public static void checkAuctions(JsonArray auctions) {
		new Thread(() -> {
			AllCombos = new HashMap<>();
			AttributePrices = new HashMap<>();
			LowestBin = new HashMap<>();
			for (AttributeItemType key : AttributeItemType.values()) AttributePrices.put(key, new HashMap<>());
			for (AttributeItemType key : AttributeItemType.values()) LowestAttributePrices.put(key, new HashMap<>());
			for (JsonElement auction : auctions) {
				try {
					JsonObject auc = auction.getAsJsonObject();

					AuctionAttributeItem item = new AuctionAttributeItem(auc);
					if (item.price == 0) continue;
					AttributeItemType itemType = item.getItemType();
					if (!LowestBin.containsKey(item.item_id)) LowestBin.put(item.item_id, item.price);
					if (LowestBin.get(item.item_id) > item.price)
						LowestBin.put(item.item_id, item.price);

					if (itemType == null) continue;

					if (item.comboString != null) {
						if (!AllCombos.containsKey(item.comboString)) AllCombos.put(item.comboString, item);
						if (AllCombos.get(item.comboString).price > item.price) AllCombos.put(item.comboString, item);
					}

					for (String attr : item.attributes.keySet()) {
						Attribute attribute = item.addAttribute(attr);

						LowestAttributePrices.get(itemType).putIfAbsent(attribute.attribute, new ArrayList<>(Collections.nCopies(11, 0L)));

						for (int i = 0; i <= attribute.tier; i++) {
							if (LowestAttributePrices.get(itemType).get(attribute.attribute).get(i) == 0) {
								LowestAttributePrices.get(itemType).get(attribute.attribute).set(i, attribute.price_per);
							} else if (LowestAttributePrices.get(itemType).get(attribute.attribute).get(i) > attribute.price_per) {
								LowestAttributePrices.get(itemType).get(attribute.attribute).set(i, attribute.price_per);
							}
						}
						AttributePrices.get(itemType).putIfAbsent(attribute.attribute, new ArrayList<>());
						AttributePrices.get(itemType).get(attribute.attribute).add(item);
					}

				} catch (NullPointerException error) {
					error.printStackTrace();
				}
			}
			equipmentExcludeAttributes = Arrays.asList(configFile.attributesToExcludeEquip.split(", "));
			armorExcludeAttributes = Arrays.asList(configFile.attributesToExcludeArmor.split(", "));
			priorityAttributes = Arrays.asList(configFile.priorityAttributes.split(", "));

			expensiveAttributes = LowestAttributePrices.get(Shard).entrySet().stream()
					.filter(entry -> !entry.getValue().isEmpty())
					.sorted((e1, e2) -> Long.compare(e2.getValue().get(0), e1.getValue().get(0)))
					.limit(5)
					.map(Map.Entry::getKey)
					.collect(Collectors.toSet());
		}).start();

	}

	public static void processSkyHelperPrices(JsonObject prices) {
		for (Map.Entry<String, JsonElement> entry : prices.entrySet()) {
			String item = entry.getKey();
			if (!item.contains("_roll_")) continue;
			if (item.startsWith("hot") || item.startsWith("burning") || item.startsWith("fiery") || item.startsWith("infernal")) continue;
			if (item.startsWith("kuudra_")) {
				skyHelperPrices.put(item, entry.getValue().getAsLong());
				continue;
			}
			String item_id = item.split("_roll_")[0].toUpperCase();
			AttributeItemType item_type = AttributeUtils.getItemType(item_id);
			if (item_type == null) continue;
			item = item.replaceAll("_roll_", "_");
			skyHelperPrices.put(item, entry.getValue().getAsLong());
		}
	}

	public static long getComboValue(AttributeItemType item_type, Set<String> attributes) {
		if (item_type == null) return 0L;
		return skyHelperPrices.getOrDefault((item_type.getID() + "_" + attributes.stream().sorted().collect(Collectors.joining("_"))).toLowerCase(), 0L);
	}

	public static long getComboValue(AttributeItemType item_type, AttributeArmorType armor_type, Set<String> attributes) {
		if (item_type == null || armor_type == null) return 0L;
		return skyHelperPrices.getOrDefault((armor_type.getID() + "_" + item_type.getID() + "_" + attributes.stream().sorted().collect(Collectors.joining("_"))).toLowerCase(), 0L);
	}


	public static AuctionAttributeItem getComboItem(AttributeItemType item_type, Set<String> attributes) {
		Set<String> combosKeys = AllCombos.keySet();
		if (combosKeys.isEmpty()) {
			long currentTimeMillis = time.getCurrentMS();
			if (currentTimeMillis - AuctionHouse.lastErrorMessage > 30000) {
				AuctionHouse.lastErrorMessage = currentTimeMillis;
				mc.thePlayer.addChatMessage(ErrorMessage);
			}
			return null;
		}

		List<String> excludeAttributes = (AttributeUtils.isArmor(item_type.getID()) ? armorExcludeAttributes : equipmentExcludeAttributes);

		for (String attribute : attributes) if (excludeAttributes.contains(attribute)) return null;

		String combo = item_type.getID() + "_" + attributes.stream().sorted().collect(Collectors.joining("_"));

		return AllCombos.get(combo);
	}

	public static AuctionAttributeItem getComboItem(AttributeItemType item_type, AttributeArmorType armor_type, Set<String> attributes) {
		Set<String> combosKeys = AllCombos.keySet();
		if (combosKeys.isEmpty()) {
			long currentTimeMillis = time.getCurrentMS();
			if (currentTimeMillis - AuctionHouse.lastErrorMessage > 30000) {
				AuctionHouse.lastErrorMessage = currentTimeMillis;
				mc.thePlayer.addChatMessage(ErrorMessage);
			}
			return null;
		}

		List<String> excludeAttributes = (AttributeUtils.isArmor(item_type.getID()) ? armorExcludeAttributes : equipmentExcludeAttributes);

		for (String attribute : attributes) if (excludeAttributes.contains(attribute)) return null;

		String combo = armor_type.getID() + "_" + item_type.getID() + "_" + attributes.stream().sorted().collect(Collectors.joining("_"));

		return AllCombos.get(combo);
	}


	public static AttributeValueResult AttributeValue(ItemStack item) {
		return AttributeValue(item, false);
	}
	public static AttributeValueResult AttributeValue(ItemStack itemStack, boolean dev) {
		AttributeItem item = new AttributeItem(itemStack.getDisplayName(), "", itemStack.serializeNBT().getCompoundTag("tag").getCompoundTag("ExtraAttributes"));

		if (item.item_type == null) return null;

		return AttributeValue(item, dev, false);
	}

	public static AttributeValueResult AttributeValue(AttributeItem item, boolean dev, boolean skyHelper) {

		AttributeValueResult result = new AttributeValueResult();

		if (item.item_type == null) return null;

		String item_id = item.item_id;

		if (!Main.configFile.valueStarredArmor && (item_id.startsWith("HOT_") || item_id.startsWith("BURNING_") || item_id.startsWith("FIERY_") || item_id.startsWith("INFERNAL"))) {
			return null;
		}

		if (dev) ChatUtils.addChatMessage("Item ID: " + item_id);
		if (dev) ChatUtils.addChatMessage("Item Type: " + item.item_type);
		if (dev) ChatUtils.addChatMessage("Lowest Bin: " + LowestBin.getOrDefault(item_id, 0L));

		if (!LowestAttributePrices.containsKey(item.item_type)) return null;
		List<String> excludeAttributes = (AttributeUtils.isArmor(item_id) ? armorExcludeAttributes : equipmentExcludeAttributes);

		String best_attribute = "";
		int best_tier = 0;
		long best_value = 0;
		long added_value = 0;
		int total_tiers = 0;
		long value;

		for (String attr_key : item.attributes.keySet()) {
			int attr_tier = item.attributes.get(attr_key);
			if (!configFile.valueHighTierItems && attr_tier >= 7) return null;
			total_tiers += 1 << (attr_tier - 1);
			if (excludeAttributes.contains(attr_key)) continue;
			if (!LowestAttributePrices.get(item.item_type).containsKey(attr_key)) continue;
			ArrayList<Long> items = LowestAttributePrices.get(item.item_type).get(attr_key);
			int min_tier = (item.item_type == Shard ? configFile.minShardTier : configFile.minArmorTier);
			if (min_tier > 0) {
				value = items.get(min_tier) << (attr_tier - 1);
			} else {
				value = items.get(attr_tier) << (attr_tier - 1);
			}
			if (dev) Kuudra.addChatMessage(attr_key + " " + attr_tier + " value : " + value);
			added_value += value;
			if (priorityAttributes.contains(best_attribute) && !priorityAttributes.contains(attr_key) && !Objects.equals(best_attribute, ""))
				continue;
			if (!priorityAttributes.contains(best_attribute) && priorityAttributes.contains(attr_key))
				best_value = 0;
			if (value > best_value) {
				best_value = value;
				best_attribute = attr_key;
				best_tier = attr_tier;
			}
		}

		long combo_value = AttributeUtils.isArmor(item_id)
				? getComboValue(item.item_type, AttributeUtils.getArmorVariation(item_id), item.attributes.keySet())
				: getComboValue(item.item_type, item.attributes.keySet());
		if (dev) {
			Kuudra.addChatMessage("Combo Value: " + combo_value);
			Kuudra.addChatMessage("Best Attribute: " + best_attribute);
			Kuudra.addChatMessage("Total Tiers: " + total_tiers);
		}

		added_value += combo_value;

		ArrayList<String> attrArray = new ArrayList<>(item.attributes.keySet());

		String displayName = item.item_name;

		int salvageValue = (int) (10 * total_tiers * AuctionHouse.ESSENCE_VALUE);

		if (configFile.compactContainerValue)
			for (String r : new String[]{"Terror ", "Aurora ", "Crimson ", "Fervor ", "Hollow ", "Molten ", "Gauntlet of ", "Attribute "})
				displayName = displayName.replace(r, "");

		result.best_attribute = new Attribute(best_attribute, best_tier, best_value);
		result.display_name = displayName;
		result.item_id = item_id;

		if (best_tier > 5 && combo_value > configFile.min_godroll_price * 1_000_000) {
			result.top_display = "GR";
			result.bottom_display = 0;
			result.display_string = String.join(" ", attrArray.stream().map(o -> ShortenedAttribute(o) + " " + item.attributes.get(o)).collect(Collectors.toList())) + " " + displayName;
			result.value = added_value;
			return result;
		} else if (combo_value > configFile.min_godroll_price * 1000000 && combo_value > best_value) {
			result.top_display = "GR";
			result.bottom_display = 0;
			result.display_string = String.join(" ", attrArray.stream().map(AttributePrice::ShortenedAttribute).sorted().collect(Collectors.toList())) + " " + displayName;
			result.value = combo_value;
			return result;
		} else if (best_value > LowestBin.getOrDefault(item_id, 0L)) {
			result.top_display = ShortenedAttribute(best_attribute);
			result.bottom_display = best_tier;
			result.display_string = ShortenedAttribute(best_attribute) + " " + best_tier + " " + displayName;
			result.value = best_value;
			return result;
		} else if (item.item_type == Shard) {
			result.top_display = ShortenedAttribute(attrArray.get(0));
			result.bottom_display = item.attributes.get(attrArray.get(0));
			result.display_string = ShortenedAttribute(attrArray.get(0)) + " " + item.attributes.get(attrArray.get(0)) + " " + displayName;
			result.value = LowestBin.getOrDefault("ATTRIBUTE_SHARD", 0L);
			return result;
		} else if (AttributeUtils.isArmor(item_id) && salvageValue > LowestBin.getOrDefault(item_id, 0L)) {
			result.top_display = "SAL";
			result.bottom_display = 0;
			result.display_string = "SALV " + displayName;
			result.value = salvageValue;
			return result;
		} else if (LowestBin.getOrDefault(item_id, 0L) > 0 && item.attributes.size() > 0) {
			result.top_display = "LBIN";
			result.bottom_display = 0;
			result.display_string = "LBIN " + displayName;
			result.value = LowestBin.getOrDefault(item_id, 0L);
			return result;
		}
		return null;
	}

	public static String ShortenedAttribute(String attribute) {
		switch (attribute) {
			case "mana_pool":
				return "MP";
			case "mana_regeneration":
				return "MR";
			case "veteran":
				return "VET";
			case "dominance":
				return "DOM";
			case "mending":
				return "VIT";
			case "magic_find":
				return "MF";
			case "speed":
				return "SP";
			case "breeze":
				return "BR";
			case "arachno":
				return "ARA";
			case "arachno_resistance":
				return "AR";
			case "attack_speed":
				return "AS";
			case "combo":
				return "COM";
			case "elite":
				return "ELI";
			case "ignition":
				return "IGN";
			case "life_recovery":
				return "LRY";
			case "midas_touch":
				return "MT";
			case "undead":
				return "UND";
			case "undead_resistance":
				return "UR";
			case "mana_steal":
				return "MS";
			case "ender":
				return "END";
			case "ender_resistance":
				return "ER";
			case "blazing":
				return "BLA";
			case "blazing_resistance":
				return "BLR";
			case "warrior":
				return "WAR";
			case "deadeye":
				return "DEA";
			case "experience":
				return "EXP";
			case "lifeline":
				return "LL";
			case "life_regeneration":
				return "LR";
			case "fortitude":
				return "FOR";
			case "blazing_fortune":
				return "BF";
			case "fishing_experience":
				return "FE";
			case "double_hook":
				return "DH";
			case "fisherman":
				return "FM";
			case "fishing_speed":
				return "FS";
			case "HUNTER":
				return "HUN";
			case "trophy_hunter":
				return "TH";
			case "infection":
				return "INF";
			case "hunter":
				return "HUN";
			default:
				Logger.warn(attribute);
				try {
					return Arrays.stream(attribute.replace("_", " ").toUpperCase().split(" "))
							.map(word -> String.valueOf(word.charAt(0)))
							.collect(Collectors.joining());
				} catch (Exception ignored) {return "";}
		}
	}

}
