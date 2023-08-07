package com.golem.skyblockutils.models.Overlay.TextOverlay;

import com.golem.skyblockutils.Main;
import com.golem.skyblockutils.features.KuudraFight.Kuudra;
import com.golem.skyblockutils.models.gui.*;
import com.golem.skyblockutils.utils.TimeHelper;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.text.DecimalFormat;

import static com.golem.skyblockutils.Main.configFile;
import static com.golem.skyblockutils.Main.mc;

public class ReaperOverlay {

    public static GuiElement element = new GuiElement("Reaper Overlay", 50, 20);

    private static final TimeHelper time = new TimeHelper();

    private final DecimalFormat formatter = new DecimalFormat("0.00");
    private static long lastUse = 0;

    public static int renderWidth(String text) {
        return mc.fontRendererObj.getStringWidth(text);
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.START || Main.mc.theWorld == null || Main.mc.thePlayer == null) return;
        ItemStack armor = Main.mc.thePlayer.getEquipmentInSlot(2);
        if (armor == null) return;
        if (armor.getDisplayName().contains("Reaper Leggings") && GameSettings.isKeyDown(mc.gameSettings.keyBindSneak) && time.getCurrentMS() - lastUse >= 25000) {
            lastUse = time.getCurrentMS();
        }
    }


    @SubscribeEvent
    public void onRenderOverlay(RenderGameOverlayEvent event) {
        if (event.type != RenderGameOverlayEvent.ElementType.TEXT) return;

        TextStyle textStyle = TextStyle.fromInt(1);

        if (configFile.testGui && (configFile.reaperTimer == 1 || (configFile.reaperTimer == 2 && Kuudra.currentPhase > 0) || (configFile.reaperTimer == 3 && Kuudra.currentPhase == 4))) {
            GlStateManager.pushMatrix();
            GlStateManager.translate(element.position.getX(), element.position.getY(), 500.0);
            GlStateManager.scale(element.position.getScale(), element.position.getScale(), 1.0);

            double buffLeft = (time.getCurrentMS() - lastUse < 6000 ? lastUse + 6000 - time.getCurrentMS() : -1);
            double cooldown = lastUse + 25000 - time.getCurrentMS();
            String string1;
            if (buffLeft < 0) {
                string1 = EnumChatFormatting.DARK_RED + "Reaper Buff: Inactive";
            } else {
                string1 = EnumChatFormatting.YELLOW + "Reaper Buff: " + EnumChatFormatting.GREEN + formatter.format(buffLeft/1000) + "s";
            }

            String string2 = EnumChatFormatting.YELLOW + "Reaper Cooldown: " + (cooldown > 0 ? EnumChatFormatting.GREEN + formatter.format(cooldown/1000) + "s" : EnumChatFormatting.DARK_GREEN + "READY");

            OverlayUtils.drawString(0, 0, string1, textStyle, Alignment.Left);
            OverlayUtils.drawString(0, 10, string2, textStyle, Alignment.Left);

            element.setWidth(Math.max(renderWidth(string1), renderWidth(string2)));
            element.setHeight(20);

            GlStateManager.popMatrix();
        } else if (mc.currentScreen instanceof MoveGui) {
            GlStateManager.pushMatrix();
            GlStateManager.translate(element.position.getX(), element.position.getY(), 500.0);
            GlStateManager.scale(element.position.getScale(), element.position.getScale(), 1.0);

            String string1 = EnumChatFormatting.DARK_RED + "Reaper Buff : Inactive";
            String string2 =  EnumChatFormatting.YELLOW + "Reaper Cooldown: " + EnumChatFormatting.DARK_GREEN + "READY";

            OverlayUtils.drawString(0, 0, string1, textStyle, Alignment.Left);
            OverlayUtils.drawString(0, 10, string2, textStyle, Alignment.Left);

            element.setWidth(Math.max(renderWidth(string1), renderWidth(string2)));
            element.setHeight(20);

            GlStateManager.popMatrix();
        }
    }

}
