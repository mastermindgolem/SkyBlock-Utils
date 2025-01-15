package com.golem.skyblockutils.features.KuudraFight;

import com.golem.skyblockutils.utils.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.Vec3;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.awt.*;
import java.util.ArrayList;
import java.util.Map;

import static com.golem.skyblockutils.Main.config;

public class Waypoints {


    public static Vec3 supply1 = new Vec3(-94, 78.125, -106.0);
    public static Vec3 supply2 = new Vec3(-98, 78.125, -112.9375);
    public static Vec3 supply3 = new Vec3(-98, 78.125, -99.0625);
    public static Vec3 supply4 = new Vec3(-106, 78.125, -112.9375);
    public static Vec3 supply5 = new Vec3(-110, 78.125, -106.0);
    public static Vec3 supply6 = new Vec3(-106, 78.125, -99.0625);


    @SubscribeEvent
    public void RenderEvent(RenderWorldLastEvent event) {

        Entity viewer = Minecraft.getMinecraft().getRenderViewEntity();
        double viewerX = viewer.lastTickPosX + (viewer.posX - viewer.lastTickPosX) * event.partialTicks;
        double viewerY = viewer.lastTickPosY + (viewer.posY - viewer.lastTickPosY) * event.partialTicks;
        double viewerZ = viewer.lastTickPosZ + (viewer.posZ - viewer.lastTickPosZ) * event.partialTicks;


        for (Map.Entry<Vec3, Integer> supply : Kuudra.supplyWaypoints.entrySet()) {
            if (supply.getValue() == 101 && config.getConfig().kuudraCategory.phase1.showSupplyWaypoint && supply.getKey().yCoord < 80) {
                RenderUtils.renderBeaconBeam(supply.getKey().xCoord - viewerX, supply.getKey().yCoord - viewerY, supply.getKey().zCoord - viewerZ, config.getConfig().kuudraCategory.phase1.supplyWaypointColour.getEffectiveColour().getRGB(), 1.0f, event.partialTicks);
            }
            else if (supply.getValue() < 101 && supply.getValue() > -1 && config.getConfig().kuudraCategory.phase2.showBuildWaypoint) {
                int percent = supply.getValue();
                int rgb = config.getConfig().kuudraCategory.phase2.buildWaypointColour.getEffectiveColour().getRGB();
                if (config.getConfig().kuudraCategory.phase2.useGradient) rgb = percent == 0 ? 0xFF0000 : percent == 100 ? 0x00FF00 : (int) ((1 - (percent / 100.0)) * 0xFF) << 16 | (int) ((percent / 100.0) * 0xFF) << 8;
                RenderUtils.renderBeaconBeam(supply.getKey().xCoord - viewerX, supply.getKey().yCoord - viewerY, supply.getKey().zCoord - viewerZ, rgb, 1.0f, event.partialTicks);
            }
        }

        ArrayList<Entity> entities = Kuudra.getAllEntitiesInRange();

        for (Entity entity : entities) {
            if (entity instanceof EntityArmorStand) {
                Vec3 supply = ClosestSupply(entity.getPositionVector());
                String name = entity.getName().replaceAll("§.", "");
                if (name.contains("BRING SUPPLY CHEST HERE")) {
                    if (Kuudra.supplyWaypoints.getOrDefault(supply, 101) == -1) Kuudra.supplyWaypoints.put(supply, 101);
                }
                if (name.contains("SUPPLIES RECEIVED")) {
                    if (Kuudra.supplyWaypoints.getOrDefault(supply, -1) == 101) Kuudra.supplyWaypoints.put(supply, -1);
                }
                if (name.contains("%") && name.contains("PROGRESS: ")) {
                    int percent = Integer.parseInt(name.split(" ")[1].split("%")[0]);
                    Kuudra.supplyWaypoints.put(supply, percent);
                }
                if (name.contains("COMPLETE") && name.contains("PROGRESS")) {
                    Kuudra.supplyWaypoints.put(supply, -1);
                }
            }
        }

        if (((Kuudra.currentPhase == 3 && (Kuudra.tier == 1 || Kuudra.tier == 2)) || Kuudra.currentPhase == 1) && config.getConfig().kuudraCategory.phase1.safeSpots) {
            RenderUtils.drawBlockBox(new BlockPos(-86, 77, -129), Color.GREEN, 5, event.partialTicks);
            RenderUtils.drawBlockBox(new BlockPos(-90, 77, -128), Color.GREEN, 5, event.partialTicks);
            RenderUtils.drawBlockBox(new BlockPos(-71, 78, -135), Color.GREEN, 5, event.partialTicks);
            RenderUtils.drawBlockBox(new BlockPos(-74, 75, -117), Color.GREEN, 5, event.partialTicks);
            RenderUtils.drawBlockBox(new BlockPos(-69, 77, -105), Color.GREEN, 5, event.partialTicks);
            RenderUtils.drawBlockBox(new BlockPos(-79, 77, -90), Color.GREEN, 5, event.partialTicks);
            RenderUtils.drawBlockBox(new BlockPos(-111, 75, -69), Color.GREEN, 5, event.partialTicks);
            RenderUtils.drawBlockBox(new BlockPos(-123, 78, -89), Color.GREEN, 5, event.partialTicks);
            RenderUtils.drawBlockBox(new BlockPos(-141, 77, -91), Color.GREEN, 5, event.partialTicks);
            RenderUtils.drawBlockBox(new BlockPos(-131, 77, -115), Color.GREEN, 5, event.partialTicks);
            RenderUtils.drawBlockBox(new BlockPos(-136, 77, -129), Color.GREEN, 5, event.partialTicks);
        }
        if (Kuudra.currentPhase == 1 && config.getConfig().kuudraCategory.phase1.safeSpots) {
            RenderUtils.drawBlockBox(new BlockPos(-68, 76, -123), Color.GREEN, 5, event.partialTicks);
            RenderUtils.drawBlockBox(new BlockPos(-66, 75, -87), Color.GREEN, 5, event.partialTicks);
            RenderUtils.drawBlockBox(new BlockPos(-111, 75, -69), Color.GREEN, 5, event.partialTicks);
            RenderUtils.drawBlockBox(new BlockPos(-135, 76, -139), Color.GREEN, 5, event.partialTicks);
            RenderUtils.renderWaypointText(EnumChatFormatting.YELLOW + "Start",-68, 76, -123, event.partialTicks);
            RenderUtils.renderWaypointText(EnumChatFormatting.YELLOW + "Start",-66, 75, -87, event.partialTicks);
            RenderUtils.renderWaypointText(EnumChatFormatting.YELLOW + "Start",-111, 75, -69, event.partialTicks);
            RenderUtils.renderWaypointText(EnumChatFormatting.YELLOW + "Start",-135, 76, -139, event.partialTicks);
        }
    }


    public static Vec3 ClosestSupply(Vec3 vector) {
        Vec3 closest = new Vec3(0, 0, 0);
        double distance = vector.distanceTo(closest);

        for (Vec3 supply : Kuudra.supplyWaypoints.keySet()) if (vector.distanceTo(supply) < distance) {
            distance = vector.distanceTo(supply);
            closest = supply;
        }
        return closest;
    }

}
