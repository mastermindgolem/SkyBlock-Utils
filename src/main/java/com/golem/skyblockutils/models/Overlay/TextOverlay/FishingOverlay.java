package com.golem.skyblockutils.models.Overlay.TextOverlay;

import com.golem.skyblockutils.features.KuudraFight.Kuudra;
import com.golem.skyblockutils.models.gui.*;
import com.golem.skyblockutils.utils.TimeHelper;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.text.DecimalFormat;
import java.util.*;

import static com.golem.skyblockutils.Main.configFile;
import static com.golem.skyblockutils.Main.mc;
import static com.golem.skyblockutils.features.KuudraFight.Kuudra.splits;

public class FishingOverlay {

    List<String> seacreatures = new ArrayList<>(Arrays.asList("§fPlhlegblast", "§fSquid", "§fNight Squid", "§fSea Walker", "§fSea Guardian", "§aSea Archer", "§aMonster of the Deep", "§aRider of the Deep", "§aSea Witch", "§9Catfish", "§9Sea Leech", "§5Guardian Defender", "§5Deep Sea Protector", "§6Water Hydra", "§6Sea Emperor", "§fFrozen Steve", "§fSnowman", "§aThe Grinch", "§6Yeti", "§fScarecrow", "§aNightmare", "§5Werewolf", "§6Phantom Fisher", "§6Grim Reaper", "§aNurse Shark", "§9Blue Shark", "§5Tiger Shark", "§6Great White Shark", "§aOasis Sheep", "§aOasis Rabbit", "§9Carrot King", "§5Lava Blaze", "§5Lava Pigman", "§9Flaming Worm", "§6Zombie Miner", "§9Water Worm", "§9Poisoned Water Worm", "§9Moogma", "§9Magma Slug", "§9Pyroclastic Worm", "§9Lava Flame", "§9Fire Eel", "§9Lava Leech", "§dThunder", "§9Taurus", "§dJawbus"));
    HashMap<String, Integer> scCount = new HashMap<>();

    public static GuiElement element = new GuiElement("Fishing Overlay", 50, 10);
    private static final TimeHelper time = new TimeHelper();
    private static long timer = 0;

    public static String text = "";

    public static int renderWidth(String text) {
        return mc.fontRendererObj.getStringWidth(text);
    }


    @SubscribeEvent
    public void RenderEvent(RenderWorldLastEvent event) {
        ArrayList<Entity> entities = Kuudra.getAllEntitiesInRange(30);
        HashMap<String, Integer> temp = new HashMap<>();
        for (Entity entity : entities) {
            for (String sc : seacreatures) if (entity.getName().contains(sc.replaceAll("§.", ""))) temp.put(sc, temp.getOrDefault(sc, 0) + 1);
        }
        if (temp.containsKey("§fSquid")) temp.put("§fSquid", temp.getOrDefault("§fSquid", 0)/2);
        if (scCount.keySet().size() == 0 && temp.keySet().size() > 0) timer = time.getCurrentMS();
        if (scCount.keySet().size() > 0 && temp.keySet().size() == 0) timer = 0;
        scCount = temp;

    }


    @SubscribeEvent
    public void onRenderOverlay(RenderGameOverlayEvent event) {
        if (event.type != RenderGameOverlayEvent.ElementType.TEXT || !configFile.fishingOverlay) return;

        TextStyle textStyle = TextStyle.fromInt(1);

        if (configFile.testGui && scCount.entrySet().size() > 0) {
            GlStateManager.pushMatrix();
            GlStateManager.translate(element.position.getX(), element.position.getY(), 500.0);
            GlStateManager.scale(element.position.getScale(), element.position.getScale(), 1.0);


            int counter = 1;
            float timeSince = (time.getCurrentMS() - timer) / 60000F;
            if (timeSince > 0 && timeSince < 5) OverlayUtils.drawString(0, 0, EnumChatFormatting.GOLD + "Time: " + EnumChatFormatting.YELLOW + SplitsOverlay.format(timeSince), textStyle, Alignment.Left);
            if (timeSince > 5) OverlayUtils.drawString(0, 0, EnumChatFormatting.GOLD + "Time: " + EnumChatFormatting.RED + SplitsOverlay.format(timeSince), textStyle, Alignment.Left);

            for (Map.Entry<String, Integer> entry : scCount.entrySet()) {
                OverlayUtils.drawString(0, 10*counter, entry.getKey() + EnumChatFormatting.RESET + ": " + entry.getValue(), textStyle, Alignment.Left);
                counter++;
            }

            int total = scCount.values().stream().mapToInt(Integer::intValue).sum();

            if (total > 0) OverlayUtils.drawString(0, 10*counter, EnumChatFormatting.GOLD + "Total SC: " + EnumChatFormatting.YELLOW + total, textStyle, Alignment.Left);

            //element.setHeight(10*counter);

            GlStateManager.popMatrix();

        }
        if (mc.currentScreen instanceof MoveGui) {
            GlStateManager.pushMatrix();
            GlStateManager.translate(element.position.getX(), element.position.getY(), 500.0);
            GlStateManager.scale(element.position.getScale(), element.position.getScale(), 1.0);

            int max = 0;
            int counter = 0;
            for (String sc : seacreatures.subList(0, 10)) {
                String string = sc + EnumChatFormatting.RESET + ": 1";
                OverlayUtils.drawString(0, 10*counter, string, textStyle, Alignment.Left);
                max = Math.max(max, renderWidth(string));
                counter++;
            }


            element.setWidth(max);
            element.setHeight(100);

            GlStateManager.popMatrix();
        }
    }

}
