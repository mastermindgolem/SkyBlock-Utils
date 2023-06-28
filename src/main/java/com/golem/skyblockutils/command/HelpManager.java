package com.golem.skyblockutils.command;

import com.golem.skyblockutils.command.commands.Help;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Get the strings
 */
public class HelpManager {
	private Help help;

	public HelpManager(Help help) {
		this.help = help;
	}

	public void addHelpStrings() {
		help.addHelpString();
	}

	public List<String> getHelpStrings() {
		return help.getHelpStrings();
	}

	public List<String> getHelpStrings(int index) {
		List<String> helpStrings = help.getHelpStrings();
		if (index < 0 || index >= helpStrings.size()) {
			return new ArrayList<>();
		}
		return Arrays.asList(helpStrings.get(index));
	}

	public List<String> getHelpStrings(int start, int end) {
		List<String> helpStrings = help.getHelpStrings();
		if (start < 0 || start >= helpStrings.size() || end < 0 || end > helpStrings.size() || start > end) {
			return new ArrayList<>();
		}
		return helpStrings.subList(start, end);
	}

	public Help getHelp() {
		return this.help;
	}
}
