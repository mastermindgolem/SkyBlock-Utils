package com.golem.skyblockutils.command;

import com.golem.skyblockutils.command.commands.Alias;
import com.golem.skyblockutils.command.commands.AttributeCommand;
import com.golem.skyblockutils.command.commands.EquipmentCommand;
import com.golem.skyblockutils.command.commands.Help;
import com.golem.skyblockutils.utils.ChatUtils;
import logger.Logger;
import net.minecraft.util.EnumChatFormatting;

import java.util.List;
import java.util.Map;

/**
 * Write to mc chat
 */
public class HelpInvocation {
	public static void displayHelp() {
		Logger.info();
		List<String> allHelpStrings = HelpCache.getAllHelpStrings();
		for (String helpString : allHelpStrings) {
			Logger.info(helpString);
		}
	}

	public static void displayHelp(Help help) {
		List<String> helpStrings = help.getHelpStrings();
		for (String helpString : helpStrings) {
			Logger.info(helpString);
		}
	}

	public static void displayHelp(Help help, int indices) {
		HelpManager helpStrings = HelpCache.getHelpManager(help.getClass().getName());
		Logger.info(helpStrings.getHelpStrings(indices));
	}

	public static void displayHelp(Help help, int start, int end) {
		HelpManager helpStrings = HelpCache.getHelpManager(help.getClass().getName());
		Logger.info(helpStrings.getHelpStrings(start, end));
		//"Help.Type.Warrior Help.Type.Help String", "Help.Type.Warrior can slash.", "Help.Type.Warrior can eat"
		// 0, 1, 2
		// 1, 2, 3
		// Visuallization ^
	}

	public static void addHelp() {
		HelpManager helpManager = HelpCache.getHelpManager(AttributeCommand.class.getName());
		helpManager.addHelpStrings();
		helpManager = HelpCache.getHelpManager(EquipmentCommand.class.getName(), new EquipmentCommand());
		helpManager.addHelpStrings();
		helpManager = HelpCache.getHelpManager(Alias.class.getName(), new Alias());
		helpManager.addHelpStrings();
		HelpCache.getHelpManager("StatCommand").addHelpStrings();
		HelpCache.getHelpManager("UpgradeCommand").addHelpStrings();
	}

	public static void sendAll() {
		Map<String, List<String>> allHelpStrings = HelpCache.getHelpMap();
		for (Map.Entry<String, List<String>> entry : allHelpStrings.entrySet()) {
			String helpString = entry.getKey();
			List<String> hoverString = entry.getValue();


			StringBuilder sb = new StringBuilder();
			for (String str : hoverString) {
				sb.append(str);
			}
			String hover = sb.toString();

			ChatUtils.addChatMessage(helpString.replace("[", "").replace("]", ""), hover);
		}
	}



	public static void aliasSendAll() {
		Map<String, Help> cache = HelpCache.getCache();
		for (Map.Entry<String, Help> entry : cache.entrySet()) {
			String key = entry.getKey();
			String cn = CommandName.cmdNameEquivalent(key);
			ChatUtils.addChatMessage("\n" + EnumChatFormatting.BOLD + EnumChatFormatting.AQUA + cn);

			Help help = entry.getValue();
			List<String> alias = help.getCommandAliases();

			for (String s : alias) {
				ChatUtils.addChatMessage(EnumChatFormatting.DARK_GREEN + "✵" + s + EnumChatFormatting.RESET);
			}

		}
			ChatUtils.addChatMessage("\n");
	}

}