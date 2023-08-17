package com.golem.skyblockutils.events;

import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import net.minecraft.network.Packet;
import net.minecraftforge.common.MinecraftForge;

public class PacketListener extends ChannelDuplexHandler {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        boolean get = true;

        if(msg instanceof Packet) {
            //if msg is packet, post new packet event
            PacketEvent.ReceiveEvent inPacket = new PacketEvent.ReceiveEvent((Packet<?>)msg);
            MinecraftForge.EVENT_BUS.post(inPacket);
            //if the packet is cancelled, dont process it
            if(inPacket.isCanceled()) {
                get = false;
            }
            //if event changes packet, update
            msg = inPacket.getPacket();
        }
        //whether or not to process the packet
        if(get) super.channelRead(ctx, msg);
    }

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {

        boolean send = true;

        if(msg instanceof Packet) {
            //if msg is packet, post new packet event
            PacketEvent.SendEvent outPacket = new PacketEvent.SendEvent((Packet<?>)msg);
            MinecraftForge.EVENT_BUS.post(outPacket);
            if(outPacket.isCanceled()) {
                send = false;
            }
            msg = outPacket.getPacket();
        }
        if(send) super.write(ctx, msg, promise);
    }
}