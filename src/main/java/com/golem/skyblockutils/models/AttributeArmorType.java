package com.golem.skyblockutils.models;

import lombok.Getter;

@Getter
public enum AttributeArmorType {
    Crimson("Crimson", "CRIMSON"),
    Aurora("Aurora", "AURORA"),
    Terror("Terror", "TERROR"),
    Fervor("Fervor", "FERVOR"),
    Hollow("Hollow", "HOLLOW");

    private final String display;
    private final String ID;
    AttributeArmorType(String display, String ID) {
        this.display = display;
        this.ID = ID;
    }
}
