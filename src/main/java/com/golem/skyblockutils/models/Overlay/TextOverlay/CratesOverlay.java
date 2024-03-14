package com.golem.skyblockutils.models.Overlay.TextOverlay;

import com.golem.skyblockutils.Main;
import com.golem.skyblockutils.features.KuudraFight.Kuudra;
import com.golem.skyblockutils.models.gui.*;
import com.golem.skyblockutils.utils.RenderUtils;
import com.golem.skyblockutils.utils.TabUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityGiantZombie;
import net.minecraft.entity.monster.EntityMagmaCube;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.golem.skyblockutils.Main.*;

public class CratesOverlay {
    public static GuiElement element = new GuiElement("Crates Overlay", 50, 20);

    public static HashMap<Integer, BlockPos> phase1 = new HashMap<>();
    public static HashMap<String, Boolean> phase0 = new HashMap<>();
    public static HashMap<String, Long> phase2 = new HashMap<>();
    public static List<Float> phase4 = new ArrayList<>();
    public static HashMap<String, Integer> playerInfo = new HashMap<>();
    private static List<String> heldCrates = new ArrayList<>();
    private static boolean inPeak = false;


    @SubscribeEvent
    public void onChat(ClientChatReceivedEvent event) {
        String message = event.message.getFormattedText().replaceAll("§.", "");
        if (message.contains(" recovered one of Elle's supplies! (")) {
            for (String name : Kuudra.partyMembers) if (message.contains(name)) {
                playerInfo.put(name, playerInfo.getOrDefault(name, 0) + 1);
                Kuudra.overview.add(EnumChatFormatting.BLUE + name + EnumChatFormatting.AQUA + " picked up supply at: " + SplitsOverlay.format(time.getCurrentMS()/60000F - Kuudra.splits[1]/60000F));
            }

        }
        if (message.equals("Your Fresh Tools Perk bonus doubles your building speed for the next 5 seconds!")) {
            if (configFile.freshAlert) {
                AlertOverlay.newAlert(EnumChatFormatting.DARK_GREEN + "FRESH TOOLS", 20);
            }
            if (configFile.freshNotify) mc.thePlayer.sendChatMessage("/pc FRESH! " + configFile.freshMessage);
        }
        if (message.startsWith("Party") && message.contains(": FRESH!")) {
            for (String player : Kuudra.partyMembers) if (message.contains(player)) {
                phase2.put(player, time.getCurrentMS());
                Kuudra.overview.add(EnumChatFormatting.BLUE + player + EnumChatFormatting.AQUA + " procced fresh tools at: " + SplitsOverlay.format(time.getCurrentMS()/60000F - Kuudra.splits[1]/60000F));
                return;
            }
        }
        if (message.endsWith("is now ready!")) phase0.put(message.split(" ")[0], true);
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.START || mc.theWorld == null || mc.thePlayer == null || Kuudra.currentPhase >= 5 || Kuudra.currentPhase < 0) return;

        List<String> tabData = TabUtils.getTabList();
        if (tabData.isEmpty()) return;
        Pattern pattern = Pattern.compile("Players \\((\\d+)\\)");
        Matcher matcher = pattern.matcher(tabData.get(0).replaceAll("§.", ""));

        if (!matcher.find()) return;

        for (String string : tabData.subList(1, 1 + Integer.parseInt(matcher.group(1)))) {
            try {
                String player = string.split(" ")[1].replaceAll("§.", "");
                if (!Kuudra.partyMembers.contains(player) && player.length() > 2) Kuudra.partyMembers.add(player);
            } catch (Exception ignored) {}
        }

        //if (time.getCurrentMS() - lastFresh > 5000 && Objects.equals(AlertOverlay.text, EnumChatFormatting.DARK_GREEN + "FRESH TOOLS")) AlertOverlay.text = "";


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
                phase1.put(entity.getEntityId(), entity.getPosition());
                if (configFile.crateWaypoints) RenderUtils.renderBeaconBeam(entity.posX - viewerX -1.5, entity.posY - viewerY, entity.posZ - viewerZ +2, 0xFF0000, 1.0F, event.partialTicks);
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

        Iterator<Map.Entry<Integer, BlockPos>> iterator = phase1.entrySet().iterator();

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
        if (event.type != RenderGameOverlayEvent.ElementType.TEXT || !configFile.runInfo) return;

        TextStyle textStyle = TextStyle.fromInt(1);

        if (mc.currentScreen instanceof MoveGui) {
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
            return;
        }

        if (!configFile.testGui) return;


