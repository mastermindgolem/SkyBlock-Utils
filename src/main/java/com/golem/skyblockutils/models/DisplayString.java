package com.golem.skyblockutils.models;

public class DisplayString {
    public int quantity;
    public long price;
    public long median;

    public DisplayString(int quantity, long price) {
        this.quantity = quantity;
        this.price = price;
        this.median = 0;
    }
    public DisplayString(int quantity, long price, long median) {
        this.quantity = quantity;
        this.price = price;
        this.median = median;
    }
}
