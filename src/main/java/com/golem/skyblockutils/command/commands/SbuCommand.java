package com.golem.skyblockutils.command.commands;

import com.golem.skyblockutils.Main;
import com.golem.skyblockutils.command.Help;
import com.golem.skyblockutils.command.HelpInvocation;
import logger.Logger;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.event.HoverEvent;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.golem.skyblockutils.Main.configFile;
import static com.golem.skyblockutils.Main.mc;

public class SbuCommand extends CommandBase {
	@Override
	public int getRequiredPermissionLevel() {
		return 0;
	}

	@Override
	public String getCommandName() {
		return "sbu";
	}

	@Override
	public List<String> getCommandAliases() {
		List<String> al = new ArrayList<>();
		al.add("skyblockutils");
		return al;
	}

	@Override
	public String getCommandUsage(ICommandSender sender) {
		return "Opens gui, shows help message";
	}

	@Override
	public void processCommand(ICommandSender sender, String[] args) throws CommandException {
		if(args.length == 0) {
			Main.display = configFile.gui();
			return;
		}
		if (args.length == 1) {
			if (Objects.equals(args[0], "help")) {
				Logger.debug("Sending help message!");
				mc.thePlayer.addChatMessage(new ChatComponentText(Help.getChat()).setChatStyle(new ChatStyle().setChatHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ChatComponentText(Help.getHoverChat())))));
				HelpInvocation.sendAll();
				return;
			}
			if (Objects.equals(args[0], "update") || Objects.equals(args[0], "refresh")) {
				return;
			}
		}
	}
}
