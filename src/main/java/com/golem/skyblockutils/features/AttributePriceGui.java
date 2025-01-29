package com.golem.skyblockutils.features;

import com.golem.skyblockutils.Main;
import com.golem.skyblockutils.models.*;
import com.golem.skyblockutils.utils.AttributeUtils;
import com.golem.skyblockutils.utils.ToolTipListener;
import com.golem.skyblockutils.utils.rendering.Renderable;
import com.golem.skyblockutils.utils.rendering.RenderableItem;
import com.golem.skyblockutils.utils.rendering.RenderableString;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.init.Items;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.util.EnumChatFormatting;

import java.util.*;
import java.util.stream.Collectors;

import static com.golem.skyblockutils.Main.coolFormat;
import static com.golem.skyblockutils.models.AttributeItemType.*;
import static com.golem.skyblockutils.utils.Colors.getRarityCode;

public class AttributePriceGui extends GuiScreen {

    public static final String[] all_attributes = new String[]{"arachno", "attack_speed", "combo", "elite", "ignition", "lifeline", "breeze", "speed", "experience", "mana_pool", "life_regeneration", "blazing_resistance", "arachno_resistance", "undead_resistance", "blazing_fortune", "fishing_experience", "double_hook", "infection", "trophy_hunter", "fisherman", "hunter", "fishing_speed", "life_recovery", "midas_touch", "mana_regeneration", "veteran", "mending", "ender_resistance", "dominance", "mana_steal", "ender", "blazing", "undead", "warrior", "deadeye", "fortitude", "magic_find"};
    public static final String[] armor_attributes = new String[]{"lifeline", "breeze", "speed", "experience", "mana_pool", "life_regeneration", "blazing_resistance", "arachno_resistance", "undead_resistance", "mana_regeneration", "veteran", "mending", "ender_resistance", "dominance", "fortitude", "magic_find"};
    private GuiButton exit;
    private static AttributeCategory selected_category = null;
    private static Attribute attribute1 = null;
    private static Attribute attribute2 = null;
    private static final ArrayList<Boolean> tiers = new ArrayList<>(Collections.nCopies(10, true));
    private static final List<Renderable> renderables = new ArrayList<>();
    private static final List<Renderable> renderables1 = new ArrayList<>();


    private static final LinkedHashMap<AttributeCategory, ItemStack> categoryIcons = new LinkedHashMap<>();

