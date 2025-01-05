package com.golem.skyblockutils.features;

import com.golem.skyblockutils.events.InventoryChangeEvent;
import com.golem.skyblockutils.models.AttributeItem;
import com.golem.skyblockutils.models.AttributeItemType;
import com.golem.skyblockutils.utils.InventoryData;
import com.golem.skyblockutils.utils.RenderUtils;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.inventory.Slot;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.awt.*;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static com.golem.skyblockutils.Main.configFile;
import static com.golem.skyblockutils.models.AttributeItemType.*;

public class CombineHelper {

    private Set<Slot> highlightSlots = new HashSet<>();

    private final AttributeItemType[] pieces = {
            Helmet, Chestplate, Leggings, Boots, MoltenNecklace, MoltenCloak, MoltenBelt, MoltenBracelet, Shard
    };

    private final String[] attributes = {"", "lifeline", "breeze", "speed", "experience", "mana_pool", "life_regeneration", "blazing_resistance", "arachno_resistance", "undead_resistance", "blazing_fortune", "fishing_experience", "double_hook", "infection", "trophy_hunter", "fisherman", "hunter", "fishing_speed", "life_recovery", "ignition", "combo", "attack_speed", "midas_touch", "mana_regeneration", "veteran", "mending", "ender_resistance", "dominance", "ender", "mana_steal", "blazing", "elite", "arachno", "undead", "warrior", "deadeye", "fortitude", "magic_find"};

    @SubscribeEvent
    public void onInventoryChange(InventoryChangeEvent event) {
        highlightSlots.clear();
        if (!(event.event.gui instanceof GuiChest)) return;
        if (!InventoryData.chestName.contains("Attribute Fusion")) return;

        for (AttributeItemType piece : pieces) {
            for (int level = 1; level < 10; level++) {
                Slot slot1 = null;
                for (Map.Entry<Slot, AttributeItem> entry : InventoryData.items.entrySet()) {
                    if (piece == entry.getValue().item_type) {
                        if (entry.getValue().attributes.getOrDefault(attributes[configFile.combineAttribute], 0) == level) {
                            if (slot1 == null) {
                                slot1 = entry.getKey();
                            } else {
                                highlightSlots.add(slot1);
                                highlightSlots.add(entry.getKey());
                            }

                        }
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void onGuiDraw(GuiScreenEvent.BackgroundDrawnEvent event) {
        if (!(event.gui instanceof GuiChest)) return;
        if (configFile.combineAttribute == 0) return;
        if (!InventoryData.chestName.contains("Attribute Fusion")) return;

        for (Slot slot : highlightSlots) {
            RenderUtils.highlight(Color.GREEN, (GuiChest) event.gui, slot);
//            Gui.drawRect(slot.xDisplayPosition, slot.yDisplayPosition, slot.xDisplayPosition + 16, slot.yDisplayPosition + 16, 0x11FCF3);
        }
    }
}
