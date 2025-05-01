package com.golem.skyblockutils.configs.overlays;

import com.golem.skyblockutils.Main;
import com.golem.skyblockutils.models.Overlay.TextOverlay.SplitsOverlay;
import com.golem.skyblockutils.models.gui.GuiElement;
import com.golem.skyblockutils.models.gui.MoveGui;
import com.google.gson.annotations.Expose;
import io.github.notenoughupdates.moulconfig.annotations.ConfigEditorBoolean;
import io.github.notenoughupdates.moulconfig.annotations.ConfigEditorButton;
import io.github.notenoughupdates.moulconfig.annotations.ConfigOption;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;

public class SplitsConfig {
    @ConfigEditorBoolean
    @ConfigOption(name = "Splits Overlay", desc = "Shows splits for the Kuudra run.")
    @Expose
    public boolean splitsOverlay;

    @ConfigEditorButton(buttonText = "Move")
    @ConfigOption(name = "Move Splits Overlay", desc = "Click to move Splits Overlay")
    @Expose
    public Runnable moveSplitsOverlay = () -> {
        if (!splitsOverlay) {
            Main.mc.thePlayer.addChatMessage(new ChatComponentText(EnumChatFormatting.GOLD + "SBU > " + EnumChatFormatting.RED + "You cannot move this since Splits Overlay is off."));
            return;
        }
        Main.mc.displayGuiScreen(new MoveGui(new GuiElement[]{SplitsOverlay.element}));
    };
}
