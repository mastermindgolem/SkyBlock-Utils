package com.golem.skyblockutils.features;

import com.golem.skyblockutils.Main;
import com.golem.skyblockutils.models.AttributeValueResult;
import com.golem.skyblockutils.utils.InventoryData;
import gg.essential.universal.UGraphics;
import gg.essential.universal.UMatrixStack;
import net.minecraft.inventory.Slot;

import java.util.Objects;

import static com.golem.skyblockutils.Main.configFile;

public class AttributeOverlay {


	public static void drawSlot(Slot slot) {
		if (slot == null || !slot.getHasStack() || !Main.configFile.attribute_overlay) return;
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
