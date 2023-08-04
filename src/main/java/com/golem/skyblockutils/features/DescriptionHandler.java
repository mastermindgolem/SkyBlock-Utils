package com.golem.skyblockutils.features;

import com.golem.skyblockutils.Main;
import com.golem.skyblockutils.models.AttributePrice;
import com.golem.skyblockutils.models.DisplayString;
import com.golem.skyblockutils.utils.RequestUtil;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;

public class DescriptionHandler{


    private static class InventoryWrapper {
        public String chestName;
        public String fullInventoryNbt;
    }


    private static String previousData = "";
    public static HashMap<ItemStack, JsonObject> tooltipItemMap = new HashMap<>();
    public static LinkedHashMap<String, DisplayString> displayStrings = new LinkedHashMap<>();

    public static final NBTTagCompound EMPTY_COMPOUND = new NBTTagCompound();




    @SubscribeEvent
    public void loadDescriptionAndListenForChanges(GuiScreenEvent.BackgroundDrawnEvent event) {
        if (Main.configFile.dataSource == 0 || !Main.configFile.container_value || !ContainerValue.isActive) return;
        if (!(event.gui instanceof GuiContainer)) return;
        GuiContainer gc = (GuiContainer) event.gui;
        new Thread(() -> loadDescriptionForInventory(event, gc)).start();

    }

    private static boolean hasAnyStackChanged(GuiContainer gc) {
        for (Slot obj : gc.inventorySlots.inventorySlots) {
            ItemStack stack = obj.getStack();
            if (stack != null && !tooltipItemMap.containsKey(stack)) {
                return true;
            }
        }
        return false;
    }

    private static void loadDescriptionForInventory(GuiScreenEvent.BackgroundDrawnEvent event, GuiContainer gc) {
        InventoryWrapper wrapper = new InventoryWrapper();
        if (event.gui instanceof GuiChest) {

            ContainerChest chest = (ContainerChest) ((GuiChest) event.gui).inventorySlots;
            IInventory inv = chest.getLowerChestInventory();
            if (inv.hasCustomName()) {
                wrapper.chestName = inv.getName();
            }
        }

        NBTTagCompound compound = new NBTTagCompound();
        NBTTagList tl = new NBTTagList();

        for (Slot obj : gc.inventorySlots.inventorySlots) {
            ItemStack stack = obj.getStack();
            if (stack != null) {
                tl.appendTag(stack.serializeNBT());
            } else {
                tl.appendTag(EMPTY_COMPOUND);
            }
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            compound.setTag("i", tl);
            CompressedStreamTools.writeCompressed(compound, baos);



            wrapper.fullInventoryNbt = Base64.getEncoder().encodeToString(baos.toByteArray());
            if (Objects.equals(wrapper.fullInventoryNbt, previousData)) return;
            previousData = wrapper.fullInventoryNbt;

            List<ItemStack> stacks = new ArrayList<>();
            for (Slot obj : gc.inventorySlots.inventorySlots) {
                stacks.add(obj.getStack());
            }

            JsonObject data = (JsonObject) new JsonParser().parse(new Gson().toJson(wrapper));


            JsonArray info = new RequestUtil().sendPostRequest("https://sky.coflnet.com/api/price/nbt", data).getJson().getAsJsonArray();
            displayStrings = new LinkedHashMap<>();
            for (int i = 0; i < info.size(); i++) {
                ItemStack stack = stacks.get(i);
                if (!(info.get(i) instanceof JsonObject)) continue;
                tooltipItemMap.put(stack, info.get(i).getAsJsonObject());
                if (isAttributeItem(stack)) {
                    JsonObject itemdata = info.get(i).getAsJsonObject();
                    Main.mc.thePlayer.addChatMessage(new ChatComponentText(Objects.requireNonNull(AttributePrice.AttributeValue(stack)).get("display_string").getAsString() + ": " + Main.formatNumber(itemdata.get("lbin").getAsInt()) + " (" + Main.formatNumber(itemdata.get("median").getAsInt()) + ")"));
                    String displayString = Objects.requireNonNull(AttributePrice.AttributeValue(stack)).get("display_string").getAsString();
                    displayStrings.put(displayString, new DisplayString(displayStrings.getOrDefault(displayString, new DisplayString(0, 0)).quantity + 1, itemdata.get("lbin").getAsLong(), itemdata.get("median").getAsLong()));
                }

            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static boolean isAttributeItem(ItemStack item) {
        try {
            return item.serializeNBT().getCompoundTag("tag").getCompoundTag("ExtraAttributes").getCompoundTag("attributes").getKeySet().size() > 0;
        } catch (Exception ignored) {
            return false;
        }
    }

    private static void waitForChestContentLoad(GuiOpenEvent event, GuiContainer gc) {
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {e.printStackTrace();}
        for (int i = 1; i < 10; i++) {
            if (gc.inventorySlots.inventorySlots.get(gc.inventorySlots.inventorySlots.size() - 37).getStack() != null)
                break;
            try {
                // incremental backoff to wait for all inventory packages to arrive
                // (each slot is sent individually)
                Thread.sleep(20 * i);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


}
