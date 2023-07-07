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

		List<String> excludeAttributes = Arrays.asList(Main.configFile.attributesToExclude.split(", "));
		List<String> priorityAttributes = Arrays.asList(Main.configFile.priorityAttributes.split(", "));
		try {
			NBTTagCompound nbt = slot.getStack().serializeNBT().getCompoundTag("tag").getCompoundTag("ExtraAttributes").getCompoundTag("attributes");
			String item_id = slot.getStack().serializeNBT().getCompoundTag("tag").getCompoundTag("ExtraAttributes").getString("id");
			for (String key : all_kuudra_categories) {
				if (!item_id.contains(key)) continue;

				String best_attribute = "";
				int best_tier = 0;
				int best_value = -1;

				for (String key2 : nbt.getKeySet()) {
					if (excludeAttributes.contains(key2)) continue;
					ArrayList<JsonObject> items = null;
					try {
						items = AttributePrices.get(key).get(key2);
					} catch (Exception ignored) {}
					try {
						if (items != null && items.size() > 0) {
							items.sort(Comparator.comparingDouble((JsonObject o) -> o.get("price_per_tier").getAsDouble()));
							int value = items.get(0).get("price_per_tier").getAsInt();
							if (priorityAttributes.contains(best_attribute) && !priorityAttributes.contains(key2) && !Objects.equals(best_attribute, ""))
								continue;
							if (!priorityAttributes.contains(best_attribute) && priorityAttributes.contains(key2))
								best_value = 0;
							if (value * Math.pow(2, nbt.getInteger(key2) - 1) > best_value) {
								best_value = (int) (value * Math.pow(2, nbt.getInteger(key2) - 1));
								best_attribute = key2;
								best_tier = nbt.getInteger(key2);
							}
						}
					} catch (Exception ignored) {
						best_attribute = key2;
						best_tier = nbt.getInteger(key2);
						best_value = AttributePrice.LowestBin.get(key) + 1;
					}



				}
				JsonObject comboitem = null;
				if (!item_id.equals("ATTRIBUTE_SHARD")) comboitem = AttributePrice.getComboValue(key, new ArrayList<>(nbt.getKeySet()));
				if (comboitem != null && comboitem.get("starting_bid").getAsInt() > Math.max(best_value, Main.configFile.min_godroll_price * 1000000)) {
					UGraphics.disableLighting();
					UGraphics.disableDepth();
					UGraphics.disableBlend();
					UMatrixStack matrixStack = new UMatrixStack();
					matrixStack.push();
					matrixStack.translate(slot.xDisplayPosition, slot.yDisplayPosition, 1f);
					matrixStack.scale(0.8, 0.8, 1.0);

					matrixStack.runWithGlobalState(() -> {
						Main.mc.fontRendererObj.drawString("GR", 0, 0, 0x00FFFF);
					});

					matrixStack.pop();
					UGraphics.enableLighting();
					UGraphics.enableDepth();
//					UGraphics.enableBlend(); Keep the comment in-case it breaks something in the future, temp fix for overlay where it goes behind background
				} else if (best_tier != 0 && !best_attribute.equals("") && best_value > AttributePrice.LowestBin.get(key)) {
					UGraphics.disableLighting();
					UGraphics.disableDepth();
					UGraphics.disableBlend();
					UMatrixStack matrixStack = new UMatrixStack();
					matrixStack.push();
					matrixStack.translate(slot.xDisplayPosition, slot.yDisplayPosition, 1f);
					matrixStack.scale(0.8, 0.8, 1.0);

					String finalBest_attribute = best_attribute;
					matrixStack.runWithGlobalState(() -> {
						Main.mc.fontRendererObj.drawString(AttributePrice.ShortenedAttribute(finalBest_attribute), 0, 0, 0x00FFFF);
					});

					matrixStack.pop();
					UGraphics.enableLighting();
					UGraphics.enableDepth();
					UGraphics.enableBlend();
					GlStateManager.disableLighting();
					GlStateManager.disableDepth();
					GlStateManager.disableBlend();
					Main.mc.fontRendererObj.drawStringWithShadow(String.valueOf(best_tier),
							(float) (slot.xDisplayPosition + 17 - Main.mc.fontRendererObj.getStringWidth(String.valueOf(best_tier))),
							slot.yDisplayPosition + 9,
							0xFFFFFFFF
					);
					GlStateManager.enableLighting();
					GlStateManager.enableDepth();
				} else if (key.equals("ATTRIBUTE_SHARD")) {
					UGraphics.disableLighting();
					UGraphics.disableDepth();
					UGraphics.disableBlend();
					UMatrixStack matrixStack = new UMatrixStack();
					matrixStack.push();
					matrixStack.translate(slot.xDisplayPosition, slot.yDisplayPosition, 1f);
					matrixStack.scale(0.8, 0.8, 1.0);

					String finalBest_attribute = new ArrayList<>(nbt.getKeySet()).get(0);
					matrixStack.runWithGlobalState(() -> {
						Main.mc.fontRendererObj.drawString(AttributePrice.ShortenedAttribute(finalBest_attribute), 0, 0, 0x00FFFF);
					});

					matrixStack.pop();
					UGraphics.enableLighting();
					UGraphics.enableDepth();
					UGraphics.enableBlend();
					GlStateManager.disableLighting();
					GlStateManager.disableDepth();
					GlStateManager.disableBlend();
					Main.mc.fontRendererObj.drawStringWithShadow(String.valueOf(nbt.getInteger(finalBest_attribute)),
							(float) (slot.xDisplayPosition + 17 - Main.mc.fontRendererObj.getStringWidth(String.valueOf(nbt.getInteger(finalBest_attribute)))),
							slot.yDisplayPosition + 9,
							0xFFFFFFFF
					);
					GlStateManager.enableLighting();
					GlStateManager.enableDepth();
				} else if (nbt.getKeySet().size() > 0){
					UGraphics.disableLighting();
					UGraphics.disableDepth();
					UGraphics.disableBlend();
					UMatrixStack matrixStack = new UMatrixStack();
					matrixStack.push();
					matrixStack.translate(slot.xDisplayPosition, slot.yDisplayPosition, 1f);
					matrixStack.scale(0.8, 0.8, 1.0);

					matrixStack.runWithGlobalState(() -> {
						Main.mc.fontRendererObj.drawString("LBIN", 0, 0, 0x00FFFF);
					});

					matrixStack.pop();
					UGraphics.enableLighting();
					UGraphics.enableDepth();
					UGraphics.enableBlend();
				}


			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
