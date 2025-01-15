package com.golem.skyblockutils.configs.kuudra;

import com.golem.skyblockutils.configs.overlays.RunInfoConfig;
import com.google.gson.annotations.Expose;
import io.github.notenoughupdates.moulconfig.annotations.ConfigEditorBoolean;
import io.github.notenoughupdates.moulconfig.annotations.ConfigLink;
import io.github.notenoughupdates.moulconfig.annotations.ConfigOption;

public class KuudraConfig {

    @ConfigEditorBoolean
    @ConfigOption(name = "Display Kuudra HP on Boss", desc = "Overlays Kuudra's HP onto the Magma Cube")
    @Expose
    public boolean displayKuudraHP = true;

    @ConfigEditorBoolean
    @ConfigOption(name = "Display Kuudra HP in Boss Bar", desc = "Displays Kuudra's HP in the boss bar")
    @Expose
    public boolean displayKuudraHPBossBar = true;

    @ConfigEditorBoolean
    @ConfigOption(name = "Show Kuudra Outline", desc = "Draw a box around Kuudra.")
    @Expose
    public boolean showKuudraOutline = true;

    @ConfigEditorBoolean
    @ConfigOption(name = "Show Splits in Chat", desc = "Shows each split in chat after the split is over.")
    @Expose
    public boolean showSplitsInChat = true;

    @ConfigLink(owner = RunInfoConfig.class, field = "runInfoOverlay")
    @ConfigOption(name = "Show Run Info", desc = "Show run info overlay.")
    @Expose
    public transient boolean link;

}
