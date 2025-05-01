package com.golem.skyblockutils.models;

import com.google.gson.annotations.SerializedName;

public class CustomItem {
    @SerializedName("newItem")
    public String newItem = "";
    @SerializedName("newName")
    public String newName = "";
    @SerializedName("newColor")
    public int newColor = -1;
    @SerializedName("newGlint")
    public int newGlint = 0;
}
