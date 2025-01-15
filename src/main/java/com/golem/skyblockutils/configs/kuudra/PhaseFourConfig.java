package com.golem.skyblockutils.configs.kuudra;

import com.golem.skyblockutils.configs.overlays.KuudraChestConfig;
import com.google.gson.annotations.Expose;
import io.github.notenoughupdates.moulconfig.annotations.ConfigEditorBoolean;
import io.github.notenoughupdates.moulconfig.annotations.ConfigLink;
import io.github.notenoughupdates.moulconfig.annotations.ConfigOption;

public class PhaseFourConfig {

    @ConfigEditorBoolean
    @ConfigOption(name = "Show Kuudra Location", desc = "Says whether Kuudra is FRONT!, BACK!, RIGHT!, or LEFT!")
    @Expose
    public boolean showKuudraLocation = true;

    @ConfigLink(owner = KuudraChestConfig.class, field = "kuudraChestOverlay")
    @ConfigOption(name = "Kuudra Chest Overlay Settings", desc = "Opens settings related to the overlay in the Paid chest.")
    public transient boolean link;
}
