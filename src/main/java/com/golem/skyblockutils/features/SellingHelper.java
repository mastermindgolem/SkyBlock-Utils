package com.golem.skyblockutils.features;

import com.golem.skyblockutils.events.InventoryChangeEvent;
import com.golem.skyblockutils.models.AttributePrice;
import com.golem.skyblockutils.models.AttributeValueResult;
import com.golem.skyblockutils.utils.AttributeUtils;
import com.golem.skyblockutils.utils.InventoryData;
import com.golem.skyblockutils.utils.LocationUtils;
import com.golem.skyblockutils.utils.RenderUtils;
import lombok.Getter;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.init.Items;
import net.minecraft.inventory.Slot;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.BlockPos;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.awt.*;
import java.util.List;
import java.util.*;
import java.util.stream.Collectors;

import static com.golem.skyblockutils.Main.configFile;
import static com.golem.skyblockutils.Main.mc;

public class SellingHelper {

    private final HashMap<Slot, SellMethod> highlightSlots = new HashMap<>();
    private final Set<Slot> highlightInvSlots = new HashSet<>();
    private final HashMap<TileEntityChest, Color> highlightChests = new HashMap<>();
    private Signature similarity = null;
    private static final HashMap<String, TileEntityChest> chestFilters = new HashMap<>();

    @SubscribeEvent
    public void onInventoryChange(InventoryChangeEvent event) {
        similarity = null;
        highlightSlots.clear();
        highlightInvSlots.clear();
        highlightChests.clear();
        if (event.event.gui instanceof GuiContainer) {
            if (InventoryData.currentChestName.contains("Paid Chest") || InventoryData.currentChestName.contains("Free Chest")) return;
            if (configFile.highlightSellMethod) highlightSellMethod();
            if (!Objects.equals(LocationUtils.getLocation(), "dynamic")) return;
            if (configFile.sortingHelper) {
                checkForSimilarItems();
                addChestsToHighlight();
            }
        }
    }

    private void addChestsToHighlight() {
        for (Slot slot : InventoryData.containerSlots) {
            if (!slot.getHasStack()) continue;
            if (slot.inventory != mc.thePlayer.inventory) continue;

            Set<Signature> signatures = getSignature(slot);

            int highestLevel = 0;
            String highestSig = null;
            for (Signature sig : signatures) {
                if (!chestFilters.containsKey(sig.signature)) continue;
                if (sig.level > highestLevel) {
                    highestLevel = sig.level;
                    highestSig = sig.signature;
                }
            }
            if (highestSig == null) continue;
            if (AttributePrice.expensiveAttributes.stream().anyMatch(highestSig::contains)) {
                highlightChests.put(chestFilters.get(highestSig), Color.RED);
            } else {
                highlightChests.put(chestFilters.get(highestSig), Color.GREEN);
            }
        }
    }

    private void checkForSimilarItems() {
        List<Slot> slots = InventoryData.containerSlots.subList(0, InventoryData.containerSlots.size() - 36);

        similarity = getSimilarity(slots).stream().max(Comparator.comparingInt(Signature::getLevel)).orElse(null);

        if (similarity == null) return;

        if (InventoryData.containerSlots.size() < 36) return;
        slots = InventoryData.containerSlots.subList(InventoryData.containerSlots.size() - 36, InventoryData.containerSlots.size()).stream().filter(Slot::getHasStack).collect(Collectors.toList());
        for (Slot slot : slots) {
            if (!slot.getHasStack()) continue;
            Set<Signature> signatures = getSignature(slot);
            for (Signature sig : signatures.stream()
                    .sorted(Collections.reverseOrder(Comparator.comparing(Signature::getLevel)))
                    .collect(Collectors.toList())) {
                if (Objects.equals(sig.signature, similarity.signature)) {
                    highlightInvSlots.add(slot);
                    break;
                }
                if (chestFilters.containsKey(sig.signature)) {
                    break;
                }
            }
        }
    }

    public static void addChest(TileEntityChest chest, List<Slot> slots) {
        Set<Signature> similarity = getSimilarity(slots);
        String sim = similarity.stream().max(Comparator.comparingInt(Signature::getLevel)).map(Signature::getSignature).orElse(null);
        chestFilters.entrySet().removeIf(entry -> entry.getValue().equals(chest));
        if (sim == null) {
            return;
        }
//        ChatUtils.addUpdatingMessage("Added chest containing " + sim);
        chestFilters.put(sim, chest);
    }

