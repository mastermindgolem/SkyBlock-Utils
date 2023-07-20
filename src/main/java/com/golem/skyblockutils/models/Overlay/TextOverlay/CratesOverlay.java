package com.golem.skyblockutils.models.Overlay.TextOverlay;

import com.golem.skyblockutils.Main;
import com.golem.skyblockutils.features.KuudraFight.Kuudra;
import com.golem.skyblockutils.models.gui.*;
import com.golem.skyblockutils.utils.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityGiantZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ClassInheritanceMultiMap;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.*;

import static com.golem.skyblockutils.Main.configFile;
import static com.golem.skyblockutils.Main.mc;

public class CratesOverlay {
    public static GuiElement element = new GuiElement("Crates Overlay", 50, 20);

    public static HashMap<Integer, BlockPos> crates = new HashMap<>();
    public static HashMap<String, Integer> playerInfo = new HashMap<>();
    private static List<String> heldCrates = new ArrayList<>();


    @SubscribeEvent
    public void onChat(ClientChatReceivedEvent event) {
        String message = event.message.getFormattedText().replaceAll("§.", "");
        if (message.contains(" recovered one of Elle's supplies! (")) {
            String name = message.split(" recovered")[0];
            playerInfo.put(name, playerInfo.getOrDefault(name, 0) + 1);
        }
    }

    @SubscribeEvent
    public void RenderEvent(RenderWorldLastEvent event) {

        if (Kuudra.currentPhase != 1) return;

        Entity viewer = Minecraft.getMinecraft().getRenderViewEntity();
        double viewerX = viewer.lastTickPosX + (viewer.posX - viewer.lastTickPosX) * event.partialTicks;
        double viewerY = viewer.lastTickPosY + (viewer.posY - viewer.lastTickPosY) * event.partialTicks;
        double viewerZ = viewer.lastTickPosZ + (viewer.posZ - viewer.lastTickPosZ) * event.partialTicks;

        //List<Entity> entities = Main.mc.theWorld.getEntitiesWithinAABB(Entity.class, Main.mc.thePlayer.getEntityBoundingBox().expand(100, 100, 100));
        List<Entity> entities = Kuudra.getAllEntitiesInRange();
        heldCrates = new ArrayList<>();

        for (Entity entity : entities) {
            if (entity instanceof EntityGiantZombie) {
                crates.put(entity.getEntityId(), entity.getPosition());
                RenderUtils.renderBeaconBeam(entity.posX - viewerX -1.5, entity.posY - viewerY, entity.posZ - viewerZ +2, 0xFF0000, 1.0F, event.partialTicks);
            }
            /*
            if (entity instanceof EntityPlayer) {
                if (((EntityPlayer) entity).getHeldItem() == null) continue;
                if (!Objects.equals(((EntityPlayer) entity).getHeldItem().getItem().getRegistryName(), Blocks.chest.getRegistryName())) continue;
                double dist = Double.MAX_VALUE;
                int closest = 0;
                for (Map.Entry<Integer, BlockPos> entry : crates.entrySet()) {
                    if (entity.getPosition().distanceSq(entry.getValue()) < dist) {
                        dist = entity.getPosition().distanceSq(entry.getValue());
                        closest = entry.getKey();
                    }
                }
                if (closest != 0) crates.remove(closest);
                heldCrates.add(entity.getName());
            }

             */
        }
        /*
        if (Main.mc.thePlayer.getHeldItem() != null && Objects.equals(mc.thePlayer.getHeldItem().getItem().getRegistryName(), Blocks.chest.getRegistryName())) {
            double dist = Double.MAX_VALUE;
            int closest = 0;
            for (Map.Entry<Integer, BlockPos> entry : crates.entrySet()) {
                if (Main.mc.thePlayer.getPosition().distanceSq(entry.getValue()) < dist) {
                    dist = Main.mc.thePlayer.getPosition().distanceSq(entry.getValue());
                    closest = entry.getKey();
                }
            }
            if (closest != 0) crates.remove(closest);
            heldCrates.add(Main.mc.thePlayer.getName());
        }

         */

        Iterator<Map.Entry<Integer, BlockPos>> iterator = crates.entrySet().iterator();

        while (iterator.hasNext()) {
            Map.Entry<Integer, BlockPos> entry = iterator.next();
            int entityID = entry.getKey();
            BlockPos blockPos = entry.getValue();

            Entity entity = mc.theWorld.getEntityByID(entityID);

            if (entity == null && blockPos.distanceSq(mc.thePlayer.getPosition()) < 32*32) {
                iterator.remove(); // Remove the entry from the map
            }
        }

    }

