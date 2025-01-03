package com.golem.skyblockutils.events;

import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.eventhandler.Event;

public class InventoryChangeEvent extends Event {

    public final GuiScreenEvent.DrawScreenEvent event;

    public InventoryChangeEvent(GuiScreenEvent.DrawScreenEvent event) {
        this.event = event;
    }
}
