package com.golem.skyblockutils.configs.overlays;

import com.golem.skyblockutils.Main;
import com.golem.skyblockutils.configs.OverlayCategory;
import com.golem.skyblockutils.models.Overlay.TextOverlay.ProfitOverlay;
import com.golem.skyblockutils.models.gui.GuiElement;
import com.golem.skyblockutils.models.gui.MoveGui;
import com.google.gson.annotations.Expose;
import io.github.notenoughupdates.moulconfig.annotations.ConfigEditorBoolean;
import io.github.notenoughupdates.moulconfig.annotations.ConfigEditorButton;
import io.github.notenoughupdates.moulconfig.annotations.ConfigEditorDropdown;
import io.github.notenoughupdates.moulconfig.annotations.ConfigOption;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;

public class KuudraProfitConfig {
    @ConfigEditorDropdown
    @ConfigOption(name = "Kuudra Profit Overlay", desc = "Shows total profit from Kuudra.")
    @Expose
    public OverlayCategory.OverlayOption profitOverlay = OverlayCategory.OverlayOption.OFF;

    @ConfigEditorButton(buttonText = "Move")
    @ConfigOption(name = "Move Profit Overlay", desc = "Click to move Profit Overlay")
    @Expose
    public Runnable moveProfitOverlay = () -> {
        if (profitOverlay == OverlayCategory.OverlayOption.OFF) {
            Main.mc.thePlayer.addChatMessage(new ChatComponentText(EnumChatFormatting.GOLD + "SBU > " + EnumChatFormatting.RED + "You cannot move this since Kuudra Profit Overlay is off."));
            return;
        }
        Main.mc.displayGuiScreen(new MoveGui(new GuiElement[]{ProfitOverlay.element}));
    };

    @ConfigEditorBoolean
    @ConfigOption(name = "Include Downtime", desc = "Include downtime in profit calculation.")
    @Expose
    public boolean includeDowntime = false;
}
