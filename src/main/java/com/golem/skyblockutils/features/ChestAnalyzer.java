package com.golem.skyblockutils.features;

import com.golem.skyblockutils.Main;
import com.golem.skyblockutils.utils.ChatUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.event.ClickEvent;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.inventory.Slot;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.HashMap;
import java.util.List;

public class ChestAnalyzer {

    public static boolean analyzeChests = false;
    private static TileEntityChest lastOpenedChest = null;
    static HashMap<BlockPos, List<Slot>> chestData = new HashMap<>();

    public static void enableAnalyzer() {
        if (analyzeChests) {
            ChatUtils.addChatMessage(EnumChatFormatting.RED + "Chest analyzer is already enabled.", true);
            return;
        }
        lastOpenedChest = null;
        chestData.clear();
        analyzeChests = true;
        ChatUtils.addChatMessage(EnumChatFormatting.GREEN + "Chest analyzer enabled.", true);
    }

    public static void disableAnalyzer() {
        if (!analyzeChests) {
            ChatUtils.addChatMessage(EnumChatFormatting.RED + "Chest analyzer is already disabled.", true);
            return;
        }
        analyzeChests = false;
        ChatUtils.addChatMessage(EnumChatFormatting.GREEN + "Chest analyzer disabled.", true);
        ChatUtils.addChatMessage(EnumChatFormatting.YELLOW + "[Click this to view the chest data]", new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/analyzechests"));

    }

    @SubscribeEvent
    public void onOpenChest(PlayerInteractEvent event) {
        if (event.action == PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK && event.world.getTileEntity(event.pos) instanceof TileEntityChest) {
            TileEntityChest chest = (TileEntityChest) event.world.getTileEntity(event.pos);
            if (Main.mc.thePlayer.isSneaking() && (chestData.containsKey(event.pos) || chestData.containsKey(getAdjacentChest(chest)))) {
                chestData.remove(event.pos);
                chestData.remove(getAdjacentChest(chest));
            } else if (!chestData.containsKey(event.pos) && !chestData.containsKey(getAdjacentChest(chest))) {
                lastOpenedChest = chest;
            }
        }
    }

    @SubscribeEvent
    public void guiDraw(GuiScreenEvent.BackgroundDrawnEvent event) {
        if (lastOpenedChest == null) return;
        if (!analyzeChests) return;
        if (!(event.gui instanceof GuiChest)) return;
        if (chestData.containsKey(lastOpenedChest.getPos())) return;
        if (getAdjacentChest(lastOpenedChest) != null && chestData.containsKey(getAdjacentChest(lastOpenedChest))) return;

        GuiChest gui = (GuiChest) event.gui;
        Container container = gui.inventorySlots;
        if (!(container instanceof ContainerChest)) return;

        List<Slot> chestInventory = ((GuiChest) Minecraft.getMinecraft().currentScreen).inventorySlots.inventorySlots;
        if (getAdjacentChest(lastOpenedChest) != null) {
            chestInventory = chestInventory.subList(0, 54);
        } else {
            chestInventory = chestInventory.subList(0, 27);
        }
        chestData.put(lastOpenedChest.getPos(), chestInventory);
    }

    private BlockPos getAdjacentChest(TileEntityChest chest) {
        if (chest.adjacentChestXNeg != null) {
            return chest.getPos().offset(EnumFacing.WEST);
        } else if (chest.adjacentChestXPos != null) {
            return chest.getPos().offset(EnumFacing.EAST);
        } else if (chest.adjacentChestZNeg != null) {
            return chest.getPos().offset(EnumFacing.NORTH);
        } else if (chest.adjacentChestZPos != null) {
            return chest.getPos().offset(EnumFacing.SOUTH);
        }
        return null;
    }
}
