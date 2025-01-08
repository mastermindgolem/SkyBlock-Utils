package com.golem.skyblockutils.utils;

import com.golem.skyblockutils.events.InventoryChangeEvent;
import com.golem.skyblockutils.models.AttributeItem;
import com.golem.skyblockutils.models.AttributePrice;
import com.golem.skyblockutils.models.AttributeValueResult;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.golem.skyblockutils.models.AttributePrice.LowestBin;

public class InventoryData {
    public static HashMap<Slot, AttributeItem> items = new HashMap<>();
    public static HashMap<Slot, AttributeValueResult> values = new HashMap<>();
    public static String currentChestName = "";
    public static String lastOpenChestName = "";
    public static List<Slot> containerSlots = new ArrayList<>();
    private static final HashMap<Slot, String> lastItemSignatures = new HashMap<>();

    @SubscribeEvent
    public void onGuiOpen(GuiOpenEvent event) {
        if (event.gui instanceof GuiContainer) {
            items.clear();
            values.clear();
            lastItemSignatures.clear();
            containerSlots.clear();
            currentChestName = "";
        }
    }

    @SubscribeEvent
    public void onGuiDraw(GuiScreenEvent.DrawScreenEvent event) {
        if (!(event.gui instanceof GuiContainer)) return;

        GuiContainer guiChest = (GuiContainer) event.gui;
        List<Slot> slots = guiChest.inventorySlots.inventorySlots;
        if (guiChest instanceof GuiChest) {
            currentChestName = ((ContainerChest) guiChest.inventorySlots).getLowerChestInventory().getDisplayName().getUnformattedText();
            lastOpenChestName = currentChestName;
        }
        boolean shouldUpdate = false;

        if (containerSlots.size() != slots.size()) {
            shouldUpdate = true;
        } else {
            for (Slot slot : slots) {
                String currentSignature = getItemSignature(slot);
                String lastSignature = lastItemSignatures.get(slot);

                if (!currentSignature.equals(lastSignature)) {
                    shouldUpdate = true;
                    break;
                }
            }
        }

        if (shouldUpdate) {
            updateAllSlots(event, slots);
        }
    }

    private void updateAllSlots(GuiScreenEvent.DrawScreenEvent event, List<Slot> slots) {
        items.clear();
        values.clear();
        lastItemSignatures.clear();
        containerSlots = new ArrayList<>(slots);

        for (Slot slot : slots) {
            lastItemSignatures.put(slot, getItemSignature(slot));
            if (!slot.getHasStack()) {
                continue;
            }
            ItemStack itemStack = slot.getStack();
            try {
                NBTTagCompound itemNbt = itemStack.serializeNBT().getCompoundTag("tag").getCompoundTag("ExtraAttributes");
                AttributeItem item = new AttributeItem(itemStack.getDisplayName(), getItemLore(itemStack), itemNbt);
                if (item.item_type == null) {
                    if (LowestBin.containsKey(item.item_id)) {
                        items.put(slot, item);
                        values.put(slot, new AttributeValueResult(item.item_name, LowestBin.get(item.item_id)));
                        ;
                    }
                }
                items.put(slot, item);
                values.put(slot, AttributePrice.AttributeValue(item, false, false));
            } catch (NullPointerException ignored) {
            }
        }
        MinecraftForge.EVENT_BUS.post(new InventoryChangeEvent(event));
    }

    private String getItemSignature(Slot slot) {
        if (!slot.getHasStack()) return "invalid";

        ItemStack stack = slot.getStack();
        try {
            AttributeItem item = new AttributeItem(stack.getDisplayName(), getItemLore(stack), stack.serializeNBT().getCompoundTag("tag").getCompoundTag("ExtraAttributes"));
            return item.toString();
        } catch (Exception ignored) {
            return stack.getDisplayName();
        }

    }

    public String getItemLore(ItemStack item) {
        if (item == null || !item.hasTagCompound() || !item.getTagCompound().hasKey("display")) {
            return "";
        }

        NBTTagCompound display = item.getTagCompound().getCompoundTag("display");
        if (!display.hasKey("Lore")) {
            return "";
        }

        NBTTagList l = display.getTagList("Lore", 8);
        StringBuilder lore = new StringBuilder();

        for (int i = 0; i < l.tagCount(); i++) {
            lore.append(l.getStringTagAt(i));
        }

        return lore.toString();
    }
}