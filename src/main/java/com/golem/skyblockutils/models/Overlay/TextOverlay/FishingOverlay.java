package com.golem.skyblockutils.models.Overlay.TextOverlay;

import com.golem.skyblockutils.Main;
import com.golem.skyblockutils.features.KuudraFight.Kuudra;
import com.golem.skyblockutils.models.gui.*;
import com.golem.skyblockutils.utils.TimeHelper;
import com.golem.skyblockutils.utils.rendering.RenderableString;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.*;

import static com.golem.skyblockutils.Main.configFile;
import static com.golem.skyblockutils.Main.mc;

public class FishingOverlay {
    List<String> seacreatures = new ArrayList<>(Arrays.asList("§fPlhlegblast", "§fSquid", "§fNight Squid", "§fSea Walker", "§fSea Guardian", "§aSea Archer", "§aMonster of the Deep", "§aRider of the Deep", "§aSea Witch", "§9Catfish", "§9Sea Leech", "§5Guardian Defender", "§5Deep Sea Protector", "§6Water Hydra", "§6Sea Emperor", "§fFrozen Steve", "§fSnowman", "§aThe Grinch", "§6Yeti", "§fScarecrow", "§aNightmare", "§5Werewolf", "§6Phantom Fisher", "§6Grim Reaper", "§aNurse Shark", "§9Blue Shark", "§5Tiger Shark", "§6Great White Shark", "§aOasis Sheep", "§aOasis Rabbit", "§9Carrot King", "§5Lava Blaze", "§5Lava Pigman", "§9Flaming Worm", "§6Zombie Miner", "§9Water Worm", "§9Poisoned Water Worm", "§9Moogma", "§9Magma Slug", "§9Pyroclastic Worm", "§9Lava Flame", "§9Fire Eel", "§9Lava Leech", "§dThunder", "§9Taurus", "§dJawbus"));
    HashMap<String, Integer> scCount = new HashMap<>();

    public static GuiElement element = new GuiElement("Fishing Overlay", 50, 10);
    private static final TimeHelper time = new TimeHelper();
    private static long timer = 0;
    private static long lastRodHeldTime = 0;

    private final List<RenderableString> overlayTexts;
    private final RenderableString timeText;
    private final RenderableString totalText;

    public FishingOverlay() {
        overlayTexts = new ArrayList<>();
        timeText = new RenderableString("", element.position.getX(), element.position.getY());
        totalText = new RenderableString("", element.position.getX(), element.position.getY());

        // Pre-initialize RenderableStrings for potential sea creatures
        for (int i = 0; i < 50; i++) {  // More than enough for all possible entries
            overlayTexts.add(new RenderableString("", element.position.getX(), element.position.getY()));
        }
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.START || Main.mc.theWorld == null || Main.mc.thePlayer == null) return;
        ItemStack heldItem = Main.mc.thePlayer.getHeldItem();
        if (heldItem != null && heldItem.hasDisplayName() && heldItem.getDisplayName().contains("Rod")) lastRodHeldTime = time.getCurrentMS();
    }

    @SubscribeEvent
    public void RenderEvent(RenderWorldLastEvent event) {
        if (!configFile.fishingOverlay || time.getCurrentMS() - lastRodHeldTime > 10000) return;

        ArrayList<Entity> entities = Kuudra.getAllEntitiesInRange(30);
        HashMap<String, Integer> temp = new HashMap<>();

        for (Entity entity : entities) {
            for (String sc : seacreatures) {
                if (entity.getName().contains(sc)) {
                    temp.put(sc, temp.getOrDefault(sc, 0) + 1);
                }
            }
        }

        if (temp.containsKey("§fSquid")) temp.put("§fSquid", temp.getOrDefault("§fSquid", 0)/2);
        if (scCount.keySet().isEmpty() && !temp.keySet().isEmpty()) timer = time.getCurrentMS();
        if (!scCount.keySet().isEmpty() && temp.keySet().isEmpty()) timer = 0;
        scCount = temp;
    }

    @SubscribeEvent
    public void onRenderOverlay(RenderGameOverlayEvent event) {
        if (event.type != RenderGameOverlayEvent.ElementType.TEXT || !configFile.fishingOverlay) return;

        if (configFile.testGui && !scCount.entrySet().isEmpty()) {
            float timeSince = (time.getCurrentMS() - timer) / 60000F;
            String timeString;
            if (timeSince > 5) {
                timeString = EnumChatFormatting.GOLD + "Time: " + EnumChatFormatting.RED + SplitsOverlay.format(timeSince);
            } else if (timeSince > 0) {
                timeString = EnumChatFormatting.GOLD + "Time: " + EnumChatFormatting.YELLOW + SplitsOverlay.format(timeSince);
            } else {
                timeString = "";
            }

            // Render time
            timeText.setText(timeString)
                    .setScale(element.position.getScale());
            timeText.setPosition(element.position.getX(), element.position.getY());
            timeText.render();

            int yOffset = 10;
            int maxWidth = 0;
            int index = 0;

            // Render sea creatures
            for (Map.Entry<String, Integer> entry : scCount.entrySet()) {
                RenderableString text = overlayTexts.get(index);
                text.setText(entry.getKey() + EnumChatFormatting.RESET + ": " + entry.getValue())
                        .setScale(element.position.getScale());
                text.setPosition(element.position.getX(), element.position.getY() + yOffset);
                text.render();

                maxWidth = Math.max(maxWidth, text.getWidth());
                yOffset += 10;
                index++;
            }

            // Render total
            int total = scCount.values().stream().mapToInt(Integer::intValue).sum();
            if (total > 0) {
                totalText.setText(EnumChatFormatting.GOLD + "Total SC: " + EnumChatFormatting.YELLOW + total)
                        .setScale(element.position.getScale());
                totalText.setPosition(element.position.getX(), element.position.getY() + yOffset);
                totalText.render();
            }

            element.setWidth(maxWidth);
            element.setHeight(yOffset + 10);
        } else if (mc.currentScreen instanceof MoveGui) {
            int yOffset = 0;
            int maxWidth = 0;

            // Show example entries in MoveGui
            for (String sc : seacreatures.subList(0, 10)) {
                RenderableString text = overlayTexts.get(yOffset / 10);
                text.setText(sc + EnumChatFormatting.RESET + ": 1")
                        .setScale(element.position.getScale());
                text.setPosition(element.position.getX(), element.position.getY() + yOffset);
                text.render();

                maxWidth = Math.max(maxWidth, text.getWidth());
                yOffset += 10;
            }

            element.setWidth(maxWidth);
            element.setHeight(100);
        }
    }
}