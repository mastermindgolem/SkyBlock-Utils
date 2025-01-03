package com.golem.skyblockutils.models;

import net.minecraft.inventory.Slot;

public class DisplayString {
    public double quantity;
    public long price;
    public long median;
    public Slot slot;
    public String display_no_name;

    public DisplayString(double quantity, long price) {
        this.quantity = quantity;
        this.price = price;
        this.median = 0;
        this.slot = null;
    }
    public DisplayString(double quantity, long price, long median) {
        this.quantity = quantity;
        this.price = price;
        this.median = median;
        this.slot = null;
    }

    public DisplayString(double quantity, long price, long median, Slot slot) {
        this.quantity = quantity;
        this.price = price;
        this.median = median;
        this.slot = slot;
    }
}
