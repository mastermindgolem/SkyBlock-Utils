package com.golem.skyblockutils.configs.overlays;

import com.golem.skyblockutils.Main;
import com.golem.skyblockutils.configs.OverlayCategory;
import com.golem.skyblockutils.models.Overlay.TextOverlay.DamageOverlay;
import com.golem.skyblockutils.models.gui.GuiElement;
import com.golem.skyblockutils.models.gui.MoveGui;
import com.google.gson.annotations.Expose;
import io.github.notenoughupdates.moulconfig.annotations.ConfigEditorButton;
import io.github.notenoughupdates.moulconfig.annotations.ConfigEditorDropdown;
import io.github.notenoughupdates.moulconfig.annotations.ConfigOption;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;

public class DamageBonusConfig {
    @ConfigEditorDropdown
    @ConfigOption(name = "Damage Bonus Overlay", desc = "Displays whether dominance/lifeline is active.")
    @Expose
    public OverlayCategory.OverlayOption damageOverlay = OverlayCategory.OverlayOption.OFF;

    @ConfigEditorDropdown
    @ConfigOption(name = "Damage Bonus Attribute", desc = "Choose which attribute to display.")
    @Expose
    public DamageAttribute damageAttribute = DamageAttribute.LIFELINE;


    @ConfigEditorButton(buttonText = "Move")
    @ConfigOption(name = "Move Damage Bonus Overlay", desc = "Click to move Damage Bonus Overlay")
    @Expose
    public Runnable moveDamageOverlay = () -> {
        if (damageOverlay == OverlayCategory.OverlayOption.OFF) {
            Main.mc.thePlayer.addChatMessage(new ChatComponentText(EnumChatFormatting.GOLD + "SBU > " + EnumChatFormatting.RED + "You cannot move this since Damage Bonus Overlay is off."));
            return;
        }
        Main.mc.displayGuiScreen(new MoveGui(new GuiElement[]{DamageOverlay.element}));
    };

    public enum DamageAttribute {
        DOMINANCE("Dominance"),
        LIFELINE("Lifeline");

        private final String name;

        DamageAttribute(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }
    }
}
