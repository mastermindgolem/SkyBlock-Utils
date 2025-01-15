package com.golem.skyblockutils.models;

import com.google.gson.JsonObject;

public class AttributeValueResult {
    public String top_display;
    public int bottom_display;
    public String display_string;
    public long value;
    public AttributeInfo best_attribute;
    public String display_name;
    public String item_id;

    public AttributeValueResult() {
        this.top_display = "";
        this.bottom_display = 0;
        this.display_string = "";
        this.value = 0;
        this.best_attribute = null;
        this.display_name = "";
    }

    public AttributeValueResult(String display_string, long value) {
        this.display_string = display_string;
        this.value = value;
    }

    @Override
    public String toString() {
        return "top_display: " + top_display + "\n" +
                "bottom_display: " + bottom_display + "\n" +
                "display_string: " + display_string + "\n" +
                "value: " + value + "\n" +
                "best_attribute: " + best_attribute + "\n";
    }

    public JsonObject toJson() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("top_display", top_display);
        jsonObject.addProperty("bottom_display", bottom_display);
        jsonObject.addProperty("display_string", display_string);
        jsonObject.addProperty("value", value);
        return jsonObject;
    }
}
