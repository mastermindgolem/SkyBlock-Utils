package com.golem.skyblockutils.configs.overlays;

import com.golem.skyblockutils.Main;
import com.golem.skyblockutils.models.Overlay.TextOverlay.TimerOverlay;
import com.golem.skyblockutils.models.gui.GuiElement;
import com.golem.skyblockutils.models.gui.MoveGui;
import com.google.gson.annotations.Expose;
import io.github.notenoughupdates.moulconfig.annotations.ConfigEditorBoolean;
import io.github.notenoughupdates.moulconfig.annotations.ConfigEditorButton;
import io.github.notenoughupdates.moulconfig.annotations.ConfigEditorSlider;
import io.github.notenoughupdates.moulconfig.annotations.ConfigOption;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;

public class TimerConfig {

    @ConfigEditorBoolean
    @ConfigOption(name = "Timer Overlay", desc = "Timer that alerts you in x seconds.")
    @Expose
    public boolean timerOverlay = false;

    @ConfigEditorSlider(minValue = 0, maxValue = 1200, minStep = 1)
    @ConfigOption(name = "Timer Length", desc = "In how long you should be reminded.")
    @Expose
    public int timerLength = 0;

    @ConfigEditorButton(buttonText = "Move")
    @ConfigOption(name = "Move Timer Overlay", desc = "Click to move Timer Overlay")
    @Expose
    public Runnable moveTimerOverlay = () -> {
        if (!timerOverlay) {
            Main.mc.thePlayer.addChatMessage(new ChatComponentText(EnumChatFormatting.GOLD + "SBU > " + EnumChatFormatting.RED + "You cannot move this since Timer Overlay is off."));
            return;
        }
        Main.mc.displayGuiScreen(new MoveGui(new GuiElement[]{TimerOverlay.element}));
    };
}
