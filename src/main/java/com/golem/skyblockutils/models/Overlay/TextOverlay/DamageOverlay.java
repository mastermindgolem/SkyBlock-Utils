package com.golem.skyblockutils.models.Overlay.TextOverlay;

import com.golem.skyblockutils.Main;
import com.golem.skyblockutils.features.KuudraFight.Kuudra;
import com.golem.skyblockutils.models.gui.*;
import com.golem.skyblockutils.utils.TimeHelper;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import static com.golem.skyblockutils.Main.configFile;
import static com.golem.skyblockutils.Main.mc;

public class DamageOverlay {

    public static GuiElement element = new GuiElement("Damage Overlay", 50, 10);
    private static final TimeHelper time = new TimeHelper();
    private static long timer = 0;
    private static float hp = 0;

    public static int renderWidth(String text) {
        return mc.fontRendererObj.getStringWidth(text);
    }


    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (mc.thePlayer == null || event.phase == TickEvent.Phase.START) return;
        hp = Main.mc.thePlayer.getHealth();
    }
    @SubscribeEvent
    public void onRenderOverlay(RenderGameOverlayEvent event) {
        if (event.type != RenderGameOverlayEvent.ElementType.TEXT || configFile.damageOverlay == 0) return;

        TextStyle textStyle = TextStyle.fromInt(1);


        if (configFile.testGui && Kuudra.currentPhase == 4) {
            GlStateManager.pushMatrix();
            GlStateManager.translate(element.position.getX(), element.position.getY(), 500.0);
            GlStateManager.scale(element.position.getScale(), element.position.getScale(), 1.0);

            if (configFile.damageOverlay == 1) {
                OverlayUtils.drawString(0, 0, EnumChatFormatting.YELLOW + "Dominance: " + (hp == 40 ? EnumChatFormatting.GREEN + "ACTIVE" : EnumChatFormatting.RED + "INACTIVE"), textStyle, Alignment.Left);
            }
            if (configFile.damageOverlay == 2) {
                OverlayUtils.drawString(0, 0, EnumChatFormatting.YELLOW + "Lifeline: " + (hp <= 8 ? EnumChatFormatting.GREEN + "ACTIVE" : EnumChatFormatting.RED + "INACTIVE"), textStyle, Alignment.Left);
            }


            GlStateManager.popMatrix();

        }
        if (mc.currentScreen instanceof MoveGui) {
            GlStateManager.pushMatrix();
            GlStateManager.translate(element.position.getX(), element.position.getY(), 500.0);
            GlStateManager.scale(element.position.getScale(), element.position.getScale(), 1.0);

            int max = 0;

            if (configFile.damageOverlay == 1) {
                String string = EnumChatFormatting.YELLOW + "Dominance: " + (hp == 40 ? EnumChatFormatting.GREEN + "ACTIVE" : EnumChatFormatting.RED + "INACTIVE");
                OverlayUtils.drawString(0, 0, string, textStyle, Alignment.Left);
                max = renderWidth(string);
            }
            if (configFile.damageOverlay == 2) {
                String string = EnumChatFormatting.YELLOW + "Lifeline: " + (hp <= 8 ? EnumChatFormatting.GREEN + "ACTIVE" : EnumChatFormatting.RED + "INACTIVE");
                OverlayUtils.drawString(0, 0, string, textStyle, Alignment.Left);
                max = renderWidth(string);
            }

            element.setWidth(max);
            element.setHeight(10);

            GlStateManager.popMatrix();
        }
    }

}
