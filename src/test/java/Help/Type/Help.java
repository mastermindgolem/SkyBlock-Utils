package Help.Type;

import java.util.List;

public interface Help {
	List<String> getHelpStrings();

//	void addHelpString(String[] helpString);
	Help getHelp(); // New getHelp() method

	void addHelpString();
}