    @SubscribeEvent
    public void onRenderOverlay(RenderGameOverlayEvent event) {
        if (event.type != RenderGameOverlayEvent.ElementType.TEXT || !configFile.supplyInfo) return;

        TextStyle textStyle = TextStyle.fromInt(1);

        if (configFile.testGui && Kuudra.currentPhase == 1) {
            GlStateManager.pushMatrix();
            GlStateManager.translate(element.position.getX(), element.position.getY(), 500.0);
            GlStateManager.scale(element.position.getScale(), element.position.getScale(), 1.0);


            OverlayUtils.drawString(0, 0, EnumChatFormatting.YELLOW + "Supplies:", textStyle, Alignment.Left);

            int counter = 1;
            for (Map.Entry<Integer, BlockPos> entry : crates.entrySet()) {
                OverlayUtils.drawString(0, counter * 10, EnumChatFormatting.RED + findClosestLabel(entry.getValue()), textStyle, Alignment.Left);
                counter++;
            }
            for (String heldCrate : heldCrates) {
                OverlayUtils.drawString(0, counter * 10, EnumChatFormatting.YELLOW + heldCrate, textStyle, Alignment.Left);
                counter++;
            }
            for (Map.Entry<String, Integer> entry : playerInfo.entrySet()) {
                OverlayUtils.drawString(0, counter * 10, entry.getKey() + EnumChatFormatting.WHITE + ": " + EnumChatFormatting.YELLOW + entry.getValue(), textStyle, Alignment.Left);
                counter++;
            }

            element.setHeight(60);

            GlStateManager.popMatrix();
        } else if (mc.currentScreen instanceof MoveGui) {
            GlStateManager.pushMatrix();
            GlStateManager.translate(element.position.getX(), element.position.getY(), 500.0);
            GlStateManager.scale(element.position.getScale(), element.position.getScale(), 1.0);

            String string = EnumChatFormatting.YELLOW + "Supplies: ";
            OverlayUtils.drawString(0, 0, string, textStyle, Alignment.Left);
            OverlayUtils.drawString(0, 10, EnumChatFormatting.RED + "Triangle", textStyle, Alignment.Left);
            OverlayUtils.drawString(0, 20, EnumChatFormatting.RED + "Square", textStyle, Alignment.Left);
            OverlayUtils.drawString(0, 30, EnumChatFormatting.RED + "X", textStyle, Alignment.Left);
            OverlayUtils.drawString(0, 40, "Player1: 3", textStyle, Alignment.Left);
            OverlayUtils.drawString(0, 50, "Player2: 2", textStyle, Alignment.Left);
            OverlayUtils.drawString(0, 60, "Player3: 1", textStyle, Alignment.Left);

            element.setHeight(70);

            GlStateManager.popMatrix();
        }
    }

    public String findClosestLabel(BlockPos pos) {
        double minDistanceSq = Double.MAX_VALUE;
        String closestLabel = "";

        BlockPos[] targetPositions = {
                new BlockPos(-133, 79, -91),  // square
                new BlockPos(-110, 79, -74),  // slash
                new BlockPos(-83, 79, -76),   // circle
                new BlockPos(-73, 79, -93),   // equals
                new BlockPos(-76, 79, -126),  // triangle
                new BlockPos(-125, 79, -126)  // X
        };

        for (int i = 0; i < targetPositions.length; i++) {
            BlockPos targetPos = targetPositions[i];
            double distanceSq = pos.distanceSq(targetPos.getX(), targetPos.getY(), targetPos.getZ());

            if (distanceSq < minDistanceSq) {
                minDistanceSq = distanceSq;
                closestLabel = getLabelFromIndex(i);
            }
        }

        return closestLabel;
    }

    private String getLabelFromIndex(int index) {
        String[] labels = {"Square", "Slash", "Circle", "Equals", "Triangle", "X"};
        return labels[index];
    }

    private boolean isBlockPosInChunk(World world, BlockPos pos, Chunk chunk) {
        int chunkX = chunk.xPosition;
        int chunkZ = chunk.zPosition;
        int blockX = pos.getX();
        int blockZ = pos.getZ();

        // Check if the chunk coordinates match the block coordinates
        if (blockX >> 4 == chunkX && blockZ >> 4 == chunkZ) {
            int blockY = pos.getY();

            // Get the local block coordinates within the chunk
            int localX = blockX & 15;
            int localZ = blockZ & 15;

            // Check if the block state at the given BlockPos exists in the chunk
            return chunk.getBlockState(new BlockPos(localX, blockY, localZ)) != null;
        }

        return false;
    }
}
