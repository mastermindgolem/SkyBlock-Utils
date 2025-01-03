package com.golem.skyblockutils.models;

import lombok.Getter;

@Getter
public enum AttributeItemType {
    Helmet("Helmet", "HELMET"),
    Chestplate("Chestplate", "CHESTPLATE"),
    Leggings("Leggings", "LEGGINGS"),
    Boots("Boots", "BOOTS"),
    Shard("Attribute Shard", "SHARD"),
    MoltenBelt("Molten Belt", "MOLTEN_BELT"),
    MoltenBracelet("Molten Bracelet", "MOLTEN_BRACELET"),
    MoltenCloak("Molten Cloak", "MOLTEN_CLOAK"),
    MoltenNecklace("Molten Necklace", "MOLTEN_NECKLACE"),
    GauntletOfContagion("Gauntlet Of Contagion", "GAUNTLET_OF_CONTAGION"),
    ImplosionBelt("Implosion Belt", "IMPLOSION_BELT"),
    MagmaNecklace("Magma Necklace", "MAGMA_NECKLACE"),
    GhastCloak("Ghast Cloak", "GHAST_CLOAK"),
    BlazeBelt("Blaze Belt", "BLAZE_BELT"),
    GlowstoneGauntlet("Glowstone Gauntlet", "GLOWSTONE_GAUNTLET"),
    LavaShellNecklace("Lava Shell Necklace", "LAVA_SHELL_NECKLACE"),
    ScourgeCloak("Scourge Cloak", "SCOURGE_CLOAK"),
    FishingHelmet("Taurus Helmet", "TAURUS_HELMET"),
    FishingChestplate("Flaming Chestplate", "FLAMING_CHESTPLATE"),
    FishingLeggings("Moogma Leggings", "MOOGMA_LEGGINGS"),
    FishingBoots("Slug Boots", "SLUG_BOOTS");

    private final String display;
    @Getter
    private final String ID;

    AttributeItemType(String display, String ID) {
        this.display = display;
        this.ID = ID;
    }

    @Override
    public String toString() {
        return display.toUpperCase().replace(" ", "_");
    }
}
