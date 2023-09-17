package com.golem.skyblockutils.features.Bestiary;

import com.golem.skyblockutils.Main;
import com.golem.skyblockutils.features.KuudraFight.Kuudra;
import com.google.gson.JsonObject;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import tv.twitch.chat.Chat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TrackKills {
    public static HashMap<String, Integer> bestiary = new HashMap<>();
    private static final Pattern pattern = Pattern.compile("§8\\[§7Lv(\\d+)§8\\] §c(.+?) §a(\\d+)§f/§a(\\d+)§c❤");
    private static final Pattern pattern2 = Pattern.compile("§b§l(.*?)§7§8([IVXLCDM]+) §8➡§b §b([IVXLCDM]+)");

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
    public void onChat(ClientChatReceivedEvent event) {
        if (!event.message.getFormattedText().contains("➡")) return;
        String message = event.message.getUnformattedText();

        Matcher matcher = pattern2.matcher(message);
        if (matcher.find()) {
            String name = matcher.group(1);
            int level = Bestiary.romanToInt(matcher.group(3));
            name = name.replaceAll("§.", "");
            if (Bestiary.bestiary.containsKey(name)) {
                Main.mc.thePlayer.addChatMessage(new ChatComponentText(EnumChatFormatting.GREEN + "Registered " + name + " bestiary leveling to " + level + " (" + Bestiary.bestiary.get(name).bracket[level - 1] + ") kills."));
                Bestiary.bestiary.get(name).newLevel(level);
            }
        }
    }

    @SubscribeEvent
    public void onEntityDeath(LivingDeathEvent event) {
        if (event.entity.getPosition().distanceSq(Main.mc.thePlayer.getPosition()) < 64) {
            //for (Entity e : Kuudra.getAllEntitiesInRange(8)) System.out.println(e + " " + e.getDisplayName());
            String entityName = Objects.requireNonNull(closest(event.entity)).getDisplayName().getFormattedText();
            Matcher matcher = pattern.matcher(entityName);


            if (matcher.find()) {
                int level = Integer.parseInt(matcher.group(1));
                String name = matcher.group(2);
                String currentHealth = matcher.group(3);
                String maxHealth = matcher.group(4);

                Kuudra.addChatMessage("Level " + level + " " + name + " " + currentHealth + "/" + maxHealth);
                name = name.replaceAll("§.", "");



                if (Bestiary.bestiary.containsKey(name)) {
                    Bestiary.bestiary.get(name).addKill();
                    Bestiary.bestiary.get(name).output(name);
                }

            }
            entityName = entityName.split("§r")[0];
            bestiary.put(entityName, bestiary.getOrDefault(entityName, 0) + 1);
        }

    }
}
