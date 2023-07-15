package com.golem.skyblockutils.models.Overlay.TextOverlay;

import com.golem.skyblockutils.models.gui.*;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import static com.golem.skyblockutils.Main.configFile;
import static com.golem.skyblockutils.Main.mc;

public class DungeonSkillOverlay {
	public static GuiElement element = new GuiElement("Dungeon Skills Overlay", 50, 20);

	public static int renderWidth(String text) {
		return mc.fontRendererObj.getStringWidth(text);
	}

	@SubscribeEvent
	public void onRenderOverlay(RenderGameOverlayEvent event) {
		if (event.type != RenderGameOverlayEvent.ElementType.TEXT) return;

		TextStyle textStyle = TextStyle.fromInt(1);

		if (configFile.testGui) {
			GlStateManager.pushMatrix();
			GlStateManager.translate(element.position.getX(), element.position.getY(), 500.0);
			GlStateManager.scale(element.position.getScale(), element.position.getScale(), 1.0);

			int debugValue = 51;
			int valueWidth = Math.max(renderWidth(String.valueOf(debugValue)), renderWidth(String.valueOf(debugValue)));

			OverlayUtils.drawString(0, 0, "§bDungeons", textStyle, Alignment.Left);
			OverlayUtils.drawString(0, 10, "§b=======", textStyle, Alignment.Left);
			OverlayUtils.drawString(0, 20, "§bCatacomb level: ", textStyle, Alignment.Left);

			OverlayUtils.drawString(30 + valueWidth, 20, String.valueOf(debugValue), textStyle, Alignment.Left);

			GlStateManager.popMatrix();
		} else if (mc.currentScreen instanceof MoveGui) {
			GlStateManager.pushMatrix();
			GlStateManager.translate(element.position.getX(), element.position.getY(), 500.0);
			GlStateManager.scale(element.position.getScale(), element.position.getScale(), 1.0);

			int debugValue = 51;

			OverlayUtils.drawString(0, 0, "§bDungeons", textStyle, Alignment.Left);
			OverlayUtils.drawString(0, 10, "§b=======", textStyle, Alignment.Left);
			OverlayUtils.drawString(0, 20, "§bCatacomb level: ", textStyle, Alignment.Left);
			OverlayUtils.drawString(30 + renderWidth("§bCatacomb level: "), 20, String.valueOf(debugValue), textStyle, Alignment.Left);

			GlStateManager.popMatrix();
		}
	}


}
