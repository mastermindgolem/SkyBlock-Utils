package com.golem.skyblockutils.init;

import com.golem.skyblockutils.Main;
import com.golem.skyblockutils.features.ContainerValue;
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
	public static KeyBinding getContainerValue;
	public static KeyBinding getPartyInfo;
	public static KeyBinding timerButton;
	public static final Minecraft mc = Minecraft.getMinecraft();
	private static final TimeHelper time = new TimeHelper();


	public static void registerKeyBinds() {
		KeyBinding[] keyBindings = {
			openGUI = new KeyBinding("key.sbu_gui", Keyboard.KEY_V, "sbu"),
			getComboValue = new KeyBinding("key.attribute_value", Keyboard.KEY_L, "sbu"),
			getContainerValue = new KeyBinding("key.container_value", Keyboard.KEY_K, "sbu"),
			getPartyInfo = new KeyBinding("key.get_party_info", Keyboard.KEY_O, "sbu"),
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
		if (GameSettings.isKeyDown(getContainerValue)) {
			ContainerValue.isActive = !ContainerValue.isActive;
			Main.mc.thePlayer.addChatMessage(new ChatComponentText((ContainerValue.isActive ?
					EnumChatFormatting.DARK_GREEN + "ENABLED" + EnumChatFormatting.GREEN + " Container Value for Attribute Items" :
					EnumChatFormatting.DARK_RED + "DISABLED" + EnumChatFormatting.RED + " Container Value for Attribute Items")));
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