package com.golem.skyblockutils.init;

import com.golem.skyblockutils.ChatListener;
import com.golem.skyblockutils.Main;
import com.golem.skyblockutils.features.KuudraOverlay;
import com.golem.skyblockutils.utils.ContainerValue;
import com.golem.skyblockutils.utils.ToolTipListener;
import net.minecraftforge.common.MinecraftForge;

public class EventInit {
	public static void registerEvents() {
		Object[] listeners = {
			new Main(),
			new ToolTipListener(),
			new KuudraOverlay(),
			new KeybindsInit(),
			new ContainerValue(),
			new ChatListener()
		};

		for (Object listener : listeners) {
			MinecraftForge.EVENT_BUS.register(listener);
		}

	}
}
