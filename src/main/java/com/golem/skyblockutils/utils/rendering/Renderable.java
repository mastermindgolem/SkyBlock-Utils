package com.golem.skyblockutils.utils.rendering;

import com.golem.skyblockutils.models.gui.ButtonManager;
import lombok.Getter;
import net.minecraftforge.client.event.GuiScreenEvent;
import org.lwjgl.input.Mouse;

public abstract class Renderable {
    @Getter
    protected int x;
    @Getter
    protected int y;
    protected boolean visible = true;
    @Getter
    protected float scale = 1.0f;
    protected boolean underline = false;
    protected Runnable onClick;
    protected Runnable onHover;

    public Renderable(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public abstract void render(int mouseX, int mouseY);
    public abstract void render();
    public abstract void render(GuiScreenEvent.BackgroundDrawnEvent event);

    public abstract int getWidth();
    public abstract int getHeight();

    public void handleMouse(int mouseX, int mouseY) {
        if (!visible) return;

        if (onClick == null && onHover == null) return;

        if (isHovered(mouseX, mouseY)) {
            underline = true;
            if (onHover != null) {
                onHover.run();
            }
            if (!ButtonManager.mousePressed && Mouse.getEventButtonState() && Mouse.getEventButton() == 0 && onClick != null) {
                ButtonManager.mousePressed = true;
                    onClick.run();
            }
        } else {
            underline = false;
        }

        if (!Mouse.getEventButtonState()) {
            ButtonManager.mousePressed = false;
        }
    }

    protected boolean isHovered(int mouseX, int mouseY) {
        return mouseX >= x && mouseX <= x + getWidth() &&
                mouseY >= y && mouseY <= y + getHeight();
    }

    public Renderable setPosition(int x, int y) {
        this.x = x;
        this.y = y;
        return this;
    }

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

    public Renderable setScale(float scale) {
        this.scale = scale;
        return this;
    }
}

