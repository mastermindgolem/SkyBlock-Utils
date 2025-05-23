package com.golem.skyblockutils.init;

import com.golem.skyblockutils.command.Help;
import com.golem.skyblockutils.command.commands.*;
import net.minecraft.command.ICommand;
import net.minecraftforge.client.ClientCommandHandler;

public class CommandInit {
	public static void registerCommands() {
		ICommand[] commands = {
			new Help(),
			new Alias(),
			new AttributeCommand(),
			new EquipmentCommand(),
			new VanquishedCommand(),
			new MoltenCommand(),
			new StatCommand(),
			new UpgradeCommand(),
			new SbuCommand(),
			new UpdateCommand(),
			new AnalyzeChestCommand()
		};
		for (ICommand command : commands) {
			ClientCommandHandler.instance.registerCommand(command);
		}
	}
}
