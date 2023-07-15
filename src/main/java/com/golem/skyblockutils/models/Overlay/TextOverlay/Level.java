package com.golem.skyblockutils.models.Overlay.TextOverlay;

public enum Level {
	LEVEL_0(10),
	LEVEL_1(20),
	LEVEL_2(30),
	LEVEL_3(40),
	LEVEL_4(50),
	LEVEL_5(60);

	private final int height;

	Level(int height) {
		this.height = height;
	}

	public int getHeight() {
		return height;
	}
}
