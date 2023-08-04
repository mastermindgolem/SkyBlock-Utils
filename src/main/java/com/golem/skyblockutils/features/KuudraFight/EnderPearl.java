package com.golem.skyblockutils.features.KuudraFight;

import com.golem.skyblockutils.Main;
import com.golem.skyblockutils.utils.RenderUtils;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.Vec3;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;

public class EnderPearl {

    // Method to check if a double value is close to zero
    private static boolean isCloseToZero(double value) {
        return Math.abs(value) < 1e-10;
    }

    public static Double[] solveCubic(double a, double b, double c, double d) {
        Double[] roots = solveCubicHelper(a, b, c, d);

        ArrayList<Double> positiveRoots = new ArrayList<>();

        for (Double root : roots) {
            if (root > 0) {
                positiveRoots.add(root);
            }
        }

        if (positiveRoots.isEmpty()) {
            return null;
        }

        return positiveRoots.toArray(new Double[0]);
    }

    // Method to solve a cubic equation and return real roots
    public static Double[] solveCubicHelper(double a, double b, double c, double d) {
        double discriminant = 18 * a * b * c * d
                - 4 * b * b * b * d
                + b * b * c * c
                - 4 * a * c * c * c
                - 27 * a * a * d * d;

        // Check for special cases: discriminant = 0 or a = 0
        if (isCloseToZero(discriminant) || isCloseToZero(a)) {
            double p = c / a - b * b / (3 * a * a);
            double q = 2 * b * b * b / (27 * a * a * a) - b * c / (3 * a * a) + d / a;

            double discriminant2 = q * q / 4 + p * p * p / 27;

            if (isCloseToZero(discriminant2)) {
                double root = -q / 2;
                return new Double[]{root};
            } else if (discriminant2 > 0) {
                double sqrtDiscriminant2 = Math.sqrt(discriminant2);
                double root1 = Math.cbrt(-q / 2 + sqrtDiscriminant2) + Math.cbrt(-q / 2 - sqrtDiscriminant2) - b / (3 * a);
                return new Double[]{root1};
            } else {
                double r = Math.sqrt(-p * p * p / 27);
                double theta = Math.acos(-q / (2 * r));
                double root1 = 2 * Math.cbrt(r) * Math.cos(theta / 3) - b / (3 * a);
                double root2 = 2 * Math.cbrt(r) * Math.cos((theta + 2 * Math.PI) / 3) - b / (3 * a);
                double root3 = 2 * Math.cbrt(r) * Math.cos((theta + 4 * Math.PI) / 3) - b / (3 * a);
                return new Double[]{root1, root2, root3};
            }
        } else {
            double f = ((3 * c / a) - (b * b / (a * a))) / 3;
            double g = ((2 * b * b * b / (a * a * a)) - (9 * b * c / (a * a)) + (27 * d / a)) / 27;
            double h = (g * g / 4) + (f * f * f / 27);

            if (h > 0) {
                double sqrtH = Math.sqrt(h);
                double r = -g / 2 + sqrtH;
                double s = Math.cbrt(r);
                double t = -g / 2 - sqrtH;
                double u = Math.cbrt(t);

                double root1 = (s + u) - (b / (3 * a));
                return new Double[]{root1};
            } else if (h == 0) {
                double root1 = 2 * Math.cbrt(-g / 2) - b / (3 * a);
                double root2 = -Math.cbrt(-g / 2) - b / (3 * a);
                return new Double[]{root1, root2};
            } else {
                double sqrtH = Math.sqrt(-h);
                double r = Math.hypot(-g / 2, sqrtH);
                double theta = Math.atan2(sqrtH, -g / 2);
                double s = Math.cbrt(r);
                double t = -f / (3 * s);
                double cosTheta3 = Math.cos(theta / 3);
                double sinTheta3 = Math.sin(theta / 3);

                double root1 = 2 * s * cosTheta3 - b / (3 * a);
                double root2 = -s * (cosTheta3 + Math.sqrt(3) * sinTheta3) - b / (3 * a);
                double root3 = -s * (cosTheta3 - Math.sqrt(3) * sinTheta3) - b / (3 * a);

                return new Double[]{root1, root2, root3};
            }
        }
    }

    public static Double[] solveQuadratic(double a, double b, double c) {
        double discriminant = b * b - 4 * a * c;

        if (isCloseToZero(discriminant)) {
            double root = -b / (2 * a);
            if (root > 0) {
                return new Double[]{root};
            } else {
                return null;
            }
        } else if (discriminant > 0) {
            double sqrtDiscriminant = Math.sqrt(discriminant);
            double root1 = (-b + sqrtDiscriminant) / (2 * a);
            double root2 = (-b - sqrtDiscriminant) / (2 * a);

            ArrayList<Double> positiveRoots = new ArrayList<>();
            if (root1 > 0) {
                positiveRoots.add(root1);
            }
            if (root2 > 0) {
                positiveRoots.add(root2);
            }

            if (positiveRoots.isEmpty()) {
                return null;
            }

            return positiveRoots.toArray(new Double[0]);
        } else {
            return null;
        }
    }

    @SubscribeEvent
    public void RenderEvent(RenderWorldLastEvent event) {
        if (Main.mc.theWorld == null || Main.mc.thePlayer == null || Kuudra.currentPhase != 1 || !Main.configFile.enderPearl) return;
        ItemStack heldItem = Main.mc.thePlayer.getHeldItem();
        if (heldItem == null) return;
        if (!heldItem.getDisplayName().contains("Ender Pearl")) return;

        double viewerX = Main.mc.thePlayer.posX;
        double viewerY = Main.mc.thePlayer.posY + Main.mc.thePlayer.getEyeHeight();
        double viewerZ = Main.mc.thePlayer.posZ;

        for (Map.Entry<Vec3, Integer> entry : Kuudra.supplyWaypoints.entrySet()) {
            if (entry.getValue() == 101) {
                Vec3 supply = entry.getKey();
                double distance = Math.sqrt((viewerX - supply.xCoord) * (viewerX - supply.xCoord) + (viewerZ - supply.zCoord) * (viewerZ - supply.zCoord));
                double height = supply.yCoord - viewerY;
                int closestAngle = 0;
                double closestDistance = Double.MAX_VALUE;
                double x = supply.xCoord;
                double y = supply.yCoord;
                double z = supply.zCoord;
                for (int i = 0; i < 90; i++) {
                    double u = Math.sin(Math.toRadians(i)) * 1.338;
                    Double[] roots = solveCubic(0.00015, 0.015 - 0.005 * u, -u, height);
                    double t = (roots == null ? 0 : roots[0]);
                    u = Math.cos(Math.toRadians(i)) * 1.338;
                    double d = Math.abs(distance - t * u);
                    if (d < closestDistance) {
                        closestAngle = i;
                        closestDistance = d;
                        y = supply.yCoord + distance * Math.tan(Math.toRadians(i));
                    }
                }
                RenderUtils.drawBlockBox(new BlockPos(x - 0.5, y - 0.5, z - 0.5), Main.configFile.enderPearlColor, 5, event.partialTicks);
            }
        }

    }
}
