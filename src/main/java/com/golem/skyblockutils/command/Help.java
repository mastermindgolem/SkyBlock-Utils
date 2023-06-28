package com.golem.skyblockutils.command;

import logger.Logger;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.event.HoverEvent;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;

import java.util.ArrayList;
import java.util.List;

import static com.golem.skyblockutils.Main.mc;

public class Help extends CommandBase {

	public Help() {}

	@Override
	public String getCommandName() {
		return "shelp";
	}

	@Override
	public List<String> getCommandAliases() {
		List<String> al = new ArrayList<>();
		al.add("khelp");
		al.add("kuudra");
		al.add("kuudere");
		return al;
	}

	@Override
	public String getCommandUsage(ICommandSender sender) {
		return "/sbu help for more information";
	}

	@Override
	public int getRequiredPermissionLevel() {
		return 0;
	}

	@Override
	public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
		return null;
	}

	/**
	 * Logic for sending chat
	 * @param sender
	 * @param args
	 */
	@Override
	public void processCommand(ICommandSender sender, String[] args) {
		/**
		 * Adding a specific chat that's not added to the caching system so it's ordered.
		 */
		mc.thePlayer.addChatMessage(new ChatComponentText(chat()).setChatStyle(new ChatStyle().setChatHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ChatComponentText(hoverChat())))));
		HelpInvocation.sendAll();
		Logger.debug("Sending help message!");
	}

	public static String getChat() {
		return chat();
	}
	private static String chat() {
		return
				EnumChatFormatting.GRAY + "▶ " +
				EnumChatFormatting.GOLD + "/" +
				EnumChatFormatting.BOLD + "help" +
				EnumChatFormatting.GOLD + " for this menu" +
				EnumChatFormatting.RESET + "\n" +
				EnumChatFormatting.GRAY + "Hover over the commands for a guide." +
				EnumChatFormatting.RESET + "\n";
	}

	public static String getHoverChat() {
		return hoverChat();
	}
	/**
	 * @return Hover strings for Help Menu
	 */
	private static String hoverChat() {
		return
				EnumChatFormatting.BLUE + "===================Help menu!===================" +
				EnumChatFormatting.RESET + "\n" +
				EnumChatFormatting.YELLOW + "/help for an overview" +
				EnumChatFormatting.RESET + "\n" +
				EnumChatFormatting.YELLOW + "/command* for a help overview of a specific command" +
				EnumChatFormatting.RESET + "\n" +
				EnumChatFormatting.YELLOW + "/help command* for a help overview of a specific command" +
				EnumChatFormatting.RESET + "\n" +
				EnumChatFormatting.YELLOW + "*command here is finicky and is based on the word after \"/\" and aliases does not count (yet)" +
				EnumChatFormatting.RESET + "\n" +
				EnumChatFormatting.RESET + "\n" +
				EnumChatFormatting.AQUA + "Text colored in aqua are optional arguments!" +
				EnumChatFormatting.RESET + "\n" +
				EnumChatFormatting.GOLD + "[] are optional arguments and are typically gold in color (override the below statement)." +
				EnumChatFormatting.RESET + "\n" +
				EnumChatFormatting.GOLD + "Text colored in gold are required for command to work!" +
				EnumChatFormatting.RESET + "\n" +
				EnumChatFormatting.GRAY + "Text colored in gray are typically not important and just side notes or descriptions!" +
				EnumChatFormatting.RESET + "\n" +
				EnumChatFormatting.RESET + "\n" +
				EnumChatFormatting.RESET + "\n" +
				EnumChatFormatting.GOLD + "Any text after the first space of \"/command\" are arguments!" +
				EnumChatFormatting.RESET + "\n" +
				EnumChatFormatting.GOLD + "Command here could be attributeprice, equipmentprice, ks (alias)" +
				EnumChatFormatting.RESET + "\n" +
				EnumChatFormatting.GOLD + "Arguments including but not limited to attribute, level, tier, ign, ..., etc." +
				EnumChatFormatting.RESET + "\n" +
				EnumChatFormatting.RESET + "\n" +
				EnumChatFormatting.RED + "Legends are just mini guide on how placeholder works :p" +
				EnumChatFormatting.RESET + "\n" +
				EnumChatFormatting.BLACK + "The color hierarchy is in order! See how Gold is optional!"
						;
	}
}
