package CommandName;

import java.util.ArrayList;
import java.util.List;

public class CN {
	public static String cmdNameEquivalent(String key) {
		switch (key) {
			case "AttributeCommand":
				return ("attributeprice");
			case "EquipmentCommand":
				return ("equipmentprice");
			case "StatCommand":
				return "kuudrastats";
			case "UpgradeCommand":
				return "attributeupgrade";
			default:
				return "ERROROROROROR";
		}
	}
	public static List<String> cmdNameEquivalent(String ...key) {
		List<String> addKey = new ArrayList<>();
		for (String s : key) {
			switch (s) {
				case "AttributeCommand":
					addKey.add("attributeprice");
					break;
				case "EquipmentCommand":
					addKey.add("equipmentprice");
					break;
				case "StatCommand":
					addKey.add("kuudrastats");
					break;
				case "UpgradeCommand":
					addKey.add("attributeupgrade");
					break;
				default:
					addKey.add("ERROROROROROR");
					break;
			}
		}
		return addKey;
	}
}
