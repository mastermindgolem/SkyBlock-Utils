package com.golem.skyblockutils.features;

import com.golem.skyblockutils.events.InventoryChangeEvent;
import com.golem.skyblockutils.models.AttributeValueResult;
import com.golem.skyblockutils.models.gui.ButtonManager;
import com.golem.skyblockutils.utils.InventoryData;
import com.golem.skyblockutils.utils.RenderUtils;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.init.Items;
import net.minecraft.inventory.Slot;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.awt.*;
import java.util.List;
import java.util.*;
import java.util.stream.Collectors;

import static com.golem.skyblockutils.Main.configFile;

public class SellingHelper {

    private final HashMap<Slot, SellMethod> highlightSlots = new HashMap<>();
    private final Set<Slot> highlightInvSlots = new HashSet<>();

    private final Set<String> possibleSimilarities = new HashSet<>();

    @SubscribeEvent
    public void onInventoryChange(InventoryChangeEvent event) {
        possibleSimilarities.clear();
        highlightSlots.clear();
        highlightInvSlots.clear();
        if (!(event.event.gui instanceof GuiChest)) return;
        if (InventoryData.chestName.contains("Paid Chest") || InventoryData.chestName.contains("Free Chest")) return;

        if (configFile.sellingHelper) highlightSellMethod();
        if (configFile.sortingHelper) checkForSimilarItems();
    }

    private void checkForSimilarItems() {
        List<Slot> slots = InventoryData.containerSlots.subList(0, InventoryData.containerSlots.size() - 36).stream().filter(Slot::getHasStack).collect(Collectors.toList());
        if (slots.isEmpty()) return;

        if (slots.stream().allMatch(
                o -> Objects.equals(o.getStack().getItem().getRegistryName(), Items.enchanted_book.getRegistryName())
        )) {
            try {
                NBTTagCompound enchants = slots.get(0).getStack().serializeNBT().getCompoundTag("tag").getCompoundTag("ExtraAttributes").getCompoundTag("enchantments");
                possibleSimilarities.addAll(enchants.getKeySet().stream().map(o -> "enchantment_" + o).collect(Collectors.toSet()));
            } catch (NullPointerException ignored) {

            }

        }

        slots = slots.stream().filter(o -> InventoryData.values.get(o) != null).collect(Collectors.toList());

        if (!slots.isEmpty()) {
            String bestAttribute = InventoryData.values.get(slots.get(0)).best_attribute.attribute;
            if (slots.stream().allMatch(
                    o -> Objects.equals(InventoryData.values.get(o).best_attribute.attribute, bestAttribute)
            )) {
                possibleSimilarities.add(bestAttribute);
            }
        }

        if (InventoryData.containerSlots.size() < 36) return;
        slots = InventoryData.containerSlots.subList(InventoryData.containerSlots.size() - 36, InventoryData.containerSlots.size()).stream().filter(Slot::getHasStack).collect(Collectors.toList());
        List<Slot> similarEnchantSlots = slots.stream().filter(
                (o) -> {
                    if (o.getStack().getItem().getRegistryName().equals(Items.enchanted_book.getRegistryName())) {
                        try {
                            NBTTagCompound enchants = o.getStack().serializeNBT().getCompoundTag("tag").getCompoundTag("ExtraAttributes").getCompoundTag("enchantments");
                            return enchants.getKeySet().stream().map(e -> "enchantment_" + e).anyMatch(possibleSimilarities::contains);
                        } catch (NullPointerException ignored) {
                        }
                    }
                    return false;
                }
        ).collect(Collectors.toList());

        List<Slot> similarAttributeSlots = slots.stream().filter(
                o -> {
                    AttributeValueResult result = InventoryData.values.get(o);
                    if (result == null) return false;
                    return possibleSimilarities.contains(result.best_attribute.attribute);
                }
        ).collect(Collectors.toList());

        highlightInvSlots.addAll(similarEnchantSlots);
        highlightInvSlots.addAll(similarAttributeSlots);

    }

    private void highlightSellMethod() {
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
        if (ButtonManager.isChecked("sortingHelper")) {
            highlightInvSlots.forEach(o -> RenderUtils.highlight(new Color(0, 255, 255, 127), gui, o));
        }
    }

    enum SellMethod {
        SALVAGE,
        AUCTION_CHEAP,
        AUCTION_EXPENSIVE
    }
}
