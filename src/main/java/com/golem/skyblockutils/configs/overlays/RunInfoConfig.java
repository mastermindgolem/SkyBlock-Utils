package com.golem.skyblockutils.configs.overlays;

import com.golem.skyblockutils.Main;
import com.golem.skyblockutils.models.Overlay.TextOverlay.CratesOverlay;
import com.golem.skyblockutils.models.gui.GuiElement;
import com.golem.skyblockutils.models.gui.MoveGui;
import com.google.gson.annotations.Expose;
import io.github.notenoughupdates.moulconfig.annotations.ConfigEditorBoolean;
import io.github.notenoughupdates.moulconfig.annotations.ConfigEditorButton;
import io.github.notenoughupdates.moulconfig.annotations.ConfigOption;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;

public class RunInfoConfig {

    @ConfigEditorBoolean
    @Expose
    @ConfigOption(name = "Run Info Overlay", desc = "Shows information about your current run.")
    public boolean runInfoOverlay = false;

    @ConfigEditorButton
    @ConfigOption(name = "Move Run Info Overlay", desc = "Click to move Run Info Overlay")
    public Runnable moveRunInfoOverlay = () -> {
        if (!runInfoOverlay) {
            Main.mc.thePlayer.addChatMessage(new ChatComponentText(EnumChatFormatting.GOLD + "SBU > " + EnumChatFormatting.RED + "You cannot move this since Run Info Overlay is off."));
            return;
        }
        Main.mc.displayGuiScreen(new MoveGui(new GuiElement[]{CratesOverlay.element}));
    };
}
