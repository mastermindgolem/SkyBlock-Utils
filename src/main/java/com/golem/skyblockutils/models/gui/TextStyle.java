package com.golem.skyblockutils.models.gui;

public enum TextStyle {
	Default,
	Shadow,
	Outline;

	public static TextStyle fromInt(int number) {
		switch (number) {
			case 0:
				return Default;
			case 1:
				return Shadow;
			case 2:
				return Outline;
			default:
				return null;
		}
	}
}
