package com.golem.skyblockutils.configs.kuudra;

import com.google.gson.annotations.Expose;
import io.github.notenoughupdates.moulconfig.ChromaColour;
import io.github.notenoughupdates.moulconfig.annotations.ConfigEditorBoolean;
import io.github.notenoughupdates.moulconfig.annotations.ConfigEditorColour;
import io.github.notenoughupdates.moulconfig.annotations.ConfigOption;

public class PhaseOneConfig {


    @ConfigEditorBoolean
    @ConfigOption(name = "Show Supply Spots", desc = "Show supply spots")
    @Expose
    public boolean safeSpots = false;
    @ConfigEditorBoolean
    @ConfigOption(name = "Show Supply Crates", desc = "Show waypoints at supply crates in lava.")
    @Expose
    public boolean showCrateWaypoint = true;

    @ConfigEditorColour
    @ConfigOption(name = "Crate Waypoint Colour", desc = "Colour of the crate waypoint.")
    @Expose
    public ChromaColour crateWaypointColour = ChromaColour.fromRGB(0, 255, 0, 0, 1);

    @ConfigEditorBoolean
    @ConfigOption(name = "Show Unfinished Supply Waypoints", desc = "Show waypoints for unfinished supply piles.")
    @Expose
    public boolean showSupplyWaypoint = true;

    @ConfigEditorColour
    @ConfigOption(name = "Supply Waypoint Colour", desc = "Colour of the supply waypoint.")
    @Expose
    public ChromaColour supplyWaypointColour = ChromaColour.fromRGB(0, 255, 0, 0, 1);

    @ConfigEditorBoolean
    @ConfigOption(name = "Show Pearl Waypoints", desc = "Highlight where to throw pearl to place supply.")
    @Expose
    public boolean showPearlWaypoint = true;

    @ConfigEditorColour
    @ConfigOption(name = "Pearl Waypoint Colour", desc = "Colour of the pearl waypoint.")
    @Expose
    public ChromaColour pearlWaypointColour = ChromaColour.fromRGB(0, 255, 0, 0, 1);


    @ConfigEditorBoolean
    @ConfigOption(name = "TAP Warning", desc = "Give a warning if no toxic/twilight arrow poison in inventory on run start.")
    @Expose
    public boolean tapWarning = true;
}
