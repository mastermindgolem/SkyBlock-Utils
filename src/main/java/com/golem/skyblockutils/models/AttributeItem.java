package com.golem.skyblockutils.models;


import com.golem.skyblockutils.utils.AttributeUtils;
import com.google.gson.JsonObject;
import lombok.Getter;
import net.minecraft.nbt.NBTTagCompound;

import java.util.HashMap;
import java.util.stream.Collectors;

public class AttributeItem {
    public String item_name;
    public String item_id;
    public String item_lore;
    public AttributeItemType item_type;
    public HashMap<String, Integer> attributes = new HashMap<>();
    @Getter
    public String comboString;
    public String uuid;

    public AttributeItem(String item_name, String item_lore, NBTTagCompound ExtraAttributes) {
        if (!ExtraAttributes.hasKey("attributes")) return;
        this.item_type = AttributeUtils.getItemType(ExtraAttributes.getString("id"));
        this.item_id = ExtraAttributes.getString("id");
        if (this.item_type == null) return;

        NBTTagCompound attributeData = ExtraAttributes.getCompoundTag("attributes");
        for (String attribute : attributeData.getKeySet()) {
            this.attributes.put(attribute, attributeData.getInteger(attribute));
        }
        if (this.attributes.size() == 2) {
            this.comboString = item_type + "_" + this.attributes.keySet().stream().sorted().collect(Collectors.joining("_"));
            AttributeArmorType armorType = AttributeUtils.getArmorVariation(ExtraAttributes.getString("id"));
            if (armorType != null) this.comboString = armorType.getID() + "_" + this.comboString;
        } else {
            this.comboString = null;
        }
        this.item_name = item_name;
        this.item_lore = item_name + "\n" + item_lore;
        this.uuid = ExtraAttributes.getString("uuid");
    }

    public AttributeItem(JsonObject auctionData) {
        this(auctionData.get("item_name").getAsString(), auctionData.get("item_lore").getAsString(), AttributeUtils.getExtraAttributes(auctionData.get("item_bytes").getAsString()));
    }

    public AttributeItemType getItemType() {
        return item_type;
    }

    @Override
    public String toString() {
        return item_name + " " + item_type + " " + attributes.toString() + " " + this.comboString;
    }
}
