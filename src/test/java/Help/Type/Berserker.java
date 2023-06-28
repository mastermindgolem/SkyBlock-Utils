package Help.Type;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A command
 */
public class Berserker implements Help {
	private final List<String> helpStrings;

	public Berserker() {
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

	@Override
	public void addHelpString() {
		helpStrings.add("Rawr");
		helpStrings.add("RAMPAGE");
	}

	public void addHelpString(String[] helpString) {
		helpStrings.addAll(Arrays.asList(helpString));
	}

	// Other methods and functionalities of the Help.Type.HelpStructure class

}
