package com.golem.skyblockutils;

import com.golem.skyblockutils.command.commands.StatCommand;
import net.minecraft.event.ClickEvent;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.golem.skyblockutils.Main.mc;

public class ChatListener {

	Pattern pattern = Pattern.compile("Party Finder > (\\w+) joined the group!");


	@SubscribeEvent(receiveCanceled = true, priority = EventPriority.HIGH)
	public void onChat(ClientChatReceivedEvent event) {
		final String unformatted = EnumChatFormatting.getTextWithoutFormattingCodes(event.message.getUnformattedText());
		if (Main.configFile.showKuudraPlayerInfo) {
			Matcher matcher = pattern.matcher(unformatted);
			if (matcher.find()) {
				if (Main.configFile.showOwnPlayerInfo && Objects.equals(matcher.group(1), Main.mc.thePlayer.getDisplayNameString())) return;
				StatCommand.showPlayerStats(matcher.group(1), true);
				}
		}
		if (Main.configFile.hideSackMessage) {
			if (unformatted.startsWith("[Sacks]")) event.setCanceled(true);
		}
	}
}
