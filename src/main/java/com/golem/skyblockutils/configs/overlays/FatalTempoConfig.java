package com.golem.skyblockutils.configs.overlays;

import com.golem.skyblockutils.Main;
import com.golem.skyblockutils.configs.OverlayCategory;
import com.golem.skyblockutils.models.Overlay.TextOverlay.FatalTempoOverlay;
import com.golem.skyblockutils.models.gui.GuiElement;
import com.golem.skyblockutils.models.gui.MoveGui;
import com.google.gson.annotations.Expose;
import io.github.notenoughupdates.moulconfig.annotations.ConfigEditorButton;
import io.github.notenoughupdates.moulconfig.annotations.ConfigEditorDropdown;
import io.github.notenoughupdates.moulconfig.annotations.ConfigOption;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;

public class FatalTempoConfig {
    @ConfigEditorDropdown
    @ConfigOption(name = "Fatal Tempo Overlay", desc = "Shows Ferocity buff from Fatal Tempo.")
    @Expose
    public OverlayCategory.OverlayOption fataltempoOverlay = OverlayCategory.OverlayOption.OFF;

    @ConfigEditorButton(buttonText = "Move")
    @ConfigOption(name = "Move Fatal Tempo Overlay", desc = "Click to move Fatal Tempo Overlay")
    @Expose
    public Runnable moveFatalTempoOverlay = () -> {
        if (fataltempoOverlay == OverlayCategory.OverlayOption.OFF) {
            Main.mc.thePlayer.addChatMessage(new ChatComponentText(EnumChatFormatting.GOLD + "SBU > " + EnumChatFormatting.RED + "You cannot move this since Fatal Tempo Overlay is off."));
            return;
        }
        Main.mc.displayGuiScreen(new MoveGui(new GuiElement[]{FatalTempoOverlay.element}));
    };
}