    static {
        ItemStack armorCategory = new ItemStack(Items.leather_chestplate);
        ((ItemArmor) armorCategory.getItem()).setColor(armorCategory, 16763661);
        categoryIcons.put(AttributeCategory.ARMOR, armorCategory);
        categoryIcons.put(AttributeCategory.SHARD, new ItemStack(Items.prismarine_shard));
        try {
            categoryIcons.put(AttributeCategory.MOLTEN_EQUIPMENT, ItemStack.loadItemStackFromNBT(JsonToNBT.getTagFromJson("{id:\"minecraft:skull\",Count:1b,tag:{HideFlags:254,SkullOwner:{Id:\"edc180da-fb33-3fd0-9d05-52ce80874304\",Properties:{textures:[0:{Value:\"ewogICJ0aW1lc3RhbXAiIDogMTY0NzAxNjQxODA4MiwKICAicHJvZmlsZUlkIiA6ICI2NmI0ZDRlMTFlNmE0YjhjYTFkN2Q5YzliZTBhNjQ5OSIsCiAgInByb2ZpbGVOYW1lIiA6ICJBcmFzdG9vWXNmIiwKICAic2lnbmF0dXJlUmVxdWlyZWQiIDogdHJ1ZSwKICAidGV4dHVyZXMiIDogewogICAgIlNLSU4iIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzZiYzBjYTQ0YzY3OGM0ZmI0ZWJlNzNmMTI3MTdmOGFiNmRmM2I1YWI4NjhkMjE3ZTY4YzM2YWFmZTJhZDgwZTQiCiAgICB9CiAgfQp9\"}]}},display:{Lore:[0:\"§7Strength: §c+20\",1:\"§7Health: §a+30\",2:\"§7Defense: §a+20\",3:\"\",4:\"§8Tiered Bonus: Molten Core (0/4)\",5:\"§7Gain §a5% §7damage resistance\",6:\"§7while fighting §6Kuudra§7.\",7:\"\",8:\"§7§8This item can be reforged!\",9:\"§7§4❣ §cRequires §aCombat Skill 22§c.\",10:\"§5§lEPIC NECKLACE\"],Name:\"§5Molten Necklace\"},ExtraAttributes:{id:\"MOLTEN_NECKLACE\"}}, Damage:3s}")));
        } catch (Exception ignored) {
            categoryIcons.put(AttributeCategory.MOLTEN_EQUIPMENT, new ItemStack(Items.apple));
        }
        try {
            categoryIcons.put(AttributeCategory.OTHER_EQUIPMENT, ItemStack.loadItemStackFromNBT(JsonToNBT.getTagFromJson("{id:\"minecraft:skull\",Count:1b,tag:{HideFlags:254,SkullOwner:{Id:\"b41b7bec-a56f-35cf-ab15-533505562b0e\",Properties:{textures:[0:{Value:\"ewogICJ0aW1lc3RhbXAiIDogMTY0NDUwMDQ1MTI0MCwKICAicHJvZmlsZUlkIiA6ICJjMDNlZTUxNjIzZTU0ZThhODc1NGM1NmVhZmJjZDA4ZSIsCiAgInByb2ZpbGVOYW1lIiA6ICJsYXltYW51ZWwiLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzdkNzViOGNmNzY1N2IwZGI3YzhmMTJjOGM3MzgzN2FhYTQxMDQ4ZTIwZTMzZmQwNTJlODU1YTQ3YmMwOTc3NiIsCiAgICAgICJtZXRhZGF0YSIgOiB7CiAgICAgICAgIm1vZGVsIiA6ICJzbGltIgogICAgICB9CiAgICB9CiAgfQp9\"}]}},display:{Lore:[0:\"§7Strength: §c+15\",1:\"§7Health: §a+20\",2:\"§7Defense: §a+10\",3:\"\",4:\"§6Ability: Contaminate\",5:\"§7Killing an enemy causes an\",6:\"§7explosion dealing §a10% §7of\",7:\"§7their total §c❤ Health §7as\",8:\"§7damage to all enemies within §a2\",9:\"§ablocks§7. Enemies in the blast\",10:\"§7radius will also be\",11:\"§7§6contaminated §7causing them to\",12:\"§7explode on death.\",13:\"\",14:\"§7§8This item can be reforged!\",15:\"§5§lEPIC GLOVES\"],Name:\"§5Gauntlet of Contagion\"},ExtraAttributes:{id:\"GAUNTLET_OF_CONTAGION\"}}, Damage:3s}")));
        } catch (NBTException ignored) {
            categoryIcons.put(AttributeCategory.OTHER_EQUIPMENT, new ItemStack(Items.stick));
        }
        System.out.println(categoryIcons);
    }
    @Override
    public void initGui() {
        this.buttonList.add(exit = new GuiButton(0, this.width - 100, 0, 100, 20, "Close"));
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawBackground(0);

        RenderHelper.enableGUIStandardItemLighting();

        int x = 50;
        int y = 20;
        int spacing = 10;

        renderables.clear();

        for (int i = 0; i < 10; i++) {
            boolean tier_selected = tiers.get(i);
            int finalI = i;
            String display = (tier_selected ? EnumChatFormatting.GREEN : EnumChatFormatting.RED) + String.valueOf(i + 1);
            renderables.add(new RenderableString(display, x, y).onClick(() -> {
                tiers.set(finalI, !tier_selected);
                loadData();
            }));
            x += spacing;
        }

        x = 50;
        y = 50;
        spacing = 20;

        for (Map.Entry<AttributeCategory, ItemStack> entry : categoryIcons.entrySet()) {
            ItemStack item = entry.getValue();
            AttributeCategory category = entry.getKey();
            if (selected_category == category) {
                renderables.add(new RenderableItem(item, x, y, true).onClick(() -> {
                    selected_category = category;
                    loadData();
                }));
            } else {
                renderables.add(new RenderableItem(item, x, y).onClick(() -> {
                    selected_category = category;
                    loadData();
                }));
            }
            x += spacing;
        }

        x = 50;
        y = 80;
        spacing = 10;

        for (String attribute : (selected_category == null || selected_category == AttributeCategory.SHARD ? all_attributes : armor_attributes)) {
            boolean selected = (attribute1 != null && attribute.equals(attribute1.attribute)) || (attribute2 != null && attribute.equals(attribute2.attribute));

            String displayAttribute = ToolTipListener.TitleCase(attribute);

            if (selected) displayAttribute = EnumChatFormatting.GREEN + displayAttribute;

            String finalAttribute = displayAttribute;
            renderables.add(new RenderableString(finalAttribute, x, y).onClick(() -> {
                if (attribute1 != null && attribute1.attribute.equals(attribute)) {
                    attribute1 = attribute2;
                    attribute2 = null;
                }
                else if (attribute2 != null && attribute2.attribute.equals(attribute)) {
                    attribute2 = null;
                }
                else if (attribute1 == null) {
                    attribute1 = new Attribute(attribute, 1, 0L);
                } else {
                    if (selected_category == AttributeCategory.SHARD) attribute1 = new Attribute(attribute, 1, 0L);
                    else attribute2 = new Attribute(attribute, 1, 0L);
                }
                loadData();
            }));
            y += spacing;
        }

        try {
            renderables.addAll(renderables1);
            renderables.forEach(renderable -> renderable.render(mouseX, mouseY));
        } catch (ConcurrentModificationException ignored) {
        }

        RenderHelper.disableStandardItemLighting();

        try {
            renderables.forEach(renderable -> renderable.renderTooltip(mouseX, mouseY));
        } catch (ConcurrentModificationException ignore) {
        }

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        if (button == exit) {
            mc.displayGuiScreen(null);
        }
    }

