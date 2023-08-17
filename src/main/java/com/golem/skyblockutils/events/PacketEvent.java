package com.golem.skyblockutils.events;

import logger.Logger;
import net.minecraft.network.Packet;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

@Cancelable
public class PacketEvent extends Event{


    private Packet<?> packet;

    public PacketEvent(Packet<?> packet) {
        this.packet = packet;
    }

    public Packet<?> getPacket() {
        return packet;
    }

    public void setPacket(Packet<?> packet) {
        this.packet = packet;
    }


    public static class ReceiveEvent extends PacketEvent {

        public ReceiveEvent(Packet<?> packet) {
            super(packet);
        }
    }

    public static class SendEvent extends PacketEvent {

        public SendEvent(Packet<?> packet) {
            super(packet);
        }
    }




}
