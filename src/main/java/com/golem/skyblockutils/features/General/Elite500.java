package com.golem.skyblockutils.features.General;

import com.golem.skyblockutils.Main;
import net.minecraft.event.ClickEvent;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.List;

public class Elite500 {

    public static List<String> elite500 = new ArrayList<>();

    @SubscribeEvent(priority = EventPriority.LOW, receiveCanceled = true)
    public void onChat(ClientChatReceivedEvent event) {
        if (Main.mc.thePlayer == null || Main.mc.theWorld == null || !Main.configFile.showElite500) return;
        IChatComponent chatComponent = event.message;
        if (event.type == 0) {
            String startsWith = null;
            boolean partyOrGuildChat = false;

            List<IChatComponent> siblings = chatComponent.getSiblings();
            if (chatComponent.getUnformattedText().replaceAll("\u00a7.", "").startsWith("[")) {
                String msg = chatComponent.getUnformattedText().replaceAll("\u00a7.", "");
                if (!msg.contains(":")) return;
                String[] usernames = msg.split(":")[0].split(" ");
                String username = usernames[usernames.length - 1];
                System.out.println(username);
                if (elite500.contains(username)) {
                    String message = chatComponent.getUnformattedText().replaceFirst(username, username + " " + EnumChatFormatting.GRAY + "[#" + (elite500.indexOf(username) + 1) + EnumChatFormatting.GRAY + "]");
                    event.setCanceled(true);
                    Main.mc.thePlayer.addChatMessage(new ChatComponentText(message).setChatStyle(event.message.getChatStyle()));
                    return;
                }
            }
            if (!siblings.isEmpty() && siblings.get(0).getChatStyle() != null && siblings.get(0).getChatStyle().getChatClickEvent() != null && siblings.get(0).getChatStyle().getChatClickEvent().getValue().startsWith("/viewprofile")) {
                startsWith = "/viewprofile";
                partyOrGuildChat = true;
            } else {
                ClickEvent chatClickEvent = chatComponent.getChatStyle().getChatClickEvent();
                if (chatClickEvent != null) {
                    if (chatClickEvent.getValue().startsWith("/socialoptions")) {
                        startsWith = "/socialoptions";
                    }
                }
            }
            if (startsWith != null) {
                String username = partyOrGuildChat ? getNameFromChatComponent(chatComponent) : chatComponent.getChatStyle().getChatClickEvent().getValue().substring(15);
                if (username.equals("")) return;
                System.out.println(username);
                if (elite500.contains(username)) {
                    String message = chatComponent.getUnformattedText().replaceFirst(username, username + " " + EnumChatFormatting.GRAY + "[#" + (elite500.indexOf(username) + 1) + EnumChatFormatting.GRAY + "]");
                    event.setCanceled(true);
                    Main.mc.thePlayer.addChatMessage(new ChatComponentText(message).setChatStyle(event.message.getChatStyle()));
                }
            }
        }
    }

    public static String getNameFromChatComponent(IChatComponent chatComponent) {
        try {
            String unformattedText = chatComponent.getSiblings().get(0).getUnformattedText().replaceAll("§.", "");
            String username = unformattedText.substring(unformattedText.indexOf(">") + 2, unformattedText.indexOf(":"));
            // If the first character is a square bracket the user has a rank
            // So we get the username from the space after the closing square bracket (end of their rank)
            if (username.charAt(0) == '[') {
                username = username.substring(username.indexOf(" ") + 1);
            }
            // If we still get any square brackets it means the user was talking in guild chat with a guild rank
            // So we get the username up to the space before the guild rank
            if (username.contains("[") || username.contains("]")) {
                username = username.substring(0, username.indexOf(" "));
            }
            return username;
        } catch (Exception ignored) {
            return "";
        }
    }


}
