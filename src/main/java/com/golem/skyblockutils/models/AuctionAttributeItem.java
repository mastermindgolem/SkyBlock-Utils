package com.golem.skyblockutils.models;

import com.google.gson.JsonObject;

import java.util.HashMap;

public class AuctionAttributeItem extends AttributeItem {
    public Long price;
    public String viewauctionID;
    public HashMap<String, Attribute> attributeInfo = new HashMap<>();
    public String tier;

    public AuctionAttributeItem(JsonObject auction_data) {
        super(auction_data);

        if (!auction_data.get("bin").getAsBoolean()) return;
        this.price = auction_data.get("starting_bid").getAsLong();
        this.viewauctionID = auction_data.get("uuid").getAsString();
        this.tier = auction_data.get("tier").getAsString();
    }

    public Attribute addAttribute(String attribute) {
        attributeInfo.put(attribute, new Attribute(attribute, attributes.get(attribute), price));
        return attributeInfo.get(attribute);
    }

}
