package com.golem.skyblockutils.features;

import com.golem.skyblockutils.Main;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.List;

import static com.golem.skyblockutils.Main.configFile;

public class CombineHelper {


    private final String[] pieces = {
            "HELMET",
            "CHESTPLATE",
            "LEGGINGS",
            "BOOTS",
            "MOLTEN_NECKLACE",
            "MOLTEN_CLOAK",
            "MOLTEN_BELT",
            "MOLTEN_BRACELET"
    };
    private Slot slot1;
    private ItemStack item;

    private final String[] attributes = {"", "lifeline", "breeze", "speed", "experience", "mana_pool", "life_regeneration", "blazing_resistance", "arachno_resistance", "undead_resistance", "blazing_fortune", "fishing_experience", "double_hook", "infection", "trophy_hunter", "fisherman", "hunter", "fishing_speed", "life_recovery", "ignition", "combo", "attack_speed", "midas_touch", "mana_regeneration", "veteran", "mending", "ender_resistance", "dominance", "ender", "mana_steal", "blazing", "elite", "arachno", "undead", "warrior", "deadeye", "fortitude", "magic_find"};

    public String getItemId(NBTTagCompound extraAttributes) {
        String itemId = extraAttributes.getString("id");
        itemId = itemId.split(":")[0];
        return itemId;
    }

    public int getAttributeLevel(NBTTagCompound extraAttributes) {
        try {
            NBTTagCompound shards = extraAttributes.getCompoundTag("attributes");
            return shards.getInteger(attributes[configFile.combineAttribute]);
        } catch (NullPointerException e) {
            return 0;
        }
    }

    @SubscribeEvent
    public void guiDraw(GuiScreenEvent.BackgroundDrawnEvent event) {
        if (!(event.gui instanceof GuiChest)) return;
        GuiChest gui = (GuiChest) event.gui;
        Container container = gui.inventorySlots;
        if (!(container instanceof ContainerChest)) return;
        String chestName = ((ContainerChest) container).getLowerChestInventory().getDisplayName().getUnformattedText();
        if (!chestName.contains("Attribute Fusion")) return;
        List<Slot> chestInventory = ((GuiChest) Minecraft.getMinecraft().currentScreen).inventorySlots.inventorySlots;

        for (String piece : pieces) {
            for (int level = 1; level < 10; level++) {
                slot1 = null;
                for (Slot slot : chestInventory) {
                    if (!slot.getHasStack()) continue;
                    if (slot.slotNumber < 53) continue;
                    item = slot.getStack();
                    NBTTagCompound itemNbt;
                    try {
                        itemNbt = item.serializeNBT().getCompoundTag("tag").getCompoundTag("ExtraAttributes");
                    } catch (NullPointerException e) {
                        // Possible bugs where items don't have nbt, ignore the item.
                        return;
                    }
                    String name = getItemId(itemNbt);
                    if ((name.contains("CRIMSON") || name.contains("AURORA") || name.contains("FERVOR") || name.contains("HOLLOW") || name.contains("TERROR") || name.equals(piece)) && name.contains(piece)) {
                        if (getAttributeLevel(itemNbt) == level) {
                            if (slot1 == null) {
                                slot1 = slot;
                            } else {
                                Gui.drawRect(slot1.xDisplayPosition, slot1.yDisplayPosition, slot1.xDisplayPosition + 16, slot1.yDisplayPosition + 16, 0x11FCF3);
                                Gui.drawRect(slot.xDisplayPosition, slot.yDisplayPosition, slot.xDisplayPosition + 16, slot.yDisplayPosition + 16, 0x11FCF3);
                            }

                        }
                    }
                }
            }
        }
    }
}
