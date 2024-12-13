package com.golem.skyblockutils.features.KuudraFight;

import com.golem.skyblockutils.Main;
import com.golem.skyblockutils.events.SlotClickEvent;
import com.golem.skyblockutils.utils.Colors;
import com.golem.skyblockutils.utils.LocationUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.golem.skyblockutils.Main.mc;

public class TokenHelper {

    private static int tokens = 0;
    private static String chosenClass = "";
    private static int mechanicLevel = 0;
    private static int blightLevel = 0;
    private static int cannonball = 0;
    private static boolean stunner = false;
    private static int minerLevel = 0;
    private static boolean hasClassBeenChosen = false;


    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.START || mc.theWorld == null || mc.thePlayer == null || Kuudra.currentPhase <= 0) return;
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
            if (line.startsWith("Tokens: ")) {
                try {
                    tokens = Integer.parseInt(line.split(" ")[1].replaceAll(",", ""));
                } catch (Exception ignored) {}
            }
        }

    }

    @SubscribeEvent
    public void onMouseClick(SlotClickEvent event) {
        if (!Objects.equals(LocationUtils.getLocation(), "kuudra")) return;
        String chestName = "";
        try {
            GuiScreen gui = Main.mc.currentScreen;
            if (!(gui instanceof GuiChest)) return;
            Container container = ((GuiChest) gui).inventorySlots;
            if (!(container instanceof ContainerChest)) return;
            chestName = ((ContainerChest) container).getLowerChestInventory().getDisplayName().getUnformattedText();
        } catch (Exception ignored) {}


        if (Objects.equals(chestName, "Perk Menu")) {
            if (!hasClassBeenChosen) {
                if (event.slotId == 10) chosenClass = "Cannoneer";
                if (event.slotId == 12) {
                    chosenClass = "Crowd Control";
                    stunner = true;
                }
                if (event.slotId == 14) chosenClass = "Specialist";
                if (event.slotId == 16) chosenClass = "Support";
            } else {
                if (Objects.equals(chosenClass, "Crowd Control")) {
                    if (event.slotId == 23) blightLevel++;
                } else if (Objects.equals(chosenClass, "Specialist")) {
                    if (event.slotId == 13) mechanicLevel++;
                    if (event.slotId == 22) minerLevel++;
                }
            }
        }
        if (Objects.equals(chestName, "Are you sure?")) {
            if (event.slotId == 20) hasClassBeenChosen = true;
            if (event.slotId == 24) {
                hasClassBeenChosen = false;
                chosenClass = "";
            }
        }


    }


}
