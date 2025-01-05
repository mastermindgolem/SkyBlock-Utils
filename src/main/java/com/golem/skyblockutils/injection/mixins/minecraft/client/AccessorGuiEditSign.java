package com.golem.skyblockutils.injection.mixins.minecraft.client;

import net.minecraft.client.gui.inventory.GuiEditSign;
import net.minecraft.tileentity.TileEntitySign;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(GuiEditSign.class)
public interface AccessorGuiEditSign {
    @Accessor
    TileEntitySign getTileSign();
}
