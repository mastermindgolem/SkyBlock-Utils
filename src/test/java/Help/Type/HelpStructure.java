package Help.Type;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A command
 */
public class HelpStructure implements Help {
	private final List<String> helpStrings;

	public HelpStructure() {
		helpStrings = new ArrayList<>();
	}

	@Override
	public Help getHelp() {
		return this;
	}

	/**
	 *
	 */
	@Override
	public void addHelpString() {
		helpStrings.add("Help");
		helpStrings.add("?");
	}

	@Override
	public List<String> getHelpStrings() {
		return helpStrings;
	}

	public void addHelpString(String[] helpString) {
		helpStrings.addAll(Arrays.asList(helpString));
	}

	// Other methods and functionalities of the Help.Type.HelpStructure class
}
