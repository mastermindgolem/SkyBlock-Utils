package com.golem.skyblockutils.injection.mixins.minecraft.client;

import com.golem.skyblockutils.command.commands.SbuCommand;
import com.golem.skyblockutils.models.CustomItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({RenderItem.class})
public abstract class MixinRenderItem {

    @Inject(method = "renderItemOverlayIntoGUI", at = @At("HEAD"))
    public void renderItemOverlayIntoGUI(FontRenderer fr, ItemStack stack, int xPosition, int yPosition, String text, CallbackInfo ci) {
        try {
            CustomItem customItem = SbuCommand.getDataForItem(stack);
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
            CustomItem customItem = SbuCommand.getDataForItem(stack);
            if (customItem == null) return model;
            if (customItem.newItem.isEmpty()) return model;
            return Minecraft.getMinecraft().getRenderItem().getItemModelMesher().getItemModel(new ItemStack(Item.getByNameOrId(customItem.newItem)));
        } catch (Exception e) {
            return model;
        }
    }

    @Redirect(method = "renderQuads",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/item/Item;getColorFromItemStack(Lnet/minecraft/item/ItemStack;I)I"
            )
    )
    public int renderItem_renderByItem(Item item, ItemStack stack, int renderPass) {

        if (renderPass == 0) {
            try {
                CustomItem customItem = SbuCommand.getDataForItem(stack);
                if (customItem != null && customItem.newColor != -1) {
                    return customItem.newColor;
                }
            } catch (Exception e) {
            }
        }

        return item.getColorFromItemStack(stack, renderPass);
    }

    @Redirect(method = "renderItem(Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/resources/model/IBakedModel;)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/item/ItemStack;hasEffect()Z"
            )
    )
    public boolean renderItem_hasEffect(ItemStack stack) {
        CustomItem customItem = SbuCommand.getDataForItem(stack);
        if (customItem != null) {
            int glint = customItem.newGlint;
            if (glint != 0) {
                return glint > 0;
            }
        }
        return stack.hasEffect();
    }

}
