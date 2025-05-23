package com.golem.skyblockutils.init;

import com.golem.skyblockutils.models.Overlay.TextOverlay.*;
import com.golem.skyblockutils.models.gui.GuiElement;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;

public class GuiInit {

	@Getter
	private static final List<GuiElement> OverlayLoaded = Arrays.asList(AlignOverlay.element, RagnarokOverlay.element, CratesOverlay.element, AlertOverlay.element, ReaperOverlay.element, SplitsOverlay.element, DamageOverlay.element, FatalTempoOverlay.element, ProfitOverlay.element, ContainerOverlay.element, EndstoneOverlay.element, TimerOverlay.element);
}
