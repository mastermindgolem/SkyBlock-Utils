package com.golem.skyblockutils.configs.kuudra;

import com.google.gson.annotations.Expose;
import io.github.notenoughupdates.moulconfig.ChromaColour;
import io.github.notenoughupdates.moulconfig.annotations.ConfigEditorBoolean;
import io.github.notenoughupdates.moulconfig.annotations.ConfigEditorColour;
import io.github.notenoughupdates.moulconfig.annotations.ConfigOption;

public class PhaseTwoConfig {

    @ConfigEditorBoolean
    @ConfigOption(name = "Show Unfinished Build Waypoints", desc = "Show waypoints for unfinished build piles.")
    @Expose
    public boolean showBuildWaypoint = true;

    @ConfigEditorBoolean
    @ConfigOption(name = "Use Gradient", desc = "Use a gradient for the waypoint colour. RED = 0%, GREEN = 100%")
    @Expose
    public boolean useGradient = true;

    @ConfigEditorColour
    @ConfigOption(name = "Build Waypoint Colour", desc = "Colour of the build piles if gradient is not used.")
    @Expose
    public ChromaColour buildWaypointColour = ChromaColour.fromRGB(0, 255, 0, 0, 1);

    @ConfigEditorBoolean
    @ConfigOption(name = "Fresh Tools Alert", desc = "Alerts you when Fresh Tools procs.")
    @Expose
    public boolean freshToolsAlert = true;

    @ConfigEditorBoolean
    @ConfigOption(name = "Notify Party of Fresh Tools", desc = "Send a message to the party when Fresh Tools procs.")
    @Expose
    public boolean notifyPartyFreshTools = true;
}
