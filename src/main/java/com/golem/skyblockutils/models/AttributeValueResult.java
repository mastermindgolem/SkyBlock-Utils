package com.golem.skyblockutils.models;

import com.google.gson.JsonObject;

public class AttributeValueResult {
    public String top_display;
    public int bottom_display;
    public String display_string;
    public int value;
    public Attribute best_attribute;

    public AttributeValueResult() {
        this.top_display = "";
        this.bottom_display = 0;
        this.display_string = "";
        this.value = 0;
        this.best_attribute = null;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("top_display: ").append(top_display).append("\n");
        sb.append("bottom_display: ").append(bottom_display).append("\n");
        sb.append("display_string: ").append(display_string).append("\n");
        sb.append("value: ").append(value).append("\n");
        sb.append("best_attribute: ").append(best_attribute).append("\n");
        return sb.toString();
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
