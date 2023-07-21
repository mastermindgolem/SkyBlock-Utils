package com.golem.skyblockutils.command.commands;

import com.golem.skyblockutils.command.HelpInvocation;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.EnumChatFormatting;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static net.minecraft.util.EnumChatFormatting.*;

public class Alias extends CommandBase implements Help {
	private final List<String> helpStrings;
	public Alias() {
		helpStrings = new ArrayList<>();
	}

	@Override
	public int getRequiredPermissionLevel() {
		return 0;
	}

	@Override
	public String getCommandName() {
		return "alias";
	}

	@Override
	public String getCommandUsage(ICommandSender sender) {
		return "Returns all of the aliases in this mod(kuudra qol mod)! -Kami";
	}

	@Override
	public List<String> getCommandAliases() {
		return new ArrayList<>();
	}

	@Override
	public void processCommand(ICommandSender sender, String[] args) {
		HelpInvocation.aliasSendAll();
	}

	@Override
	public List<String> getHelpStrings() {
		return helpStrings;
	}

	@Override
	public String getHelpMessage() {
		return
				GRAY + "▶ " +
				GOLD + "/alias " +
				AQUA + "command" +
				RESET + "\n";
	}

	@Override
	public List<String> getHoverStrings() {
		return Arrays.asList(
			STRIKETHROUGH +
			String.valueOf(RED) + "/alias command1 command2",
			RESET + "\n ",
			STRIKETHROUGH +
			String.valueOf(YELLOW) + "That will show all the available aliases for both.",
			RESET + "\n ",
			STRIKETHROUGH +
			String.valueOf(GRAY) + "E.g. /alias attributeprice equipmentprice",
			RESET + "\n ",
			BOLD + String.valueOf(RED) + "W.I.P"
		);
	}

	@Override
	public Help getHelp() {
		return this;
	}

	@Override
	public void addHelpString() {
		helpStrings.add(getHelpMessage());
	}
}
