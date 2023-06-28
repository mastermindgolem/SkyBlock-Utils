package com.golem.skyblockutils.command;

import com.golem.skyblockutils.command.commands.Help;
import logger.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HelpCache {
	private static final Map<String, Help> cache = new HashMap<>();
	private static final Map<String, List<String>> helpMap = new HashMap<>();

	private static void addHelpString(String helpString, List<String> hoverString) {
		helpMap.put(helpString, hoverString);
	}

	public static Map<String, List<String>> getHelpMap() {
		return new HashMap<>(helpMap);
	}

	public static Map<String, Help> getCache() {
		return new HashMap<>(cache);
	}

	public static void addStrings() {
			for (Help help : cache.values()) {
				addHelpString(help.getHelpStrings().toString(), help.getHoverStrings());
			}
	}
	public static void addHelpProvider(String className, Help helpProvider) {
		int end = className.lastIndexOf(".");
		String cleanedTypeName = className.substring(end+1);
		Logger.info(	"Added " + cleanedTypeName);
		cache.put(cleanedTypeName, helpProvider);
	}

	public static HelpManager getHelpManager(String className) {
		int end = className.lastIndexOf(".");
		String cleanedTypeName = className.substring(end+1);
		Help helpProvider = cache.get(cleanedTypeName);
		if (helpProvider == null) {
			throw new IllegalArgumentException("Help provider not found for class: " + className);
		}
		return new HelpManager(helpProvider);
	}

	public static HelpManager getHelpManager(String className, Help clazz) {
		int end = className.lastIndexOf(".");
		String cleanedTypeName = className.substring(end + 1);
		Help helpProvider = cache.get(cleanedTypeName);
		if (helpProvider == null) {
			HelpCache.addHelpProvider(cleanedTypeName, clazz);
			helpProvider = cache.get(cleanedTypeName);
		}
		return new HelpManager(helpProvider);
	}

	public static List<String> getAllHelpStrings() {
		List<String> allHelpStrings = new ArrayList<>();
		for (Help help : cache.values()) {
//			Remember to add titles!
			allHelpStrings.addAll(help.getHelpStrings());
			allHelpStrings.add("");
		}
		return allHelpStrings;
	}
}
