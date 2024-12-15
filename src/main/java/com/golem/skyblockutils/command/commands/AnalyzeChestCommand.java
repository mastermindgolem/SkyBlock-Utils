package com.golem.skyblockutils.command.commands;

import com.golem.skyblockutils.Main;
import com.golem.skyblockutils.features.ChestAnalyzer;
import com.golem.skyblockutils.features.ChestDataGui;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;

import java.util.ArrayList;
import java.util.List;

public class AnalyzeChestCommand extends CommandBase {

    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }

    @Override
    public String getCommandName() {
        return "analyzechests";
    }

    @Override
    public List<String> getCommandAliases() {
        List<String> al = new ArrayList<>();
        al.add("analyzechest");
        return al;
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/analyzechests [on/off] to turn on/off Kuudra chest analyzer.";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        if (args.length == 0 && !ChestAnalyzer.analyzeChests) Main.mc.displayGuiScreen(new ChestDataGui());
        if (args.length != 1) return;
        if (args[0].equalsIgnoreCase("on")) {
            ChestAnalyzer.enableAnalyzer();
        } else if (args[0].equalsIgnoreCase("off")) {
            ChestAnalyzer.disableAnalyzer();
        }
    }

}
