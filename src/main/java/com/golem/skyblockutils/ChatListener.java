package com.golem.skyblockutils;

import com.golem.skyblockutils.command.commands.StatCommand;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChatListener {

	Pattern pattern = Pattern.compile("Party Finder > (\\w+) joined the group!");


	@SubscribeEvent(receiveCanceled = true, priority = EventPriority.HIGH)
	public void onChat(ClientChatReceivedEvent event) {
		final String unformatted = EnumChatFormatting.getTextWithoutFormattingCodes(event.message.getUnformattedText());
		if (Main.configFile.showKuudraPlayerInfo) {
			Matcher matcher = pattern.matcher(unformatted);
			if (matcher.find()) {
				if (Main.configFile.showOwnPlayerInfo && Objects.equals(matcher.group(1), Main.mc.thePlayer.getDisplayNameString())) return;
				StatCommand.showPlayerStats(matcher.group(1));
			}

		}
	}
}
