package com.golem.skyblockutils.command.commands;

import com.golem.skyblockutils.utils.AttributeUtils;
import com.golem.skyblockutils.utils.ToolTipListener;
import com.google.gson.JsonObject;
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
import net.minecraft.util.IChatComponent;

import java.text.DecimalFormat;
import java.util.*;

import static com.golem.skyblockutils.Main.coolFormat;
import static com.golem.skyblockutils.Main.mc;
import static com.golem.skyblockutils.models.AttributePrice.AttributePrices;
import static com.golem.skyblockutils.models.AttributePrice.all_kuudra_categories;
import static com.golem.skyblockutils.utils.Colors.getRarityCode;

public class UpgradeCommand extends CommandBase implements Help {
	private final List<String> helpStrings;
	private final String[] item_types = new String[]{"AURORA", "CRIMSON", "TERROR", "HOLLOW", "FERVOR", "ATTRIBUTE_SHARD"};
	private final String[] armor_types = new String[]{"HELMET", "CHESTPLATE", "LEGGINGS", "BOOTS"};
	private int level;

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
			int start_tier = 0;
			try {
				start_tier = itemNbt.getCompoundTag("attributes").getInteger(attribute);
			} catch (Exception ignored) {
			}

			int tiers_needed = (int) Math.ceil(Math.pow(2, end_tier - 1) - Math.pow(2, start_tier - 1));
			String id = getItemId(itemNbt);
			String armor_type = null;
			for (String key : all_kuudra_categories) {
				if (id.contains(key)) armor_type = key;
			}
			if (armor_type == null) {
				addClickChatMessage("[SHARD]", "/au SHARD " + args[0] + " " + args[1]);
				addClickChatMessage("[HELMET]", "/au HELMET " + args[0] + " " + args[1]);
				addClickChatMessage("[CHESTPLATE]", "/au CHESTPLATE " + args[0] + " " + args[1]);
				addClickChatMessage("[LEGGINGS]", "/au LEGGINGS " + args[0] + " " + args[1]);
				addClickChatMessage("[BOOTS]", "/au BOOTS " + args[0] + " " + args[1]);
				addClickChatMessage("[MOLTEN_BELT]", "/au MOLTEN_BELT " + args[0] + " " + args[1]);
				addClickChatMessage("[MOLTEN_BRACELET]", "/au MOLTEN_BRACELET " + args[0] + " " + args[1]);
				addClickChatMessage("[MOLTEN_CLOAK]", "/au MOLTEN_CLOAK " + args[0] + " " + args[1]);
				addClickChatMessage("[MOLTEN_NECKLACE]", "/au MOLTEN_NECKLACE " + args[0] + " " + args[1]);
				addClickChatMessage("[GAUNTLET_OF_CONTAGION]", "/au GAUNTLET_OF_CONTAGION " + args[0] + " " + args[1]);
				addClickChatMessage("[IMPLOSION_BELT]", "/au IMPLOSION_BELT " + args[0] + " " + args[1]);
				return;
			}
			getAttributePrice(attribute, armor_type, tiers_needed, end_tier);
		}
		if (args.length == 3) {
			String armor_type = args[0];
			String attribute = AttributeUtils.AttributeAliases(args[1]);
			int end_tier = Integer.parseInt(args[2]);
			int start_tier = 0;
			int tiers_needed = (int) Math.ceil(Math.pow(2, end_tier - 1) - Math.pow(2, start_tier - 1));
			getAttributePrice(attribute, armor_type, tiers_needed, end_tier);
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

	public void getAttributePrice(String attribute, String key, int tiers_needed, int end_tier) {
		if (!AttributePrices.get(key).containsKey(attribute)) return;
		System.out.println("Passed guard clause");
		ArrayList<JsonObject> items = AttributePrices.get(key).get(attribute);
		if (!Objects.equals(key, "SHARD")) {
			ArrayList<JsonObject> items2 = AttributePrices.get("SHARD").get(attribute);
			if (items2 != null) items.addAll(items2);
		}
		if (items == null) return;

		items.sort(Comparator.comparingDouble((JsonObject o) -> o.get("price_per_tier").getAsDouble()));

		int tiers_achieved = 0;
		int price = 0;

		List<JsonObject> auctions = new ArrayList<>();
		for (JsonObject item : items) {
			int tiers = item.get(attribute).getAsInt();
			auctions.add(item);
			tiers_achieved += Math.pow(2, tiers - 1);
			if (tiers_achieved > tiers_needed) break;
		}

		List<JsonObject> chosen = new ArrayList<>();

		for (JsonObject item : auctions) {
			items.sort(Comparator.comparingDouble((JsonObject o) -> -o.get("price_per_tier").getAsDouble()));
			int tiers = (int) Math.pow(2, item.get(attribute).getAsInt() - 1);

			if (tiers_achieved - tiers >= tiers_needed) {
				tiers_achieved -= tiers;
			} else chosen.add(item);

		}


		for (JsonObject item : chosen) {
			int tiers = item.get(attribute).getAsInt();
			final IChatComponent msg = new ChatComponentText(getRarityCode(item.get("tier").getAsString()) + ToolTipListener.TitleCase(item.get("item_name").getAsString()) + EnumChatFormatting.YELLOW + ": " + attribute + " " + tiers + " - " + EnumChatFormatting.GREEN + coolFormat(item.get("starting_bid").getAsDouble(), 0)).setChatStyle(new ChatStyle().setChatClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/viewauction " + item.get("uuid").getAsString()) {
				public Action getAction() {
					return Action.RUN_COMMAND;
				}
			}).setChatHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ChatComponentText(getRarityCode(item.get("tier").getAsString()) + item.get("item_name").getAsString() + "\n" + item.get("item_lore").getAsString()))));
			mc.thePlayer.addChatMessage(msg);
			price += item.get("starting_bid").getAsInt();
		}
		addChatMessage(EnumChatFormatting.AQUA + ToolTipListener.TitleCase("Upgrading " + attribute + " to " + end_tier + " on " + key));
		mc.thePlayer.addChatMessage(new ChatComponentText(EnumChatFormatting.YELLOW + String.valueOf(tiers_achieved) + "/" + tiers_needed + " tiers"));
		DecimalFormat formatter = new DecimalFormat("#,###");
		mc.thePlayer.addChatMessage(new ChatComponentText(EnumChatFormatting.GREEN + "Total Price: " + formatter.format(price)));
	}

	private void addClickChatMessage(String message, String command) {
		mc.thePlayer.addChatMessage(new ChatComponentText(message).setChatStyle(new ChatStyle().setChatClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, command) {
			@Override
			public Action getAction() {
				//custom behavior
				return Action.RUN_COMMAND;
			}
		})));
	}

	public void addChatMessage(String string) {
		mc.thePlayer.addChatMessage(new ChatComponentText(string));
	}
}
