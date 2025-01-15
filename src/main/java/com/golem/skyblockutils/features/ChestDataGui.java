package com.golem.skyblockutils.features;

import com.golem.skyblockutils.Main;
import com.golem.skyblockutils.models.AttributePrice;
import com.golem.skyblockutils.models.AttributeValueResult;
import com.golem.skyblockutils.models.DisplayString;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.fml.client.config.GuiButtonExt;
import net.minecraftforge.fml.client.config.GuiCheckBox;

import java.text.DecimalFormat;
import java.util.*;

public class ChestDataGui extends GuiScreen {

    private int lastSort = 3;
    private GuiButton copyWithPrices;
    private GuiButton copyWithoutPrices;
    private GuiCheckBox itemNameSorting;
    private GuiCheckBox quantitySorting;
    private GuiCheckBox pricePerSorting;
    private GuiCheckBox valueSorting;
    private GuiCheckBox showShards;
    private GuiCheckBox showArmor;
    private GuiCheckBox showEquipment;
    private GuiCheckBox simplifyItems;
    private GuiButton sortOrder;
    private GuiButton exit;
    LinkedHashMap<String, DisplayString> displayStrings = new LinkedHashMap<>();
    DecimalFormat df;

    @Override
    public void initGui() {

        if (df == null) {
            df = new DecimalFormat("#.###");
            df.setMinimumFractionDigits(0);
        }

        this.buttonList.add(this.copyWithPrices = new GuiButtonExt(1, 25, 20, 100, 20, "Copy with Prices"));
        this.buttonList.add(this.copyWithoutPrices = new GuiButtonExt(2, 150, 20, 125, 20, "Copy without Prices"));
        this.buttonList.add(this.itemNameSorting = new GuiCheckBox(3, 25, 75, "Item Name", false));
        this.buttonList.add(this.quantitySorting = new GuiCheckBox(4, 25, 100, "Quantity", false));
        this.buttonList.add(this.pricePerSorting = new GuiCheckBox(5, 25, 125, "Price Per", false));
        this.buttonList.add(this.valueSorting = new GuiCheckBox(6, 25, 150, "Value", true));
        this.buttonList.add(this.showShards = new GuiCheckBox(7, 300, 25, "Show Shards", true));
        this.buttonList.add(this.showArmor = new GuiCheckBox(8, 400, 25, "Show Armor", true));
        this.buttonList.add(this.showEquipment = new GuiCheckBox(9, 500, 25, "Show Equipment", true));
        this.buttonList.add(this.simplifyItems = new GuiCheckBox(12, 600, 25, "Simplify Items", false));
        this.buttonList.add(this.sortOrder = new GuiButtonExt(10, 25, 50, 100, 20, "Sorting: Ascending"));
        this.buttonList.add(this.exit = new GuiButtonExt(11, this.width - 100, 20, 50, 20, "Exit"));

        loadData();

    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawBackground(0);

        int xSize = this.width / 5;
        int guiTop = 60;

        int counter = 1;

        long totalLbin = displayStrings.values().stream().mapToLong(displayString -> (long) (displayString.price * displayString.quantity)).sum();

        this.drawString(this.fontRendererObj, EnumChatFormatting.YELLOW + "Total Value: " + EnumChatFormatting.GREEN + Main.formatNumber(totalLbin), this.width - 200, 30, 0xffffffff);

        this.drawString(this.fontRendererObj, EnumChatFormatting.BLUE + "Item", this.width / 5, 50, 0xffffffff);
        this.drawString(this.fontRendererObj, EnumChatFormatting.BLUE + "Quantity", 2 * this.width / 5, 50, 0xffffffff);
        this.drawString(this.fontRendererObj, EnumChatFormatting.BLUE + "Price Per", 3 * this.width / 5, 50, 0xffffffff);
        this.drawString(this.fontRendererObj, EnumChatFormatting.BLUE + "Value", 4 * this.width / 5, 50, 0xffffffff);

        for (String displayString : displayStrings.keySet()) {
            double amount = displayStrings.get(displayString).quantity;
            long value = displayStrings.get(displayString).price;
            this.drawString(this.fontRendererObj, displayString, xSize, guiTop + 10 * counter, 0xffffffff);
            this.drawString(this.fontRendererObj, df.format(amount), 2 * xSize, guiTop + 10 * counter, 0xffffffff);
            this.drawString(this.fontRendererObj, Main.formatNumber(value), 3 * xSize, guiTop + 10 * counter, 0xffffffff);
            this.drawString(this.fontRendererObj, Main.formatNumber(value * amount), 4 * xSize, guiTop + 10 * counter, 0xffffffff);
            counter++;
        }

        super.drawScreen(mouseX, mouseY, partialTicks);
    }


