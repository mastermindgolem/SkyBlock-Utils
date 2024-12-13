package com.golem.skyblockutils.features;

import com.golem.skyblockutils.Main;
import com.golem.skyblockutils.utils.RenderUtils;
import com.golem.skyblockutils.utils.TimeHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChatWaypoints {


    private static final TimeHelper time = new TimeHelper();

    private static HashMap<Long, Object[]> waypoints = new HashMap<>();
    private static final Pattern pattern = Pattern.compile("(?:\\[\\w+])?\\s*(?:Party >)?\\s*([^:]+).*x:\\s*(-?\\d+),\\s*y:\\s*(-?\\d+),\\s*z:\\s*(-?\\d+)");


    @SubscribeEvent
    public void onChat(ClientChatReceivedEvent event) {
        if (Main.configFile.showWaypoints == 0) return;
        String message = event.message.getUnformattedText().replaceAll("§.", "");
        if (!message.startsWith("Party > ") || !message.contains("x: ") || !message.contains(", y:") || !message.contains(", z:")) return;
        Matcher matcher = pattern.matcher(message);
        if (matcher.find()) {
            String text = matcher.group(1);
            String x = matcher.group(2);
            String y = matcher.group(3);
            String z = matcher.group(4);
            waypoints.put(time.getCurrentMS() + 1000L * Main.configFile.showWaypoints, new Object[]{text, x, y, z});
        }
    }

    @SubscribeEvent
    public void RenderEvent(RenderWorldLastEvent event) {
        if (Main.configFile.showWaypoints == 0 || Main.mc.thePlayer == null) return;
        Entity viewer = Minecraft.getMinecraft().getRenderViewEntity();
        double viewerX = viewer.lastTickPosX + (viewer.posX - viewer.lastTickPosX) * event.partialTicks;
        double viewerY = viewer.lastTickPosY + (viewer.posY - viewer.lastTickPosY) * event.partialTicks;
        double viewerZ = viewer.lastTickPosZ + (viewer.posZ - viewer.lastTickPosZ) * event.partialTicks;
        HashMap<Long, Object[]> waypoints2 = new HashMap<>();
        for (Map.Entry<Long, Object[]> entry : waypoints.entrySet()) if (time.getCurrentMS() < entry.getKey()) {
            Object[] waypoint = entry.getValue();
            RenderUtils.renderWaypointText(waypoint[0].toString(), Integer.parseInt(waypoint[1].toString()), Integer.parseInt(waypoint[2].toString()) + 2, Integer.parseInt(waypoint[3].toString()), event.partialTicks);
            RenderUtils.renderBeaconBeam(Integer.parseInt(waypoint[1].toString()) - viewerX, Integer.parseInt(waypoint[2].toString()) - viewerY, Integer.parseInt(waypoint[3].toString()) - viewerZ, Main.configFile.supplyColor.getRGB(), 1.0f, event.partialTicks);
            waypoints2.put(entry.getKey(), waypoint);
        }
        waypoints = waypoints2;
    }

}
