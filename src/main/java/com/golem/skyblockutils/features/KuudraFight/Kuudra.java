package com.golem.skyblockutils.features.KuudraFight;

import com.golem.skyblockutils.Main;
import com.golem.skyblockutils.PersistentData;
import com.golem.skyblockutils.models.Overlay.TextOverlay.AlertOverlay;
import com.golem.skyblockutils.models.Overlay.TextOverlay.CratesOverlay;
import com.golem.skyblockutils.models.Overlay.TextOverlay.ProfitOverlay;
import com.golem.skyblockutils.models.Overlay.TextOverlay.SplitsOverlay;
import com.golem.skyblockutils.utils.Colors;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.monster.EntityMagmaCube;
import net.minecraft.event.HoverEvent;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.Vec3;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.golem.skyblockutils.Main.*;

public class Kuudra {
    public static HashMap<Vec3, Integer> supplyWaypoints = new HashMap<>(6);
    public static List<String> partyMembers = new ArrayList<>();
    public static Float[] splits = new Float[]{0F, 0F, 0F, 0F, 0F, 0F};
    public static List<String> overview = new ArrayList<>();
    public static int currentPhase = -1;
    public static EntityMagmaCube boss = null;
    public static boolean stunner = false;
    public static int tier = 0;

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
        //AlertOverlay.text = "";
        boss = null;
        splits = new Float[]{0F, 0F, 0F, 0F, 0F, 0F};
        overview = new ArrayList<>();
    }

    public void getKuudraTier() {
        Scoreboard scoreboard = Minecraft.getMinecraft().thePlayer.getWorldScoreboard();

        ScoreObjective sidebarObjective = scoreboard.getObjectiveInDisplaySlot(1);

        List<Score> scores = new ArrayList<>(scoreboard.getSortedScores(sidebarObjective));

        for (int i = scores.size() - 1; i >= 0; i--) {
            Score score = scores.get(i);
            ScorePlayerTeam scoreplayerteam1 = scoreboard.getPlayersTeam(score.getPlayerName());
            String line = ScorePlayerTeam.formatPlayerName(scoreplayerteam1, score.getPlayerName());
            line = line.replaceAll("§.", "");
            line = Colors.cleanDuplicateColourCodes(line);
            line = Colors.cleanColour(line);
            if (line.contains("Kuudra's Hollow")) {
                if (line.contains("(T1)")) tier = 1;
                if (line.contains("(T2)")) tier = 2;
                if (line.contains("(T3)")) tier = 3;
                if (line.contains("(T4)")) tier = 4;
                if (line.contains("(T5)")) tier = 5;
            }
        }
    }

    @SubscribeEvent
    public void onChat(ClientChatReceivedEvent event) {
        String message = event.message.getUnformattedText().replaceAll("§.", "");
        if (message.equals("[NPC] Elle: Talk with me to begin!")) {
            splits = new Float[]{0F, 0F, 0F, 0F, 0F, 0F};
            overview = new ArrayList<>();
            currentPhase = 0;
            supplyWaypoints = new HashMap<>(6);
            CratesOverlay.phase0 = new HashMap<>();
            CratesOverlay.phase1 = new HashMap<>();
            CratesOverlay.phase2 = new HashMap<>();
            CratesOverlay.phase4 = new ArrayList<>();
            CratesOverlay.playerInfo = new HashMap<>();
            //AlertOverlay.text = "";
            splits[0] = (float) Main.time.getCurrentMS();
            partyMembers = new ArrayList<>();
            ProfitOverlay.runStartTime = time.getCurrentMS();

            //Account for DT if less than 5 mins
            if (ProfitOverlay.runStartTime - ProfitOverlay.runEndTime < 300000) {
                ProfitOverlay.totalTime += ProfitOverlay.runStartTime - ProfitOverlay.runEndTime;
            }

            stunner = false;
        }

        if (splits[0] == 0) return;

        if (message.equals("[NPC] Elle: Okay adventurers, I will go and fish up Kuudra!")) {
            currentPhase = 1;
            splits[1] = (float) Main.time.getCurrentMS();
            addChatMessage(EnumChatFormatting.AQUA + "Ready Up: " + EnumChatFormatting.RESET + SplitsOverlay.format(splits[1]/60000F - splits[0]/60000F));
            overview.add(EnumChatFormatting.AQUA + "Ready Up: " + EnumChatFormatting.RESET + SplitsOverlay.format(splits[1]/60000F - splits[0]/60000F));
            getKuudraTier();

            supplyWaypoints.put(Waypoints.supply1, 101);
            supplyWaypoints.put(Waypoints.supply2, 101);
            supplyWaypoints.put(Waypoints.supply3, 101);
            supplyWaypoints.put(Waypoints.supply4, 101);
            supplyWaypoints.put(Waypoints.supply5, 101);
            supplyWaypoints.put(Waypoints.supply6, 101);

            if (configFile.TapWarning && mc.thePlayer.inventoryContainer.inventorySlots.stream().noneMatch(slot -> slot.getHasStack() && slot.getStack().getDisplayName().contains("Toxic Arrow Poison"))) {
                //AlertOverlay.text = EnumChatFormatting.RED + "NO TAP";
                AlertOverlay.newAlert(EnumChatFormatting.RED + "NO TAP", 20);
            }
        }

        if (splits[1] == 0) return;

        if (message.equals("[NPC] Elle: OMG! Great work collecting my supplies!")) {
            currentPhase = 2;
            splits[2] = (float) Main.time.getCurrentMS();
            addChatMessage(EnumChatFormatting.AQUA + "Supplies: " + EnumChatFormatting.RESET + SplitsOverlay.format(splits[2]/60000F - splits[1]/60000F));
            overview.add(EnumChatFormatting.AQUA + "Supplies: " + EnumChatFormatting.RESET + SplitsOverlay.format(splits[2]/60000F - splits[1]/60000F));

            supplyWaypoints.put(Waypoints.supply1, -1);
            supplyWaypoints.put(Waypoints.supply2, -1);
            supplyWaypoints.put(Waypoints.supply3, -1);
            supplyWaypoints.put(Waypoints.supply4, -1);
            supplyWaypoints.put(Waypoints.supply5, -1);
            supplyWaypoints.put(Waypoints.supply6, -1);
        }

        if (splits[2] == 0) return;

        if (message.equals("[NPC] Elle: Phew! The Ballista is finally ready! It should be strong enough to tank Kuudra's blows now!")) {
            currentPhase = 3;
            splits[3] = (float) Main.time.getCurrentMS();
            addChatMessage(EnumChatFormatting.AQUA + "Build: " + EnumChatFormatting.RESET + SplitsOverlay.format(splits[3]/60000F - splits[2]/60000F));
            overview.add(EnumChatFormatting.AQUA + "Build: " + EnumChatFormatting.RESET + SplitsOverlay.format(splits[3]/60000F - splits[1]/60000F));

            supplyWaypoints.put(Waypoints.supply1, -1);
            supplyWaypoints.put(Waypoints.supply2, -1);
            supplyWaypoints.put(Waypoints.supply3, -1);
            supplyWaypoints.put(Waypoints.supply4, -1);
            supplyWaypoints.put(Waypoints.supply5, -1);
            supplyWaypoints.put(Waypoints.supply6, -1);
        }

        if (splits[3] == 0) return;

        if (message.equals("[NPC] Elle: POW! SURELY THAT'S IT! I don't think he has any more in him!")) {
            currentPhase = 4;
            stunner = false;
            splits[4] = (float) Main.time.getCurrentMS();
            addChatMessage(EnumChatFormatting.AQUA + "Fuel/Stun: " + EnumChatFormatting.RESET + SplitsOverlay.format(splits[4]/60000F - splits[3]/60000F));
            overview.add(EnumChatFormatting.AQUA + "Fuel/Stun: " + EnumChatFormatting.RESET + SplitsOverlay.format(splits[4]/60000F - splits[1]/60000F));
        }

        if (splits[4] == 0) return;

        if (message.contains("DEFEAT") && currentPhase == 4) {
            currentPhase = 5;
            splits[5] = (float) Main.time.getCurrentMS();
            //AlertOverlay.text = "";
            CratesOverlay.phase4.add(0F);
            addChatMessage(EnumChatFormatting.AQUA + "Kuudra Kill: " + EnumChatFormatting.RESET + SplitsOverlay.format(splits[5]/60000F - splits[4]/60000F));
            overview.add(EnumChatFormatting.AQUA + "Kuudra Kill: " + EnumChatFormatting.RESET + SplitsOverlay.format(splits[5]/60000F - splits[1]/60000F));
            overview.add(EnumChatFormatting.DARK_RED + "BOSS FAILED");
            mc.thePlayer.addChatMessage(new ChatComponentText(EnumChatFormatting.GRAY + "(Run Overview)").setChatStyle(new ChatStyle().setChatHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ChatComponentText(String.join("\n", overview))))));
            overview = new ArrayList<>();
            ProfitOverlay.runEndTime = time.getCurrentMS();
            ProfitOverlay.totalTime += (long) (splits[5] - splits[0]);
            ProfitOverlay.totalTimeWithoutDowntime += (long) (splits[5] - splits[0]);
            ProfitOverlay.totalRuns++;
        }
        if (message.contains("KUUDRA DOWN") && currentPhase == 4) {
            currentPhase = 5;
            splits[5] = (float) Main.time.getCurrentMS();
            //AlertOverlay.text = "";
            CratesOverlay.phase4.add(0F);
            overview.add(EnumChatFormatting.AQUA + "Kuudra Kill: " + EnumChatFormatting.RESET + SplitsOverlay.format(splits[5]/60000F - splits[1]/60000F));
            if (configFile.showSplits) {
                addChatMessage(EnumChatFormatting.AQUA + "Supplies: " + EnumChatFormatting.RESET + SplitsOverlay.format(splits[2]/60000F - splits[1]/60000F));
                addChatMessage(EnumChatFormatting.AQUA + "Build: " + EnumChatFormatting.RESET + SplitsOverlay.format(splits[3]/60000F - splits[2]/60000F));
                addChatMessage(EnumChatFormatting.AQUA + "Fuel/Stun: " + EnumChatFormatting.RESET + SplitsOverlay.format(splits[4]/60000F - splits[3]/60000F));
                addChatMessage(EnumChatFormatting.AQUA + "Kuudra Kill: " + EnumChatFormatting.RESET + SplitsOverlay.format(splits[5]/60000F - splits[4]/60000F));
                mc.thePlayer.addChatMessage(new ChatComponentText(EnumChatFormatting.GRAY + "(Run Overview)").setChatStyle(new ChatStyle().setChatHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ChatComponentText(String.join("\n", overview))))));
            }
            overview = new ArrayList<>();
            ProfitOverlay.runEndTime = time.getCurrentMS();
            ProfitOverlay.totalTime += (long) (splits[5] - splits[0]);
            ProfitOverlay.totalTimeWithoutDowntime += (long) (splits[5] - splits[0]);
            ProfitOverlay.totalRuns++;
            JsonObject splitsData = new JsonObject();
            List<Float> splits2 = new ArrayList<>();
            splits2.add(splits[2] - splits[1]);
            splits2.add(splits[3] - splits[2]);
            splits2.add(splits[4] - splits[3]);
            splits2.add(splits[5] - splits[4]);
            splitsData.addProperty("time", System.currentTimeMillis());
            splitsData.addProperty("tier", tier);
            splitsData.add("party", new Gson().toJsonTree(partyMembers).getAsJsonArray());
            splitsData.add("splits", new Gson().toJsonTree(splits2).getAsJsonArray());
            splitsData.addProperty("overall", splits[5] - splits[1]);
            PersistentData.splits.add(splitsData);
            persistentData.saveSplits();
        }
        if (message.endsWith("has been eaten by Kuudra!") && message.startsWith(Main.mc.getSession().getUsername())) stunner = true;
        if (message.endsWith("has been eaten by Kuudra!") && !message.contains("Elle")) overview.add(EnumChatFormatting.BLUE + "Stunner eaten: " + SplitsOverlay.format(Main.time.getCurrentMS()/60000F - splits[1]/60000F));
        if (message.endsWith("destroyed one of Kuudra's pods!")) overview.add(EnumChatFormatting.BLUE + "Stun at: " + SplitsOverlay.format(Main.time.getCurrentMS()/60000F - splits[1]/60000F));
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
