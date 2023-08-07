package com.golem.skyblockutils.features.KuudraFight;

import com.golem.skyblockutils.Main;
import com.golem.skyblockutils.models.Overlay.TextOverlay.AlertOverlay;
import com.golem.skyblockutils.models.Overlay.TextOverlay.CratesOverlay;
import com.golem.skyblockutils.models.Overlay.TextOverlay.ProfitOverlay;
import com.golem.skyblockutils.models.Overlay.TextOverlay.SplitsOverlay;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.monster.EntityMagmaCube;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.Vec3;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static com.golem.skyblockutils.Main.*;

public class Kuudra {
    public static HashMap<Vec3, Integer> supplyWaypoints = new HashMap<>(6);
    public static List<String> partyMembers = new ArrayList<>();
    public static Float[] splits = new Float[]{0F, 0F, 0F, 0F, 0F, 0F};
    public static int currentPhase = -1;
    public static EntityMagmaCube boss = null;
    public static boolean stunner = false;

    @SubscribeEvent
    public void onWorldLoad(EntityJoinWorldEvent event) {
        if (event.entity != Main.mc.thePlayer) return;
        Kuudra.currentPhase = -1;
        supplyWaypoints = new HashMap<>(6);
        CratesOverlay.phase0 = new HashMap<>();
        CratesOverlay.phase1 = new HashMap<>();
        CratesOverlay.phase2 = new HashMap<>();
        CratesOverlay.phase4 = new ArrayList<>();
        CratesOverlay.playerInfo = new HashMap<>();
        if(Arrays.asList(new String[]{"FRONT!", "BACK!", "LEFT!", "RIGHT!"}).contains(AlertOverlay.text)) AlertOverlay.text = "";
    }

