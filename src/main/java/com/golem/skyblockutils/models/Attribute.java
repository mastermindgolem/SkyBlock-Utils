package com.golem.skyblockutils.models;

public class Attribute {
    public final String attribute;
    public final Integer tier;
    public final Integer price_per;

    public Attribute(String attribute, Integer tier, Integer price) {
        this.attribute = attribute;
        this.tier = tier;
        this.price_per = price >> (tier - 1);
    }

    @Override
    public String toString() {
        return attribute;
    }
}
