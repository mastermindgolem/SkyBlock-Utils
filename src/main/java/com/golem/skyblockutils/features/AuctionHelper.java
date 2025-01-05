package com.golem.skyblockutils.features;

import com.golem.skyblockutils.events.InventoryChangeEvent;
import com.golem.skyblockutils.injection.mixins.minecraft.client.AccessorGuiEditSign;
import com.golem.skyblockutils.models.AttributeValueResult;
import com.golem.skyblockutils.models.gui.ButtonManager;
import com.golem.skyblockutils.utils.InventoryData;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.client.gui.inventory.GuiEditSign;
import net.minecraft.inventory.Slot;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Arrays;
import java.util.Objects;

import static com.golem.skyblockutils.Main.configFile;

public class AuctionHelper {
    long value = 0;
    TileEntitySign sign;

    @SubscribeEvent
    public void onInventoryChange(InventoryChangeEvent event) {
        value = 0;
        if (!(event.event.gui instanceof GuiChest)) return;
        if (!configFile.auctionHelper) return;
        if (!ButtonManager.isChecked("auctionHelper")) return;
        if (!Arrays.asList("Create Auction", "Create BIN Auction").contains(InventoryData.currentChestName)) return;

        Slot auctionSlot = InventoryData.containerSlots.get(13);
        if (!auctionSlot.getHasStack()) return;
        AttributeValueResult result = InventoryData.values.get(auctionSlot);
        if (result == null) return;
        value = result.value;
    }

    @SubscribeEvent
    public void onGuiOpen(GuiOpenEvent event) {
        if (!configFile.auctionHelper) return;
        if (!ButtonManager.isChecked("auctionHelper")) return;
        if (!(event.gui instanceof GuiEditSign)) return;
        if (!Arrays.asList("Create Auction", "Create BIN Auction").contains(InventoryData.lastOpenChestName)) return;
        if (value == 0) return;

        sign = ((AccessorGuiEditSign) event.gui).getTileSign();
        if (sign != null && sign.getPos().getY() == 0
                && Objects.equals(sign.signText[1].getUnformattedText(), "^^^^^^^^^^^^^^^")
                && Objects.equals(sign.signText[2].getUnformattedText(), "Your auction")
                && Objects.equals(sign.signText[3].getUnformattedText(), "starting bid")) {
            sign.signText[0] = new ChatComponentText(String.valueOf(value));
//            ChatUtils.addChatMessage("AuctionHelper: " + value);
//            Main.mc.getNetHandler().addToSendQueue(new C12PacketUpdateSign(sign.getPos(), lines));
        }
    }
}
