package com.golem.skyblockutils.models.gui;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;

import java.awt.*;
import java.util.regex.Pattern;

import static com.golem.skyblockutils.Main.mc;
import static org.lwjgl.opengl.GL11.GL_QUADS;

public class OverlayUtils {

	static Pattern removeColorCodesPattern = Pattern.compile("§[0-9a-f]");

	public void renderDurabilityBar(int x, int y, Double percentFilled) {
		double percent = Math.max(0.0, Math.min(percentFilled, 1.0));
		//percent guard clause
		if (percent == 0.0) return;

		int barWidth = (int) Math.round(percentFilled * 13.0);
		int barColorIndex = (int) Math.round(percentFilled * 255.0);

		GlStateManager.disableLighting();
		GlStateManager.disableDepth();
		GlStateManager.disableTexture2D();
		GlStateManager.disableAlpha();
		GlStateManager.disableBlend();
		Tessellator tessellator = Tessellator.getInstance();
		WorldRenderer worldRenderer = tessellator.getWorldRenderer();
		draw(worldRenderer, x + 2, y + 13, 13, 2, 0, 0, 0, 255);
		draw(worldRenderer, x + 2, y + 13, 12, 1, (255 - barColorIndex) / 4, 64, 0, 255);
		draw(worldRenderer, x + 2, y + 13, barWidth, 1, 255 - barColorIndex, barColorIndex, 0, 255);

		GlStateManager.enableAlpha();
		GlStateManager.enableTexture2D();
		GlStateManager.enableLighting();
		GlStateManager.enableDepth();
		}

	private void draw(
		WorldRenderer renderer, int x, int y, int width, int height, int red, int green, int blue, int alpha
	) {
		renderer.begin(7, DefaultVertexFormats.POSITION_COLOR);
		renderer.pos((double)(x), (double)(y), 0.0).color(red, green, blue, alpha).endVertex();
		renderer.pos((double)(x), (double)(y + height), 0.0).color(red, green, blue, alpha).endVertex();
		renderer.pos((double)(x + width), (double)(y + height), 0.0).color(red, green, blue, alpha).endVertex();
		renderer.pos((double)(x + width), (double)(y), 0.0).color(red, green, blue, alpha).endVertex();
		Tessellator.getInstance().draw();

	}

	public static void drawString(int x, int y, String str, TextStyle textStyle, Alignment alignment) {
		String text = "§r" + str;
		int startX;

		switch (alignment) {
			case Center:
				startX = x - mc.fontRendererObj.getStringWidth(str) / 2;
				break;
			case Right:
				startX = x - mc.fontRendererObj.getStringWidth(str);
				break;
			default:
				startX = x;
				break;
		}

		switch (textStyle) {
			case Default:
				mc.fontRendererObj.drawString(text, startX, y, -1, false);
				break;
			case Shadow:
				mc.fontRendererObj.drawString(text, startX, y, -1, true);
				break;
			case Outline:
				String rawString = text.replaceAll(removeColorCodesPattern.pattern(), "");
				mc.fontRendererObj.drawString(rawString, startX - 1, y, -16777216);
				mc.fontRendererObj.drawString(rawString, startX + 1, y, -16777216);
				mc.fontRendererObj.drawString(rawString, startX, y - 1, -16777216);
				mc.fontRendererObj.drawString(rawString, startX, y + 1, -16777216);
				mc.fontRendererObj.drawString(text, startX, y, -1);
				break;
			}
		}


	public static void renderRect(double x, double y, double w, double h, Color color) {
		//Color guard clause
		if (color.getAlpha() == 0) return;

		GlStateManager.enableBlend();
		GlStateManager.disableTexture2D();
		GlStateManager.enableAlpha();
		GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
		GlStateManager.color(color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f, color.getAlpha() / 255f);

		Tessellator tessellator = Tessellator.getInstance();
		WorldRenderer worldRenderer = tessellator.getWorldRenderer();

		worldRenderer.begin(GL_QUADS, DefaultVertexFormats.POSITION);
		worldRenderer.pos(x, y + h, 0.0).endVertex();
		worldRenderer.pos(x + w, y + h, 0.0).endVertex();
		worldRenderer.pos(x + w, y, 0.0).endVertex();
		worldRenderer.pos(x, y, 0.0).endVertex();
		tessellator.draw();

		GlStateManager.disableAlpha();
		GlStateManager.enableTexture2D();
		GlStateManager.disableBlend();
	}

	public void drawTexturedModalRect(int x, int y, int width, int height) {
		Tessellator tessellator = Tessellator.getInstance();
		WorldRenderer worldRenderer = tessellator.getWorldRenderer();

		worldRenderer.begin(GL_QUADS, DefaultVertexFormats.POSITION_TEX);
		worldRenderer.pos((double) x, (double) (y + height), 0.0).tex(0.0, 1.0).endVertex();
		worldRenderer.pos((double) (x + width), (double) (y + height), 0.0).tex(1.0, 1.0).endVertex();
		worldRenderer.pos((double) (x + width), (double) y, 0.0).tex(1.0, 0.0).endVertex();
		worldRenderer.pos((double) x, (double) y, 0.0).tex(0.0, 0.0).endVertex();
		tessellator.draw();
	}
}

