package com.golem.skyblockutils.features.General;

import com.golem.skyblockutils.Main;
import io.netty.channel.*;
import net.minecraft.network.play.client.C01PacketChatMessage;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;

import java.util.HashMap;

@ChannelHandler.Sharable
public class CustomEmotes extends ChannelOutboundHandlerAdapter {

    private final HashMap<String, String> replacements  = new HashMap<String, String>() {{
        put("<3", "❤");
        put(":star:", "✮");
        put(":yes:", "✔");
        put(":no:", "✖");
        put(":java:", "☕");
        put(":arrow:", "➜");
        put(":shrug:", "¯\\_(ツ)_/¯");
        put(":tableflip:", "(╯°□°）╯︵ ┻━┻");
        put("o/", "( ﾟ◡ﾟ)/");
        put(":123:", "123");
        put(":totem:", "◎_◎");
        put(":typing:", "✎...");
        put(":maths:", "√(π+x)=L");
        put(":snail:", "@'-'");
        put(":thinking:", "(0.o?)");
        put(":gimme:", "༼つ ◕_◕ ༽つ");
        put(":wizard:", "('-')⊃━☆ﾟ.*･｡ﾟ");
        put(":pvp:", "⚔");
        put(":peace:", "✌");
        put(":oof:", "OOF");
        put(":puffer:", "<('O')>");
        put(":snow:", "☃");
        put(":dog:", "(ᵔᴥᵔ)");
        put(":sloth:", "( ⬩ ⊝ ⬩ )");
        put(":dab:", "<o/");
        put(":cat:", "= ＾● ⋏ ●＾ =");
        put(":yey:", "ヽ (◕◡◕) ﾉ");
        put("h/", "ヽ(^◇^*)/");
        put(":dj:", "ヽ(⌐■_■)ノ♬");
        put(":cute:", "(✿ᴖ‿ᴖ)");
    }};


    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) {
        if (msg instanceof C01PacketChatMessage) {
            if (Main.configFile.customEmotes) {
                String message = ((C01PacketChatMessage) msg).getMessage();
                if (replacements.keySet().stream().anyMatch(message::contains)) {
                    for (String r : replacements.keySet()) message = message.replaceAll(r, replacements.get(r));
                }
                if (!message.equals(((C01PacketChatMessage) msg).getMessage())) {
                    Main.mc.thePlayer.sendChatMessage(message);
                    return;
                }
            }
        }
        ctx.write(msg, promise);
    }

    @SubscribeEvent
    public void connect(FMLNetworkEvent.ClientConnectedToServerEvent event) {
        ChannelPipeline pipeline = event.manager.channel().pipeline();
        pipeline.addBefore("packet_handler", this.getClass().getName(), this);
    }
}
