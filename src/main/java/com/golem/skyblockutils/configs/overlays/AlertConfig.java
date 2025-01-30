package com.golem.skyblockutils.configs.overlays;

import com.golem.skyblockutils.Main;
import com.golem.skyblockutils.models.Overlay.TextOverlay.AlertOverlay;
import com.golem.skyblockutils.models.gui.GuiElement;
import com.golem.skyblockutils.models.gui.MoveGui;
import com.google.gson.annotations.Expose;
import io.github.notenoughupdates.moulconfig.annotations.ConfigEditorBoolean;
import io.github.notenoughupdates.moulconfig.annotations.ConfigEditorButton;
import io.github.notenoughupdates.moulconfig.annotations.ConfigOption;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;

public class AlertConfig {
    @ConfigEditorBoolean
    @ConfigOption(name = "Custom Alert Location", desc = "Off = Alerts in center of screen. On = Custom location")
    @Expose
    public boolean alertOverlay = false;

    @ConfigEditorButton(buttonText = "Move")
    @ConfigOption(name = "Move Alert Overlay", desc = "Click to move Alert Overlay")
    @Expose
    public Runnable moveAlertOverlay = () -> {
        if (!alertOverlay) {
            Main.mc.thePlayer.addChatMessage(new ChatComponentText(EnumChatFormatting.GOLD + "SBU > " + EnumChatFormatting.RED + "You cannot move this since Custom Alert Location is off."));
            return;
        }
        Main.mc.displayGuiScreen(new MoveGui(new GuiElement[]{AlertOverlay.element}));
    };
}
