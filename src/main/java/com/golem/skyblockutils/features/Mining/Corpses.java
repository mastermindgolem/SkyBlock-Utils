package com.golem.skyblockutils.features.Mining;

import com.golem.skyblockutils.Main;
import com.golem.skyblockutils.utils.LocationUtils;
import com.golem.skyblockutils.utils.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Corpses {

    private HashMap<EntityArmorStand, Boolean> corpses = new HashMap<>();

    @SubscribeEvent
    public void onWorldLoad(WorldEvent.Load event) {
        corpses.clear();
    }
    @SubscribeEvent
    public void renderEvent(RenderWorldLastEvent event) {
        if (!Objects.equals(LocationUtils.getLocation(), "mineshaft")) return;
        if (!Main.configFile.corpseLocator) return;
        Minecraft mc = Minecraft.getMinecraft();
        List<EntityArmorStand> entities = mc.theWorld.getEntities(EntityArmorStand.class, entity -> true);


        for (EntityArmorStand armorStand : entities) {
            if (armorStand.getCurrentArmor(3) != null) {
//            if (armorStand.getCurrentArmor(3) != null && mc.thePlayer.canEntityBeSeen(armorStand)) {
                corpses.putIfAbsent(armorStand, false);
            }
        }

        for (Map.Entry<EntityArmorStand, Boolean> armorStand : corpses.entrySet()) {
            EntityArmorStand corpse = armorStand.getKey();
            boolean looted = armorStand.getValue();
            if (!looted) {
                RenderUtils.renderWaypointText("§cCorpse!", corpse.posX, corpse.posY, corpse.posZ, event.partialTicks);
                RenderUtils.renderBeaconBeam(corpse.posX, corpse.posY, corpse.posZ, 0xff0000, 1, event.partialTicks);
            }
        }
    }

    @SubscribeEvent
    public void onChatMessage(ClientChatReceivedEvent event) {
        if (event.type == 2) return;
        String message = event.message.getUnformattedText();
        System.out.println(message);
        if (message.contains("FROZEN CORPSE LOOT!")) {
            removeClosestCorpse();
        }
        if (message.equals(" WOW! You found a Glacite Mineshaft portal!")) {
            //idk what to do here yet
        }
    }

    private void removeClosestCorpse() {
        Minecraft mc = Minecraft.getMinecraft();
        Entity player = mc.thePlayer;
        double closestDistanceSq = Double.MAX_VALUE;
        EntityArmorStand closestCorpse = null;

        for (EntityArmorStand corpse : corpses.keySet()) {
            double distanceSq = player.getDistanceSq(corpse.posX, corpse.posY, corpse.posZ);
            if (distanceSq < closestDistanceSq) {
                closestDistanceSq = distanceSq;
                closestCorpse = corpse;
            }
        }

        if (closestCorpse != null) {
            corpses.put(closestCorpse, true);
        }
    }
}
