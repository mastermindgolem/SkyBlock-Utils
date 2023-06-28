package Help;

import Help.Type.Help;
import Help.Type.Mage;
import Help.Type.Warrior;
import logger.Logger;

import java.util.List;

/**
 * Write to mc chat
 */
public class HelpInvocation {
	public static void displayHelp() {
		System.out.println();
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
	}

	public static void addHelp() {
		// Get current help
		HelpManager helpManager = HelpCache.getHelpManager(Warrior.class.getName());
		// Add the strings to the help class
		helpManager.addHelpStrings();
		helpManager = HelpCache.getHelpManager(Mage.class.getName(), new Mage());
		helpManager.addHelpStrings();
	}
}
