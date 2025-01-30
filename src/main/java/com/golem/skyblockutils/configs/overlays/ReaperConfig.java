package com.golem.skyblockutils.configs.overlays;

import com.golem.skyblockutils.Main;
import com.golem.skyblockutils.configs.OverlayCategory;
import com.golem.skyblockutils.models.Overlay.TextOverlay.ReaperOverlay;
import com.golem.skyblockutils.models.gui.GuiElement;
import com.golem.skyblockutils.models.gui.MoveGui;
import com.google.gson.annotations.Expose;
import io.github.notenoughupdates.moulconfig.annotations.ConfigEditorButton;
import io.github.notenoughupdates.moulconfig.annotations.ConfigEditorDropdown;
import io.github.notenoughupdates.moulconfig.annotations.ConfigOption;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;

public class ReaperConfig {
    @ConfigEditorDropdown
    @ConfigOption(name = "Reaper Overlay", desc = "Shows cooldown for Reaper Armor.")
    @Expose
    public OverlayCategory.OverlayOption reaperOverlay = OverlayCategory.OverlayOption.OFF;

    @ConfigEditorButton(buttonText = "Move")
    @ConfigOption(name = "Move Reaper Overlay", desc = "Click to move Reaper Overlay")
    @Expose
    public Runnable moveReaperOverlay = () -> {
        if (reaperOverlay == OverlayCategory.OverlayOption.OFF) {
            Main.mc.thePlayer.addChatMessage(new ChatComponentText(EnumChatFormatting.GOLD + "SBU > " + EnumChatFormatting.RED + "You cannot move this since Reaper Overlay is off."));
            return;
        }
        Main.mc.displayGuiScreen(new MoveGui(new GuiElement[]{ReaperOverlay.element}));
    };
}
