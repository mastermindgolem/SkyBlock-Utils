package com.golem.skyblockutils.configs;

import com.golem.skyblockutils.models.Attribute;
import com.google.gson.annotations.Expose;
import io.github.notenoughupdates.moulconfig.annotations.ConfigEditorBoolean;
import io.github.notenoughupdates.moulconfig.annotations.ConfigEditorDropdown;
import io.github.notenoughupdates.moulconfig.annotations.ConfigEditorSlider;
import io.github.notenoughupdates.moulconfig.annotations.ConfigOption;

public class AuctionCategory {

    @ConfigEditorDropdown
    @ConfigOption(name = "Combine Helper", desc = "Highlights items to click to combine a certain attribute.")
    @Expose
    public Attribute combineHelper = Attribute.NONE;

    @ConfigEditorBoolean
    @ConfigOption(name = "Highlight Sell Method", desc = "Highlights items. RED = Salvage, YELLOW = cheap item, GREEN = expensive item")
    @Expose
    public boolean highlightSellMethod = false;

    @ConfigEditorSlider(minValue = 0, maxValue = 10_000_000, minStep = 1)
    @ConfigOption(name = "Cheap Item Cutoff", desc = "Items below this value will be highlighted yellow. Items above this value will be highlighted green.")
    @Expose
    public int cheapItemCutoff = 5_000_000;

    @ConfigEditorBoolean
    @ConfigOption(name = "Auction Helper", desc = "Shows various values of the item in the auctioning sign. Click a line to enter its price in the sign")
    @Expose
    public boolean auctionHelper = false;

    @ConfigEditorSlider(minValue = 0, maxValue = 10_000_000, minStep = 1)
    @ConfigOption(name = "Undercut by (Coins)", desc = "Undercut by how many coins, 0 = off")
    @Expose
    public int undercutCoins = 0;

    @ConfigEditorSlider(minValue = 0, maxValue = 100, minStep = 1)
    @ConfigOption(name = "Undercut By Percent", desc = "Undercut by a certain %, empty = off")
    @Expose
    public int undercutPercent = 0;

    @ConfigEditorBoolean
    @ConfigOption(name = "Sorting Helper", desc = "Helps you sort your loot by telling you which chests to open and what items to put in.")
    @Expose
    public boolean sortingHelper = false;
}