    private void loadData() {

        displayStrings = new LinkedHashMap<>();

        for (List<Slot> slots : ChestAnalyzer.chestData.values()) {
            for (Slot slot : slots) {
                if (!slot.getHasStack() || slot.getStack().getItem() == Item.getItemFromBlock(Blocks.stained_glass_pane))
                    continue;
                AttributeValueResult valueData = AttributePrice.AttributeValue(slot.getStack());
                if (valueData == null) continue;
                String displayString = valueData.display_string;
                if (displayString.contains("Shard")) {
                    if (!showShards.isChecked()) continue;
                } else if (displayString.contains("Helmet") || displayString.contains("Chestplate") || displayString.contains("Leggings") || displayString.contains("Boots")) {
                    if (!showArmor.isChecked()) continue;
                } else {
                    if (!showEquipment.isChecked()) continue;
                }
                if (simplifyItems.isChecked()) {
                    if (Arrays.asList("LBIN", "GR", "SAL").contains(valueData.top_display)) {
                        displayStrings.put(displayString, new DisplayString(displayStrings.getOrDefault(displayString, new DisplayString(0, 0)).quantity + 1, valueData.value));
                        displayStrings.get(displayString).display_no_name = displayString;
                    } else {
                        int tier = valueData.display_string.contains("Shard") ? 4 : 5;
                        String display = AttributePrice.ShortenedAttribute(valueData.best_attribute.attribute) + " " + tier + " " + valueData.display_name;
                        if (displayStrings.get(display) == null) {
                            if (tier > valueData.best_attribute.tier) {
                                displayStrings.put(display, new DisplayString(0, valueData.value << (tier - valueData.best_attribute.tier)));
                            } else {
                                displayStrings.put(display, new DisplayString(0, valueData.value >> (valueData.best_attribute.tier - tier)));
                            }
                        }
                        displayStrings.get(display).display_no_name = AttributePrice.ShortenedAttribute(valueData.best_attribute.attribute) + " " + tier;
                        displayStrings.get(display).quantity += (double) (1 << valueData.best_attribute.tier) / (1 << tier);
                    }
                } else {
                    if (displayStrings.get(displayString) == null) {
                        displayStrings.put(displayString, new DisplayString(0, valueData.value));
                    }
                    displayStrings.get(displayString).quantity++;
                }
            }
        }
        displayStrings = ContainerValue.sort(displayStrings);
    }

    private void flipCheckBoxes(GuiCheckBox btn) {
        for (GuiCheckBox box : Arrays.asList(itemNameSorting, quantitySorting, pricePerSorting, valueSorting)) {
            if (box.id != btn.id) {
                box.setIsChecked(false);
            }
        }
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        if (!button.enabled) return;
        if (button == copyWithPrices) {
            copyToClipboard(true);
        }
        else if (button == copyWithoutPrices) {
            copyToClipboard(false);
        }
        else if (button == itemNameSorting) {
            displayStrings = sort(displayStrings, 0);
            lastSort = 0;
            flipCheckBoxes(itemNameSorting);
        }
        else if (button == quantitySorting) {
            displayStrings = sort(displayStrings, 1);
            lastSort = 1;
            flipCheckBoxes(quantitySorting);
        }
        else if (button == pricePerSorting) {
            displayStrings = sort(displayStrings, 2);
            lastSort = 2;
            flipCheckBoxes(pricePerSorting);
        }
        else if (button == valueSorting) {
            displayStrings = sort(displayStrings, 3);
            lastSort = 3;
            flipCheckBoxes(valueSorting);
        }
        else if (button == showShards || button == showArmor || button == showEquipment || button == simplifyItems) {
            loadData();
        }
        else if (button == sortOrder) {
            sortOrder.displayString = Objects.equals(sortOrder.displayString, "Sorting: Ascending") ? "Sorting: Descending" : "Sorting: Ascending";
            displayStrings = sort(displayStrings, lastSort);
        } else if (button == exit) {
            this.mc.displayGuiScreen(null);
        }

    }