    private static Set<Signature> getSimilarity(List<Slot> slots) {
        Set<Signature> possible = new HashSet<>();
        if (slots.stream().allMatch(Slot::getHasStack)) return possible;
        slots = slots.stream().filter(Slot::getHasStack).collect(Collectors.toList());

        if (slots.isEmpty()) return possible;

        List<Set<Signature>> slotSignatures = slots.stream().map(SellingHelper::getSignature).filter(o -> !o.isEmpty()).collect(Collectors.toList());

        if (slotSignatures.isEmpty()) return possible;

        possible = new HashSet<>(slotSignatures.get(0));
        slotSignatures.forEach(possible::retainAll);
        return possible;
    }

    private void highlightSellMethod() {
        for (Slot slot : InventoryData.containerSlots) {
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
    public void onTick(RenderWorldLastEvent event) {
        if (mc.theWorld == null || mc.thePlayer == null) return;
        if (!configFile.sortingHelper) return;
//        if (!ButtonManager.isChecked("sortingHelper")) return;
        if (!Objects.equals(LocationUtils.getLocation(), "dynamic")) return;

        for (Map.Entry<TileEntityChest, Color> entry : highlightChests.entrySet()) {
            RenderUtils.drawBlockBox(entry.getKey().getPos(), entry.getValue(), 5, event.partialTicks);
            BlockPos adjacent = ChestAnalyzer.getAdjacentChest(entry.getKey());
            if (adjacent != null) RenderUtils.drawBlockBox(adjacent, entry.getValue(), 5, event.partialTicks);
        }
    }

    private static Set<Signature> getSignature(Slot slot) {
        Set<Signature> signatures = new HashSet<>();
        if (slot.getStack().getItem().getRegistryName().equals(Items.enchanted_book.getRegistryName())) {
            try {
                NBTTagCompound enchants = slot.getStack().serializeNBT().getCompoundTag("tag").getCompoundTag("ExtraAttributes").getCompoundTag("enchantments");
                for (String enchant : enchants.getKeySet()) {
                    signatures.add(new Signature(enchant, 5));
                }
            } catch (NullPointerException ignored) {}
        }

        AttributeValueResult result = InventoryData.values.get(slot);
        if (result == null) return signatures;
        if (AttributeUtils.isArmor(result.item_id)) {
            signatures.add(new Signature(AttributeUtils.getItemType(result.item_id) + "_" + result.best_attribute.attribute, 4));
            signatures.add(new Signature("armor_" + result.best_attribute.attribute, 3));
            signatures.add(new Signature("attribute_armor", 2));
            return signatures;
        } else if (result.item_id.equals("ATTRIBUTE_SHARD")) {
            signatures.add(new Signature("attribute_shard", 2));
            signatures.add(new Signature("shard_" + result.top_display.toLowerCase(), 3));
            return signatures;
        }
        signatures.add(new Signature(result.item_id + "_" + result.best_attribute.attribute, 4));
        signatures.add(new Signature("attribute_item_" + result.best_attribute.attribute, 3));
        signatures.add(new Signature("attribute_item", 2));
        return signatures;
    }

    @SubscribeEvent
    public void onGuiDraw(GuiScreenEvent.BackgroundDrawnEvent event) {
        if (!(event.gui instanceof GuiChest)) return;
        if (InventoryData.currentChestName.contains("Paid Chest") || InventoryData.currentChestName.contains("Free Chest")) return;
        GuiChest gui = (GuiChest) event.gui;
        if (configFile.highlightSellMethod) {
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
        if (configFile.sortingHelper) {
            highlightInvSlots.forEach(o -> RenderUtils.highlight(new Color(0, 255, 255, 127), gui, o));
        }
    }

    enum SellMethod {
        SALVAGE,
        AUCTION_CHEAP,
        AUCTION_EXPENSIVE
    }

    @Getter
    private static class Signature {
        private final String signature;
        private final int level;

        public Signature(String signature, int level) {
            this.signature = signature;
            this.level = level;
        }

        @Override
        public String toString() {
            return signature + " " + level;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            Signature signature1 = (Signature) obj;
            return Objects.equals(signature, signature1.signature);
        }

        @Override
        public int hashCode() {
            return Objects.hash(signature, level);
        }
    }
}
