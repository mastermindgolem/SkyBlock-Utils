package com.golem.skyblockutils.features.General;

import com.golem.skyblockutils.Main;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Keyboard;

public class Perspective {

    private boolean keyPressed = false;

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (Main.mc.thePlayer == null || Main.mc.theWorld == null || event.phase == TickEvent.Phase.START || !Main.configFile.removeSelfie) return;
        KeyBinding key = Main.mc.gameSettings.keyBindTogglePerspective;
        if (Keyboard.isKeyDown(key.getKeyCode()) && !keyPressed) {
            if (Main.mc.gameSettings.thirdPersonView == 2)
                Main.mc.gameSettings.thirdPersonView = 0;
            keyPressed = true;
        } else if (!Keyboard.isKeyDown(key.getKeyCode()) && keyPressed) {
            keyPressed = false;
        }

    }
}
