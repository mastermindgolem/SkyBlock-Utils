package com.golem.skyblockutils.command.commands;

import com.golem.skyblockutils.features.KuudraFight.Kuudra;
import com.golem.skyblockutils.models.*;
import com.golem.skyblockutils.utils.AttributeUtils;
import com.golem.skyblockutils.utils.ChatUtils;
import com.golem.skyblockutils.utils.ToolTipListener;
import com.google.gson.JsonObject;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.event.ClickEvent;
import net.minecraft.event.HoverEvent;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;

import java.util.*;
import java.util.stream.Collectors;

import static com.golem.skyblockutils.Main.*;
import static com.golem.skyblockutils.models.AttributeItemType.*;
import static com.golem.skyblockutils.models.AttributePrice.AttributePrices;
import static com.golem.skyblockutils.utils.AuctionHouse.CheckIfAuctionsSearched;
import static com.golem.skyblockutils.utils.Colors.getRarityCode;

public class AttributeCommand extends CommandBase implements Help {
	private int level;

	private final List<String> helpStrings;

	private final AttributeArmorType[] item_types = AttributeArmorType.values();

	private final AttributeItemType[] armor_types = new AttributeItemType[] {Helmet, Chestplate, Leggings, Boots};

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
			if (Objects.equals(args[0], "info")) {
				AttributeValueResult result = AttributePrice.AttributeValue(mc.thePlayer.getHeldItem(), true);
				if (result != null) {
					Kuudra.addChatMessage(result.toString());
				}
				return;
			}
			attribute1 = AttributeUtils.AttributeAliases(args[0]);

			if (CheckIfAuctionsSearched()) return;

			addChatMessage(EnumChatFormatting.AQUA + "Auctions for " + EnumChatFormatting.BOLD + EnumChatFormatting.DARK_BLUE + attribute1.toUpperCase());
			getAttributePrice(attribute1, new AttributeItemType[]{Shard, Helmet, Chestplate, Leggings, Boots}, 0);

			mc.thePlayer.addChatMessage(new ChatComponentText(EnumChatFormatting.BLUE + "[EQUIPMENT]").setChatStyle(new ChatStyle().setChatClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/equipmentprice " + attribute1) {
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
			if (auctions.size() == 0) {
				final IChatComponent msg = new ChatComponentText(EnumChatFormatting.RED + "Auctions not checked yet. If you have logged in more than 5 minutes ago, contact golem.");
				mc.thePlayer.addChatMessage(msg);
				return;
			}
			if (!combo) {
				if (CheckIfAuctionsSearched()) return;

				addChatMessage(EnumChatFormatting.AQUA + "Auctions for " + EnumChatFormatting.BOLD + "" + EnumChatFormatting.DARK_BLUE + attribute1.toUpperCase() + " " + level);
				getAttributePrice(attribute1, new AttributeItemType[]{Shard, Helmet, Chestplate, Leggings, Boots}, level);

				mc.thePlayer.addChatMessage(new ChatComponentText(EnumChatFormatting.BLUE + "[EQUIPMENT]").setChatStyle(new ChatStyle().setChatClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/equipmentprice " + attribute1 + " " + level) {
				})));
			} else {


				String attribute2 = AttributeUtils.AttributeAliases(args[1]);
				Set<String> attributeSet = new HashSet<>();
				attributeSet.add(attribute1);
				attributeSet.add(attribute2);
				addChatMessage(EnumChatFormatting.AQUA + "Auctions for " + EnumChatFormatting.DARK_BLUE + attribute1.toUpperCase() + " " + attribute2.toUpperCase());
				for (AttributeArmorType key : item_types) {
					addChatMessage(EnumChatFormatting.AQUA + ToolTipListener.TitleCase(key.getDisplay()));
					for (AttributeItemType key2 : armor_types) {
						AuctionAttributeItem item = AttributePrice.getComboValue(key2, key, attributeSet);
						if (item == null) continue;
						ChatUtils.addChatMessage(ToolTipListener.TitleCase(key2.getDisplay()) + ": " + getRarityCode(item.tier) + item.item_name + " - " + EnumChatFormatting.GREEN + coolFormat(item.price, 0), new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/viewauction " + item.viewauctionID), getRarityCode(item.tier) + item.item_lore);
					}
				}
				mc.thePlayer.addChatMessage(new ChatComponentText(EnumChatFormatting.BLUE + "[EQUIPMENT]").setChatStyle(new ChatStyle().setChatClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/equipmentprice " + attribute1 + " " + attribute2) {
				})));
			}
		}

	}



	public void getAttributePrice(String attribute, AttributeItemType[] keys, int level) {
		for (AttributeItemType key : keys) {
			if (!AttributePrices.get(key).containsKey(attribute)) continue;
			ArrayList<AuctionAttributeItem> items = AttributePrices.get(key).get(attribute);
			if (items == null) continue;
			if (level == 0) {
				items = items.stream()
					.filter(item -> item.attributes.get(attribute) >= (key.equals(Shard) ? configFile.minShardTier : configFile.minArmorTier))
					.collect(Collectors.toCollection(ArrayList::new));
			} else {
				items = items.stream()
					.filter(item -> item.attributes.get(attribute) == level)
					.collect(Collectors.toCollection(ArrayList::new));
			}


			items.sort(Comparator.comparingDouble((AuctionAttributeItem o) -> o.attributeInfo.get(attribute).price_per));
			addChatMessage(EnumChatFormatting.AQUA + ToolTipListener.TitleCase(key.getDisplay()));
			if (items.size() < 5) {
				for (AuctionAttributeItem item: items) {
					ChatUtils.addChatMessage(getRarityCode(item.tier) + item.item_name + " - " + EnumChatFormatting.YELLOW + coolFormat(item.attributeInfo.get(attribute).price_per, 0) + EnumChatFormatting.RESET + " - " + EnumChatFormatting.GREEN + coolFormat(item.price,0), new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/viewauction " + item.viewauctionID), getRarityCode(item.tier) + item.item_lore);
				}
			} else {
				for (int i = 0; i < 5; i++) {
					AuctionAttributeItem item = items.get(i);
					ChatUtils.addChatMessage(getRarityCode(item.tier) + item.item_name + " - " + EnumChatFormatting.YELLOW + coolFormat(item.attributeInfo.get(attribute).price_per, 0) + EnumChatFormatting.RESET + " - " + EnumChatFormatting.GREEN + coolFormat(item.price,0), new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/viewauction " + item.viewauctionID), getRarityCode(item.tier) + item.item_lore);

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