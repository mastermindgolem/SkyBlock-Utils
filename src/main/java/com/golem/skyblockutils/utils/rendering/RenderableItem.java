package com.golem.skyblockutils.utils.rendering;

import com.golem.skyblockutils.utils.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.GuiScreenEvent;
import org.lwjgl.input.Mouse;

public class RenderableItem extends Renderable {
    private ItemStack itemStack;
    private boolean bordered;

    public RenderableItem(ItemStack itemStack, int x, int y) {
        super(x, y);
        this.itemStack = itemStack;
        this.bordered = false;
    }
    public RenderableItem(ItemStack itemStack, int x, int y, boolean bordered) {
        super(x, y);
        this.itemStack = itemStack;
        this.bordered = bordered;
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
        if (bordered) {
            RenderUtils.drawBorderedRect(x - 1, y - 1, x + 17, y + 17, 1, 0x80000000, 0x80000000);
        }
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
