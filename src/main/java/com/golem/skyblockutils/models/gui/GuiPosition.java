package com.golem.skyblockutils.models.gui;


import com.google.gson.JsonObject;

import java.io.Serializable;

public class GuiPosition implements Serializable {
	private double x;
	private double y;
	private double scale;

	public GuiPosition(double x, double y, double scale) {
		this.x = x;
		this.y = y;
		this.scale = scale;
	}
	public GuiPosition(JsonObject data) {
		this.x = data.get("x").getAsDouble();
		this.y = data.get("y").getAsDouble();
		this.scale = data.get("scale").getAsDouble();
	}

	public int getX() {
		return (int) x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public int getY() {
		return (int) y;
	}

	public void setY(double y) {
		this.y = y;
	}

	public float getScale() {
		return (float) scale;
	}

	public void setScale(double scale) {
		this.scale = scale;
	}
}