    private void copyToClipboard(boolean withPrices) {
        StringBuilder shardSB = new StringBuilder();
        StringBuilder armorSB = new StringBuilder();
        StringBuilder equipSB = new StringBuilder();
        for (Map.Entry<String, DisplayString> entry : displayStrings.entrySet()) {
            String s = entry.getKey();
            DisplayString ds = entry.getValue();
            int amount = (int) ds.quantity;
            long value = ds.price;
            if (s.contains("Shard")) {
                if (simplifyItems.isChecked()) {
                    shardSB.append(ds.display_no_name.replaceAll("§.", "")).append(" x").append(amount);
                } else {
                    shardSB.append(s.replaceAll("§.", "")).append(" x").append(amount);
                }
                if (withPrices) {
                    shardSB.append(": ").append(Main.formatNumber(value)).append(" per");
                }
                shardSB.append("\n");
            } else if (s.contains("Helmet") || s.contains("Chestplate") || s.contains("Leggings") || s.contains("Boots")) {
                if (simplifyItems.isChecked()) {
                    armorSB.append(ds.display_no_name.replaceAll("§.", "")).append(" x").append(amount);
                } else {
                    armorSB.append(s.replaceAll("§.", "")).append(" x").append(amount);
                }
                if (withPrices) {
                    armorSB.append(": ").append(Main.formatNumber(value)).append(" per");
                }
                armorSB.append("\n");
            } else {
                if (simplifyItems.isChecked()) {
                    equipSB.append(ds.display_no_name.replaceAll("§.", "")).append(" x").append(amount);
                } else {
                    equipSB.append(s.replaceAll("§.", "")).append(" x").append(amount);
                }
                if (withPrices) {
                    equipSB.append(": ").append(Main.formatNumber(value)).append(" per");
                }
                equipSB.append("\n");
            }
        }

        String clipboard = "";
        if (shardSB.length() > 0) clipboard += "Shards:\n" + shardSB + "\n";
        if (armorSB.length() > 0) clipboard += "Armor:\n" + armorSB + "\n";
        if (equipSB.length() > 0) clipboard += "Equipment:\n" + equipSB;

        GuiScreen.setClipboardString(clipboard);
    }

    private LinkedHashMap<String, DisplayString> sort(LinkedHashMap<String, DisplayString> map, int sorting) {
        List<Map.Entry<String, DisplayString>> list = new ArrayList<>(map.entrySet());

        Comparator<Map.Entry<String, DisplayString>> valueComparator = null;

        switch (sorting) {
            case 0:
                valueComparator = (e1, e2) -> {
                    if (Objects.equals(sortOrder.displayString, "Sorting: Ascending")) {
                        return e1.getKey().compareTo(e2.getKey());
                    } else {
                        return e2.getKey().compareTo(e1.getKey());
                    }
                };
                break;
            case 1:
                valueComparator = (e1, e2) -> {
                    if (!Objects.equals(sortOrder.displayString, "Sorting: Ascending")) {
                        return Double.compare(e1.getValue().quantity, e2.getValue().quantity);
                    } else {
                        return Double.compare(e2.getValue().quantity, e1.getValue().quantity);
                    }
                };
                break;
            case 2:
                valueComparator = (e1, e2) -> {
                    if (!Objects.equals(sortOrder.displayString, "Sorting: Ascending")) {
                        return Long.compare(e1.getValue().price, e2.getValue().price);
                    } else {
                        return Long.compare(e2.getValue().price, e1.getValue().price);
                    }
                };
                break;
            case 3:
                valueComparator = (e1, e2) -> {
                    if (!Objects.equals(sortOrder.displayString, "Sorting: Ascending")) {
                        return Long.compare((long) (e1.getValue().quantity * e1.getValue().price), (long) (e2.getValue().quantity * e2.getValue().price));
                    } else {
                        return Long.compare((long) (e2.getValue().quantity * e2.getValue().price), (long) (e1.getValue().quantity * e1.getValue().price));
                    }
                };
                break;
        }

        list.sort(valueComparator);

        // Create a new LinkedHashMap to hold the sorted entries
        LinkedHashMap<String, DisplayString> sortedMap = new LinkedHashMap<>();

        // Populate the new LinkedHashMap with sorted entries
        for (Map.Entry<String, DisplayString> entry : list) {
            sortedMap.put(entry.getKey(), entry.getValue());
        }

        return sortedMap;
    }

}