    @SubscribeEvent
    public void onChat(ClientChatReceivedEvent event) {
        String message = event.message.getUnformattedText().replaceAll("§.", "");
        if (message.equals("[NPC] Elle: Talk with me to begin!")) {
            currentPhase = 0;
            supplyWaypoints = new HashMap<>(6);
            CratesOverlay.phase0 = new HashMap<>();
            CratesOverlay.phase1 = new HashMap<>();
            CratesOverlay.phase2 = new HashMap<>();
            CratesOverlay.phase4 = new ArrayList<>();
            CratesOverlay.playerInfo = new HashMap<>();
            if(Arrays.asList(new String[]{"FRONT!", "BACK!", "LEFT!", "RIGHT!"}).contains(AlertOverlay.text)) AlertOverlay.text = "";
            splits[0] = (float) Main.time.getCurrentMS();
            partyMembers = new ArrayList<>();
            if (ProfitOverlay.start == 0) ProfitOverlay.start = time.getCurrentMS();


            stunner = false;
            if (configFile.TapWarning && mc.thePlayer.inventoryContainer.inventorySlots.stream().noneMatch(slot -> slot.getStack().getDisplayName().contains("Toxic Arrow Poison"))) {
                AlertOverlay.text = EnumChatFormatting.RED + "NO TAP";
            }
        }
        if (message.equals("[NPC] Elle: Okay adventurers, I will go and fish up Kuudra!")) {
            currentPhase = 1;
            splits[1] = (float) Main.time.getCurrentMS();
            addChatMessage(EnumChatFormatting.AQUA + "Ready Up: " + EnumChatFormatting.RESET + SplitsOverlay.format(splits[1]/60000F - splits[0]/60000F));

            supplyWaypoints.put(Waypoints.supply1, 101);
            supplyWaypoints.put(Waypoints.supply2, 101);
            supplyWaypoints.put(Waypoints.supply3, 101);
            supplyWaypoints.put(Waypoints.supply4, 101);
            supplyWaypoints.put(Waypoints.supply5, 101);
            supplyWaypoints.put(Waypoints.supply6, 101);
        }
        if (message.equals("[NPC] Elle: OMG! Great work collecting my supplies!")) {
            currentPhase = 2;
            splits[2] = (float) Main.time.getCurrentMS();
            addChatMessage(EnumChatFormatting.AQUA + "Supplies: " + EnumChatFormatting.RESET + SplitsOverlay.format(splits[2]/60000F - splits[1]/60000F));
            
            supplyWaypoints.put(Waypoints.supply1, -1);
            supplyWaypoints.put(Waypoints.supply2, -1);
            supplyWaypoints.put(Waypoints.supply3, -1);
            supplyWaypoints.put(Waypoints.supply4, -1);
            supplyWaypoints.put(Waypoints.supply5, -1);
            supplyWaypoints.put(Waypoints.supply6, -1);

        }
        if (message.equals("[NPC] Elle: Phew! The Ballista is finally ready! It should be strong enough to tank Kuudra's blows now!")) {
            currentPhase = 3;
            splits[3] = (float) Main.time.getCurrentMS();
            addChatMessage(EnumChatFormatting.AQUA + "Build: " + EnumChatFormatting.RESET + SplitsOverlay.format(splits[3]/60000F - splits[2]/60000F));


            supplyWaypoints.put(Waypoints.supply1, -1);
            supplyWaypoints.put(Waypoints.supply2, -1);
            supplyWaypoints.put(Waypoints.supply3, -1);
            supplyWaypoints.put(Waypoints.supply4, -1);
            supplyWaypoints.put(Waypoints.supply5, -1);
            supplyWaypoints.put(Waypoints.supply6, -1);
        }
        if (message.equals("[NPC] Elle: POW! SURELY THAT'S IT! I don't think he has any more in him!")) {
            currentPhase = 4;
            stunner = false;
            splits[4] = (float) Main.time.getCurrentMS();
            addChatMessage(EnumChatFormatting.AQUA + "Fuel/Stun: " + EnumChatFormatting.RESET + SplitsOverlay.format(splits[4]/60000F - splits[3]/60000F));

        }
        if (message.contains("DEFEAT") && currentPhase == 4) {
            currentPhase = 5;
            splits[5] = (float) Main.time.getCurrentMS();
            addChatMessage(EnumChatFormatting.AQUA + "Kuudra Kill: " + EnumChatFormatting.RESET + SplitsOverlay.format(splits[5]/60000F - splits[4]/60000F));
            ProfitOverlay.end = time.getCurrentMS();
        }
        if (message.contains("KUUDRA DOWN") && currentPhase == 4) {
            currentPhase = 5;
            splits[5] = (float) Main.time.getCurrentMS();
            addChatMessage(EnumChatFormatting.AQUA + "Supplies: " + EnumChatFormatting.RESET + SplitsOverlay.format(splits[2]/60000F - splits[1]/60000F));
            addChatMessage(EnumChatFormatting.AQUA + "Build: " + EnumChatFormatting.RESET + SplitsOverlay.format(splits[3]/60000F - splits[2]/60000F));
            addChatMessage(EnumChatFormatting.AQUA + "Fuel/Stun: " + EnumChatFormatting.RESET + SplitsOverlay.format(splits[4]/60000F - splits[3]/60000F));
            addChatMessage(EnumChatFormatting.AQUA + "Kuudra Kill: " + EnumChatFormatting.RESET + SplitsOverlay.format(splits[5]/60000F - splits[4]/60000F));
            ProfitOverlay.end = time.getCurrentMS();
        }
        if (message.endsWith("has been eaten by Kuudra!") && message.startsWith(Main.mc.getSession().getUsername())) stunner = true;

    }


    public static ArrayList<Entity> getAllEntitiesInRange() {
        ArrayList<Entity> entities = new ArrayList<>();
        for (Entity entity1 : (Minecraft.getMinecraft().theWorld.loadedEntityList)) {
            if (!(entity1 instanceof EntityItem) && !(entity1 instanceof EntityXPOrb) &&!(entity1 instanceof EntityWither) && !(entity1 instanceof EntityPlayerSP)) {
                entities.add(entity1);
            }
        }
        return entities;
    }

    public static ArrayList<Entity> getAllEntitiesInRange(int range) {
        ArrayList<Entity> entities = new ArrayList<>();
        for (Entity entity1 : (Minecraft.getMinecraft().theWorld.loadedEntityList)) {
            if (!(entity1 instanceof EntityItem) && !(entity1 instanceof EntityXPOrb) &&!(entity1 instanceof EntityWither) && !(entity1 instanceof EntityPlayerSP) && Math.pow(entity1.posX - Main.mc.thePlayer.posX, 2) + Math.pow(entity1.posZ - Main.mc.thePlayer.posZ, 2) < range*range) {
                entities.add(entity1);
            }
        }
        return entities;
    }


    public static void addChatMessage(String string) {
        mc.thePlayer.addChatMessage(new ChatComponentText(string));
    }
}
