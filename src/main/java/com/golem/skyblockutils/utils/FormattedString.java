package com.golem.skyblockutils.utils;

import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;

import java.util.HashMap;
import java.util.Map;

public class FormattedString
{
	private String s;
	private ChatComponentText v;
	private HashMap<String, EnumChatFormatting> cc;

	public FormattedString(final String text) {
		this.cc = new HashMap<String, EnumChatFormatting>();
		this.initColors();
		this.s = text;
		for (final Map.Entry<String, EnumChatFormatting> entry : this.cc.entrySet()) {
			this.s = this.s.replace("&" + entry.getKey(), entry.getValue() + "");
		}
		this.v = new ChatComponentText(this.s);
	}

	public String getString() {
		return this.s;
	}

	public ChatComponentText getValue() {
		return this.v;
	}

	private void initColors() {
		this.cc.put("a", EnumChatFormatting.GREEN);
		this.cc.put("b", EnumChatFormatting.AQUA);
		this.cc.put("c", EnumChatFormatting.RED);
		this.cc.put("d", EnumChatFormatting.LIGHT_PURPLE);
		this.cc.put("e", EnumChatFormatting.YELLOW);
		this.cc.put("f", EnumChatFormatting.WHITE);
		this.cc.put("0", EnumChatFormatting.BLACK);
		this.cc.put("1", EnumChatFormatting.DARK_BLUE);
		this.cc.put("2", EnumChatFormatting.DARK_GREEN);
		this.cc.put("3", EnumChatFormatting.DARK_AQUA);
		this.cc.put("4", EnumChatFormatting.DARK_RED);
		this.cc.put("5", EnumChatFormatting.DARK_PURPLE);
		this.cc.put("6", EnumChatFormatting.GOLD);
		this.cc.put("7", EnumChatFormatting.GRAY);
		this.cc.put("8", EnumChatFormatting.DARK_GRAY);
		this.cc.put("9", EnumChatFormatting.BLUE);
		this.cc.put("k", EnumChatFormatting.OBFUSCATED);
		this.cc.put("m", EnumChatFormatting.STRIKETHROUGH);
		this.cc.put("o", EnumChatFormatting.ITALIC);
		this.cc.put("l", EnumChatFormatting.BOLD);
		this.cc.put("n", EnumChatFormatting.UNDERLINE);
		this.cc.put("r", EnumChatFormatting.RESET);
	}
}
