package com.golem.skyblockutils.models;

import lombok.Getter;

public enum ArmorAttribute {
    ARACHNO_RESISTANCE("arachno_resistance", "Arachno Resistance"),
    BLAZING_RESISTANCE("blazing_resistance", "Blazing Resistance"),
    BREEZE("breeze", "Breeze"),
    DOMINANCE("dominance", "Dominance"),
    ENDER_RESISTANCE("ender_resistance", "Ender Resistance"),
    EXPERIENCE("experience", "Experience"),
    FORTITUDE("fortitude", "Fortitude"),
    LIFE_REGENERATION("life_regeneration", "Life Regeneration"),
    LIFELINE("lifeline", "Lifeline"),
    MAGIC_FIND("magic_find", "Magic Find"),
    MANA_POOL("mana_pool", "Mana Pool"),
    MANA_REGENERATION("mana_regeneration", "Mana Regeneration"),
    VITALITY("mending", "Vitality"),
    SPEED("speed", "Speed"),
    UNDEAD_RESISTANCE("undead_resistance", "Undead Resistance"),
    VETERAN("veteran", "Veteran"),
    BLAZING_FORTUNE("blazing_fortune", "Blazing Fortune"),
    FISHING_EXPERIENCE("fishing_experience", "Fishing Experience");

    @Getter
    private final String id;
    private final String name;

    @Override
    public String toString() {
        return name;
    }

    ArmorAttribute(String id, String name) {
        this.id = id;
        this.name = name;
    }

}
