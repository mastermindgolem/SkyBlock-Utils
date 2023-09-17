package com.golem.skyblockutils.features.Bestiary;

import com.golem.skyblockutils.features.KuudraFight.Kuudra;
import com.google.gson.JsonObject;
import net.minecraft.util.EnumChatFormatting;

import java.util.List;
import java.util.Map;

import static com.golem.skyblockutils.features.Bestiary.Bestiary.bestiary;
import static com.golem.skyblockutils.features.Bestiary.Bestiary.killBrackets;

public class Mob {

    private final List<String> names;
    private final List<Integer> levels;
    public final int[] bracket;
    public final int maxLevel;
    private int level;
    private int kills;
    private int next;
    private double time;

    public Mob(List<String> names, List<Integer> levels, int bracketIndex, int maxLevel, double time) {
        this.names = names;
        this.levels = levels;
        this.bracket = killBrackets[bracketIndex - 1];
        this.maxLevel = maxLevel - 1;
        this.level = 0;
        this.kills = 0;
        this.next = 0;
        this.time = time;
    }

    public void addKill() {
        this.kills++;
        for (int i = bracket.length - 1; i >= 0; i--) {
            if (kills > bracket[i]) {
                this.level = i;
                break;
            }
        }
        this.next = Math.max(bracket[Math.min(level + 1, maxLevel - 1)] - kills, 0);
    }

    public void newLevel(int level) {
        this.level = level - 1;
        kills = this.bracket[level - 1];
        this.next = Math.max(bracket[Math.min(level + 1, maxLevel - 1)] - kills, 0);
    }

    public void updateKills(JsonObject bestiaryApi) {
        int temp_kills = 0;
        for (String name : names) {
            for (int mobLevel : levels) {
                if (!bestiaryApi.has(name + "_" + mobLevel)) continue;
                int kills = bestiaryApi.get(name + "_" + mobLevel).getAsInt();
                temp_kills += kills;
            }
        }
        this.kills = temp_kills;

        for (int i = bracket.length - 1; i >= 0; i--) {
            if (kills > bracket[i]) {
                this.level = i;
                break;
            }
        }

        this.next = Math.max(bracket[Math.min(level + 1, maxLevel - 1)] - kills, 0);
    }

    public void output(String key) {
        Kuudra.addChatMessage(EnumChatFormatting.YELLOW + "Tier " + this.level + "/" + this.maxLevel + " " + EnumChatFormatting.RED + key + ": " + EnumChatFormatting.YELLOW + this.kills + ". Next tier in: " + this.next);
    }
}
