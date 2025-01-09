package com.golem.skyblockutils.init;

import com.golem.skyblockutils.Main;
import com.golem.skyblockutils.models.Overlay.TextOverlay.TimerOverlay;
import com.golem.skyblockutils.utils.TimeHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import org.lwjgl.input.Keyboard;


public class KeybindsInit {
	public static KeyBinding openGUI;
	public static KeyBinding getComboValue;
	public static KeyBinding getPartyInfo;
	public static KeyBinding timerButton;
	public static final Minecraft mc = Minecraft.getMinecraft();
	private static final TimeHelper time = new TimeHelper();


	public static void registerKeyBinds() {
		KeyBinding[] keyBindings = {
			openGUI = new KeyBinding("key.sbu_gui", Keyboard.KEY_NONE, "sbu"),
			getComboValue = new KeyBinding("key.attribute_value", Keyboard.KEY_NONE, "sbu"),
			getPartyInfo = new KeyBinding("key.get_party_info", Keyboard.KEY_NONE, "sbu"),
			timerButton = new KeyBinding("key.timer", Keyboard.KEY_NONE, "sbu")
		};
		for (KeyBinding keyBinding : keyBindings) {
			ClientRegistry.registerKeyBinding(keyBinding);
		}
	}

	@SubscribeEvent
	public void onKeyInput(InputEvent.KeyInputEvent event) {
		if (GameSettings.isKeyDown(openGUI)) {
			Main.display = Main.configFile.gui();
		}
		if (GameSettings.isKeyDown(timerButton)) {
			TimerOverlay.active = !TimerOverlay.active;
			Main.mc.thePlayer.addChatMessage(new ChatComponentText((TimerOverlay.active ?
					EnumChatFormatting.DARK_GREEN + "ENABLED" + EnumChatFormatting.GREEN + " SBU Timer" :
					EnumChatFormatting.DARK_RED + "DISABLED" + EnumChatFormatting.RED + " SBU Timer")));
			if (TimerOverlay.active) {
				TimerOverlay.timer = time.getCurrentMS();
			}
		}
	}
}