package com.golem.skyblockutils.command;

import logger.Logger;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.event.HoverEvent;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;

import java.util.ArrayList;
import java.util.List;

import static com.golem.skyblockutils.Main.mc;
import static net.minecraft.util.EnumChatFormatting.*;

public class Help extends CommandBase {
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
				GRAY + "▶ " +
				GOLD + "/" +
				BOLD + "help" +
				GOLD + " for this menu" +
				RESET + "\n" +
				GRAY + "Hover over the commands for a guide." +
				RESET + "\n";
	}

	public static String getHoverChat() {
		return hoverChat();
	}
	/**
	 * @return Hover strings for Help Menu
	 */
	private static String hoverChat() {
		return
				BLUE + "===================Help menu!===================" +
				RESET + "\n" +
				YELLOW + "/help for an overview" +
				RESET + "\n" +
				YELLOW + "/command* for a help overview of a specific command" +
				RESET + "\n" +
				YELLOW + "/help command* for a help overview of a specific command" +
				RESET + "\n" +
				YELLOW + "*command here is finicky and is based on the word after \"/\" and aliases does not count (yet)" +
				RESET + "\n" +
				RESET + "\n" +
				AQUA + "Text colored in aqua are optional arguments!" +
				RESET + "\n" +
				GOLD + "[] are optional arguments and are typically gold in color (override the below statement)." +
				RESET + "\n" +
				GOLD + "Text colored in gold are required for command to work!" +
				RESET + "\n" +
				GRAY + "Text colored in gray are typically not important and just side notes or descriptions!" +
				RESET + "\n" +
				RESET + "\n" +
				RESET + "\n" +
				GOLD + "Any text after the first space of \"/command\" are arguments!" +
				RESET + "\n" +
				GOLD + "Command here could be attributeprice, equipmentprice, ks (alias)" +
				RESET + "\n" +
				GOLD + "Arguments including but not limited to attribute, level, tier, ign, ..., etc." +
				RESET + "\n" +
				RESET + "\n" +
				RED + "Legends are just mini guide on how placeholder works :p" +
				RESET + "\n" +
				BLACK + "The color hierarchy is in order! See how Gold is optional!";
	}
}
