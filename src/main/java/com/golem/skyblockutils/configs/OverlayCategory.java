package com.golem.skyblockutils.configs;

import com.golem.skyblockutils.Main;
import com.golem.skyblockutils.configs.overlays.*;
import com.golem.skyblockutils.features.KuudraFight.Kuudra;
import com.golem.skyblockutils.init.GuiInit;
import com.golem.skyblockutils.models.gui.GuiElement;
import com.golem.skyblockutils.models.gui.MoveGui;
import com.google.gson.annotations.Expose;
import io.github.notenoughupdates.moulconfig.annotations.Category;
import io.github.notenoughupdates.moulconfig.annotations.ConfigEditorButton;
import io.github.notenoughupdates.moulconfig.annotations.ConfigOption;
import logger.Logger;

public class OverlayCategory {

    @ConfigEditorButton
    @ConfigOption(name = "Move All Overlays", desc = "Open GUI to move all overlays.")
    @Expose
    public Runnable moveAllOverlays = () -> {
        Main.mc.displayGuiScreen(new MoveGui(GuiInit.getOverlayLoaded().toArray(new GuiElement[0])));
        Main.display = null;
    };

    @Category(name = "Timer", desc = "Set a timer.")
    @Expose
    public TimerConfig timerConfig = new TimerConfig();

    @Category(name = "Kuudra Chest", desc = "Show Kuudra Chest Overlay.")
    @Expose
    public KuudraChestConfig kuudraChestConfig = new KuudraChestConfig();

    @Category(name = "Alert", desc = "Full screen notifications.")
    @Expose
    public AlertConfig alertConfig = new AlertConfig();

    @Category(name = "Align", desc = "Cells Alignment.")
    @Expose
    public AlignConfig alignConfig = new AlignConfig();

    @Category(name = "Splits", desc = "Kuudra run splits.")
    @Expose
    public SplitsConfig splitsConfig = new SplitsConfig();

    @Category(name = "Ragnarock", desc = "Ragnarock Axe cooldown.")
    @Expose
    public RagnarockConfig ragnarockConfig = new RagnarockConfig();

    @Category(name = "Reaper", desc = "Reaper Armor cooldown.")
    @Expose
    public ReaperConfig reaperConfig = new ReaperConfig();

    @Category(name = "End Stone Sword", desc = "End Stone Sword invincibility timer.")
    @Expose
    public EndStoneConfig endStoneConfig = new EndStoneConfig();

    @Category(name = "Run Info", desc = "Show run info.")
    @Expose
    public RunInfoConfig runInfoConfig = new RunInfoConfig();

    @Category(name = "Kuudra Profit", desc = "Show Kuudra profit.")
    @Expose
    public KuudraProfitConfig kuudraProfitConfig = new KuudraProfitConfig();

    @Category(name = "Damage Bonus", desc = "Show damage bonus.")
    @Expose
    public DamageBonusConfig damageBonusConfig = new DamageBonusConfig();

    @Category(name = "Container Value", desc = "Show container value.")
    @Expose
    public ContainerConfig containerValueConfig = new ContainerConfig();

    @Category(name = "Fatal Tempo", desc = "Show Fatal Tempo buff.")
    @Expose
    public FatalTempoConfig fatalTempoConfig = new FatalTempoConfig();

    public enum OverlayOption {
        OFF("Off"),
        ALWAYS_ON("Always on"),
        IN_KUUDRA("In Kuudra only"),
        IN_P4("In Phase 4 only");

        private final String name;

        @Override
        public String toString() {
            return name;
        }

        OverlayOption(String name) {
            this.name = name;
        }
    }

    public static boolean isOverlayOn(OverlayOption overlayOption) {
        switch (overlayOption) {
            case ALWAYS_ON:
                return true;
            case IN_KUUDRA:
                return Kuudra.currentPhase > 0;
            case IN_P4:
                return Kuudra.currentPhase == 4;
            default:
                return false;
        }
    }
}
