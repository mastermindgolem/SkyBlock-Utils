package com.golem.skyblockutils.models.Overlay;

import com.golem.skyblockutils.models.gui.GuiElement;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.event.entity.living.LivingEvent;

public interface Overlay {

	int renderWidth(String text);

	void onRenderOverlay(RenderGameOverlayEvent event);

	void defaultMoveOverlayText();

//	void onRenderOverlay(RenderGameOverlayEvent event, LivingEvent.LivingUpdateEvent e);
}