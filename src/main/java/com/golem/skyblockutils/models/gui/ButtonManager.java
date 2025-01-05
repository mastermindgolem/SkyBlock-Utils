package com.golem.skyblockutils.models.gui;

import com.golem.skyblockutils.Main;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.client.config.GuiCheckBox;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Mouse;

import java.util.ArrayList;
import java.util.HashMap;

import static com.golem.skyblockutils.Main.configFile;

public class ButtonManager {
    private static HashMap<String, GuiCheckBox> checkBoxes = new HashMap<>();
    public static boolean mousePressed = false;
    ArrayList<String> activeBoxes = new ArrayList<>();

    public ButtonManager() {
        checkBoxes.put("containerValue", new GuiCheckBox(0, 5, 0, "Container Value", false));
        checkBoxes.put("sellMethod", new GuiCheckBox(1, 5, 0, "Highlight Sell Method", false));
        checkBoxes.put("auctionHelper", new GuiCheckBox(2, 5, 0, "Auction Helper", false));
    }

    @SubscribeEvent
    public void guiDraw(GuiScreenEvent.BackgroundDrawnEvent event) {
        if (!(event.gui instanceof GuiChest)) return;
        activeBoxes.clear();
        if (configFile.container_value != 0) activeBoxes.add("containerValue");;
        if (configFile.sellingHelper) activeBoxes.add("sellMethod");
        if (configFile.auctionHelper) activeBoxes.add("auctionHelper");

        int y = event.gui.height - 25;

        for (String box : checkBoxes.keySet()) {
            if (activeBoxes.contains(box)) {
                checkBoxes.get(box).yPosition = y;
                checkBoxes.get(box).drawButton(event.gui.mc, 0, 0);
                y -= 25;
            } else {
                checkBoxes.get(box).setIsChecked(false);
            }
        }
    }

    @SubscribeEvent
    public void onGuiClick(GuiScreenEvent.MouseInputEvent.Pre event) {
        if (Mouse.getEventButtonState()) {
            int mouseX = Mouse.getEventX() * event.gui.width / Minecraft.getMinecraft().displayWidth;
            int mouseY = event.gui.height - Mouse.getEventY() * event.gui.height / Minecraft.getMinecraft().displayHeight - 1;

            checkBoxes.values().forEach(box -> box.mousePressed(Main.mc, mouseX, mouseY));

        }
    }

    public static boolean isChecked(String box) {
        return checkBoxes.get(box).isChecked();
    }
}
