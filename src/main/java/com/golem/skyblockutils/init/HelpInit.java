package com.golem.skyblockutils.init;

import com.golem.skyblockutils.command.HelpCache;
import com.golem.skyblockutils.command.HelpInvocation;
import com.golem.skyblockutils.command.commands.*;
import logger.Logger;
import net.minecraftforge.client.ClientCommandHandler;

public class HelpInit {
	public static void registerHelp() {
		HelpCache.addHelpProvider(Alias.class.getName(), new Alias());
		HelpCache.addHelpProvider(AttributeCommand.class.getName(), new AttributeCommand());
		HelpCache.addHelpProvider(EquipmentCommand.class.getName(), new EquipmentCommand());
		HelpCache.addHelpProvider(StatCommand.class.getName(), new StatCommand());
		HelpCache.addHelpProvider(UpgradeCommand.class.getName(), new UpgradeCommand());
		if (ClientCommandHandler.instance.getCommands().containsKey("shelp")) {
			Logger.info("Added Help");
		}
		if (ClientCommandHandler.instance.getCommands().containsKey("sbu")) {
			Logger.info("Added SbuCommand");
		}
		HelpInvocation.addHelp();
		HelpCache.addStrings();
	}
}