        if (Kuudra.currentPhase == 0) {
            GlStateManager.pushMatrix();
            GlStateManager.translate(element.position.getX(), element.position.getY(), 500.0);
            GlStateManager.scale(element.position.getScale(), element.position.getScale(), 1.0);

            int counter = 1;
            OverlayUtils.drawString(0, 0, EnumChatFormatting.YELLOW + "Ready Up:", textStyle, Alignment.Left);
            for (String player : Kuudra.partyMembers) {
                OverlayUtils.drawString(0, 10*counter, player + ": " + (phase0.getOrDefault(player, false) ? EnumChatFormatting.GREEN + "READY" : EnumChatFormatting.RED + "NOT READY"), textStyle, Alignment.Left);
                counter++;
            }

            GlStateManager.popMatrix();
        } else if (Kuudra.currentPhase == 1) {
            GlStateManager.pushMatrix();
            GlStateManager.translate(element.position.getX(), element.position.getY(), 500.0);
            GlStateManager.scale(element.position.getScale(), element.position.getScale(), 1.0);


            OverlayUtils.drawString(0, 0, EnumChatFormatting.YELLOW + "Supplies:", textStyle, Alignment.Left);

            int counter = 1;
            for (Map.Entry<Integer, BlockPos> entry : phase1.entrySet()) {
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

            GlStateManager.popMatrix();
        } else if (Kuudra.currentPhase == 2) {

            GlStateManager.pushMatrix();
            GlStateManager.translate(element.position.getX(), element.position.getY(), 500.0);
            GlStateManager.scale(element.position.getScale(), element.position.getScale(), 1.0);

            int counter = 1;
            OverlayUtils.drawString(0, 0, EnumChatFormatting.YELLOW + "Build:", textStyle, Alignment.Left);
            for (String player : Kuudra.partyMembers) {
                if (time.getCurrentMS() - phase2.getOrDefault(player, 0L) < 5000) {
                    OverlayUtils.drawString(0, 10*counter, player + ": " + EnumChatFormatting.GREEN + SplitsOverlay.format((5000 - time.getCurrentMS() + phase2.getOrDefault(player, 0L))/60000F), textStyle, Alignment.Left);
                } else {
                    OverlayUtils.drawString(0, 10*counter, player + ": " + EnumChatFormatting.RED + "No Fresh", textStyle, Alignment.Left);
                }
                counter++;
            }
            GlStateManager.popMatrix();
        } else if (Kuudra.currentPhase == 4 || Kuudra.currentPhase == 5) {

            GlStateManager.pushMatrix();
            GlStateManager.translate(element.position.getX(), element.position.getY(), 500.0);
            GlStateManager.scale(element.position.getScale(), element.position.getScale(), 1.0);


            int counter = 1;
            OverlayUtils.drawString(0, 0, EnumChatFormatting.YELLOW + "Kuudra Kill:", textStyle, Alignment.Left);
            for (int i = 1; i < phase4.size(); i++) {
                float dmg = (phase4.get(i-1) - phase4.get(i)) * 12000;
                if (dmg < 100000 || dmg > 300_000_000) continue;
                OverlayUtils.drawString(0, 10 * counter, EnumChatFormatting.YELLOW + "Peak " + counter + ": " + EnumChatFormatting.RESET + Main.formatNumber(dmg), textStyle, Alignment.Left);
                String string = EnumChatFormatting.BLUE + "Peak " + counter + " damage: " + EnumChatFormatting.RESET + Main.formatNumber(dmg);
                if (!Kuudra.overview.contains(string)) Kuudra.overview.add(string);
                counter++;
            }

            EntityMagmaCube kuudra = Kuudra.boss;

            if (kuudra.posY > 45 && kuudra.getHealth() / kuudra.getMaxHealth() < 0.24 && kuudra.getHealth() / kuudra.getMaxHealth() > 0.01) return;

            boolean currentPeak = kuudra.posY < 25;


            if (currentPeak != inPeak) {
                if (configFile.showKuudraLocation) {
                    if (kuudra.posX < -128) AlertOverlay.newAlert(EnumChatFormatting.BOLD + "RIGHT!", 20);
                    if (kuudra.posX > -72) AlertOverlay.newAlert(EnumChatFormatting.BOLD + "LEFT!", 20);
                    if (kuudra.posZ < -132) AlertOverlay.newAlert(EnumChatFormatting.BOLD + "BACK!", 20);
                    if (kuudra.posZ > -84) AlertOverlay.newAlert(EnumChatFormatting.BOLD + "FRONT!", 20);
                }
                inPeak = currentPeak;
                if (inPeak) {
                    if (!phase4.isEmpty() && phase4.get(phase4.size() - 1) - kuudra.getHealth() < 0.008 * kuudra.getMaxHealth()) return;
                    phase4.add(kuudra.getHealth());
                }

            }
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
