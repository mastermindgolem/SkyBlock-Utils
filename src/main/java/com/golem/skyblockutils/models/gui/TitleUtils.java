package com.golem.skyblockutils.models.gui;

import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import static com.golem.skyblockutils.Main.mc;

public class TitleUtils {

    private static final TitleUtils INSTANCE = new TitleUtils();

    public static TitleUtils getInstance() {
        return INSTANCE;
    }

    private String title = null;
    private long titleLifetime = 0;
    private int color = 0xFF0000;

    public void createTitle(String title, int ticks, int color) {
        this.title = title;
        this.titleLifetime = System.nanoTime() + (ticks * 50000000L);
        this.color = color;
    }
    private void renderTitles (ScaledResolution scaledResolution) {

        if (mc.theWorld == null || mc.thePlayer == null) return;

        int scaledWidth = scaledResolution.getScaledWidth();
        int scaledHeight = scaledResolution.getScaledHeight();

        if (this.title != null) {
            int stringWidth = mc.fontRendererObj.getStringWidth(this.title);
            float scale = 4f; // Scale is normally 4, but if it's larger than the screen, scale it down...
            if (stringWidth * scale > scaledWidth * 0.9f) {
                scale = scaledWidth * 0.9f / (float) stringWidth;
            }
            GlStateManager.pushMatrix();
            GlStateManager.translate((float)(scaledWidth / 2),(float)(scaledHeight / 2), 0.0f);
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
            GlStateManager.pushMatrix();
            GlStateManager.scale(scale, scale, scale);
            mc.fontRendererObj.drawString(
                    this.title,
                    ((float)-mc.fontRendererObj.getStringWidth(this.title) / 2),
                    -20.0f,
                    color,
                    true
            );
            GlStateManager.popMatrix();
            GlStateManager.popMatrix();
        }
    }

    @SubscribeEvent
    public void onRenderHUD(RenderGameOverlayEvent event) {
        if (event.type != RenderGameOverlayEvent.ElementType.TEXT) return;
        if (System.nanoTime() >= titleLifetime) {
            titleLifetime = 0;
            title = null;
        }
        renderTitles(event.resolution);
    }
}
