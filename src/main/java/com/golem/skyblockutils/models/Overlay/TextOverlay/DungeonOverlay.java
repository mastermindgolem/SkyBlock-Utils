package com.golem.skyblockutils.models.Overlay.TextOverlay;

import com.golem.skyblockutils.models.Overlay.Overlay;
import com.golem.skyblockutils.models.gui.*;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import static com.golem.skyblockutils.Main.configFile;
import static com.golem.skyblockutils.Main.mc;

public class DungeonOverlay implements Overlay {
	private static final String catacombs = "master_mode_catacombs";
	public static int height = Level.LEVEL_3.getHeight();
	public static GuiElement element = new GuiElement("Dungeon Overlay", new DungeonOverlay().renderWidth(), height);

	public int renderWidth() {

		return (int) (catacombs.length()*7.5);
	}

	@Override
	public int renderWidth(String text) {
		return mc.fontRendererObj.getStringWidth(text);
	}

	@Override
	@SubscribeEvent
	public void onRenderOverlay(RenderGameOverlayEvent event) {
		if (event.type != RenderGameOverlayEvent.ElementType.TEXT) return;

		TextStyle textStyle = TextStyle.fromInt(1);

		if (configFile.testGui) {
			GlStateManager.pushMatrix();
			GlStateManager.translate(element.position.getX(),element.position.getY(), 500.0);
			GlStateManager.scale(element.position.getScale(), element.position.getScale(), 1.0);

			String tpsValue = "7";
			String debugValue = "Rawr~";
			int valueWidth = Math.max(renderWidth(debugValue), Math.max(renderWidth(catacombs), renderWidth(tpsValue)));

			OverlayUtils.drawString(0, 0, "§bFloor: ", textStyle, Alignment.Left);
			OverlayUtils.drawString(0, 10, "§bType: ", textStyle, Alignment.Left);
			OverlayUtils.drawString(0, 20, "§bNecron says: ", textStyle, Alignment.Left);
			OverlayUtils.drawString(35 + valueWidth, 0, catacombs, textStyle, Alignment.Right);
			OverlayUtils.drawString(35 + valueWidth, 10, tpsValue, textStyle, Alignment.Right);
			OverlayUtils.drawString(35 + valueWidth, 20, debugValue, textStyle, Alignment.Right);

			GlStateManager.popMatrix();
		} else if (mc.currentScreen instanceof MoveGui) {
			defaultMoveOverlayText();
		}
	}

	@Override
	public void defaultMoveOverlayText() {
		TextStyle textStyle = TextStyle.fromInt(1);
		GlStateManager.pushMatrix();
		GlStateManager.translate(element.position.getX(),element.position.getY(), 500.0);
		GlStateManager.scale(element.position.getScale(), element.position.getScale(), 1.0);

		String tpsValue = "7";
		String debugValue = "Rawr~";
		int valueWidth = Math.max(renderWidth(debugValue), Math.max(renderWidth(catacombs), renderWidth(tpsValue)));

		OverlayUtils.drawString(0, 0, "§bFloor: ", textStyle, Alignment.Left);
		OverlayUtils.drawString(0, 10, "§bType: ", textStyle, Alignment.Left);
		OverlayUtils.drawString(0, 20, "§bNecron says: ", textStyle, Alignment.Left);
		OverlayUtils.drawString(35 + valueWidth, 0, catacombs, textStyle, Alignment.Right);
		OverlayUtils.drawString(35 + valueWidth, 10, tpsValue, textStyle, Alignment.Right);
		OverlayUtils.drawString(35 + valueWidth, 20, debugValue, textStyle, Alignment.Right);

		GlStateManager.popMatrix();
	}
}
