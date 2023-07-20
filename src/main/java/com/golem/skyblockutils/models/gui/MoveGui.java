package com.golem.skyblockutils.models.gui;

import com.golem.skyblockutils.PersistentData;
import logger.Logger;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import gg.essential.universal.UResolution;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

import java.awt.*;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

import static com.golem.skyblockutils.Main.persistentData;
import static com.golem.skyblockutils.init.GuiInit.*;

public class MoveGui extends GuiScreen {
	private GuiElement currentElement = null;
	private double clickOffsetX = 0.0;
	private double clickOffsetY = 0.0;
	private GuiElement Element = null;

	public MoveGui(GuiElement element) {
		this.Element = element;
	}

	@Override
	public void drawScreen(int x, int y, float partialTicks) {
		super.drawScreen(x, y, partialTicks);
		double mouseX, mouseY;
		double[] mouseCoordinates = getMouseCoordinates();
		mouseX = mouseCoordinates[0];
		mouseY = mouseCoordinates[1];

		OverlayUtils.renderRect(0.0, 0.0, UResolution.getScaledWidth(), UResolution.getScaledHeight(), new Color(0, 0, 0, 64));
		GlStateManager.pushMatrix();
		GlStateManager.translate(UResolution.getScaledWidth() / 2.0, UResolution.getScaledHeight() / 2.0, 300.0);
		OverlayUtils.drawString(0, 10, "Drag to move GUI elements.", TextStyle.Outline, Alignment.Center);
		OverlayUtils.drawString(0, 20, "Scroll inside elements to scale.", TextStyle.Outline, Alignment.Center);
		GlStateManager.popMatrix();
		this.Element.draw(mouseX, mouseY);
	}

	@Override
	public void mouseClicked(int x, int y, int mouseButton) throws IOException {
		double mouseX, mouseY;
		double[] mouseCoordinates = getMouseCoordinates();
		mouseX = mouseCoordinates[0];
		mouseY = mouseCoordinates[1];

		currentElement = (Element.isInsideElement(mouseX, mouseY) ? Element : null);

		super.mouseClicked(x, y, mouseButton);
	}

	@Override
	public void mouseClickMove(int x, int y, int clickedMouseButton, long timeSinceLastClick) {
		if (currentElement != null) {
			double mouseX, mouseY;
			double[] mouseCoordinates = getMouseCoordinates();
			mouseX = mouseCoordinates[0];
			mouseY = mouseCoordinates[1];

			currentElement.position.setX(mouseX - clickOffsetX);
			currentElement.position.setY(mouseY - clickOffsetY);
			currentElement.coerceIntoScreen();
		}

		super.mouseClickMove(x, y, clickedMouseButton, timeSinceLastClick);
	}

	/**
	 * Scrolling stuff
	 *
	 * @throws IOException
	 */
	@Override
	public void handleMouseInput() throws IOException {
		super.handleMouseInput();
		double mouseX, mouseY;
		double[] mouseCoordinates = getMouseCoordinates();
		mouseX = mouseCoordinates[0];
		mouseY = mouseCoordinates[1];

		double dScroll = (Integer.signum(Mouse.getEventDWheel()) * 0.1);
		if (dScroll == 0) {
			return;
		}

		if (Element.isInsideElement(mouseX, mouseY)) {
			currentElement = Element;
			clickOffsetX = mouseX - Element.position.getX();
			clickOffsetY = mouseY - Element.position.getY();

			double oldScale = Element.position.getScale();
			double newScale = Math.max(oldScale + dScroll, 0.1);

			Element.position.setX(mouseX + ((newScale / oldScale) * (Element.position.getX() - mouseX)));
			Element.position.setY(mouseY + ((newScale / oldScale) * (Element.position.getY() - mouseY)));
			Element.position.setScale(newScale);

			Element.coerceIntoScreen();
		}
	}

	private double[] getMouseCoordinates() {
		double mouseX = Mouse.getX() / UResolution.getScaleFactor();
		double mouseY = (Display.getHeight() - Mouse.getY()) / UResolution.getScaleFactor();
		return new double[] { mouseX, mouseY };
	}

	@Override
	public void onGuiClosed() {
		PersistentData.positions.put(Element.getName(), Element.position);
		persistentData.save();
	}

}
