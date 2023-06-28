package com.golem.skyblockutils.command.commands;

import com.golem.skyblockutils.command.HelpInvocation;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.EnumChatFormatting;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
				EnumChatFormatting.GRAY + "▶ " +
				EnumChatFormatting.GOLD + "/alias " +
				EnumChatFormatting.AQUA + "command" +
				EnumChatFormatting.RESET + "\n";
	}

	@Override
	public List<String> getHoverStrings() {
		return Arrays.asList(
			EnumChatFormatting.STRIKETHROUGH +
			String.valueOf(EnumChatFormatting.RED) + "/alias command1 command2",
			EnumChatFormatting.RESET + "\n ",
			EnumChatFormatting.STRIKETHROUGH +
			String.valueOf(EnumChatFormatting.YELLOW) + "That will show all the available aliases for both.",
			EnumChatFormatting.RESET + "\n ",
			EnumChatFormatting.STRIKETHROUGH +
			String.valueOf(EnumChatFormatting.GRAY) + "E.g. /alias attributeprice equipmentprice",
			EnumChatFormatting.RESET + "\n ",
			EnumChatFormatting.BOLD + String.valueOf(EnumChatFormatting.RED) + "W.I.P"
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
