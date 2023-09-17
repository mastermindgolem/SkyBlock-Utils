package com.golem.skyblockutils.features;

import com.golem.skyblockutils.Main;
import com.golem.skyblockutils.features.KuudraFight.Kuudra;
import com.golem.skyblockutils.models.AttributePrice;
import com.golem.skyblockutils.models.DisplayString;
import com.golem.skyblockutils.utils.RequestUtil;
import com.golem.skyblockutils.utils.ToolTipListener;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.*;

public class DescriptionHandler{


    private static class InventoryWrapper {
        public String chestName;
        public String fullInventoryNbt;
    }


    private static String previousData = "";
    public static HashMap<ItemStack, JsonObject> tooltipItemMap = new HashMap<>();
    public static LinkedHashMap<String, DisplayString> displayStrings = new LinkedHashMap<>();
    private static final DecimalFormat formatter = new DecimalFormat("#,###,###,###");
    public static final NBTTagCompound EMPTY_COMPOUND = new NBTTagCompound();
    private static long lastUpdate = 0;

    private boolean IsOpen = true;
    private boolean shouldUpdate = false;


    public void Close() {
        IsOpen = false;
    }

    public static String ExtractStackableIdFromItemStack(ItemStack stack) {
        if (stack != null) {
            try {
                NBTTagCompound serialized = stack.serializeNBT();
                String itemTag = serialized.getCompoundTag("tag").getCompoundTag("ExtraAttributes")
                        .getString("id");
                if (itemTag != null && itemTag.length() > 1)
                    return itemTag + ":" + stack.stackSize;
                return serialized.getCompoundTag("tag").getCompoundTag("display")
                        .getString("Name");
            } catch (Exception e) {
            }
        }
        return "";
    }

    public static String ExtractIdFromItemStack(ItemStack stack) {
        if (stack != null) {
            try {
                String uuid = stack.serializeNBT().getCompoundTag("tag").getCompoundTag("ExtraAttributes")
                        .getString("uuid");
                if (uuid.length() == 0) {
                    throw new Exception();
                }
                return uuid;
            } catch (Exception e) {
            }
        }
        return ExtractStackableIdFromItemStack(stack);
    }


    @SubscribeEvent
    public void onToolTipEvent(ItemTooltipEvent event) {
        if (event.toolTip.size() == 0 || !Main.configFile.showItemValue) return;
        JsonObject data = tooltipItemMap.getOrDefault(event.itemStack, null);
        if (data == null) return;
        event.toolTip.add(EnumChatFormatting.GOLD + "Item Value: " + EnumChatFormatting.GREEN + formatter.format(data.get("median").getAsDouble()));
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onGuiOpen(GuiOpenEvent event) {
        if (event.gui == null) {
            Close();
            return;
        }

        if (!(event.gui instanceof GuiContainer)) return;
        new Thread(() -> {
            try {
                loadDescriptionAndListenForChanges(event);
            } catch (Exception e) {
                System.out.println("failed to update description " + e);
            }
        }).start();
    }

    public void loadDescriptionAndListenForChanges(GuiOpenEvent event) {
        if (event.gui == null) return;
        if (!(event.gui instanceof GuiContainer)) return;
        if (!Main.configFile.showItemValue) return;
        GuiContainer gc = (GuiContainer) event.gui;

        loadDescriptionForInventory(event, gc, false);
        int iteration = 1;
        while (IsOpen) {
            try {
                Thread.sleep(300 + iteration++);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            if (shouldUpdate || hasAnyStackChanged(gc)) {
                shouldUpdate = false;
                loadDescriptionForInventory(event, gc, true);
                // reduce update time since its more likely that more changes occure after one
                iteration = 5;
            }
            if (iteration >= 30)
                iteration = 29; // cap at 9 second update interval
        }
    }

    private static boolean hasAnyStackChanged(GuiContainer gc) {
        for (Slot obj : gc.inventorySlots.inventorySlots) {
            ItemStack stack = obj.getStack();
            if (stack != null && stack.serializeNBT().hasKey("tag") && stack.serializeNBT().getCompoundTag("tag").hasKey("ExtraAttributes") && !tooltipItemMap.containsKey(stack)) {
                Kuudra.addChatMessage(stack.serializeNBT().getCompoundTag("tag").getCompoundTag("ExtraAttributes").getString("id"));
                return true;
            }
        }
        return false;
    }

    private static void loadDescriptionForInventory(GuiOpenEvent event, GuiContainer gc, boolean skipLoadCheck) {
        if (!Main.configFile.showItemValue && Main.configFile.dataSource == 0) return;
        InventoryWrapper wrapper = new InventoryWrapper();
        if (event.gui instanceof GuiChest) {
            if (!skipLoadCheck)
                waitForChestContentLoad(event, gc);

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
        new Thread(() -> {
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
                if (Main.configFile.dataSource == 1 && Main.configFile.container_value > 0 && ContainerValue.isActive) displayStrings = new LinkedHashMap<>();
                for (int i = 0; i < info.size(); i++) {
                    ItemStack stack = stacks.get(i);
                    if (!(info.get(i) instanceof JsonObject)) continue;
                    tooltipItemMap.put(stack, info.get(i).getAsJsonObject());
                    JsonObject itemdata = info.get(i).getAsJsonObject();
                    if (Main.configFile.dataSource == 1 && Main.configFile.container_value > 0 && ContainerValue.isActive) {
                        String displayString = (isAttributeItem(stack) ? Objects.requireNonNull(AttributePrice.AttributeValue(stack)).get("display_string").getAsString() : stack.serializeNBT().getCompoundTag("tag").getCompoundTag("display").getString("Name"));
                        displayStrings.put(displayString, new DisplayString(displayStrings.getOrDefault(displayString, new DisplayString(0, 0)).quantity + 1, itemdata.get("lbin").getAsLong(), itemdata.get("median").getAsLong()));
                    }


                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();

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
