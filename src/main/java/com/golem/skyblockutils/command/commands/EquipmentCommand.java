package com.golem.skyblockutils.command.commands;

import com.golem.skyblockutils.models.AttributePrice;
import com.golem.skyblockutils.utils.AttributeUtils;
import com.golem.skyblockutils.utils.AuctionHouse;
import com.golem.skyblockutils.utils.RequestUtil;
import com.golem.skyblockutils.utils.ToolTipListener;
import com.google.gson.JsonObject;
import logger.Logger;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.event.ClickEvent;
import net.minecraft.event.HoverEvent;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static com.golem.skyblockutils.Main.*;
import static com.golem.skyblockutils.models.AttributePrice.AttributePrices;
import static com.golem.skyblockutils.utils.AuctionHouse.CheckIfAuctionsSearched;
import static com.golem.skyblockutils.utils.Colors.getRarityCode;

public class EquipmentCommand extends CommandBase implements Help {
	private int level;

	private final List<String> helpStrings;

	private final String[] item_types = new String[] {"MOLTEN_BELT", "MOLTEN_BRACELET", "MOLTEN_NECKLACE", "MOLTEN_CLOAK", "IMPLOSION_BELT", "GAUNTLET_OF_CONTAGION"};

	public EquipmentCommand() {
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
	public List<String> getHelpStrings() {
		return helpStrings;
	}

	@Override
	public List<String> getHoverStrings() {
		return Arrays.asList(
			EnumChatFormatting.BLUE + " ===================Equipment help menu!=================== ",
			EnumChatFormatting.RESET + "\n",
			example() + "                  Attribute price but for equipment                  ",
			EnumChatFormatting.RESET + "\n",
			EnumChatFormatting.GOLD + "/equipmentprice help" +
			EnumChatFormatting.GRAY + " ⬅ Shows this message.",
			EnumChatFormatting.RESET + "\n",
			EnumChatFormatting.RESET + "\n",
			EnumChatFormatting.GRAY + " " + EnumChatFormatting.STRIKETHROUGH + "/equipmentprice ah W.I.P.",
			EnumChatFormatting.RESET + "\n",
			EnumChatFormatting.RESET + "\n",
			EnumChatFormatting.GOLD + "/equipmentprice attribute [level]" +
			EnumChatFormatting.GRAY  + " ⬅ Shows best price for attribute at any level, unless [level] is specified",
			EnumChatFormatting.RESET + "\n",
			example() + "E.g. /equipmentprice veteran 1",
			EnumChatFormatting.RESET + "\n",
			EnumChatFormatting.RESET + "\n",
			EnumChatFormatting.GOLD + "/equipmentprice attribute1 attribute2" +
			EnumChatFormatting.GRAY + " ⬅ Shows cheapest equipment of specified combo",
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
			EnumChatFormatting.GOLD + "/" +
			EnumChatFormatting.BOLD + "equipment" +
			EnumChatFormatting.RESET +
			EnumChatFormatting.GOLD + "price attribute " +
			EnumChatFormatting.AQUA +	"level" +
			example() +	"(Aliases: /ep)" +
			EnumChatFormatting.RESET + "\n";
	}

	@Override
	public void addHelpString() {
		helpStrings.add(getHelpMessage());
	}

	@Override
	public String getCommandName() {
		return "equipmentprice";
	}

	@Override
	public List<String> getCommandAliases() {
		List<String> al = new ArrayList<>();
		al.add("ep");
		return al;
	}

	@Override
	public String getCommandUsage(ICommandSender sender) {
		return "/equipmentprice help for more information";
	}

	@Override
	public void processCommand(ICommandSender sender, String[] args) {
	//		TODO:	Separate commands later! !!! <
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
			getAttributePrice(attribute1, item_types, 0);

			mc.thePlayer.addChatMessage(new ChatComponentText(EnumChatFormatting.BLUE + "[ARMOR]").setChatStyle(new ChatStyle().setChatClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/equipmentprice " + attribute1) {
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
			Logger.debug("Checking " + auctions.size() + " auctions");
			if (auctions.size() == 0) {
				final IChatComponent msg = new ChatComponentText(EnumChatFormatting.RED + "Auctions not checked yet. If you have logged in more than 5 minutes ago, contact golem.");
				mc.thePlayer.addChatMessage(msg);
				return;
			}
			if (!combo) {
				if (CheckIfAuctionsSearched()) return;

				addChatMessage(EnumChatFormatting.AQUA + "Auctions for " + EnumChatFormatting.BOLD + EnumChatFormatting.DARK_BLUE + attribute1.toUpperCase() + " " + level);
				getAttributePrice(attribute1, item_types, level);

				mc.thePlayer.addChatMessage(new ChatComponentText(EnumChatFormatting.BLUE + "[ARMOR]").setChatStyle(new ChatStyle().setChatClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/equipmentprice " + attribute1 + " " + level) {
					@Override
					public Action getAction() {
						return Action.RUN_COMMAND;
					}
				})));
			} else {
				String attribute2 = AttributeUtils.AttributeAliases(args[1]);
				addChatMessage(EnumChatFormatting.AQUA + "Auctions for " + EnumChatFormatting.DARK_BLUE + attribute1.toUpperCase() + " " + attribute2.toUpperCase());
				for (String key: item_types) {
					JsonObject item = AttributePrice.getComboValue(key, new ArrayList<>(Arrays.asList(attribute1,attribute2)));
					if (item == null) continue;
					Logger.debug(item);
					final IChatComponent msg = new ChatComponentText(getRarityCode(item.get("tier").getAsString()) + item.get("item_name").getAsString() + " - " + EnumChatFormatting.GREEN + coolFormat(item.get("starting_bid").getAsDouble(), 0)).setChatStyle(new ChatStyle().setChatClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/viewauction " + item.get("uuid").getAsString()) {
						public Action getAction() {
							return Action.RUN_COMMAND;
						}
					}).setChatHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ChatComponentText(getRarityCode(item.get("tier").getAsString()) + item.get("item_name").getAsString() + "\n" + item.get("item_lore").getAsString()))));
					mc.thePlayer.addChatMessage(msg);
				}
				mc.thePlayer.addChatMessage(new ChatComponentText(EnumChatFormatting.BLUE + "[ARMOR]").setChatStyle(new ChatStyle().setChatClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/equipmentprice " + attribute1 + " " + attribute2) {
					@Override
					public Action getAction() {
						return Action.RUN_COMMAND;
					}
				})));
			}
		}
	}

	public String example() {
		return EnumChatFormatting.GRAY + " " + EnumChatFormatting.ITALIC;
	}



	public void getAttributePrice(String attribute, String[] keys, int level) {
		for (String key: keys) {
			if (!AttributePrices.get(key).containsKey(attribute)) continue;
			ArrayList<JsonObject> items = AttributePrices.get(key).get(attribute);
			if (items == null) continue;
			if (level == 0) {
				items = items.stream()
						.filter(item -> item.get(attribute).getAsInt() >= configFile.min_tier)
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
					Logger.debug(item);
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
}