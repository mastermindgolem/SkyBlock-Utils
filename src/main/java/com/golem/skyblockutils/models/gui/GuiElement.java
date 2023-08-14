package com.golem.skyblockutils.models.gui;


import com.golem.skyblockutils.Main;
import com.golem.skyblockutils.PersistentData;
import gg.essential.universal.UResolution;
import logger.Logger;
import net.minecraft.client.renderer.GlStateManager;

import java.awt.Color;

import static com.golem.skyblockutils.Main.StaticPosition;

public class GuiElement {
	public String getName() {
		return name;
	}

	private final String name;
	private int width;
	private int height;
	public GuiPosition position;
	private static final double padding = 0.05;

	public GuiElement(String name, int width, int height) {
		this.name = name;
		this.width = width;
		this.height = height;
		this.position = PersistentData.positions.get(name);

		if (position == null) {
			Logger.info("Position entered?");
			double rndWidth = Math.random() * UResolution.getScaledWidth();
			double rndHeight = Math.random() * UResolution.getScaledHeight();
			Logger.debug(rndWidth, " ", rndHeight);
			Logger.debug(this.name);
			position = new GuiPosition(rndWidth, rndHeight, 1.0);
			PersistentData.positions.put(name, position);
			Main.persistentData.savePositions();

		}

	}

	public boolean isInsideElement(double mouseX, double mouseY) {
		double renderWidth = width * position.getScale();
		double renderHeight = height * position.getScale();
		boolean xInside = mouseX >= (position.getX() - padding * renderWidth) && mouseX <= (position.getX() + renderWidth * (1 + padding));
		boolean yInside = mouseY >= (position.getY() - padding * renderHeight) && mouseY <= (position.getY() + renderHeight * (1 + padding));
		return xInside && yInside;
	}

	public void coerceIntoScreen() {
		double xRangeStart = 0.0;
		double xRangeEnd = UResolution.getScaledWidth() - width * position.getScale();
		double yRangeStart = 0.0;
		double yRangeEnd = UResolution.getScaledHeight() - height * position.getScale();
		double x = position.getX();
		double y = position.getY();
		double coercedX = (xRangeEnd >= xRangeStart) ? Math.min(Math.max(x, xRangeStart), xRangeEnd) : 0.0;
		double coercedY = (yRangeEnd >= yRangeStart) ? Math.min(Math.max(y, yRangeStart), yRangeEnd) : 0.0;
		position.setX(coercedX);
		position.setY(coercedY);
	}

	public void setWidth(int w) {width = w;}

	public void setHeight(int h) {height = h;}

	public void draw(double mouseX, double mouseY) {
		GlStateManager.pushMatrix();
		double renderWidth = width * position.getScale();
		double renderHeight = height * position.getScale();
		GlStateManager.translate(position.getX() - padding * renderWidth, position.getY() - padding * renderHeight, 400.0);
		Color color = new Color(255, 255, 255);
		try {
			color = (StaticPosition.stream().filter(element -> element.isInsideElement(mouseX, mouseY)).findFirst().orElse(null) == this) ?
					new Color(255, 255, 255, 128) : new Color(128, 128, 128, 128);
		} catch (Exception ignored) {}
		OverlayUtils.renderRect(0.0, 0.0, renderWidth, renderHeight, color);
		GlStateManager.popMatrix();
	}


}