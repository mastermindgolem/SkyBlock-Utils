package com.golem.skyblockutils.models;

import com.golem.skyblockutils.Main;
import com.golem.skyblockutils.utils.AuctionHouse;
import com.golem.skyblockutils.utils.RequestUtil;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import logger.Logger;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static com.golem.skyblockutils.Main.*;

public class AttributePrice {

	public static final String[] all_attributes = new String[]{"arachno", "attack_speed", "combo", "elite", "ignition", "lifeline", "breeze", "speed", "experience", "mana_pool", "life_regeneration", "blazing_resistance", "arachno_resistance", "undead_resistance", "blazing_fortune", "fishing_experience", "double_hook", "infection", "trophy_hunter", "fisherman", "hunter", "fishing_speed", "life_recovery", "midas_touch", "mana_regeneration", "veteran", "mending", "ender_resistance", "dominance", "mana_steal", "ender", "blazing", "undead", "warrior", "deadeye", "fortitude", "magic_find"};
	public static final String[] all_kuudra_categories = new String[]{"SHARD", "HELMET", "CHESTPLATE", "LEGGINGS", "BOOTS", "MOLTEN_BELT", "MOLTEN_BRACELET", "MOLTEN_CLOAK", "MOLTEN_NECKLACE", "GAUNTLET_OF_CONTAGION", "IMPLOSION_BELT"};
	private static HashMap<ArrayList<String>, JsonObject> AllCombos = new HashMap<>();
	public static HashMap<String, Integer> LowestBin = new HashMap<>();
	public static HashMap<String, HashMap<String, ArrayList<JsonObject>>> AttributePrices = new HashMap<>();


	public static void checkAuctions(JsonArray auctions) {
		new Thread(() -> {
			AllCombos = new HashMap<>();
			AttributePrices = new HashMap<>();
			LowestBin = new HashMap<>();
			double price_per;
			for (String key : all_kuudra_categories) AttributePrices.put(key, new HashMap<>());

			for (JsonElement auction : auctions) {
				try {
					JsonObject auc = auction.getAsJsonObject();

					if (!auc.get("bin").getAsBoolean()) continue;
					String encoded_data = auc.get("item_bytes").getAsString();

					NBTTagCompound item_tag;
					item_tag = CompressedStreamTools.readCompressed(new ByteArrayInputStream(Base64.getDecoder().decode(encoded_data)));
					NBTTagCompound ExtraAttributes = (NBTTagCompound) item_tag.getTagList("i", 10).get(0);
					ExtraAttributes = ExtraAttributes.getCompoundTag("tag").getCompoundTag("ExtraAttributes");
					String item_id = ExtraAttributes.getString("id");
					if (!LowestBin.containsKey(item_id)) LowestBin.put(item_id, auc.get("starting_bid").getAsInt());
					if (LowestBin.getOrDefault(item_id, 0) > auc.get("starting_bid").getAsInt())
						LowestBin.put(item_id, auc.get("starting_bid").getAsInt());


					if (ExtraAttributes.hasKey("attributes") && ExtraAttributes.getCompoundTag("attributes").getKeySet().size() == 2) {
						ArrayList<String> item_info = new ArrayList<>();
						item_info.add(item_id);

						for (String attribute : all_attributes)
							if (ExtraAttributes.getCompoundTag("attributes").hasKey(attribute))
								item_info.add(attribute);

						if (!AllCombos.containsKey(item_info)) AllCombos.put(item_info, auc);
						if (AllCombos.get(item_info).get("starting_bid").getAsDouble() > auc.get("starting_bid").getAsDouble())
							AllCombos.put(item_info, auc);
					}

					if (ExtraAttributes.hasKey("attributes")) {
						for (String key : all_kuudra_categories) {
							if (!item_id.contains(key)) continue;
							if ((key.equals("HELMET") || key.equals("CHESTPLATE") || key.equals("LEGGINGS") || key.equals("BOOTS")) && !(item_id.contains("CRIMSON") || item_id.contains("AURORA") || item_id.contains("TERROR") || item_id.contains("FERVOR") || item_id.contains("HOLLOW")))
								continue;

							NBTTagCompound attributes = ExtraAttributes.getCompoundTag("attributes");

							for (String attribute : attributes.getKeySet()) {
								price_per = (auc.get("starting_bid").getAsInt() / Math.pow(2, attributes.getInteger(attribute) - 1));
								JsonObject auc2 = new JsonObject();
								auc2.addProperty("item_name", auc.get("item_name").getAsString());
								auc2.addProperty("item_lore", auc.get("item_lore").getAsString());
								auc2.addProperty("tier", auc.get("tier").getAsString());
								auc2.addProperty("uuid", auc.get("uuid").getAsString());
								auc2.addProperty("starting_bid", auc.get("starting_bid").getAsInt());
								auc2.addProperty("price_per_tier", price_per);
								auc2.addProperty(attribute, attributes.getInteger(attribute));
								if (!AttributePrices.get(key).containsKey(attribute))
									AttributePrices.get(key).put(attribute, new ArrayList<>());
								AttributePrices.get(key).get(attribute).add(auc2);
							}
						}
					}

				} catch (NullPointerException | IOException ignored) {
				}
			}
		}).start();
	}

