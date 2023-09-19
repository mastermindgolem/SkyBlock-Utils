package com.golem.skyblockutils.models.Overlay.TextOverlay;

import com.golem.skyblockutils.Main;
import com.golem.skyblockutils.features.KuudraFight.Kuudra;
import com.golem.skyblockutils.models.gui.*;
import com.golem.skyblockutils.utils.TimeHelper;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.golem.skyblockutils.Main.configFile;
import static com.golem.skyblockutils.Main.mc;

public class FatalTempoOverlay {

    //Adapted from nwjnaddons

    public static GuiElement element = new GuiElement("Fatal Tempo Overlay", 50, 10);
    private static final TimeHelper time = new TimeHelper();

    private static List<Long> hits = new ArrayList<>();
    private static int level = 0;

    public static int renderWidth(String text) {
        return mc.fontRendererObj.getStringWidth(text);
    }

    @SubscribeEvent
    public void onClick(PlayerInteractEvent event) {
        if (event.action == PlayerInteractEvent.Action.RIGHT_CLICK_AIR) {
            ItemStack heldItem = Main.mc.thePlayer.getHeldItem();
            if (heldItem == null) return;
            try {
                int FTLvl = heldItem.serializeNBT().getCompoundTag("tag").getCompoundTag("ExtraAttributes").getCompoundTag("enchantments").getInteger("ultimate_fatal_tempo");
                if (FTLvl <= 0) return;
                level = FTLvl * (heldItem.getDisplayName().contains("Terminator") ? 3 : 1);
                hits.add(time.getCurrentMS());
                hits = hits.stream().filter(hit -> time.getCurrentMS() - hit < 3000).collect(Collectors.toList());

            } catch (Exception ignored) {}
        }
    }


    @SubscribeEvent
    public void onRenderOverlay(RenderGameOverlayEvent event) {
        if (event.type != RenderGameOverlayEvent.ElementType.TEXT) return;

        TextStyle textStyle = TextStyle.fromInt(1);

        if (configFile.testGui && (configFile.ftOverlay == 1 || (configFile.ftOverlay == 2 && Kuudra.currentPhase > 0) || (configFile.ftOverlay == 3 && Kuudra.currentPhase == 4))) {
            GlStateManager.pushMatrix();
            GlStateManager.translate(element.position.getX(), element.position.getY(), 500.0);
            GlStateManager.scale(element.position.getScale(), element.position.getScale(), 1.0);

            int percent = (int) hits.stream().filter(hit -> time.getCurrentMS() - hit < 3000).count() * level * 10;
            percent = Math.min(200, percent);

            OverlayUtils.drawString(0, 0, EnumChatFormatting.YELLOW + "Fatal Tempo: " +
                    (percent > 0 ? EnumChatFormatting.GREEN : EnumChatFormatting.RED) + percent + "%"
            , textStyle, Alignment.Left);

            GlStateManager.popMatrix();

        }
        if (mc.currentScreen instanceof MoveGui) {
            GlStateManager.pushMatrix();
            GlStateManager.translate(element.position.getX(), element.position.getY(), 500.0);
            GlStateManager.scale(element.position.getScale(), element.position.getScale(), 1.0);

            int percent = (int) hits.stream().filter(hit -> time.getCurrentMS() - hit < 3000).count() * level * 10;
            percent = Math.min(200, percent);

            OverlayUtils.drawString(0, 0, EnumChatFormatting.YELLOW + "Fatal Tempo: " +
                            (percent > 0 ? EnumChatFormatting.GREEN : EnumChatFormatting.RED) + percent + "%"
                    , textStyle, Alignment.Left);


            element.setWidth(renderWidth("Fatal Tempo: 200%"));
            element.setHeight(10);

            GlStateManager.popMatrix();
        }
    }
}
