package com.golem.skyblockutils.features.CrimsonIsles;

import com.golem.skyblockutils.Main;
import com.golem.skyblockutils.utils.LocationUtils;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Objects;

public class Vanquisher {



    @SubscribeEvent
    public void onDeath(LivingDeathEvent event) {
        if (!Objects.equals(LocationUtils.getLocation(), "crimson_isle")) return;
        ItemStack heldItem = Main.mc.thePlayer.getHeldItem();
        if (heldItem == null) return;


    }
}