	public static JsonObject getComboValue(String item_id, ArrayList<String> attributes) {
		JsonObject auction = null;

		if (AllCombos.keySet().size() == 0) {
			if (System.currentTimeMillis() - AuctionHouse.lastErrorMessage > 30000) {
				final IChatComponent msg = new ChatComponentText(EnumChatFormatting.RED + "Auctions not checked yet. If you have logged in more than 5 minutes ago, contact golem. Run /sbu refresh");
				mc.thePlayer.addChatMessage(msg);
			}

			return null;
		}

		List<String> excludeAttributes = Arrays.asList(Main.configFile.attributesToExclude.split(", "));

		for (String attribute : attributes) if (excludeAttributes.contains(attribute)) return null;

		Set<ArrayList<String>> CombosKeys = AllCombos.keySet();
		try {
			for (ArrayList<String> key : CombosKeys) {
				if (item_id.contains(key.get(0)) && key.contains(attributes.get(0)) && key.contains(attributes.get(1))) {
					auction = AllCombos.get(key);
				}
			}
		} catch (NullPointerException e) {
			e.printStackTrace();
			return auction;
		}

		return auction;
	}

	public static JsonObject AttributeValue(ItemStack item) {

		JsonObject result = new JsonObject();
		result.addProperty("top_display", "");
		result.addProperty("bottom_display", 0);
		result.addProperty("display_string", "");
		result.addProperty("value", 0);

		List<String> excludeAttributes = Arrays.asList(Main.configFile.attributesToExclude.split(", "));
		List<String> priorityAttributes = Arrays.asList(Main.configFile.priorityAttributes.split(", "));

		NBTTagCompound nbt = item.serializeNBT().getCompoundTag("tag").getCompoundTag("ExtraAttributes").getCompoundTag("attributes");
		String item_id = item.serializeNBT().getCompoundTag("tag").getCompoundTag("ExtraAttributes").getString("id");
		for (String item_key : all_kuudra_categories) {
			if (!item_id.contains(item_key)) continue;
			if ((item_key.equals("HELMET") || item_key.equals("CHESTPLATE") || item_key.equals("LEGGINGS") || item_key.equals("BOOTS")) &&
					!item_id.contains("AURORA") && !item_id.contains("CRIMSON") && !item_id.contains("TERROR") && !item_id.contains("FERVOR") && !item_id.contains("HOLLOW")
			) continue;

			String best_attribute = "";
			int best_tier = 0;
			int best_value = 0;
			int added_value = 0;

			for (String attr_key : nbt.getKeySet()) {
				if (excludeAttributes.contains(attr_key)) continue;
				ArrayList<JsonObject> items = AttributePrices.get(item_key).get(attr_key);
				if (items == null || items.size() == 0) {
					continue;
				}
				items = items.stream().filter(i -> i.get(attr_key).getAsInt() >= configFile.min_tier).collect(Collectors.toCollection(ArrayList::new));
				if (items.size() == 0) {
					continue;
				}
				items.sort(Comparator.comparingDouble((JsonObject o) -> o.get("price_per_tier").getAsDouble()));
				int value = (int) (items.get(0).get("price_per_tier").getAsInt() * Math.pow(2, nbt.getInteger(attr_key) - 1));
				added_value += value;
				if (priorityAttributes.contains(best_attribute) && !priorityAttributes.contains(attr_key) && !Objects.equals(best_attribute, ""))
					continue;
				if (!priorityAttributes.contains(best_attribute) && priorityAttributes.contains(attr_key))
					best_value = 0;
				if (value > best_value) {
					best_value = value;
					best_attribute = attr_key;
					best_tier = nbt.getInteger(attr_key);
				}
			}

			JsonObject comboitem = null;
			if (!item_id.equals("ATTRIBUTE_SHARD")) {
				comboitem = getComboValue(item_id, new ArrayList<>(nbt.getKeySet()));
				if (!Main.configFile.valueStarredArmor && (item_id.startsWith("HOT_") || item_id.startsWith("BURNING_") || item_id.startsWith("FIERY_") || item_id.startsWith("INFERNAL"))) {
					return null;
				}
			}

			int combo_value = (comboitem == null ? 0 : comboitem.get("starting_bid").getAsInt());

			added_value += combo_value;

			ArrayList<String> attrArray = new ArrayList<>(nbt.getKeySet());

			String displayName = item.getDisplayName();
			if (configFile.compactContainerValue) for (String r : new String[]{"Terror ", "Aurora ", "Crimson ", "Fervor ", "Hollow ", "Molten ", "Gauntlet of ", "Attribute "}) displayName = displayName.replace(r, "");


			if (best_tier > 5 && combo_value > configFile.min_godroll_price * 1000000) {
				result.addProperty("top_display", "GR");
				result.addProperty("bottom_display", 0);
				result.addProperty("display_string", ShortenedAttribute(attrArray.get(0)) + " " + nbt.getInteger(attrArray.get(0)) + " " + ShortenedAttribute(attrArray.get(1)) + " " + nbt.getInteger(attrArray.get(1)) + " " + displayName);
				result.addProperty("value", added_value);
				return result;
			} else if (combo_value > configFile.min_godroll_price * 1000000 && combo_value > best_value) {
				result.addProperty("top_display", "GR");
				result.addProperty("bottom_display", 0);
				result.addProperty("display_string", ShortenedAttribute(attrArray.get(0)) + " " + ShortenedAttribute(attrArray.get(1)) + " " + displayName);
				result.addProperty("value", combo_value);
				return result;
			} else if (best_value > LowestBin.getOrDefault(item_id, 0)) {
				result.addProperty("top_display", ShortenedAttribute(best_attribute));
				result.addProperty("bottom_display", best_tier);
				result.addProperty("display_string", ShortenedAttribute(best_attribute) + " " + best_tier + " " + displayName);
				result.addProperty("value", best_value);
				return result;
			} else if (item_id.equals("ATTRIBUTE_SHARD")) {
				result.addProperty("top_display", ShortenedAttribute(attrArray.get(0)));
				result.addProperty("bottom_display", nbt.getInteger(attrArray.get(0)));
				result.addProperty("display_string", ShortenedAttribute(attrArray.get(0)) + " " + nbt.getInteger(attrArray.get(0)) + " " + displayName);
				result.addProperty("value", LowestBin.getOrDefault("ATTRIBUTE_SHARD", 0));
				return result;
			} else if (LowestBin.getOrDefault(item_id, 0) > 0 && nbt.getKeySet().size() > 0) {
				result.addProperty("top_display", "LBIN");
				result.addProperty("bottom_display", 0);
				result.addProperty("display_string", "LBIN " + displayName);
				result.addProperty("value", LowestBin.getOrDefault(item_id, 0));
				return result;
			}


			return null;
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
			case "arachno_resistance":
				return "ARA";
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
			case "undead_resistance":
				return "UND";
			case "mana_steal":
				return "MS";
			case "ender":
			case "ender_resistance":
				return "END";
			case "blazing":
			case "blazing_resistance":
				return "BLA";
			case "warrior":
				return "WAR";
			case "deadeye":
				return "DEA";
			case "experience":
				return "EXP";
			case "lifeline":
				return "LIF";
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