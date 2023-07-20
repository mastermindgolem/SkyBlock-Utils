package com.golem.skyblockutils.features;

import com.golem.skyblockutils.Main;
import com.golem.skyblockutils.models.gui.OverlayUtils;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.monster.EntityBlaze;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.Arrays;
import java.util.List;

public class BrokenHyp {


    private static final List<String> weapons = Arrays.asList("HYPERION", "VALKYRIE", "ASTRAEA", "SCYLLA", "NECRON_BLADE");
    private static int kills = 0;
    public static int currentXP = 0;
    public static int gainedXP = 0;
    private static int oldXP = 0;
    public static long lastKill = 0;
    private static long lastBreak = 0;


    @SubscribeEvent
    public void onWorldLoad(EntityJoinWorldEvent event) {
        if (event.entity != Main.mc.thePlayer) return;
        lastKill = 0;
    }

    /*
    @SubscribeEvent
    public void onTick(TickEvent event) {
        if (System.currentTimeMillis() - lastBreak < 1000) OverlayUtils.drawTitle(EnumChatFormatting.DARK_RED + "HYPE BROKEN", new ScaledResolution(Main.mc));
    }

     */


    @SubscribeEvent
    public void onEntityDeath(LivingDeathEvent event) {
        if (!(event.entity instanceof EntityBlaze) || !Main.configFile.brokenHyp) return;

        ItemStack heldItem = Main.mc.thePlayer.getHeldItem();
        if (heldItem != null && Main.mc.thePlayer.getDistanceToEntity(event.entity) < 6) {
            NBTTagCompound extraAttributes = heldItem.serializeNBT().getCompoundTag("tag").getCompoundTag("ExtraAttributes");
            if (!weapons.contains(extraAttributes.getString("id")) || !extraAttributes.hasKey("champion_combat_xp")) return;
            kills++;
            lastKill = System.currentTimeMillis();
            currentXP = (int) extraAttributes.getDouble("champion_combat_xp");
            if (currentXP != oldXP) {
                int nowCexp2 = (int) extraAttributes.getDouble("champion_combat_xp");
                gainedXP = nowCexp2 - oldXP;
                oldXP = currentXP;
                kills = -2;
            } else if (kills >= 3) {
                lastBreak = System.currentTimeMillis();
                gainedXP = 0;
                kills = 0;
            }
        }

    }
}
