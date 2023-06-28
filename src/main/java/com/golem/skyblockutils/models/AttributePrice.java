package com.golem.skyblockutils.models;

import com.golem.skyblockutils.utils.AuctionHouse;
import com.golem.skyblockutils.utils.RequestUtil;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import logger.Logger;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static com.golem.skyblockutils.Main.auctions;
import static com.golem.skyblockutils.Main.mc;

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

			for (JsonElement auction: auctions) {
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
					if (LowestBin.get(item_id) > auc.get("starting_bid").getAsInt()) LowestBin.put(item_id, auc.get("starting_bid").getAsInt());


					if (ExtraAttributes.hasKey("attributes") && ExtraAttributes.getCompoundTag("attributes").getKeySet().size() == 2) {
						ArrayList<String> item_info = new ArrayList<>();
						item_info.add(item_id);

						for (String attribute : all_attributes)
							if (ExtraAttributes.getCompoundTag("attributes").hasKey(attribute)) item_info.add(attribute);

						if (!AllCombos.containsKey(item_info)) AllCombos.put(item_info, auc);
						if (AllCombos.get(item_info).get("starting_bid").getAsDouble() > auc.get("starting_bid").getAsDouble())
							AllCombos.put(item_info, auc);
					}

					if (ExtraAttributes.hasKey("attributes")) {
						for (String key : all_kuudra_categories) {
							if (!item_id.contains(key)) continue;
							if ((key.equals("HELMET") || key.equals("CHESTPLATE") || key.equals("LEGGINGS") || key.equals("BOOTS")) && !(item_id.contains("CRIMSON") || item_id.contains("AURORA") || item_id.contains("TERROR") || item_id.contains("FERVOR") || item_id.contains("HOLLOW"))) continue;

							NBTTagCompound attributes = ExtraAttributes.getCompoundTag("attributes");

							for (String attribute : attributes.getKeySet()) {
								price_per = (auc.get("starting_bid").getAsInt() / Math.pow(2,attributes.getInteger(attribute)-1));
								JsonObject auc2 = new JsonObject();
								auc2.addProperty("item_name", auc.get("item_name").getAsString());
								auc2.addProperty("item_lore", auc.get("item_lore").getAsString());
								auc2.addProperty("tier", auc.get("tier").getAsString());
								auc2.addProperty("uuid", auc.get("uuid").getAsString());
								auc2.addProperty("starting_bid", auc.get("starting_bid").getAsInt());
								auc2.addProperty("price_per_tier", price_per);
								auc2.addProperty(attribute, attributes.getInteger(attribute));
								if (!AttributePrices.get(key).containsKey(attribute)) AttributePrices.get(key).put(attribute, new ArrayList<>());
								AttributePrices.get(key).get(attribute).add(auc2);
							}
						}
					}

				} catch (NullPointerException | IOException ignored) {}
			}
		}).start();
	}

	public static JsonObject getComboValue(String item_id, ArrayList<String> attributes) {
		JsonObject auction = null;

		if (AllCombos.keySet().size() == 0) {
			final IChatComponent msg = new ChatComponentText(EnumChatFormatting.RED + "Auctions not checked yet. If you have logged in more than 5 minutes ago, contact golem.");
			mc.thePlayer.addChatMessage(msg);
			String urlString = "https://mastermindgolem.pythonanywhere.com/?auctions=mb";
			auctions = new RequestUtil().sendGetRequest(urlString).getJsonAsObject().get("auctions").getAsJsonArray();
			AuctionHouse.lastKnownLastUpdated = System.currentTimeMillis();
			checkAuctions(auctions);

			return null;
		}

		Set<ArrayList<String>> CombosKeys = AllCombos.keySet();
		try {
			for (ArrayList<String> key : CombosKeys) {
				if (item_id.contains(key.get(0)) && key.contains(attributes.get(0)) && key.contains(attributes.get(1))) {
					auction = AllCombos.get(key);
				}
			}
		} catch(NullPointerException e) {
			e.printStackTrace();
			return auction;
		}

		return auction;
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
