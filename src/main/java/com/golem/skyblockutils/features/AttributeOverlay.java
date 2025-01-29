package com.golem.skyblockutils.features;

import com.golem.skyblockutils.Main;
import com.golem.skyblockutils.models.AttributeItem;
import com.golem.skyblockutils.models.AttributePrice;
import com.golem.skyblockutils.models.AttributeValueResult;
import com.golem.skyblockutils.utils.InventoryData;
import gg.essential.universal.UGraphics;
import gg.essential.universal.UMatrixStack;
import net.minecraft.inventory.Slot;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.golem.skyblockutils.Main.configFile;

public class AttributeOverlay {


	private static final double SCALE = (double) 4 / 7;

	public static void drawSlot(Slot slot) {
		if (slot == null || !slot.getHasStack() || Main.configFile.attribute_overlay == 0) return;

		if (configFile.attribute_overlay == 1) SBUAttributeOverlay(slot);
		if (configFile.attribute_overlay == 2) SkyHanniAttributeOverlay(slot);

	}

	private static void SkyHanniAttributeOverlay(Slot slot) {
		try {
			AttributeItem item = InventoryData.items.get(slot);
			if (item == null) return;
			AttributeValueResult valueData = InventoryData.values.get(slot);
			if (valueData == null) return;
			Color topColor = valueData.top_display.equals("GR") ? Color.YELLOW : Color.BLUE;

			UGraphics.disableLighting();
			UGraphics.disableDepth();
			UGraphics.disableBlend();
			UMatrixStack matrixStack = new UMatrixStack();
			matrixStack.push();
			matrixStack.translate(slot.xDisplayPosition, slot.yDisplayPosition, 1f);
			matrixStack.scale((double) 4 /7, (double) 4 /7, 1.0);
			List<String> attributes = new ArrayList<>(item.attributes.keySet());

			for (int i = 0; i < attributes.size(); i++) {
				String attribute = AttributePrice.VeryShortenedAttribute(attributes.get(i));
				int tier = item.attributes.get(attributes.get(i));
				int width1 = Main.mc.fontRendererObj.getStringWidth(attribute);
				int width2 = Main.mc.fontRendererObj.getStringWidth(tier + "");
				int finalI = i;
				matrixStack.runWithGlobalState(() -> {
					Main.mc.fontRendererObj.drawString(attribute, finalI == 0 ? 0 : (int) (16 / SCALE - width1), 0, topColor.getRGB());
					Main.mc.fontRendererObj.drawString(tier + "", finalI == 0 ? 0 : (int) (16 / SCALE - width2), 10, Color.GREEN.getRGB());
				});
			}

		} catch (Exception ignored) {
		}
	}

	private static void SBUAttributeOverlay(Slot slot) {
		try {
			AttributeValueResult valueData = InventoryData.values.get(slot);
			if (valueData == null) return;
			if (Objects.equals(valueData.top_display, "LBIN") && !configFile.showLbinOverlay) return;

			UGraphics.disableLighting();
			UGraphics.disableDepth();
			UGraphics.disableBlend();
			UMatrixStack matrixStack = new UMatrixStack();
			matrixStack.push();
			matrixStack.translate(slot.xDisplayPosition, slot.yDisplayPosition, 1f);
			matrixStack.scale(0.8, 0.8, 1.0);

			matrixStack.runWithGlobalState(() -> {
				Main.mc.fontRendererObj.drawString(valueData.top_display, 0, 0, 0x00FFFF);
			});

			matrixStack.scale(1.25, 1.25, 1.0);

			if (valueData.bottom_display > 0) {
				matrixStack.runWithGlobalState(() -> {
					Main.mc.fontRendererObj.drawStringWithShadow(String.valueOf(valueData.bottom_display),
							(17 - Main.mc.fontRendererObj.getStringWidth(String.valueOf(valueData.bottom_display))),
							9,
							0xFFFFFFFF
					);
				});
			}
			matrixStack.pop();
			UGraphics.enableLighting();
			UGraphics.enableDepth();
			UGraphics.enableBlend();
		} catch (Exception ignored) {
		}
	}
}
