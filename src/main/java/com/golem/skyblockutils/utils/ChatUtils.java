package com.golem.skyblockutils.utils;

import com.golem.skyblockutils.Main;
import com.google.gson.JsonObject;
import net.minecraft.client.Minecraft;
import net.minecraft.event.ClickEvent;
import net.minecraft.event.HoverEvent;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import static com.golem.skyblockutils.Main.mc;

@SideOnly(Side.CLIENT)
public class ChatUtils {
	public static final String PRIMARY_COLOR = "§7";
	public static final String SECONDARY_COLOR = "§1";
	private static final String PREFIX = PRIMARY_COLOR + "[" + SECONDARY_COLOR + Main.MODID + PRIMARY_COLOR + "] ";

	public static void send(final String s) {
		JsonObject object = new JsonObject();
		object.addProperty("text", s);
		Minecraft.getMinecraft().thePlayer.addChatMessage(IChatComponent.Serializer.jsonToComponent(object.toString()));
	}

	public static void success(String s) {
		info(s);
	}

	public static void info(String s) {
		send(PREFIX + s);
	}

	public static void addChatMessage(String chat, boolean sbuprefix) {
		mc.thePlayer.addChatMessage(new ChatComponentText((sbuprefix ? EnumChatFormatting.GOLD + "SBU > " : "") + chat));
	}

	public static void addChatMessage(String chat) {
		mc.thePlayer.addChatMessage(new ChatComponentText(chat));
	}

	public static void addChatMessage(String chat, String hoverChat) {
		mc.thePlayer.addChatMessage(new ChatComponentText(chat).setChatStyle(new ChatStyle().setChatHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ChatComponentText(hoverChat)))));
	}

	public static void addChatMessage(String chat, ClickEvent clickEvent) {
		mc.thePlayer.addChatMessage(new ChatComponentText(chat).setChatStyle(new ChatStyle().setChatClickEvent(clickEvent)));
	}

	public static void addChatMessage(String chat, ClickEvent clickEvent, String hoverEvent) {
		mc.thePlayer.addChatMessage(new ChatComponentText(chat).setChatStyle(new ChatStyle().setChatClickEvent(clickEvent).setChatHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ChatComponentText(hoverEvent)))));
	}
}
