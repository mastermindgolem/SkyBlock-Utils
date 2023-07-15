package com.golem.skyblockutils.models.Overlay.TextOverlay;

import com.golem.skyblockutils.models.gui.*;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import static com.golem.skyblockutils.Main.configFile;
import static com.golem.skyblockutils.Main.mc;

public class KuudraTestOverlay {
	public static GuiElement element = new GuiElement("Kuudra Overlay", 50, 20);

	public static int renderWidth(String text) {
		return mc.fontRendererObj.getStringWidth(text);
	}

	@SubscribeEvent
	public void onRenderOverlay(RenderGameOverlayEvent event) {
		if (event.type != RenderGameOverlayEvent.ElementType.TEXT) return;

		TextStyle textStyle = TextStyle.fromInt(1);

		String tpsValue = "NYA";
		String pingValue = "GWA?";
		if (configFile.testGui) {
			GlStateManager.pushMatrix();
			GlStateManager.translate(element.position.getX(), element.position.getY(), 500.0);
			GlStateManager.scale(element.position.getScale(), element.position.getScale(), 1.0);


			OverlayUtils.drawString(0, 0, "§bIDK:", textStyle, Alignment.Left);
			OverlayUtils.drawString(0, 10, "§bSOMETHING KUUDRA:", textStyle, Alignment.Left);
			OverlayUtils.drawString(30 + renderWidth("§bSOMETHING KUUDRA:"), 0, pingValue, textStyle, Alignment.Left);
			OverlayUtils.drawString(30 + renderWidth("§bSOMETHING KUUDRA:"), 10, tpsValue, textStyle, Alignment.Left);

			GlStateManager.popMatrix();
		} else if (mc.currentScreen instanceof MoveGui) {
			GlStateManager.pushMatrix();
			GlStateManager.translate(element.position.getX(), element.position.getY(), 500.0);
			GlStateManager.scale(element.position.getScale(), element.position.getScale(), 1.0);

			OverlayUtils.drawString(0, 0, "§bIDK:", textStyle, Alignment.Left);
			OverlayUtils.drawString(0, 10, "§bSOMETHING KUUDRA:", textStyle, Alignment.Left);
			OverlayUtils.drawString(30 + renderWidth("§bSOMETHING KUUDRA:"), 0, pingValue, textStyle, Alignment.Left);
			OverlayUtils.drawString(30 + renderWidth("§bSOMETHING KUUDRA:"), 10, tpsValue, textStyle, Alignment.Left);

			GlStateManager.popMatrix();
		}
	}
}
