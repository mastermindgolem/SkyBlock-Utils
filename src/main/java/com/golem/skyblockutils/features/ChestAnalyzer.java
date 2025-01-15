package com.golem.skyblockutils.features;

import com.golem.skyblockutils.Main;
import com.golem.skyblockutils.events.InventoryChangeEvent;
import com.golem.skyblockutils.utils.ChatUtils;
import com.golem.skyblockutils.utils.InventoryData;
import com.golem.skyblockutils.utils.LocationUtils;
import com.golem.skyblockutils.utils.RenderUtils;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.event.ClickEvent;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.inventory.Slot;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.golem.skyblockutils.Main.*;

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
        ChatUtils.addChatMessage(EnumChatFormatting.YELLOW + "[Click this to view the chest data]", new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/analyzechests gui"));

    }

    @SubscribeEvent
    public void onTick(RenderWorldLastEvent event) {
        if (mc.theWorld == null || mc.thePlayer == null) return;
        if (!analyzeChests) return;
        for (BlockPos pos : chestData.keySet()) {
            if (!(mc.theWorld.getTileEntity(pos) instanceof TileEntityChest)) continue;
            TileEntityChest chest = (TileEntityChest) mc.theWorld.getTileEntity(pos);
            RenderUtils.drawBlockBox(pos, Color.GREEN, 5, event.partialTicks);
            BlockPos adjacent = getAdjacentChest(chest);
            if (adjacent != null) RenderUtils.drawBlockBox(adjacent, Color.GREEN, 5, event.partialTicks);
        }
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
    public void onInventoryChange(InventoryChangeEvent event) {
        if (lastOpenedChest == null) return;
        if (!(event.event.gui instanceof GuiChest)) return;
        if (!analyzeChests && !config.getConfig().auctionCategory.sortingHelper) return;

        GuiChest gui = (GuiChest) event.event.gui;
        Container container = gui.inventorySlots;
        List<Slot> chestInventory = container.inventorySlots.stream().filter(slot -> slot.inventory != mc.thePlayer.inventory).collect(Collectors.toList());
        if (!(container instanceof ContainerChest)) return;
        if (analyzeChests) {
            if (!chestData.containsKey(lastOpenedChest.getPos())) {
                if (getAdjacentChest(lastOpenedChest) == null || !chestData.containsKey(getAdjacentChest(lastOpenedChest))) {
                    chestData.put(lastOpenedChest.getPos(), chestInventory);
                }
            }
        }

        if (!Objects.equals(InventoryData.currentChestName, "Large Chest") && !Objects.equals(InventoryData.currentChestName, "Small Chest")) return;

        if (config.getConfig().auctionCategory.sortingHelper && Objects.equals(LocationUtils.getLocation(), "dynamic")) {
            SellingHelper.addChest(lastOpenedChest, chestInventory);
        }
    }

    public static BlockPos getAdjacentChest(TileEntityChest chest) {
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
