package Help.Type;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A command
 */
public class Warrior implements Help {
		private final List<String> helpStrings;

		public Warrior() {
			helpStrings = new ArrayList<>();
		}

		@Override
		public Help getHelp() {
			return this; // Return the instance of the current help provider
		}

		@Override
		public List<String> getHelpStrings() {
			return helpStrings;
		}

		public void addHelpString(String[] helpString) {
			helpStrings.addAll(Arrays.asList(helpString));
		}

		@Override
		public void addHelpString() {
			helpStrings.add("Warrior Help String");
			helpStrings.add("Warrior can slash.");
			helpStrings.add("Warrior can eat");
		}
		// Other methods and functionalities of the Help.Type.HelpStructure class
}
