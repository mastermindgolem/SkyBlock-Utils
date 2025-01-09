package com.golem.skyblockutils.models.Overlay.TextOverlay;

import com.golem.skyblockutils.Main;
import com.golem.skyblockutils.features.KuudraFight.Kuudra;
import com.golem.skyblockutils.models.gui.GuiElement;
import com.golem.skyblockutils.models.gui.MoveGui;
import com.golem.skyblockutils.utils.rendering.RenderableString;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import static com.golem.skyblockutils.Main.configFile;
import static com.golem.skyblockutils.Main.mc;

public class DamageOverlay {

    public static GuiElement element = new GuiElement("Damage Overlay", 50, 10);
    private static float hp = 0;
    private static final RenderableString display = new RenderableString("", 0, 0);

    public static int renderWidth(String text) {
        return mc.fontRendererObj.getStringWidth(text);
    }


    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (mc.thePlayer == null || event.phase == TickEvent.Phase.START || Kuudra.currentPhase < 0 || Kuudra.currentPhase > 5) return;
        hp = Main.mc.thePlayer.getHealth();
    }
    @SubscribeEvent
    public void onRenderOverlay(RenderGameOverlayEvent event) {
        if (event.type != RenderGameOverlayEvent.ElementType.TEXT || configFile.damageOverlay == 0) return;

        display.setPosition(element.position.getX(), element.position.getY());
        display.setScale(element.position.getScale());

        if (configFile.testGui && Kuudra.currentPhase == 4) {
            if (configFile.damageOverlay == 1) {
                display.setText(EnumChatFormatting.YELLOW + "Dominance: " + (hp >= 36 ? EnumChatFormatting.GREEN + "ACTIVE" : EnumChatFormatting.RED + "INACTIVE"));
            }
            if (configFile.damageOverlay == 2) {
                display.setText(EnumChatFormatting.YELLOW + "Lifeline: " + (hp <= 8 ? EnumChatFormatting.GREEN + "ACTIVE" : EnumChatFormatting.RED + "INACTIVE"));
            }

            display.render();

        }
        else if (mc.currentScreen instanceof MoveGui) {
            int max = 0;
            if (configFile.damageOverlay == 1) {
                display.setText(EnumChatFormatting.YELLOW + "Dominance: " + (hp >= 36 ? EnumChatFormatting.GREEN + "ACTIVE" : EnumChatFormatting.RED + "INACTIVE"));
                max = renderWidth(display.getText());
            }
            if (configFile.damageOverlay == 2) {
                display.setText(EnumChatFormatting.YELLOW + "Lifeline: " + (hp <= 8 ? EnumChatFormatting.GREEN + "ACTIVE" : EnumChatFormatting.RED + "INACTIVE"));
                max = renderWidth(display.getText());
            }

            element.setWidth(max);
            element.setHeight(10);
        }
    }

}
