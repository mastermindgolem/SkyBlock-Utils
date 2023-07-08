package com.golem.skyblockutils.features;

import com.golem.skyblockutils.Main;
import com.golem.skyblockutils.models.AttributePrice;
import com.google.gson.JsonObject;
import gg.essential.universal.UGraphics;
import gg.essential.universal.UMatrixStack;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.inventory.Slot;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentText;

import java.util.*;

import static com.golem.skyblockutils.models.AttributePrice.AttributePrices;
import static com.golem.skyblockutils.models.AttributePrice.all_kuudra_categories;

public class AttributeOverlay {


	public static void drawSlot(Slot slot) {
		if (slot == null || !slot.getHasStack() || !Main.configFile.attribute_overlay) return;
		try {
			JsonObject valueData = AttributePrice.AttributeValue(slot.getStack());
			assert valueData != null;

			UGraphics.disableLighting();
			UGraphics.disableDepth();
			UGraphics.disableBlend();
			UMatrixStack matrixStack = new UMatrixStack();
			matrixStack.push();
			matrixStack.translate(slot.xDisplayPosition, slot.yDisplayPosition, 1f);
			matrixStack.scale(0.8, 0.8, 1.0);

			matrixStack.runWithGlobalState(() -> {
				Main.mc.fontRendererObj.drawString(valueData.get("top_display").getAsString(), 0, 0, 0x00FFFF);
			});

			matrixStack.pop();
			UGraphics.enableLighting();
			UGraphics.enableDepth();
			UGraphics.enableBlend();
			if (valueData.get("bottom_display").getAsInt() > 0) {
				GlStateManager.disableLighting();
				GlStateManager.disableDepth();
				GlStateManager.disableBlend();
				Main.mc.fontRendererObj.drawStringWithShadow(valueData.get("bottom_display").getAsString(),
						(float) (slot.xDisplayPosition + 17 - Main.mc.fontRendererObj.getStringWidth(valueData.get("bottom_display").getAsString())),
						slot.yDisplayPosition + 9,
						0xFFFFFFFF
				);
				GlStateManager.enableLighting();
				GlStateManager.enableDepth();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
