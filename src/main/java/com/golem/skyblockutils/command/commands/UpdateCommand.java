package com.golem.skyblockutils.command.commands;

import com.golem.skyblockutils.features.AutoUpdater;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;

public class UpdateCommand extends CommandBase {
    @Override
    public String getCommandName() {
        return "updatesbu";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/updatesbu";
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        return true;
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        if (sender instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) sender;
            player.addChatMessage(new ChatComponentText(EnumChatFormatting.RED + "Updating Skyblock Utils..."));
            AutoUpdater.downloadAndExtractUpdate(player);
        }
    }
}
