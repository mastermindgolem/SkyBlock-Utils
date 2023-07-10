package com.golem.skyblockutils.command.commands;

import com.golem.skyblockutils.Main;
import com.golem.skyblockutils.models.AttributePrice;
import com.golem.skyblockutils.utils.*;
import com.google.gson.JsonObject;
import com.mojang.authlib.GameProfile;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.event.ClickEvent;
import net.minecraft.event.HoverEvent;
import net.minecraft.util.*;

import java.util.*;
import java.util.stream.Collectors;

import static com.golem.skyblockutils.Main.*;
import static com.golem.skyblockutils.models.AttributePrice.AttributePrices;
import static com.golem.skyblockutils.utils.AuctionHouse.CheckIfAuctionsSearched;
import static com.golem.skyblockutils.utils.Colors.getRarityCode;

public class AttributeCommand extends CommandBase implements Help {
	private int level;

	private final List<String> helpStrings;

	private final String[] item_types = new String[] {"AURORA", "CRIMSON", "TERROR", "HOLLOW", "FERVOR", "ATTRIBUTE_SHARD"};

	private final String[] armor_types = new String[] {"HELMET", "CHESTPLATE", "LEGGINGS", "BOOTS"};

	public AttributeCommand() {
		helpStrings = new ArrayList<>();
	}

	@Override
	public Help getHelp() {
		return this;
	}

	@Override
	public int getRequiredPermissionLevel() {
		return 0;
	}

	@Override
	public void addHelpString() {
		helpStrings.add(getHelpMessage());
	}

	@Override
	public List<String> getHelpStrings() {
		return helpStrings;
	}

	@Override
	public List<String> getHoverStrings() {
		return Arrays.asList(
			EnumChatFormatting.BLUE + " ===================Attribute help menu!=================== ",
			EnumChatFormatting.RESET + "\n",
			EnumChatFormatting.GOLD + "/attributeprice help" +
			EnumChatFormatting.GRAY + " ⬅ Shows this message.",
			EnumChatFormatting.RESET + "\n",
			EnumChatFormatting.RESET + "\n",
			EnumChatFormatting.GRAY + " " +
			EnumChatFormatting.STRIKETHROUGH + "/attributeprice ah W.I.P. (Kami might be dumb)",
			EnumChatFormatting.RESET + "\n",
			EnumChatFormatting.RESET + "\n",
			EnumChatFormatting.GOLD + "/attributeprice attribute [level]" +
			EnumChatFormatting.GRAY  + " ⬅ Shows best price for attribute at any level, unless [level] is specified",
			EnumChatFormatting.RESET + "\n",
			example() + "E.g. /attributeprice veteran 1",
			EnumChatFormatting.RESET + "\n",
			EnumChatFormatting.RESET + "\n",
			EnumChatFormatting.GOLD + "/attributeprice attribute1 attribute2" +
			EnumChatFormatting.GRAY + " ⬅ Shows cheapest armor of specified combo",
			EnumChatFormatting.RESET + "\n",
			EnumChatFormatting.RESET + "\n",
			EnumChatFormatting.RED + "Legend:" +
			EnumChatFormatting.RESET + "\n  " +
			EnumChatFormatting.WHITE + "Attribute: veteran, mana_regeneration, mending" +
			EnumChatFormatting.RESET + "\n  " +
			EnumChatFormatting.WHITE + "Level: 1, 10, 5",
			EnumChatFormatting.RESET + "\n",
			EnumChatFormatting.BLUE + " ======================================================== "
		);
	}

	@Override
	public String getHelpMessage() {
		return
			EnumChatFormatting.GRAY + "▶ " +
			EnumChatFormatting.GOLD + "/attribute" +
			EnumChatFormatting.BOLD + "price " +
			EnumChatFormatting.RESET +
			EnumChatFormatting.GOLD + "attribute " +
			EnumChatFormatting.AQUA + "level" +
			example() +	"(Aliases: /ap)" +
			EnumChatFormatting.RESET + "\n" +
			EnumChatFormatting.GRAY +
			EnumChatFormatting.ITALIC +	"Mending is vitality, we just never changed it." +
			EnumChatFormatting.RESET + "\n";
	}

	@Override
	public String getCommandName() {
		return "attributeprice";
	}

	@Override
	public List<String> getCommandAliases() {
		List<String> al = new ArrayList<>();
		al.add("ap");
		return al;
	}

