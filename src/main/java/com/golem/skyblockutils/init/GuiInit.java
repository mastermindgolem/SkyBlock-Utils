package com.golem.skyblockutils.init;

import com.golem.skyblockutils.models.Overlay.TextOverlay.*;
import com.golem.skyblockutils.models.gui.GuiElement;

import java.util.Arrays;
import java.util.List;

public class GuiInit {
	public static List<GuiElement> getOverlayLoaded() {
		return OverlayLoaded;
	}

	private static final List<GuiElement> OverlayLoaded = Arrays.asList(
		AlignOverlay.element, RagnarokOverlay.element, CratesOverlay.element, ChampionOverlay.element, AlertOverlay.element, ReaperOverlay.element, SplitsOverlay.element, FishingOverlay.element
	);
}
