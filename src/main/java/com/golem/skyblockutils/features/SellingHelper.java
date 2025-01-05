package com.golem.skyblockutils.features;

import com.golem.skyblockutils.events.InventoryChangeEvent;
import com.golem.skyblockutils.models.AttributeValueResult;
import com.golem.skyblockutils.models.gui.ButtonManager;
import com.golem.skyblockutils.utils.InventoryData;
import com.golem.skyblockutils.utils.RenderUtils;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.inventory.Slot;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.awt.*;
import java.util.HashMap;
import java.util.Objects;

import static com.golem.skyblockutils.Main.configFile;

public class SellingHelper {

    private final HashMap<Slot, SellMethod> highlightSlots = new HashMap<>();

    @SubscribeEvent
    public void onInventoryChange(InventoryChangeEvent event) {
        if (!(event.event.gui instanceof GuiChest)) return;
        if (InventoryData.chestName.contains("Paid Chest") || InventoryData.chestName.contains("Free Chest")) return;

        if (configFile.sellingHelper) highlightSellMethod();
    }

    private void highlightSellMethod() {
        highlightSlots.clear();

        for (Slot slot : InventoryData.containerSlots.subList(0, InventoryData.containerSlots.size() - 36)) {
            AttributeValueResult result = InventoryData.values.get(slot);
            if (result == null) continue;
            if (Objects.equals(result.top_display, "SAL")) {
                highlightSlots.put(slot, SellMethod.SALVAGE);
            } else if (result.value > 5_000_000) {
                highlightSlots.put(slot, SellMethod.AUCTION_EXPENSIVE);
            } else {
                highlightSlots.put(slot, SellMethod.AUCTION_CHEAP);
            }
        }
    }

    @SubscribeEvent
    public void onGuiDraw(GuiScreenEvent.BackgroundDrawnEvent event) {
        if (!(event.gui instanceof GuiChest)) return;
        if (InventoryData.chestName.contains("Paid Chest") || InventoryData.chestName.contains("Free Chest")) return;
        GuiChest gui = (GuiChest) event.gui;
        if (ButtonManager.isChecked("sellMethod")) {
            highlightSlots.forEach((slot, method) -> {
                switch (method) {
                    case SALVAGE:
                        RenderUtils.highlight(new Color(255, 0, 0, 127), gui, slot);
                        break;
                    case AUCTION_CHEAP:
                        RenderUtils.highlight(new Color(255, 255, 0, 127), gui, slot);
                        break;
                    case AUCTION_EXPENSIVE:
                        RenderUtils.highlight(new Color(0, 255, 0, 127), gui, slot);
                        break;
                }
            });
        }
    }

    enum SellMethod {
        SALVAGE,
        AUCTION_CHEAP,
        AUCTION_EXPENSIVE
    }
}
