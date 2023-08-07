package com.golem.skyblockutils.injection.mixins.minecraft.client;

import com.golem.skyblockutils.events.SlotClickEvent;
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

    @Inject(method = "handleMouseClick", at = @At(value = "HEAD"), cancellable = true)
    public void handleMouseClick(Slot slotIn, int slotId, int clickedButton, int clickType, CallbackInfo ci) {
        if (slotIn == null) return;
        GuiContainer $this = (GuiContainer) (Object) this;
        SlotClickEvent event = new SlotClickEvent($this, slotIn, slotId, clickedButton, clickType);
        event.post();
        if (event.isCanceled()) {
            ci.cancel();
            return;
        }
        if (event.usePickblockInstead) {
            $this.mc.playerController.windowClick(
                    $this.inventorySlots.windowId,
                    slotId, 2, 3, $this.mc.thePlayer
            );
            ci.cancel();
        }
    }
}
