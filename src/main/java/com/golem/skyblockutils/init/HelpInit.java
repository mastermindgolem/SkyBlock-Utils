package com.golem.skyblockutils.init;

import com.golem.skyblockutils.command.HelpCache;
import com.golem.skyblockutils.command.HelpInvocation;
import com.golem.skyblockutils.command.commands.*;

public class HelpInit {
	public static void registerHelp() {
		HelpCache.addHelpProvider(Alias.class.getName(), new Alias());
		HelpCache.addHelpProvider(AttributeCommand.class.getName(), new AttributeCommand());
		HelpCache.addHelpProvider(EquipmentCommand.class.getName(), new EquipmentCommand());
		HelpCache.addHelpProvider(StatCommand.class.getName(), new StatCommand());
		HelpCache.addHelpProvider(UpgradeCommand.class.getName(), new UpgradeCommand());
		HelpInvocation.addHelp();
		HelpCache.addStrings();
	}
}
