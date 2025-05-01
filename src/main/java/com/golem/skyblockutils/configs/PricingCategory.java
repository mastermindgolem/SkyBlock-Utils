package com.golem.skyblockutils.configs;

import com.golem.skyblockutils.models.ArmorAttribute;
import com.golem.skyblockutils.models.Attribute;
import com.google.gson.annotations.Expose;
import io.github.notenoughupdates.moulconfig.annotations.*;

import java.util.Arrays;
import java.util.List;

public class PricingCategory {

    @ConfigEditorSlider(minValue = 0, maxValue = 30, minStep = 1)
    @ConfigOption(name = "Time between AH checks (in minutes)", desc = "How often prices should be refreshed. 0 to turn off")
    @Expose
    public int timeBetweenChecks = 10;

    @ConfigEditorSlider(minValue = 0, maxValue = 10, minStep = 1)
    @ConfigOption(name = "Armor/Eq Min. Tier", desc = "Minimum tier for armor and equipment to use when pricing items.")
    @Expose
    public int minArmorTier = 4;

    @ConfigEditorSlider(minValue = 0, maxValue = 10, minStep = 1)
    @ConfigOption(name = "Shard Min. Tier", desc = "Minimum tier for attribute shards to use when pricing items")
    @Expose
    public int minShardTier = 4;

    @ConfigEditorBoolean
    @ConfigOption(name = "Value STARRED items", desc = "Whether or not to value starred attribute items.")
    @Expose
    public boolean valueStarred = true;

    @ConfigEditorSlider(minValue = 0, maxValue = 10, minStep = 1)
    @ConfigOption(name = "Maximum attribute tier to value", desc = "Set this to a lower attribute tier if you don't want the mod to value higher tier items.")
    @Expose
    public int maxTier = 10;

    @ConfigEditorBoolean
    @ConfigOption(name = "Show Item Value in Lore", desc = "Shows the item's value according to the mod at the bottom of the item's lore.")
    @Expose
    public boolean showValueInLore = true;

    @ConfigEditorBoolean
    @ConfigOption(name = "Show Attribute Overlay", desc = "Show attribute overlay.")
    @Expose
    public boolean showAttributeOverlay = true;

    @ConfigEditorBoolean
    @ConfigOption(name = "Show LBIN Overlay", desc = "Whether or not to show 'LBIN' on items that are only worth lowest BIN.")
    @Expose
    public boolean showLbinOverlay = true;

    @ConfigEditorBoolean
    @ConfigOption(name = "Show SALVAGE Overlay", desc = "Whether or not to show 'SALV' on items that are worth BIN.")
    @Expose
    public boolean showSalvageOverlay = true;

    @ConfigEditorDraggableList
    @ConfigOption(name = "Armor Attributes to exclude", desc = "Excludes these attributes from being priced at all on armors.")
    @Expose
    public List<ArmorAttribute> armorAttributesToExclude = Arrays.asList(ArmorAttribute.EXPERIENCE, ArmorAttribute.ARACHNO_RESISTANCE, ArmorAttribute.ENDER_RESISTANCE, ArmorAttribute.BLAZING_RESISTANCE, ArmorAttribute.LIFE_REGENERATION, ArmorAttribute.SPEED);

    @ConfigEditorDraggableList
    @ConfigOption(name = "Equipment Attributes to exclude", desc = "Excludes these attributes from being priced at all on equipments.")
    @Expose
    public List<ArmorAttribute> equipmentAttributesToExclude = Arrays.asList(ArmorAttribute.EXPERIENCE, ArmorAttribute.ARACHNO_RESISTANCE, ArmorAttribute.ENDER_RESISTANCE, ArmorAttribute.BLAZING_RESISTANCE, ArmorAttribute.LIFE_REGENERATION);

    @ConfigEditorDraggableList
    @ConfigOption(name = "Shard Attributes to exclude", desc = "Excludes these attributes from being priced at all on shards.")
    @Expose
    public List<Attribute> shardAttributesToExclude = Arrays.asList(Attribute.EXPERIENCE, Attribute.ARACHNO_RESISTANCE, Attribute.ENDER_RESISTANCE, Attribute.BLAZING_RESISTANCE, Attribute.LIFE_REGENERATION);

    @ConfigEditorSlider(minValue = 0, maxValue = 300, minStep = 1)
    @ConfigOption(name = "Min. Price for Godrolls", desc = "Items above this price (in millions) will be marked as a godroll.")
    @Expose
    public int minGodrollPrice = 50;

    @ConfigEditorDropdown
    @ConfigOption(name = "Kuudra Pet Rarity", desc = "Enter Kuudra Pet Rarity to include the extra essence in calculation")
    @Expose
    public Rarity kuudraPetRarity = Rarity.COMMON;

    @ConfigEditorSlider(minValue = 0, maxValue = 100, minStep = 1)
    @ConfigOption(name = "Kuudra Pet Level", desc = "Enter Kuudra Pet Level to include the extra essence in calculation")
    @Expose
    public int kuudraPetLevel = 0;

    @ConfigEditorDropdown
    @ConfigOption(name = "Book Valuation", desc = "Choose how enchanted books are valued.")
    @Expose
    public BookSellMethod bookSellMethod = BookSellMethod.INSTANT_SELL;

    @ConfigEditorBoolean
    @ConfigOption(name = "Sell Essence", desc = "Choose whether essence is considered in the value and profit of a chest.")
    @Expose
    public boolean considerEssenceValue = true;

    @ConfigEditorDropdown
    @ConfigOption(name = "Faction", desc = "Needed to calculate key cost for kuudra.")
    @Expose
    public Faction faction = Faction.CHEAPEST;


    public enum Rarity {
        COMMON("Common"),
        UNCOMMON("Uncommon"),
        RARE("Rare"),
        EPIC("Epic"),
        LEGENDARY("Legendary");

        private final String name;

        @Override
        public String toString() {
            return name;
        }

        Rarity(String name) {
            this.name = name;
        }
    }

    public enum BookSellMethod {
        INSTANT_SELL("Instant Sell"),
        SELL_OFFER("Sell Offer"),
        AVERAGE("Average");

        private final String name;

        @Override
        public String toString() {
            return name;
        }

        BookSellMethod(String name) {
            this.name = name;
        }
    }

    public enum Faction {
        MAGE("Mage"),
        BARBARIAN("Barbarian"),
        CHEAPEST("Cheapest");

        private final String name;

        @Override
        public String toString() {
            return name;
        }

        Faction(String name) {
            this.name = name;
        }
    }
}
