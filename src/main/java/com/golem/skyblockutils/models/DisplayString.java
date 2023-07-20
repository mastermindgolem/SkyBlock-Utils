package com.golem.skyblockutils.models;

public class DisplayString {
    private int quantity;
    private long price;

    public DisplayString(int quantity, long price) {
        this.quantity = quantity;
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
