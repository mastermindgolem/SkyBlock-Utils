package com.golem.skyblockutils.init;

import com.golem.skyblockutils.models.Overlay.ImageOverlay.FlareOverlay;
import com.golem.skyblockutils.models.Overlay.TextOverlay.DungeonOverlay;
import com.golem.skyblockutils.models.Overlay.TextOverlay.DungeonSkillOverlay;
import com.golem.skyblockutils.models.Overlay.TextOverlay.KuudraTestOverlay;
import com.golem.skyblockutils.models.Overlay.TextOverlay.KuudraTestSkillOverlay;
import com.golem.skyblockutils.models.gui.GuiElement;

import java.util.Arrays;
import java.util.List;

public class GuiInit {
	public static List<GuiElement> getOverlayLoaded() {
		return OverlayLoaded;
	}

	private static final List<GuiElement> OverlayLoaded = Arrays.asList(
		KuudraTestOverlay.element,
		KuudraTestSkillOverlay.element
	);

	public static List<GuiElement> get2ndOverlayLoaded() { return Overlay2Loaded; }

	private static final List<GuiElement> Overlay2Loaded = Arrays.asList(
		DungeonOverlay.element,
		DungeonSkillOverlay.element
	);

	public static List<GuiElement> getFlareOverlayList() {
		return FlareOverlayList;
	}

	private static final List<GuiElement> FlareOverlayList = Arrays.asList(
		FlareOverlay.element
	);
}
