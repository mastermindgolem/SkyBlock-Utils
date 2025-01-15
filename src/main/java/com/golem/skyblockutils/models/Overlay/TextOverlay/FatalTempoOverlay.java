package com.golem.skyblockutils.models.Overlay.TextOverlay;

import com.golem.skyblockutils.Main;
import com.golem.skyblockutils.configs.OverlayCategory;
import com.golem.skyblockutils.models.gui.GuiElement;
import com.golem.skyblockutils.models.gui.MoveGui;
import com.golem.skyblockutils.utils.TimeHelper;
import com.golem.skyblockutils.utils.rendering.RenderableString;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.golem.skyblockutils.Main.config;
import static com.golem.skyblockutils.Main.mc;

public class FatalTempoOverlay {
    public static GuiElement element = new GuiElement("Fatal Tempo Overlay", 50, 10);
    private static final TimeHelper time = new TimeHelper();
    private static List<Long> hits = new ArrayList<>();
    private static int level = 0;
    private final RenderableString display;

    public FatalTempoOverlay() {
        display = new RenderableString("", element.position.getX(), element.position.getY());
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

        if (OverlayCategory.isOverlayOn(config.getConfig().overlayCategory.fatalTempoConfig.fataltempoOverlay)) {
            int percent = (int) hits.stream().filter(hit -> time.getCurrentMS() - hit < 3000).count() * level * 10;
            percent = Math.min(200, percent);

            display
                    .setText(EnumChatFormatting.YELLOW + "Fatal Tempo: " +
                            (percent > 0 ? EnumChatFormatting.GREEN : EnumChatFormatting.RED) + percent + "%")
                    .setScale(element.position.getScale());

            display.setPosition(element.position.getX(), element.position.getY());
            display.render();

            element.setWidth(display.getWidth());
            element.setHeight(display.getHeight());
        } else if (mc.currentScreen instanceof MoveGui) {
            display
                    .setText(EnumChatFormatting.YELLOW + "Fatal Tempo: " + EnumChatFormatting.GREEN + "200%")
                    .setScale(element.position.getScale());

            display.setPosition(element.position.getX(), element.position.getY());
            display.render();

            element.setWidth(display.getWidth());
            element.setHeight(display.getHeight());
        }
    }
}