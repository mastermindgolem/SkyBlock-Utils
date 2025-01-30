package com.golem.skyblockutils.configs.overlays;

import com.golem.skyblockutils.Main;
import com.golem.skyblockutils.configs.OverlayCategory;
import com.golem.skyblockutils.models.Overlay.TextOverlay.RagnarokOverlay;
import com.golem.skyblockutils.models.gui.GuiElement;
import com.golem.skyblockutils.models.gui.MoveGui;
import com.google.gson.annotations.Expose;
import io.github.notenoughupdates.moulconfig.annotations.ConfigEditorButton;
import io.github.notenoughupdates.moulconfig.annotations.ConfigEditorDropdown;
import io.github.notenoughupdates.moulconfig.annotations.ConfigOption;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;

public class RagnarockConfig {
    @ConfigEditorDropdown
    @ConfigOption(name = "Ragnarock Overlay", desc = "Shows cooldown for Ragnarock Axe.")
    @Expose
    public OverlayCategory.OverlayOption ragnarockOverlay = OverlayCategory.OverlayOption.OFF;

    @ConfigEditorButton(buttonText = "Move")
    @ConfigOption(name = "Move Ragnarock Overlay", desc = "Click to move Ragnarock Overlay")
    @Expose
    public Runnable moveRagnarokOverlay = () -> {
        if (ragnarockOverlay == OverlayCategory.OverlayOption.OFF) {
            Main.mc.thePlayer.addChatMessage(new ChatComponentText(EnumChatFormatting.GOLD + "SBU > " + EnumChatFormatting.RED + "You cannot move this since Ragnarock Overlay is off."));
            return;
        }
        Main.mc.displayGuiScreen(new MoveGui(new GuiElement[]{RagnarokOverlay.element}));
    };
}
