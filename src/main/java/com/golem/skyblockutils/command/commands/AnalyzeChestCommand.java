package com.golem.skyblockutils.command.commands;

import com.golem.skyblockutils.Main;
import com.golem.skyblockutils.features.ChestAnalyzer;
import com.golem.skyblockutils.features.ChestDataGui;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;

import java.util.ArrayList;
import java.util.List;

import static com.golem.skyblockutils.Main.OpenChestData;
import static com.golem.skyblockutils.Main.ReleaseGui;

public class AnalyzeChestCommand extends CommandBase implements Help {
    private final List<String> helpStrings;

    public AnalyzeChestCommand() {
        helpStrings = new ArrayList<>();
    }

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
        al.add("analysechests");
        al.add("analysechest");
        al.add("ac-dev");
        return al;
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/analyzechests [on/off] to turn on/off Kuudra chest analyzer.";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        if (args.length == 0) {
            if (ChestAnalyzer.analyzeChests) {
                ChestAnalyzer.disableAnalyzer();
            } else {
                ChestAnalyzer.enableAnalyzer();
            }
        }

        if (args.length != 1) return;
        if (args[0].equalsIgnoreCase("on")) {
            ChestAnalyzer.enableAnalyzer();
        } else if (args[0].equalsIgnoreCase("off")) {
            ChestAnalyzer.disableAnalyzer();
        } else if (args[0].equalsIgnoreCase("gui") && !ChestAnalyzer.analyzeChests) {
            Logger.debug("Analyzing Chest GUI...");
//            mc.displayGuiScreen(new ChestDataGui());
            ReleaseGui.addFirst(3);
            OpenChestData = true;
        }
    }

}
