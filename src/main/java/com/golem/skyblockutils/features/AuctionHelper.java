package com.golem.skyblockutils.features;

import com.golem.skyblockutils.events.InventoryChangeEvent;
import com.golem.skyblockutils.injection.mixins.minecraft.client.AccessorGuiEditSign;
import com.golem.skyblockutils.models.AttributeItemType;
import com.golem.skyblockutils.models.AttributeValueResult;
import com.golem.skyblockutils.models.AuctionAttributeItem;
import com.golem.skyblockutils.utils.AttributeUtils;
import com.golem.skyblockutils.utils.InventoryData;
import com.golem.skyblockutils.utils.ScreenUtils;
import com.golem.skyblockutils.utils.rendering.Renderable;
import com.golem.skyblockutils.utils.rendering.RenderableString;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.client.gui.inventory.GuiEditSign;
import net.minecraft.inventory.Slot;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Mouse;

import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Collectors;

import static com.golem.skyblockutils.Main.configFile;
import static com.golem.skyblockutils.Main.coolFormat;
import static com.golem.skyblockutils.models.AttributePrice.AttributePrices;
import static com.golem.skyblockutils.utils.Colors.getRarityCode;

public class AuctionHelper {
    long value = 0;
    TileEntitySign sign;

    ArrayList<Renderable> display = new ArrayList<>();

    DecimalFormat formatter = new DecimalFormat("###,###,###,###");

    @SubscribeEvent
    public void onInventoryChange(InventoryChangeEvent event) {
        display.clear();
        value = 0;
        if (!(event.event.gui instanceof GuiChest)) return;
        if (!configFile.auctionHelper) return;
//        if (!ButtonManager.isChecked("auctionHelper")) return;
        if (!Arrays.asList("Create Auction", "Create BIN Auction").contains(InventoryData.currentChestName)) return;

        Slot auctionSlot = InventoryData.containerSlots.get(13);
        if (!auctionSlot.getHasStack()) return;
        AttributeValueResult result = InventoryData.values.get(auctionSlot);
        if (result == null) return;
        value = result.value;
        int[] center = ScreenUtils.getCenter();
        int yOffset = 0;
        display.add(new RenderableString(EnumChatFormatting.YELLOW + "SBU Value: " + EnumChatFormatting.GREEN + formatter.format(value), center[0] + 100, 50 + 10 * ++yOffset)
                .onClick(() -> setSign(value)
        ));

        AttributeItemType itemType = AttributeUtils.getItemType(result.item_id);
        String best_attr = result.best_attribute.attribute;
        HashMap<String, Integer> attributes = InventoryData.items.get(auctionSlot).attributes;
        if (!Objects.equals(best_attr, "")) {
            if (!AttributePrices.get(itemType).containsKey(best_attr)) return;
            List<AuctionAttributeItem> items = AttributePrices.get(itemType).get(best_attr);
            if (Objects.equals(result.top_display, "GR")) {
                items = items.stream()
                        .filter(item -> item.attributeInfo.keySet().equals(attributes.keySet()))
                        .sorted(Comparator.comparingDouble((AuctionAttributeItem o) -> o.price))
                        .collect(Collectors.toList());
            } else {
                items = items.stream()
                        .filter(item -> Objects.equals(item.attributes.get(best_attr), result.best_attribute.tier))
                        .sorted(Comparator.comparingDouble((AuctionAttributeItem o) -> o.attributeInfo.get(best_attr).price_per))
                        .collect(Collectors.toList());
            }
            if (items.isEmpty()) return;
            if (items.size() > 5) items = items.subList(0, 5);
            display.add(new RenderableString("", center[0] + 100, 50 + 10 * ++yOffset));
            display.add(new RenderableString("Cheapest 5 similar:", center[0] + 100, 50 + 10 * ++yOffset));

            if (Objects.equals(result.top_display, "GR")) {
                for (AuctionAttributeItem item : items) {
                    display.add(new RenderableString(getRarityCode(item.tier) + item.item_name + " - " + EnumChatFormatting.RESET + " - " + EnumChatFormatting.GREEN + formatter.format(item.price), center[0] + 100, 50 + 10 * ++yOffset)
                            .onClick(() -> setSign(item.price)));
                }
            } else {
                for (AuctionAttributeItem item : items) {
                    display.add(new RenderableString(getRarityCode(item.tier) + item.item_name + " - " + EnumChatFormatting.YELLOW + coolFormat(item.attributeInfo.get(best_attr).price_per, 0) + EnumChatFormatting.RESET + " - " + EnumChatFormatting.GREEN + formatter.format(item.price), center[0] + 100, 50 + 10 * ++yOffset)
                            .onClick(() -> setSign(item.price)));
                }
            }
        }

    }

    private void setSign(Long price) {
        sign.signText[0] = new ChatComponentText(String.valueOf(price));
    }

    @SubscribeEvent
    public void onGuiOpen(GuiOpenEvent event) {
        if (!configFile.auctionHelper) return;
//        if (!ButtonManager.isChecked("auctionHelper")) return;
        if (!(event.gui instanceof GuiEditSign)) return;
        sign = null;
        if (!Arrays.asList("Create Auction", "Create BIN Auction").contains(InventoryData.lastOpenChestName)) return;
        if (value == 0) return;

        sign = ((AccessorGuiEditSign) event.gui).getTileSign();
        if (sign == null || sign.getPos().getY() != 0
                || !Objects.equals(sign.signText[1].getUnformattedText(), "^^^^^^^^^^^^^^^")
                || !Objects.equals(sign.signText[2].getUnformattedText(), "Your auction")
                || !Objects.equals(sign.signText[3].getUnformattedText(), "starting bid")) {
            sign = null;
        }
    }

    @SubscribeEvent
    public void onGuiDraw(GuiScreenEvent.DrawScreenEvent.Post event) {
        if (!(event.gui instanceof GuiEditSign)) return;

        if (sign == null) return;
        if (!configFile.auctionHelper) return;
//        if (!ButtonManager.isChecked("auctionHelper")) return;
        if (!(sign.getWorld().getTileEntity(sign.getPos()) instanceof TileEntitySign)) return;

        int mouseX = Mouse.getEventX() * event.gui.width / Minecraft.getMinecraft().displayWidth;
        int mouseY = event.gui.height - Mouse.getEventY() * event.gui.height / Minecraft.getMinecraft().displayHeight - 1;

        display.forEach(o -> o.render(mouseX, mouseY));
    }

}
