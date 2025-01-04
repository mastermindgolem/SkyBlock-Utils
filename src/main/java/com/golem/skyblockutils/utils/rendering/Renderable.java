package com.golem.skyblockutils.utils.rendering;

import com.golem.skyblockutils.features.ContainerValue;
import net.minecraftforge.client.event.GuiScreenEvent;
import org.lwjgl.input.Mouse;

public abstract class Renderable {
    protected int x;
    protected int y;
    protected boolean visible = true;
    protected boolean underline = false;
    protected Runnable onClick;
    protected Runnable onHover;

    public Renderable(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public abstract void render(int mouseX, int mouseY);
    public abstract void render(GuiScreenEvent.BackgroundDrawnEvent event);

    public abstract int getWidth();
    public abstract int getHeight();

    public void handleMouse(int mouseX, int mouseY) {
        if (!visible) return;

        if (isHovered(mouseX, mouseY)) {
            underline = true;
            if (onHover != null) {
                onHover.run();
            }
            if (!ContainerValue.mousePressed && Mouse.getEventButtonState() && Mouse.getEventButton() == 0 && onClick != null) {
                ContainerValue.mousePressed = true;
                    onClick.run();
            }
        } else {
            underline = false;
        }

        if (!Mouse.getEventButtonState()) {
            ContainerValue.mousePressed = false;
        }
    }

    protected boolean isHovered(int mouseX, int mouseY) {
        return mouseX >= x && mouseX <= x + getWidth() &&
                mouseY >= y && mouseY <= y + getHeight();
    }

    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() { return x; }
    public int getY() { return y; }
    public boolean isVisible() { return visible; }

    public Renderable setVisible(boolean visible) {
        this.visible = visible;
        return this;
    }

    public Renderable onClick(Runnable onClick) {
        this.onClick = onClick;
        return this;
    }

    public Renderable onHover(Runnable onHover) {
        this.onHover = onHover;
        return this;
    }
}

