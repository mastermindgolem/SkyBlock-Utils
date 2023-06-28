package com.golem.skyblockutils.utils;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class AttributeUtils {
	private static List<String> all_attributes = Arrays.asList(new String[]{"vitality", "arachno", "attack_speed", "combo", "elite", "ignition", "lifeline", "breeze", "speed", "experience", "mana_pool", "life_regeneration", "blazing_resistance", "arachno_resistance", "undead_resistance", "blazing_fortune", "fishing_experience", "double_hook", "infection", "trophy_hunter", "fisherman", "hunter", "fishing_speed", "life_recovery", "midas_touch", "mana_regeneration", "veteran", "mending", "ender_resistance", "dominance", "mana_steal", "ender", "blazing", "undead", "warrior", "deadeye", "fortitude", "magic_find"});

	public static String AttributeAliases (String input) {
		if (all_attributes.contains(input)) return input;
		for (String attribute : all_attributes) {
			if (attribute.startsWith(input)) return (attribute.equals("vitality") ? "mending" : attribute);
			if (Objects.equals(Arrays.stream(attribute.split("_")).map(s -> s.substring(0, 1)).collect(Collectors.joining()), input)) return (attribute.equals("vitality") ? "mending" : attribute);
		}
		return input;
	}
}