    private static void loadData() {
        renderables1.clear();
        int x = 250;
        int y = 50;
        int spacing = 10;
        if (attribute1 == null) return;
        if (selected_category == null) return;
        if (attribute2 == null) { // Only 1 attribute
            for (AttributeItemType itemType : selected_category.getTypes()) {
                renderables1.add(new RenderableString(EnumChatFormatting.GREEN + itemType.getDisplay(), x, y));
                y += spacing;
                List<AuctionAttributeItem> items = AttributePrice.AttributePrices.get(itemType).get(attribute1.attribute);
                if (items == null) continue;
                items = items.stream().filter(item -> tiers.get(item.attributes.get(attribute1.attribute) - 1)).sorted(Comparator.comparingDouble(o -> o.attributeInfo.get(attribute1.attribute).price_per)).limit(5).collect(Collectors.toList());
                for (AuctionAttributeItem item : items) {
                    renderables1.add(new RenderableString(getRarityCode(item.tier) + item.item_name + " - " + EnumChatFormatting.YELLOW + coolFormat(item.attributeInfo.get(attribute1.attribute).price_per, 0) + EnumChatFormatting.RESET + " - " + EnumChatFormatting.GREEN + coolFormat(item.price,0), x, y).onClick(() -> Main.mc.thePlayer.sendChatMessage("/viewauction " + item.viewauctionID)).setHoverText(getRarityCode(item.tier) + item.item_lore));
                    y += spacing;
                }
            }
        } else {
            if (selected_category == AttributeCategory.ARMOR) {
                for (AttributeArmorType armorType : AttributeArmorType.values()) {
                    renderables1.add(new RenderableString(EnumChatFormatting.GREEN + armorType.getDisplay(), x, y));
                    for (AttributeItemType itemType : selected_category.getTypes()) {
                        y += spacing;
                        List<AuctionAttributeItem> items = AttributePrice.AttributePrices.get(itemType).get(attribute1.attribute);
                        if (items == null) continue;
                        items = items.stream().filter(item -> AttributeUtils.getArmorVariation(item.item_id) == armorType && item.attributes.containsKey(attribute2.attribute) && tiers.get(item.attributes.get(attribute1.attribute) - 1) && tiers.get(item.attributes.get(attribute2.attribute) - 1)).sorted(Comparator.comparingDouble(o -> o.price)).limit(1).collect(Collectors.toList());
                        for (AuctionAttributeItem item : items) {
                            renderables1.add(new RenderableString(getRarityCode(item.tier) + item.item_name + EnumChatFormatting.RESET + " - " + EnumChatFormatting.GREEN + coolFormat(item.price,0), x, y).onClick(() -> Main.mc.thePlayer.sendChatMessage("/viewauction " + item.viewauctionID)).setHoverText(getRarityCode(item.tier) + item.item_lore));
                            y += spacing;
                        }
                    }
                }
            } else {
                for (AttributeItemType itemType : selected_category.getTypes()) {
                    renderables1.add(new RenderableString(EnumChatFormatting.GREEN + itemType.getDisplay(), x, y));
                    y += spacing;
                    List<AuctionAttributeItem> items = AttributePrice.AttributePrices.get(itemType).get(attribute1.attribute);
                    if (items == null) continue;
                    items = items.stream().filter(item -> item.attributes.containsKey(attribute2.attribute) && tiers.get(item.attributes.get(attribute1.attribute) - 1) && tiers.get(item.attributes.get(attribute2.attribute) - 1)).sorted(Comparator.comparingDouble(o -> o.price)).limit(5).collect(Collectors.toList());
                    for (AuctionAttributeItem item : items) {
                        renderables1.add(new RenderableString(getRarityCode(item.tier) + item.item_name + EnumChatFormatting.RESET + " - " + EnumChatFormatting.GREEN + coolFormat(item.price,0), x, y).onClick(() -> Main.mc.thePlayer.sendChatMessage("/viewauction " + item.viewauctionID)).setHoverText(getRarityCode(item.tier) + item.item_lore));
                        y += spacing;
                    }
                }
            }
        }
    }

    private enum AttributeCategory {
        ARMOR(Helmet, Chestplate, Leggings, Boots),
        SHARD(Shard),
        MOLTEN_EQUIPMENT(MoltenNecklace, MoltenCloak, MoltenBelt, MoltenBracelet),
        OTHER_EQUIPMENT(LavaShellNecklace, ScourgeCloak, ImplosionBelt, GauntletOfContagion);

        private final List<AttributeItemType> itemTypes = new ArrayList<>();

        AttributeCategory(AttributeItemType... items) {
            Collections.addAll(itemTypes, items);
        }

        public List<AttributeItemType> getTypes() {
            return itemTypes;
        }
    }
}