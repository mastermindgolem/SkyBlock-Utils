package com.golem.skyblockutils.configs;

import com.google.gson.annotations.Expose;
import io.github.notenoughupdates.moulconfig.ChromaColour;
import io.github.notenoughupdates.moulconfig.annotations.ConfigEditorBoolean;
import io.github.notenoughupdates.moulconfig.annotations.ConfigEditorColour;
import io.github.notenoughupdates.moulconfig.annotations.ConfigEditorSlider;
import io.github.notenoughupdates.moulconfig.annotations.ConfigOption;

public class GeneralCategory {

    @ConfigEditorBoolean
    @ConfigOption(name = "Hide Sack Messages", desc = "Hides messages related to items entering/exiting sacks.")
    @Expose
    public boolean hideSackMessages;

    @ConfigEditorBoolean
    @ConfigOption(name = "Show Elite 500", desc = "Show player's SkyBlock level leaderboard position.")
    @Expose
    public boolean elite500;

    @ConfigEditorSlider(minValue = 0, maxValue = 60, minStep = 1)
    @ConfigOption(name = "Show Chat Waypoints", desc = "Select delay before waypoints when coordinates sent in chat disappear. 0 = off")
    @Expose
    public int showWaypoints = 0;

    @ConfigEditorColour
    @ConfigOption(name = "Waypoint Color", desc = "Color of the waypoint text.")
    @Expose
    public ChromaColour waypointColor = ChromaColour.fromRGB(0, 255, 0, 0, 1);

    @ConfigEditorBoolean
    @ConfigOption(name = "Remove Selfie Mode", desc = "Remove Selfie mode when toggling perspective")
    @Expose
    public boolean removeSelfie = false;

    @ConfigEditorBoolean
    @ConfigOption(name = "Use Custom Emotes", desc = "Allows you to use MVP++/Gifting emotes without the requirements.")
    @Expose
    public boolean customEmotes = false;
}
