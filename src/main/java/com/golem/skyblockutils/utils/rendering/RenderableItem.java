package com.golem.skyblockutils.utils.rendering;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.GuiScreenEvent;
import org.lwjgl.input.Mouse;

public class RenderableItem extends Renderable {
    private ItemStack itemStack;

    public RenderableItem(ItemStack itemStack, int x, int y) {
        super(x, y);
        this.itemStack = itemStack;
    }

    @Override
    public void render(GuiScreenEvent.BackgroundDrawnEvent event) {
        int mouseX = Mouse.getEventX() * event.gui.width / Minecraft.getMinecraft().displayWidth;
        int mouseY = event.gui.height - Mouse.getEventY() * event.gui.height / Minecraft.getMinecraft().displayHeight - 1;

        render(mouseX, mouseY);
    }

    @Override
    public void render(int mouseX, int mouseY) {
        if (!visible || itemStack == null) return;
        GlStateManager.pushMatrix();
        Minecraft.getMinecraft().getRenderItem().renderItemIntoGUI(itemStack, x, y);
        handleMouse(mouseX, mouseY);
        GlStateManager.popMatrix();
    }

    @Override
    public int getWidth() {
        return 16;
    }

    @Override
    public int getHeight() {
        return 16;
    }

    public RenderableItem setItem(ItemStack itemStack) {
        this.itemStack = itemStack;
        return this;
    }
}
