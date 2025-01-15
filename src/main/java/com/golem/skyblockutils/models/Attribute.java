package com.golem.skyblockutils.models;

import lombok.Getter;

public enum Attribute {
    NONE("none", "None"),
    ARACHNO("arachno", "Arachno"),
    ATTACK_SPEED("attack_speed", "Attack Speed"),
    BLAZING("blazing", "Blazing"),
    COMBO("combo", "Combo"),
    ELITE("elite", "Elite"),
    ENDER("ender", "Ender"),
    IGNITION("ignition", "Ignition"),
    LIFE_RECOVERY("life_recovery", "Life Recovery"),
    MANA_STEAL("mana_steal", "Mana Steal"),
    MIDAS_TOUCH("midas_touch", "Midas Touch"),
    UNDEAD("undead", "Undead"),
    WARRIOR("warrior", "Warrior"),
    DEADEYE("deadeye", "Deadeye"),
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
    FISHING_EXPERIENCE("fishing_experience", "Fishing Experience"),
    INFECTION("infection", "Infection"),
    DOUBLE_HOOK("double_hook", "Double Hook"),
    FISHERMAN("fisherman", "Fisherman"),
    FISHING_SPEED("fishing_speed", "Fishing Speed"),
    HUNTER("hunter", "Hunter"),
    TROPHY_HUNTER("trophy_hunter", "Trophy Hunter");


    @Getter
    private final String id;
    private final String name;

    @Override
    public String toString() {
        return name;
    }

    Attribute(String id, String name) {
        this.id = id;
        this.name = name;
    }

}
