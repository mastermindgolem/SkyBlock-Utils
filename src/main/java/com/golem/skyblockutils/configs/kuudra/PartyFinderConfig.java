package com.golem.skyblockutils.configs.kuudra;

import com.google.gson.annotations.Expose;
import io.github.notenoughupdates.moulconfig.annotations.ConfigEditorBoolean;
import io.github.notenoughupdates.moulconfig.annotations.ConfigEditorDropdown;
import io.github.notenoughupdates.moulconfig.annotations.ConfigEditorSlider;
import io.github.notenoughupdates.moulconfig.annotations.ConfigOption;
import lombok.Getter;

public class PartyFinderConfig {
    @ConfigEditorBoolean
    @ConfigOption(name = "Show Player Info", desc = "Show player's info when they join party.")
    @Expose
    public boolean showPlayerInfo = true;

    @ConfigEditorBoolean
    @ConfigOption(name = "Auto-kick Players", desc = "Auto-kick players who don't meet the requirements set below.")
    @Expose
    public boolean autoKickPlayers = false;

    @ConfigEditorSlider(minValue = 0, maxValue = 3000, minStep = 1)
    @ConfigOption(name = "Minimum Kuudra Level", desc = "Minimum Kuudra level according to Kuudra Gang for player to join party.")
    @Expose
    public int minKuudraLevel = 0;

    @ConfigEditorSlider(minValue = 0, maxValue = 2000, minStep = 1)
    @ConfigOption(name = "Minimum Magical Power", desc = "Minimum Magical Power for player to join party.")
    @Expose
    public int minMagicalPower = 0;

    @ConfigEditorDropdown
    @ConfigOption(name = "Minimum Runs (Tier)", desc = "Choose the tier of Kuudra to use for the next config option.")
    @Expose
    public KuudraTier minRunsTier = KuudraTier.NONE;

    @ConfigEditorSlider(minValue = 0, maxValue = 10000, minStep = 1)
    @ConfigOption(name = "Minimum Runs", desc = "Minimum runs for player to join party.")
    @Expose
    public int minRuns = 0;

    @ConfigEditorDropdown
    @ConfigOption(name = "Minimum Terror Tier", desc = "Choose how upgraded the player's Terror must be to join party.")
    @Expose
    public KuudraTier minTerrorTier = KuudraTier.NONE;

    @ConfigEditorDropdown
    @ConfigOption(name = "Minimum RCM Set Tier", desc = "Choose how upgraded the player's Aurora/RCM Terror must be to join party.")
    @Expose
    public KuudraTier minRCMTier = KuudraTier.NONE;

    @ConfigEditorSlider(minValue = 0, maxValue = 175, minStep = 1)
    @ConfigOption(name = "Minimum Damage Buff (Lifeline/Dominance)", desc = "Minimum damage bonus from lifeline/dominance. Dominance gives +1.5% per level, Lifeline gives +2.5% per level.")
    @Expose
    public int minDamageBuff = 0;

    @ConfigEditorBoolean
    @ConfigOption(name = "Kick API Off Players", desc = "Kick players who have API off.")
    @Expose
    public boolean kickAPIOffPlayers = false;

    public enum KuudraTier {
        NONE("None", 0),
        BASIC("Basic", 10),
        HOT("Hot", 20),
        BURNING("Burning", 30),
        FIERY("Fiery", 40),
        INFERNAL("Infernal", 50);

        private final String name;
        @Getter
        private final int stars;

        @Override
        public String toString() {
            return name;
        }

        KuudraTier(String name, int stars) {
            this.name = name;
            this.stars = stars;
        }

    }
}
