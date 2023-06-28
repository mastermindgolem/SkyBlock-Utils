package Help.Type;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A command
 */
public class Mage implements Help {
		private final List<String> helpStrings;

		public Mage() {
			helpStrings = new ArrayList<>();
		}

		@Override
		public Help getHelp() {
			return this;
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
			helpStrings.add("Mage Help String");
			helpStrings.add("Mage pew pew");
		}
		// Other methods and functionalities of the Help.Type.HelpStructure class
}
