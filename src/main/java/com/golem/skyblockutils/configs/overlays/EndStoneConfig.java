package com.golem.skyblockutils.configs.overlays;

import com.golem.skyblockutils.Main;
import com.golem.skyblockutils.configs.OverlayCategory;
import com.golem.skyblockutils.models.Overlay.TextOverlay.EndstoneOverlay;
import com.golem.skyblockutils.models.gui.GuiElement;
import com.golem.skyblockutils.models.gui.MoveGui;
import com.google.gson.annotations.Expose;
import io.github.notenoughupdates.moulconfig.annotations.ConfigEditorButton;
import io.github.notenoughupdates.moulconfig.annotations.ConfigEditorDropdown;
import io.github.notenoughupdates.moulconfig.annotations.ConfigOption;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;

public class EndStoneConfig {
    @ConfigEditorDropdown
    @ConfigOption(name = "End Stone Sword Overlay", desc = "Shows end stone sword invincibility timer.")
    @Expose
    public OverlayCategory.OverlayOption endstoneOverlay = OverlayCategory.OverlayOption.OFF;

    @ConfigEditorButton(buttonText = "Move")
    @ConfigOption(name = "Move End Stone Sword Overlay", desc = "Click to move End Stone Sword Overlay")
    @Expose
    public Runnable moveEndstoneOverlay = () -> {
        if (endstoneOverlay == OverlayCategory.OverlayOption.OFF) {
            Main.mc.thePlayer.addChatMessage(new ChatComponentText(EnumChatFormatting.GOLD + "SBU > " + EnumChatFormatting.RED + "You cannot move this since End Stone Sword Overlay is off."));
            return;
        }
        Main.mc.displayGuiScreen(new MoveGui(new GuiElement[]{EndstoneOverlay.element}));
    };
}
