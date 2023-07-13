package com.golem.skyblockutils.features.KuudraFight;

import com.golem.skyblockutils.utils.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.util.Vec3;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.Map;

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
            if (supply.getValue() == 101) {
                int rgb = 0x00FFFF; //AQUA
                RenderUtils.renderBeaconBeam(supply.getKey().xCoord - viewerX, supply.getKey().yCoord - viewerY, supply.getKey().zCoord - viewerZ, rgb, 1.0f, event.partialTicks);
            }
            else if (supply.getValue() > -1) {
                int percent = supply.getValue();
                int rgb = percent == 0 ? 0xFF0000 : percent == 100 ? 0x00FF00 : (int) ((1 - (percent / 100.0)) * 0xFF) << 16 | (int) ((percent / 100.0) * 0xFF) << 8;
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
                    if (name.contains("COMPLETE")) {
                        Kuudra.supplyWaypoints.put(supply, -1);
                    } else {
                        int percent = Integer.parseInt(name.split(" ")[1].split("%")[0]);
                        Kuudra.supplyWaypoints.put(supply, percent);
                    }
                }
            }
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
