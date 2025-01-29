package com.golem.skyblockutils.injection.mixins.minecraft.client;

import com.golem.skyblockutils.Main;
import com.golem.skyblockutils.models.CustomItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({RenderItem.class})
public abstract class MixinRenderItem {

    @Inject(method = "renderItemOverlayIntoGUI", at = @At("HEAD"))
    public void renderItemOverlayIntoGUI(FontRenderer fr, ItemStack stack, int xPosition, int yPosition, String text, CallbackInfo ci) {
        try {
            if (stack == null) return;
            NBTTagCompound extraAttributes = stack.serializeNBT().getCompoundTag("tag").getCompoundTag("ExtraAttributes");
            if (!extraAttributes.hasKey("uuid")) return;
            String uuid = extraAttributes.getString("uuid");
            CustomItem customItem = Main.customItems.get(uuid);
            if (customItem == null) return;
            if (customItem.newName.isEmpty()) return;
            stack.setStackDisplayName(customItem.newName);
        } catch (Exception ignored) {
        }
    }

    @ModifyArg(
            method = "renderItem(Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/resources/model/IBakedModel;)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/renderer/entity/RenderItem;renderModel(Lnet/minecraft/client/resources/model/IBakedModel;Lnet/minecraft/item/ItemStack;)V"
            ),
            index = 0
    )
    private IBakedModel modifyModel(IBakedModel model, ItemStack stack) {
        try {
            if (stack == null) return model;
            NBTTagCompound extraAttributes = stack.serializeNBT().getCompoundTag("tag").getCompoundTag("ExtraAttributes");
            if (!extraAttributes.hasKey("uuid")) return model;
            String uuid = extraAttributes.getString("uuid");
            CustomItem customItem = Main.customItems.get(uuid);
            if (customItem == null) return model;
            if (customItem.newItem.isEmpty()) return model;
            return Minecraft.getMinecraft().getRenderItem().getItemModelMesher().getItemModel(new ItemStack(Item.getByNameOrId(customItem.newItem)));
        } catch (Exception e) {
            return model;
        }
    }

    @ModifyVariable(
            method = "renderItem(Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/resources/model/IBakedModel;)V",
            at = @At("HEAD"),
            ordinal = 0,
            argsOnly = true)
    private ItemStack modifyLeatherColor(ItemStack stack) {
        if (stack == null) return null;
        NBTTagCompound extraAttributes = stack.serializeNBT().getCompoundTag("tag").getCompoundTag("ExtraAttributes");
        if (!extraAttributes.hasKey("uuid")) return stack;
        String uuid = extraAttributes.getString("uuid");
        CustomItem customItem = Main.customItems.get(uuid);
        if (customItem == null) return stack;
        if (customItem.newColor >= 0) return stack;
        if (!(stack.getItem() instanceof ItemArmor)) return stack;
        if (((ItemArmor) stack.getItem()).getArmorMaterial() != ItemArmor.ArmorMaterial.LEATHER) return stack;

        ItemStack modifiedStack = stack.copy();

        NBTTagCompound nbt = modifiedStack.hasTagCompound()
                ? modifiedStack.getTagCompound()
                : new NBTTagCompound();

        NBTTagCompound display = nbt.hasKey("display", 10)
                ? nbt.getCompoundTag("display")
                : new NBTTagCompound();

        display.setInteger("color", customItem.newColor);
        nbt.setTag("display", display);
        modifiedStack.setTagCompound(nbt);

        return modifiedStack;
    }
}
