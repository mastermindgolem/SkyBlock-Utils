package com.golem.skyblockutils.models.gui;


import java.io.Serializable;
import java.util.Objects;

public class GuiPosition implements Serializable {
	private double x;
	private double y;
	private double scale;

	public GuiPosition(double x, double y, double scale) {
		this.x = x;
		this.y = y;
		this.scale = scale;
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

	public double getScale() {
		return scale;
	}

	public void setScale(double scale) {
		this.scale = scale;
	}
}

