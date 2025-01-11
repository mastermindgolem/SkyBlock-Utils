package com.golem.skyblockutils.command.commands;

import com.golem.skyblockutils.models.AttributeItem;
import com.golem.skyblockutils.models.AttributeItemType;
import com.golem.skyblockutils.models.AuctionAttributeItem;
import com.golem.skyblockutils.utils.AttributeUtils;
import com.golem.skyblockutils.utils.ChatUtils;
import com.golem.skyblockutils.utils.ToolTipListener;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.event.ClickEvent;
import net.minecraft.event.HoverEvent;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import static com.golem.skyblockutils.Main.coolFormat;
import static com.golem.skyblockutils.Main.mc;
import static com.golem.skyblockutils.models.AttributePrice.AttributePrices;
import static com.golem.skyblockutils.utils.Colors.getRarityCode;

public class UpgradeCommand extends CommandBase implements Help {
	private final List<String> helpStrings;
	private final DecimalFormat formatter = new DecimalFormat("#,###,###,###");

	public UpgradeCommand() {
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
			EnumChatFormatting.BLUE + "====================Upgrade guide!====================",
			EnumChatFormatting.RESET + "\n",
			example() + "                      Checks pricing on gear!                  ",
			EnumChatFormatting.RESET + "\n",
			EnumChatFormatting.RESET + "\n",
			EnumChatFormatting.AQUA + "/au attribute level_desired",
			EnumChatFormatting.RESET + "\n",
			EnumChatFormatting.AQUA + "/au attribute current_level level_desired ",
			EnumChatFormatting.RESET + "\n",
			EnumChatFormatting.RESET + "\n",
			EnumChatFormatting.GOLD +
				"  ──── Steps! ────  \n" +
				" Hold the armour you want! \n" +
				" run /au attribute level     \n" +
				" e.g. /au veteran 8           \n" +
				EnumChatFormatting.GOLD +
				" ───────────── " +
			EnumChatFormatting.RESET + "\n",
			EnumChatFormatting.RESET + "\n",
			EnumChatFormatting.DARK_RED + " " +
			EnumChatFormatting.ITALIC + "If you hold an armour piece,",
			EnumChatFormatting.RESET + "\n",
			EnumChatFormatting.DARK_RED + " " +
			EnumChatFormatting.ITALIC + "it'll assume you want to upgrade that piece",
			EnumChatFormatting.RESET + "\n",
			EnumChatFormatting.RESET + "\n",
			example() + "More content to be added...",
			EnumChatFormatting.RESET + "\n",
			EnumChatFormatting.BLUE + " ======================================================== "
		);
	}

	@Override
	public String getHelpMessage() {
		return
			EnumChatFormatting.GRAY + "▶ " +
			EnumChatFormatting.GOLD + "/attribute" +
			EnumChatFormatting.BOLD + "upgrade " +
			EnumChatFormatting.GOLD + "attribute " +
			EnumChatFormatting.AQUA + "start_tier " +
			EnumChatFormatting.GOLD + "end_tier " +
			example() +	"(Aliases: /au /upgrade)" +
			EnumChatFormatting.RESET + "\n";
	}

	@Override
	public void addHelpString() {
		helpStrings.add(getHelpMessage());
	}

	@Override
	public String getCommandName() {
		return "attributeupgrade";
	}

	@Override
	public List<String> getCommandAliases() {
		List<String> al = new ArrayList<>();
		al.add("au");
		al.add("upgrade");
		return al;
	}

	@Override
	public String getCommandUsage(ICommandSender sender) {
		return "/attributeupgrade help for more information";
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

		if (args.length == 2) {
			String attribute = AttributeUtils.AttributeAliases(args[0]);
			int end_tier = Integer.parseInt(args[1]);
			ItemStack heldItem = mc.thePlayer.getHeldItem();

			NBTTagCompound itemNbt = heldItem.serializeNBT().getCompoundTag("tag").getCompoundTag("ExtraAttributes");

			AttributeItem item = new AttributeItem(heldItem.getDisplayName(), "", itemNbt);

			int start_tier = item.attributes.getOrDefault(attribute, 0);

			int tiers_needed = (1 << (end_tier - 1)) - (1 << (start_tier - 1));
			ChatUtils.addChatMessage(EnumChatFormatting.AQUA + "Upgrading " + start_tier + " to " + end_tier + " total tiers " + tiers_needed + "!");
			String id = getItemId(itemNbt);
			AttributeItemType itemType = AttributeUtils.getItemType(id);
			if (itemType == null) {
				addClickChatMessage("[Shard]", "/au SHARD " + args[0] + " " + args[1]);
				addClickChatMessage("[Helmet]", "/au HELMET " + args[0] + " " + args[1]);
				addClickChatMessage("[Chestplate]", "/au CHESTPLATE " + args[0] + " " + args[1]);
				addClickChatMessage("[Leggings]", "/au LEGGINGS " + args[0] + " " + args[1]);
				addClickChatMessage("[Boots]", "/au BOOTS " + args[0] + " " + args[1]);
				addClickChatMessage("[Molten Belt]", "/au MOLTEN_BELT " + args[0] + " " + args[1]);
				addClickChatMessage("[Molten Bracelet]", "/au MOLTEN_BRACELET " + args[0] + " " + args[1]);
				addClickChatMessage("[Molten Cloak]", "/au MOLTEN_CLOAK " + args[0] + " " + args[1]);
				addClickChatMessage("[Molten Necklace]", "/au MOLTEN_NECKLACE " + args[0] + " " + args[1]);
				addClickChatMessage("[Gauntlet of Contagion]", "/au GAUNTLET_OF_CONTAGION " + args[0] + " " + args[1]);
				addClickChatMessage("[Lava Shell Necklace]", "/au LAVA_SHELL_NECKLACE " + args[0] + " " + args[1]);
				addClickChatMessage("[Implosion Belt]", "/au IMPLOSION_BELT " + args[0] + " " + args[1]);
				return;
			}
			getAttributePrice(attribute, itemType, tiers_needed, end_tier);
		}
		if (args.length == 3) {
			AttributeItemType itemType = AttributeUtils.getItemType(args[0]);
			String attribute = AttributeUtils.AttributeAliases(args[1]);
			int end_tier = Integer.parseInt(args[2]);
			int tiers_needed = 1 << (end_tier - 1);
			getAttributePrice(attribute, itemType, tiers_needed, end_tier);
		}
	}

	public String example() {
		return EnumChatFormatting.GRAY + " " + EnumChatFormatting.ITALIC;
	}

	public String getItemId(NBTTagCompound extraAttributes) {
		try {
			String itemId = extraAttributes.getString("id");
			itemId = itemId.split(":")[0];
			return itemId;
		} catch (Exception ignored) {
			return "";
		}
	}

	public void getAttributePrice(String attribute, AttributeItemType key, int tiers_needed, int end_tier) {
		if (!AttributePrices.get(key).containsKey(attribute)) return;
		ArrayList<AuctionAttributeItem> items = AttributePrices.get(key).get(attribute);
		if (key != AttributeItemType.Shard) {
			ArrayList<AuctionAttributeItem> items2 = AttributePrices.get(AttributeItemType.Shard).get(attribute);
			if (items2 != null) items.addAll(items2);
		}
		if (items == null) return;

		items.sort(Comparator.comparingDouble((AuctionAttributeItem o) -> o.attributeInfo.get(attribute).price_per));

		int tiers_achieved = 0;
		long price = 0;

		List<AuctionAttributeItem> auctions = new ArrayList<>();
		for (AuctionAttributeItem item : items) {
			int tiers = item.attributeInfo.get(attribute).tier;
			auctions.add(item);
			tiers_achieved += 1 << (tiers - 1);
			if (tiers_achieved > tiers_needed) break;
		}

		List<AuctionAttributeItem> chosen = new ArrayList<>();
		auctions.sort(Comparator.comparingDouble((AuctionAttributeItem o) -> -o.attributeInfo.get(attribute).price_per));

		for (AuctionAttributeItem item : auctions) {
			int tiers = 1 << (item.attributeInfo.get(attribute).tier - 1);

			if (tiers_achieved - tiers >= tiers_needed) {
				tiers_achieved -= tiers;
			} else chosen.add(item);

		}


		for (AuctionAttributeItem item : chosen) {
			int tiers = item.attributeInfo.get(attribute).tier;
			ChatUtils.addChatMessage(
					getRarityCode(item.tier) + ToolTipListener.TitleCase(item.item_name) + EnumChatFormatting.YELLOW + ": " + attribute + " " + tiers + " - " + EnumChatFormatting.GREEN + coolFormat(item.price, 0),
					new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/viewauction " + item.viewauctionID)
			);
			price += item.price;
		}
		ChatUtils.addChatMessage(EnumChatFormatting.AQUA + ToolTipListener.TitleCase("Upgrading " + attribute + " to " + end_tier + " on " + key));
		ChatUtils.addChatMessage(EnumChatFormatting.YELLOW + String.valueOf(tiers_achieved) + "/" + tiers_needed + " tiers");
		ChatUtils.addChatMessage(EnumChatFormatting.GREEN + "Total Price: " + formatter.format(price));
	}

	private void addClickChatMessage(String message, String command) {
		ChatUtils.addChatMessage(message, new ClickEvent(ClickEvent.Action.RUN_COMMAND, command));
	}
}
