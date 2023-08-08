package com.golem.skyblockutils.features;

import com.golem.skyblockutils.Main;
import com.golem.skyblockutils.features.KuudraFight.Kuudra;
import com.golem.skyblockutils.utils.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.monster.EntityMagmaCube;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class KuudraHealth {


    @SubscribeEvent
    public void renderWorld(RenderWorldLastEvent event) {
        try {
            ArrayList<Entity> entities = Kuudra.getAllEntitiesInRange();
            List<Entity> kuudra = entities.stream().filter(e -> e instanceof EntityMagmaCube && e.width > 14 && ((EntityMagmaCube) e).getMaxHealth() <= 100000).collect(Collectors.toList());
            for (Entity e : kuudra) {

                if (Kuudra.tier == 5 && Kuudra.currentPhase == 4) RenderUtils.drawEntityBox(e, Color.GREEN, 20, event.partialTicks);
                Kuudra.boss = (EntityMagmaCube) e;
            }
        } catch (Exception ignored) {}
    }


}
