package com.golem.skyblockutils.features.Dungeons;

import com.golem.skyblockutils.Main;
import com.golem.skyblockutils.features.KuudraFight.Kuudra;
import com.golem.skyblockutils.models.Overlay.TextOverlay.AlertOverlay;
import com.golem.skyblockutils.utils.Colors;
import com.golem.skyblockutils.utils.LocationUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.golem.skyblockutils.Main.mc;

public class PlayerAlert {

    private static boolean alerted = false;
    private static long lastLoad = 0;

    @SubscribeEvent
    public void onWorldLoad(WorldEvent.Load event) {
        alerted = false;
        lastLoad = System.currentTimeMillis();
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.START || mc.theWorld == null || mc.thePlayer == null || System.currentTimeMillis() - lastLoad < 1000) return;

        if (Objects.equals(LocationUtils.getLocation(), "dungeon") && Main.configFile.dungeonPlayerAlert) {
            try {
                Scoreboard scoreboard = Minecraft.getMinecraft().thePlayer.getWorldScoreboard();

                ScoreObjective sidebarObjective = scoreboard.getObjectiveInDisplaySlot(1);

                List<Score> scores = new ArrayList<>(scoreboard.getSortedScores(sidebarObjective));

                List<String> lines = new ArrayList<>();
                for (int i = scores.size() - 1; i >= 0; i--) {
                    Score score = scores.get(i);
                    ScorePlayerTeam scoreplayerteam1 = scoreboard.getPlayersTeam(score.getPlayerName());
                    String line = ScorePlayerTeam.formatPlayerName(scoreplayerteam1, score.getPlayerName());
                    line = Colors.cleanDuplicateColourCodes(line);
                    String cleanLine = Colors.cleanColour(line);
                    if (cleanLine.contains(" [Lv") && !cleanLine.contains(Main.mc.thePlayer.getName())) lines.add(cleanLine.split(" ")[1]);
                }
                if (lines.size() < 4 && !alerted) {
                    AlertOverlay.newAlert(EnumChatFormatting.RED + "NOT 5/5 PLAYERS", 40);
                    alerted = true;
                }
            } catch (Exception ignored) {
            }
        } else if (Objects.equals(LocationUtils.getLocation(), "kuudra") && Main.configFile.kuudraPlayerAlert) {
            if (Kuudra.partyMembers.size() < 4 && !alerted) {
                AlertOverlay.newAlert(EnumChatFormatting.RED + "NOT 4/4 PLAYERS", 40);
                alerted = true;
            }
        }
    }
}
