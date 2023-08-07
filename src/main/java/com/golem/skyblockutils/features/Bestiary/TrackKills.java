package com.golem.skyblockutils.features.Bestiary;

import com.golem.skyblockutils.Main;
import com.golem.skyblockutils.features.KuudraFight.Kuudra;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class TrackKills {
    public static HashMap<String, Integer> bestiary = new HashMap<>();

    private static EntityArmorStand closest(Entity entity) {
        ArrayList<Entity> entities = Kuudra.getAllEntitiesInRange(8);
        double closest = Double.MAX_VALUE;
        EntityArmorStand close = null;
        for (Entity e : entities)
            if (e instanceof EntityArmorStand && e.getDistanceToEntity(entity) < closest && e.getDisplayName().getUnformattedText().contains("Lv")) {
                closest = e.getDistanceToEntity(entity);
                close = (EntityArmorStand) e;
            }
        return close;
    }

    @SubscribeEvent
    public void onEntityDeath(LivingDeathEvent event) {

        if (event.entity.getPosition().distanceSq(Main.mc.thePlayer.getPosition()) < 64) {
            //for (Entity e : Kuudra.getAllEntitiesInRange(8)) System.out.println(e + " " + e.getDisplayName());
            String entityName = Objects.requireNonNull(closest(event.entity)).getDisplayName().getFormattedText().split("§r")[0];
            bestiary.put(entityName, bestiary.getOrDefault(entityName, 0) + 1);
            Main.mc.thePlayer.addChatMessage(new ChatComponentText(entityName + ": " + bestiary.get(entityName)));
        }

    }
}
