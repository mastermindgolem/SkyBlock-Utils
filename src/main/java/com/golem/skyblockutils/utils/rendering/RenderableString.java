package com.golem.skyblockutils.utils.rendering;

import lombok.Getter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.client.event.GuiScreenEvent;
import org.lwjgl.input.Mouse;

import java.awt.*;

public class RenderableString extends Renderable {
    @Getter
    private String text;
    private int color = 0xFFFFFF;
    private boolean shadow = true;

    public RenderableString(String text, int x, int y) {
        super(x, y);
        this.text = text;
    }

    public RenderableString(String text, int x, int y, float scale) {
        super(x, y);
        this.text = text;
        this.scale = scale;
    }


    @Override
    public void render(GuiScreenEvent.BackgroundDrawnEvent event) {
        int mouseX = Mouse.getEventX() * event.gui.width / Minecraft.getMinecraft().displayWidth;
        int mouseY = event.gui.height - Mouse.getEventY() * event.gui.height / Minecraft.getMinecraft().displayHeight - 1;

        render(mouseX, mouseY);
    }

    @Override
    public void render() {
        if (!visible) return;
        GlStateManager.pushMatrix();
        GlStateManager.scale(scale, scale, 1);
        Minecraft.getMinecraft().fontRendererObj.drawString(text, x, y, color, shadow);
        if (underline) Gui.drawRect(x, y + getHeight(), x + getWidth(), y + getHeight() + 1, Color.WHITE.getRGB());
        GlStateManager.popMatrix();
    }



    @Override
    public void render(int mouseX, int mouseY) {
        if (!visible) return;
        GlStateManager.pushMatrix();
        GlStateManager.scale(scale, scale, 1);
        Minecraft.getMinecraft().fontRendererObj.drawString(text, x, y, color, shadow);
        if (underline) Gui.drawRect(x, y + getHeight(), x + getWidth(), y + getHeight() + 1, Color.WHITE.getRGB());
        GlStateManager.popMatrix();
        handleMouse(mouseX, mouseY);
    }

    @Override
    public int getWidth() {
        return (int) (Minecraft.getMinecraft().fontRendererObj.getStringWidth(text));
    }

    @Override
    public int getHeight() {
        return Minecraft.getMinecraft().fontRendererObj.FONT_HEIGHT;
    }

    public RenderableString setColor(int color) {
        this.color = color;
        return this;
    }

    public RenderableString setText(String text) {
        this.text = text;
        return this;
    }
}
