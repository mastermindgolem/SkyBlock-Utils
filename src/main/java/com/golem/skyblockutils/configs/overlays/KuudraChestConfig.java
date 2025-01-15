package com.golem.skyblockutils.configs.overlays;

import com.golem.skyblockutils.Main;
import com.golem.skyblockutils.models.Overlay.TextOverlay.ContainerOverlay;
import com.golem.skyblockutils.models.gui.GuiElement;
import com.golem.skyblockutils.models.gui.MoveGui;
import com.google.gson.annotations.Expose;
import io.github.notenoughupdates.moulconfig.annotations.*;
import lombok.Getter;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;

public class KuudraChestConfig {

    @ConfigEditorDropdown()
    @ConfigOption(name = "Kuudra Chest Overlay", desc = "Show Kuudra Chest Overlay")
    @Expose
    public KuudraChestPosition kuudraChestOverlay = KuudraChestPosition.OFF;

    @ConfigEditorBoolean
    @ConfigOption(name = "Highlight Best Option", desc = "Highlight kismet/open chest slot depending on value.")
    @Expose
    public boolean highlightBestOption = true;

    @ConfigEditorSlider(minValue = 0, maxValue = 10, minStep = 1)
    @ConfigOption(name = "Reroll below x profit (in millions)", desc = "Suggest rerolling if chest profit is below this value. 0 = reroll depending on expected value.")
    @Expose
    public int rerollBelowProfit = 0;


    @ConfigEditorBoolean
    @ConfigOption(name = "Send Kuudra Chest Data", desc = "Send Kuudra Chest Data to server")
    @Expose
    public boolean sendChestData = true;

    @ConfigEditorButton(buttonText = "Move")
    @ConfigOption(name = "Move Kuudra Chest Overlay", desc = "Click to move Kuudra Chest Overlay")
    @Expose
    public Runnable moveKuudraOverlay = () -> {
        if (kuudraChestOverlay != KuudraChestPosition.CUSTOM) {
            Main.mc.thePlayer.addChatMessage(new ChatComponentText(EnumChatFormatting.GOLD + "SBU > " + EnumChatFormatting.RED + "You cannot move this since Kuudra Chest Overlay is not set to CUSTOM."));
            return;
        }
        Main.mc.displayGuiScreen(new MoveGui(new GuiElement[]{ContainerOverlay.element}));
    };


    @Getter
    public enum KuudraChestPosition {
        OFF("Off"),
        NEXT_TO_GUI("Next to GUI"),
        CUSTOM("Custom");

        private final String name;

        @Override
        public String toString() {
            return name;
        }

        KuudraChestPosition(String name) {
            this.name = name;
        }
    }

}
