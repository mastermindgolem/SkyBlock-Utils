package com.golem.skyblockutils.utils;

import net.minecraft.client.gui.ScaledResolution;

import static com.golem.skyblockutils.Main.mc;

public class ScreenUtils {
    private static int lastWidth = 0;
    private static int lastHeight = 0;
    private static int lastGuiScale = 0;

    private static int cachedCenterX = 0;
    private static int cachedCenterY = 0;

    public static int[] getCenter() {
        ScaledResolution scaledResolution = new ScaledResolution(mc);

        int currentWidth = mc.displayWidth;
        int currentHeight = mc.displayHeight;
        int currentGuiScale = mc.gameSettings.guiScale;

        if (currentWidth != lastWidth ||
                currentHeight != lastHeight ||
                currentGuiScale != lastGuiScale) {

            int screenWidth = scaledResolution.getScaledWidth();
            int screenHeight = scaledResolution.getScaledHeight();

            int gameWidth = mc.displayWidth * mc.gameSettings.guiScale;
            int gameHeight = mc.displayWidth * mc.gameSettings.guiScale;
            int offsetX = (screenWidth - gameWidth) / 2;
            int offsetY = (screenHeight - gameHeight) / 2;

            cachedCenterX = offsetX + gameWidth / 2;
            cachedCenterY = offsetY + gameHeight / 2;

            lastWidth = currentWidth;
            lastHeight = currentHeight;
            lastGuiScale = currentGuiScale;
        }

        return new int[] {cachedCenterX, cachedCenterY};
    }
}
