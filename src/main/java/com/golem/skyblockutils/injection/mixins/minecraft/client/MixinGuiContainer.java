package com.golem.skyblockutils.injection.mixins.minecraft.client;

import com.golem.skyblockutils.features.AttributeOverlay;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Slot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiContainer.class)
public abstract class MixinGuiContainer extends GuiScreen {

    @Inject(method = "drawSlot", at = @At("RETURN"))
    public void drawSlotRet(Slot slotIn, CallbackInfo ci) {
        AttributeOverlay.drawSlot(slotIn);
    }

}
