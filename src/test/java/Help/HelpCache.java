package Help;

import Help.Type.Help;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HelpCache {
	private static Map<String, Help> cache = new HashMap<>();

	public static void addHelpProvider(String className, Help helpProvider) {
		String cleanedTypeName = className.replaceAll("^Type\\.", "");
		System.out.println("Added " + cleanedTypeName);
		cache.put(cleanedTypeName, helpProvider);
	}

	public static HelpManager getHelpManager(String className) {
		String cleanedTypeName = className.replaceAll("^Type\\.", "");
		Help helpProvider = cache.get(cleanedTypeName);
		if (helpProvider == null) {
			throw new IllegalArgumentException("Help provider not found for class: " + className);
		}
		return new HelpManager(helpProvider);
	}
	public static HelpManager getHelpManager(String className, Help clazz) {
		String cleanedTypeName = className.replaceAll("^Type\\.", "");
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
