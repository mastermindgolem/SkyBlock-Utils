package com.golem.skyblockutils.command;

import logger.Logger;

import java.util.ArrayList;
import java.util.List;

public class CommandName {
	public static String cmdNameEquivalent(String key) {
		switch (key) {
			case "AttributeCommand":
				return ("attributeprice");
			case "EquipmentCommand":
				return ("equipmentprice");
			case "VanquishedCommand":
				return ("vanquishedprice");
			case "StatCommand":
				return "kuudrastats";
			case "UpgradeCommand":
				return "attributeupgrade";
			case "Alias":
				return "alias";
			default:
				Logger.error(key);
				throw new RuntimeException("Unable to parse key");
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
