package com.golem.skyblockutils.features;

import com.golem.skyblockutils.Main;
import com.golem.skyblockutils.features.KuudraFight.Kuudra;
import com.golem.skyblockutils.models.gui.Alignment;
import com.golem.skyblockutils.models.gui.OverlayUtils;
import com.golem.skyblockutils.models.gui.TextStyle;
import com.golem.skyblockutils.utils.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.boss.BossStatus;
import net.minecraft.entity.boss.IBossDisplayData;
import net.minecraft.entity.monster.EntityMagmaCube;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.awt.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.golem.skyblockutils.Main.config;
import static com.golem.skyblockutils.init.KeybindsInit.mc;

public class KuudraHealth {

    //Adapted from VolcAddons

    private static final DecimalFormat formatter = new DecimalFormat("###,###");
    private static final DecimalFormat formatter2 = new DecimalFormat("##.##");
    private static String BossHPmessage = "";


    @SubscribeEvent
    public void renderWorld(RenderWorldLastEvent event) {
        try {
            ArrayList<Entity> entities = Kuudra.getAllEntitiesInRange();
            List<Entity> kuudra = entities.stream().filter(e -> e instanceof EntityMagmaCube && e.width > 14 && ((EntityMagmaCube) e).getHealth() <= 100000).collect(Collectors.toList());
            for (Entity e : kuudra) {
                Kuudra.boss = (EntityMagmaCube) e;
                if (config.getConfig().kuudraCategory.kuudraConfig.displayKuudraHP) {
                    String string = EnumChatFormatting.RED + formatter.format(Kuudra.boss.getHealth()) + "/100,000";
                    if (Kuudra.tier == 5) string = EnumChatFormatting.RED + formatter.format((Kuudra.boss.getHealth() - 25000)/3*4) + "/100,000";
                    if (Kuudra.currentPhase >= 4) string = EnumChatFormatting.YELLOW + Main.formatNumber(Kuudra.boss.getHealth() * 9600).toUpperCase() + "/240M";
                    RenderUtils.renderNameTag(string, e.posX, e.posY + e.height / 2, e.posZ, 4.0f);
                }
                if (config.getConfig().kuudraCategory.kuudraConfig.displayKuudraHPBossBar) {
                    String string = EnumChatFormatting.RED + formatter2.format(Kuudra.boss.getHealth()/1000) + "%";
                    if (Kuudra.tier == 5) {
                        if (Kuudra.currentPhase < 4) {
                            string = EnumChatFormatting.RED + formatter2.format((Kuudra.boss.getHealth() - 25000) / 750) + "%";
                            BossStatus.setBossStatus(new IBossDisplayData() {
                                @Override
                                public float getMaxHealth() {
                                    return 75000;
                                }

                                @Override
                                public float getHealth() {
                                    return (Kuudra.boss.getHealth()-25000);
                                }

                                @Override
                                public IChatComponent getDisplayName() {
                                    ChatComponentText bossText = new ChatComponentText(EnumChatFormatting.BOLD + "Kuudra");
                                    bossText.getChatStyle().setColor(EnumChatFormatting.RED);
                                    return bossText;
                                }
                            }, true);
                        }

                        if (Kuudra.currentPhase >= 4) {
                            string = EnumChatFormatting.YELLOW + formatter2.format(Kuudra.boss.getHealth() / 250) + "%";
                            BossStatus.setBossStatus(new IBossDisplayData() {
                                @Override
                                public float getMaxHealth() {
                                    return 240_000_000;
                                }

                                @Override
                                public float getHealth() {
                                    return Kuudra.boss.getHealth() * 9600;
                                }

                                @Override
                                public IChatComponent getDisplayName() {
                                    ChatComponentText bossText = new ChatComponentText(EnumChatFormatting.BOLD + "Kuudra");
                                    bossText.getChatStyle().setColor(EnumChatFormatting.RED);
                                    return bossText;
                                }
                            }, true);
                        }
                    }

                    BossHPmessage = string;
                    }
                if (config.getConfig().kuudraCategory.kuudraConfig.showKuudraOutline) {
                    RenderUtils.drawEntityBox(e, Color.GREEN, 5, event.partialTicks);
                }
            }
        } catch (Exception ignored) {}
    }


    @SubscribeEvent
    public void onRenderOverlay(RenderGameOverlayEvent event) {
        if (!config.getConfig().kuudraCategory.kuudraConfig.displayKuudraHPBossBar) return;
        if (event.type != RenderGameOverlayEvent.ElementType.TEXT || Kuudra.currentPhase <= 0) return;

        ScaledResolution scaledResolution = new ScaledResolution(Minecraft.getMinecraft());
        int screenWidth = scaledResolution.getScaledWidth();

        // Calculate the game viewport size and position within the window
        int gameWidth = mc.displayWidth * mc.gameSettings.guiScale;
        int offsetX = (screenWidth - gameWidth) / 2;

        // Calculate the center of the game viewport
        int x = offsetX + gameWidth / 2;

        int y = 10;

        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, 400);
        OverlayUtils.drawString(0, 0, BossHPmessage, TextStyle.Default, Alignment.Center);
        GlStateManager.popMatrix();
    }

}
