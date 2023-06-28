package com.golem.skyblockutils.command.commands;

import java.util.List;

public interface Help {
	List<String> getHelpStrings();

	String getHelpMessage();

	List<String> getHoverStrings();

	List<String> getCommandAliases();

	Help getHelp();

	void addHelpString();
}
