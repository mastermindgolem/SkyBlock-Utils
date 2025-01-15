package com.golem.skyblockutils.models;

public class AttributeInfo {
    public final String attribute;
    public final Integer tier;
    public final Long price_per;

    public AttributeInfo(String attribute, Integer tier, Long price) {
        this.attribute = attribute;
        this.tier = tier;
        this.price_per = price >> (tier - 1);
    }

    @Override
    public String toString() {
        return attribute;
    }
}
