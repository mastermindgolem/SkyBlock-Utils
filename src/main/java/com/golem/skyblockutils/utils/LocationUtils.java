package com.golem.skyblockutils.utils;

import com.golem.skyblockutils.Main;
import com.golem.skyblockutils.events.LocationChangeEvent;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LocationUtils {
    private static final TimeHelper time = new TimeHelper();
    private static final Pattern JSON_BRACKET_PATTERN = Pattern.compile("^\\{.+}");

    private static long lastManualLocRaw = -1;
    private long lastLocRaw = -1;
    public static String mode = null;

    private JsonObject locraw = null;
    public long joinedWorld = -1;

    private static boolean active = false;

    @SubscribeEvent
    public void onClientConnected(FMLNetworkEvent.ClientConnectedToServerEvent event) {
        if (Main.mc.getCurrentServerData() != null) {
            if (Main.mc.getCurrentServerData().serverIP.contains("hypixel.net")) {
                active = true;
            }
        }
    }

    public static void onSendChatMessage(String msg) {
        if (msg.trim().startsWith("/locraw") || msg.trim().startsWith("/locraw ")) {
            lastManualLocRaw = time.getCurrentMS();
        }
    }

    @SubscribeEvent
    public void onWorldLoad(WorldEvent.Load event) {
        if (!active) return;
        lastLocRaw = -1;
        locraw = null;
        this.setLocation(null);
        joinedWorld = time.getCurrentMS();
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onTick(TickEvent.ClientTickEvent event) {
        if (!active) return;
        if (Main.mc.thePlayer == null) return;
        if (Main.mc.theWorld == null) return;
        if (locraw != null) return;
        if (time.getCurrentMS() - joinedWorld <= 1000) return;
        if (time.getCurrentMS() - lastLocRaw <= 15000) return;
        lastLocRaw = time.getCurrentMS();
        Main.mc.thePlayer.sendChatMessage("/locraw");
    }

    @SubscribeEvent(priority = EventPriority.LOWEST, receiveCanceled = true)
    public void onChatMessage(ClientChatReceivedEvent event) {
        if (!active) return;
        Matcher matcher = JSON_BRACKET_PATTERN.matcher(event.message.getUnformattedText());
        if (matcher.find()) {
            try {
                JsonObject obj = new Gson().fromJson(matcher.group(), JsonObject.class);
                if (obj.has("server")) {
                    if (time.getCurrentMS() - lastManualLocRaw > 5000) event.setCanceled(true);
                    if (obj.has("gametype") && obj.has("mode") && obj.has("map")) {
                        locraw = obj;
                        setLocation(locraw.get("mode").getAsString());
                    }
                }
            } catch (Exception ignored) {
            }
        }
    }

    public static String getLocation() {
        return mode;
    }


    public void setLocation(String location) {
        location = location == null ? null : location.intern();
        if (!Objects.equals(mode, location)) {
            MinecraftForge.EVENT_BUS.post(new LocationChangeEvent(location, mode));
        }
        mode = location;
    }
}
