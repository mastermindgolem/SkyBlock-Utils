package com.golem.skyblockutils.command.commands;

import com.golem.skyblockutils.Main;
import com.golem.skyblockutils.features.AttributePriceGui;
import com.golem.skyblockutils.features.ChestDataGui;
import com.golem.skyblockutils.features.KuudraFight.Kuudra;
import com.golem.skyblockutils.models.*;
import com.golem.skyblockutils.utils.AttributeUtils;
import com.golem.skyblockutils.utils.ChatUtils;
import com.golem.skyblockutils.utils.ToolTipListener;
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
import static com.golem.skyblockutils.utils.ChatUtils.messagesSent;
import static com.golem.skyblockutils.utils.Colors.getRarityCode;

public class AttributeCommand extends CommandBase implements Help {
	private static final String AUCTIONS_HEADER = EnumChatFormatting.AQUA + "Auctions for ";
	private int level;
	private final List<String> helpStrings;

	private final AttributeArmorType[] item_types = AttributeArmorType.values();

	private final AttributeItemType[] armor_types = new AttributeItemType[] {Helmet, Chestplate, Leggings, Boots};

	public AttributeCommand() {
		helpStrings = new ArrayList<>();
	}


	private final Integer firstMessageID = "sbu".hashCode();

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
		if (args.length == 0) {
			Main.display = new AttributePriceGui();
			return;
//			StringBuilder sb = new StringBuilder();
//			for (String str : getHoverStrings()) {
//				sb.append(str);
//			}
//			String hover = sb.toString();
//			Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText(getHelpMessage()).setChatStyle(new ChatStyle().setChatHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ChatComponentText(hover)))));
		}

		String attribute1;

		for (int i = 0; i < messagesSent; ++i) mc.ingameGUI.getChatGUI().deleteChatLine(firstMessageID + i);
		messagesSent = 0;

		if (args.length == 1) {
			if (Objects.equals(args[0], "info")) {
				AttributeValueResult result = AttributePrice.AttributeValue(mc.thePlayer.getHeldItem(), true);
				if (result != null) {
					ChatUtils.addChatMessage(result.toString());
				}
				return;
			}
			attribute1 = AttributeUtils.AttributeAliases(args[0]);

			if (CheckIfAuctionsSearched()) return;

			ChatUtils.addChatMessage(AUCTIONS_HEADER + EnumChatFormatting.BOLD + EnumChatFormatting.DARK_BLUE + attribute1.toUpperCase(), firstMessageID + messagesSent++);
			getAttributePrice(attribute1, new AttributeItemType[]{Shard, Helmet, Chestplate, Leggings, Boots}, 0);
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
				ChatUtils.addChatMessage(EnumChatFormatting.RED + "Auctions not checked yet.", firstMessageID + messagesSent++);
				return;
			}
			if (!combo) {
				if (CheckIfAuctionsSearched()) return;

				ChatUtils.addChatMessage(AUCTIONS_HEADER + EnumChatFormatting.BOLD + "" + EnumChatFormatting.DARK_BLUE + attribute1.toUpperCase() + " " + level, firstMessageID + messagesSent++);
				getAttributePrice(attribute1, new AttributeItemType[]{Shard, Helmet, Chestplate, Leggings, Boots}, level);

			} else {
				String attribute2 = AttributeUtils.AttributeAliases(args[1]);
				Set<String> attributeSet = new HashSet<>();
				attributeSet.add(attribute1);
				attributeSet.add(attribute2);
				ChatUtils.addChatMessage(AUCTIONS_HEADER + EnumChatFormatting.DARK_BLUE + attribute1.toUpperCase() + " " + attribute2.toUpperCase(), firstMessageID + messagesSent++);
				for (AttributeArmorType key : item_types) {
					ChatUtils.addChatMessage(EnumChatFormatting.AQUA + ToolTipListener.TitleCase(key.getDisplay()), firstMessageID + messagesSent++);
					for (AttributeItemType key2 : armor_types) {
						AuctionAttributeItem item = AttributePrice.getComboItem(key2, key, attributeSet);
						if (item == null) continue;
						ChatUtils.addChatMessage(ToolTipListener.TitleCase(key2.getDisplay()) + ": " + getRarityCode(item.tier) + item.item_name + " - " + EnumChatFormatting.GREEN + coolFormat(item.price, 0), new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/viewauction " + item.viewauctionID), getRarityCode(item.tier) + item.item_lore, firstMessageID + messagesSent++);
					}
				}
			}
		}

		mc.ingameGUI.getChatGUI().printChatMessageWithOptionalDeletion(
				new ChatComponentText(EnumChatFormatting.GREEN + "[Armor] ").setChatStyle(new ChatStyle().setChatClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/attributeprice " + String.join(" ", args)))).appendSibling(
				new ChatComponentText(EnumChatFormatting.YELLOW + " [Molten Eq] ").setChatStyle(new ChatStyle().setChatClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/moltenprice " + String.join(" ", args))))).appendSibling(
				new ChatComponentText(EnumChatFormatting.YELLOW + " [Other Eq]").setChatStyle(new ChatStyle().setChatClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/equipmentprice " + String.join(" ", args)))))
		, firstMessageID + messagesSent++);

	}


	public void getAttributePrice(String attribute, AttributeItemType[] keys, int level) {
		for (AttributeItemType key : keys) {
			if (!AttributePrices.get(key).containsKey(attribute)) continue;
			ArrayList<AuctionAttributeItem> items = AttributePrices.get(key).get(attribute);
			if (items == null) continue;
			if (level == 0) {
				items.removeIf(item -> item.attributes.get(attribute) < (key.equals(Shard) ? configFile.minShardTier : configFile.minArmorTier));
			} else {
				items.removeIf(item -> item.attributes.get(attribute) != level);
			}


			items.sort(Comparator.comparingDouble((AuctionAttributeItem o) -> o.attributeInfo.get(attribute).price_per));
			ChatUtils.addChatMessage(EnumChatFormatting.AQUA + ToolTipListener.TitleCase(key.getDisplay()), firstMessageID + messagesSent++);
			if (items.size() < 5) {
				for (AuctionAttributeItem item: items) {
					ChatUtils.addChatMessage(getRarityCode(item.tier) + item.item_name + " - " + EnumChatFormatting.YELLOW + coolFormat(item.attributeInfo.get(attribute).price_per, 0) + EnumChatFormatting.RESET + " - " + EnumChatFormatting.GREEN + coolFormat(item.price,0), new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/viewauction " + item.viewauctionID), getRarityCode(item.tier) + item.item_lore, firstMessageID + messagesSent++);
				}
			} else {
				for (int i = 0; i < 5; i++) {
					AuctionAttributeItem item = items.get(i);
					ChatUtils.addChatMessage(getRarityCode(item.tier) + item.item_name + " - " + EnumChatFormatting.YELLOW + coolFormat(item.attributeInfo.get(attribute).price_per, 0) + EnumChatFormatting.RESET + " - " + EnumChatFormatting.GREEN + coolFormat(item.price,0), new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/viewauction " + item.viewauctionID), getRarityCode(item.tier) + item.item_lore, firstMessageID + messagesSent++);
				}
			}
		}
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