	@Override
	public String getCommandUsage(ICommandSender sender) {
		return "/attributeprice help for more information";
	}

	@Override
	public void processCommand(ICommandSender sender, String[] args) {
//			Separate commands later!
		if (args.length == 0) {
			StringBuilder sb = new StringBuilder();
			for (String str : getHoverStrings()) {
				sb.append(str);
			}
			String hover = sb.toString();
			Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText(getHelpMessage()).setChatStyle(new ChatStyle().setChatHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ChatComponentText(hover)))));
		}



		//Check all tiers
		String attribute1;
		if (args.length == 1) {
			attribute1 = AttributeUtils.AttributeAliases(args[0]);

			if (CheckIfAuctionsSearched()) return;

			addChatMessage(EnumChatFormatting.AQUA + "Auctions for " + EnumChatFormatting.BOLD + EnumChatFormatting.DARK_BLUE + attribute1.toUpperCase());
			getAttributePrice(attribute1, new String[]{"SHARD", "HELMET", "CHESTPLATE", "LEGGINGS", "BOOTS"}, 0);

			mc.thePlayer.addChatMessage(new ChatComponentText(EnumChatFormatting.BLUE + "[EQUIPMENT]").setChatStyle(new ChatStyle().setChatClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/equipmentprice " + attribute1) {
				@Override
				public Action getAction() {
					//custom behavior
					return Action.RUN_COMMAND;
				}
			})));
		}
		if (args.length == 2) {
			attribute1 = AttributeUtils.AttributeAliases(args[0]);
			boolean combo = false;
			try {
				level = Integer.parseInt(args[1]);
			} catch (NumberFormatException ignored) {
				combo = true;
			}
			System.out.println("Checking " + auctions.size() + " auctions");
			if (auctions.size() == 0) {
				final IChatComponent msg = new ChatComponentText(EnumChatFormatting.RED + "Auctions not checked yet. If you have logged in more than 5 minutes ago, contact golem.");
				mc.thePlayer.addChatMessage(msg);
				return;
			}
			if (!combo) {
				if (CheckIfAuctionsSearched()) return;

				addChatMessage(EnumChatFormatting.AQUA + "Auctions for " + EnumChatFormatting.BOLD + "" + EnumChatFormatting.DARK_BLUE + attribute1.toUpperCase() + " " + level);
				getAttributePrice(attribute1, new String[]{"SHARD", "HELMET", "CHESTPLATE", "LEGGINGS", "BOOTS"}, level);

				mc.thePlayer.addChatMessage(new ChatComponentText(EnumChatFormatting.BLUE + "[EQUIPMENT]").setChatStyle(new ChatStyle().setChatClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/equipmentprice " + attribute1 + " " + level) {
					@Override
					public Action getAction() {
						//custom behavior
						return Action.RUN_COMMAND;
					}
				})));
			} else {

				HashMap<String, HashMap<String, JsonObject>> possible_items = new HashMap<>();
				possible_items.put("CRIMSON", new HashMap<>());
				possible_items.put("AURORA", new HashMap<>());
				possible_items.put("TERROR", new HashMap<>());
				possible_items.put("FERVOR", new HashMap<>());
				possible_items.put("HOLLOW", new HashMap<>());

				for (String key : possible_items.keySet()) {
					possible_items.get(key).put("HELMET", null);
					possible_items.get(key).put("CHESTPLATE", null);
					possible_items.get(key).put("LEGGINGS", null);
					possible_items.get(key).put("BOOTS", null);
				}
				String attribute2 = AttributeUtils.AttributeAliases(args[1]);
				addChatMessage(EnumChatFormatting.AQUA + "Auctions for " + EnumChatFormatting.DARK_BLUE + attribute1.toUpperCase() + " " + attribute2.toUpperCase());
				for (String key: item_types) {
					if (key.equals("ATTRIBUTE_SHARD")) continue;
					addChatMessage(EnumChatFormatting.AQUA + ToolTipListener.TitleCase(key));
					for (String key2: armor_types) {
						JsonObject item = AttributePrice.getComboValue(key + "_" + key2, new ArrayList<>(Arrays.asList(attribute1,attribute2)));
						if (item == null) continue;
						System.out.println(item);
						final IChatComponent msg = new ChatComponentText(ToolTipListener.TitleCase(key2) + ": " + getRarityCode(item.get("tier").getAsString()) + item.get("item_name").getAsString() + " - " + EnumChatFormatting.GREEN + coolFormat(item.get("starting_bid").getAsDouble(), 0)).setChatStyle(new ChatStyle().setChatClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/viewauction " + item.get("uuid").getAsString()) {
							public Action getAction() {
								return Action.RUN_COMMAND;
							}
						}).setChatHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ChatComponentText(getRarityCode(item.get("tier").getAsString()) + item.get("item_name").getAsString() + "\n" + item.get("item_lore").getAsString()))));
						mc.thePlayer.addChatMessage(msg);
					}
				}
				mc.thePlayer.addChatMessage(new ChatComponentText(EnumChatFormatting.BLUE + "[EQUIPMENT]").setChatStyle(new ChatStyle().setChatClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/equipmentprice " + attribute1 + " " + attribute2) {
					@Override
					public Action getAction() {
						//custom behavior
						return Action.RUN_COMMAND;
					}
				})));
			}
		}

	}



	public void getAttributePrice(String attribute, String[] keys, int level) {
		for (String key: keys) {
			if (!AttributePrices.get(key).containsKey(attribute)) continue;
			ArrayList<JsonObject> items = AttributePrices.get(key).get(attribute);
			if (items == null) continue;
			if (level == 0) {
				items = items.stream()
					.filter(item -> item.get(attribute).getAsInt() >= 1 /*configFile.min_tier*/)
					.collect(Collectors.toCollection(ArrayList::new));
			} else {
				items = items.stream()
					.filter(item -> item.get(attribute).getAsInt() == level)
					.collect(Collectors.toCollection(ArrayList::new));
			}


			items.sort(Comparator.comparingDouble((JsonObject o) -> o.get("price_per_tier").getAsDouble()));
			addChatMessage(EnumChatFormatting.AQUA + ToolTipListener.TitleCase(key));
			if (items.size() < 5) {
				for (JsonObject item: items) {
					final IChatComponent msg = new ChatComponentText(getRarityCode(item.get("tier").getAsString()) + item.get("item_name").getAsString() + " - " + EnumChatFormatting.YELLOW + coolFormat(item.get("price_per_tier").getAsDouble(), 0) + EnumChatFormatting.RESET + " - " + EnumChatFormatting.GREEN + coolFormat(item.get("starting_bid").getAsDouble(),0)).setChatStyle(new ChatStyle().setChatClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/viewauction " + item.get("uuid").getAsString()) {
						public Action getAction() {
							return Action.RUN_COMMAND;
						}
					}).setChatHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ChatComponentText(getRarityCode(item.get("tier").getAsString()) + item.get("item_name").getAsString() + "\n" + item.get("item_lore").getAsString()))));
					mc.thePlayer.addChatMessage(msg);
				}
			} else {
				for (int i = 0; i < 5; i++) {
					JsonObject item = items.get(i);
					System.out.println(item);
					final IChatComponent msg = new ChatComponentText(getRarityCode(item.get("tier").getAsString()) + item.get("item_name").getAsString() + " - " + EnumChatFormatting.YELLOW + coolFormat(item.get("price_per_tier").getAsDouble(), 0) + EnumChatFormatting.RESET + " - " + EnumChatFormatting.GREEN + coolFormat(item.get("starting_bid").getAsDouble(),0)).setChatStyle(new ChatStyle().setChatClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/viewauction " + item.get("uuid").getAsString()) {
						public Action getAction() {
							return Action.RUN_COMMAND;
						}
					}).setChatHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ChatComponentText(getRarityCode(item.get("tier").getAsString()) + item.get("item_name").getAsString() + "\n" + item.get("item_lore").getAsString()))));
					mc.thePlayer.addChatMessage(msg);
				}
			}
		}
	}

	public void addChatMessage(String string) {
		mc.thePlayer.addChatMessage(new ChatComponentText(string));
	}

	private static String trimString(String str) {
		StringBuilder trimmed = new StringBuilder();
		boolean leadingWhitespace = true;

		for (int i = 0; i < str.length(); i++) {
			char c = str.charAt(i);

			if (leadingWhitespace) {
				if (!Character.isWhitespace(c)) {
					leadingWhitespace = false;
					trimmed.append(c);
				}
			} else {
				trimmed.append(c);
			}
		}

		return trimmed.toString();
	}

	public String example() {
		return EnumChatFormatting.GRAY + " " + EnumChatFormatting.ITALIC;
	}
}