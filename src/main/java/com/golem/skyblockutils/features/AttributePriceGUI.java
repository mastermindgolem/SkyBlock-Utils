package com.golem.skyblockutils.features;

import com.golem.skyblockutils.models.AttributeItemType;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import static com.golem.skyblockutils.models.AttributeItemType.*;

public class AttributePriceGUI extends GuiScreen {

    private static HashMap<AttributeCategory, ItemStack> categoryIcons = new HashMap<>();

//    static {
//        ItemStack armorCategory = new ItemStack(Items.leather_chestplate);
//        ((ItemArmor) armorCategory.getItem()).setColor(armorCategory, 16763661);
//        categoryIcons.put(AttributeCategory.ARMOR, armorCategory);
//        categoryIcons.put(AttributeCategory.SHARD, new ItemStack(Items.prismarine_shard));
//        try {
//            categoryIcons.put(AttributeCategory.MOLTEN_EQUIPMENT, ItemStack.loadItemStackFromNBT(JsonToNBT.getTagFromJson("{HideFlags:254,SkullOwner:{Id:\"edc180da-fb33-3fd0-9d05-52ce80874304\",Properties:{textures:[0:{Value:\"ewogICJ0aW1lc3RhbXAiIDogMTY0NzAxNjQxODA4MiwKICAicHJvZmlsZUlkIiA6ICI2NmI0ZDRlMTFlNmE0YjhjYTFkN2Q5YzliZTBhNjQ5OSIsCiAgInByb2ZpbGVOYW1lIiA6ICJBcmFzdG9vWXNmIiwKICAic2lnbmF0dXJlUmVxdWlyZWQiIDogdHJ1ZSwKICAidGV4dHVyZXMiIDogewogICAgIlNLSU4iIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzZiYzBjYTQ0YzY3OGM0ZmI0ZWJlNzNmMTI3MTdmOGFiNmRmM2I1YWI4NjhkMjE3ZTY4YzM2YWFmZTJhZDgwZTQiCiAgICB9CiAgfQp9\"}]}},display:{Lore:[0:\"§7Strength: §c+20\",1:\"§7Health: §a+30\",2:\"§7Defense: §a+20\",3:\"\",4:\"§8Tiered Bonus: Molten Core (0/4)\",5:\"§7Gain §a5% §7damage resistance\",6:\"§7while fighting §6Kuudra§7.\",7:\"\",8:\"§7§8This item can be reforged!\",9:\"§7§4❣ §cRequires §aCombat Skill 22§c.\",10:\"§5§lEPIC NECKLACE\"],Name:\"§5Molten Necklace\"},ExtraAttributes:{id:\"MOLTEN_NECKLACE\"}}")));
//        } catch (NBTException ignored) {
//            categoryIcons.put(AttributeCategory.MOLTEN_EQUIPMENT, new ItemStack(Items.apple));
//        }
//        try {
//            categoryIcons.put(AttributeCategory.OTHER_EQUIPMENT, ItemStack.loadItemStackFromNBT(JsonToNBT.getTagFromJson("{HideFlags:254,SkullOwner:{Id:\"b41b7bec-a56f-35cf-ab15-533505562b0e\",Properties:{textures:[0:{Value:\"ewogICJ0aW1lc3RhbXAiIDogMTY0NDUwMDQ1MTI0MCwKICAicHJvZmlsZUlkIiA6ICJjMDNlZTUxNjIzZTU0ZThhODc1NGM1NmVhZmJjZDA4ZSIsCiAgInByb2ZpbGVOYW1lIiA6ICJsYXltYW51ZWwiLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzdkNzViOGNmNzY1N2IwZGI3YzhmMTJjOGM3MzgzN2FhYTQxMDQ4ZTIwZTMzZmQwNTJlODU1YTQ3YmMwOTc3NiIsCiAgICAgICJtZXRhZGF0YSIgOiB7CiAgICAgICAgIm1vZGVsIiA6ICJzbGltIgogICAgICB9CiAgICB9CiAgfQp9\"}]}},display:{Lore:[0:\"§7Strength: §c+15\",1:\"§7Health: §a+20\",2:\"§7Defense: §a+10\",3:\"\",4:\"§6Ability: Contaminate\",5:\"§7Killing an enemy causes an\",6:\"§7explosion dealing §a10% §7of\",7:\"§7their total §c❤ Health §7as\",8:\"§7damage to all enemies within §a2\",9:\"§ablocks§7. Enemies in the blast\",10:\"§7radius will also be\",11:\"§7§6contaminated §7causing them to\",12:\"§7explode on death.\",13:\"\",14:\"§7§8This item can be reforged!\",15:\"§5§lEPIC GLOVES\"],Name:\"§5Gauntlet of Contagion\"},ExtraAttributes:{id:\"GAUNTLET_OF_CONTAGION\"}}")));
//        } catch (NBTException ignored) {
//            categoryIcons.put(AttributeCategory.OTHER_EQUIPMENT, new ItemStack(Items.stick));
//        }
//    }
    @Override
    public void initGui() {
        this.buttonList.add(new GuiButton(0, this.width - 100, 0, 100, 20, "Close"));
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawBackground(0);

        this.drawString(this.fontRendererObj, EnumChatFormatting.YELLOW + "Total Value: ", this.width - 200, 30, 0xffffffff);

        RenderHelper.enableGUIStandardItemLighting();

        int x = 50;
        int y = 50;
        int spacing = 20;

        for (ItemStack item : categoryIcons.values()) {
            mc.getRenderItem().renderItemAndEffectIntoGUI(item, x, y);
            mc.getRenderItem().renderItemOverlayIntoGUI(mc.fontRendererObj, item, x, y, null);
            x += spacing;
        }

        RenderHelper.disableStandardItemLighting();

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    private enum AttributeCategory {
        ARMOR(Helmet, Chestplate, Leggings, Boots),
        SHARD(Shard),
        MOLTEN_EQUIPMENT(MoltenNecklace, MoltenCloak, MoltenBelt, MoltenBracelet),
        OTHER_EQUIPMENT(LavaShellNecklace, ScourgeCloak, ImplosionBelt, GauntletOfContagion);

        private final Set<AttributeItemType> itemTypes = new HashSet<>();

        AttributeCategory(AttributeItemType... items) {
            Collections.addAll(itemTypes, items);
        }

        public Set<AttributeItemType> getTypes() {
            return itemTypes;
        }
    }
}
