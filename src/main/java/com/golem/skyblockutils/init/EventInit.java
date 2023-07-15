package com.golem.skyblockutils.init;

import com.golem.skyblockutils.ChatListener;
import com.golem.skyblockutils.Main;
import com.golem.skyblockutils.features.GuiEvent;
import com.golem.skyblockutils.features.KuudraFight.Kuudra;
import com.golem.skyblockutils.features.KuudraFight.Waypoints;
import com.golem.skyblockutils.features.KuudraHealth;
import com.golem.skyblockutils.features.KuudraOverlay;
import com.golem.skyblockutils.features.ContainerValue;
import com.golem.skyblockutils.models.Overlay.ImageOverlay.FlareOverlay;
import com.golem.skyblockutils.models.Overlay.TextOverlay.DungeonOverlay;
import com.golem.skyblockutils.models.Overlay.TextOverlay.DungeonSkillOverlay;
import com.golem.skyblockutils.models.Overlay.TextOverlay.KuudraTestOverlay;
import com.golem.skyblockutils.models.Overlay.TextOverlay.KuudraTestSkillOverlay;
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
			new ChatListener(),
			new GuiEvent(),
			new KuudraHealth(),
			new Kuudra(),
			new Waypoints(),
			new GuiEvent(),
			new KuudraTestOverlay(),
			new KuudraTestSkillOverlay(),
			new DungeonSkillOverlay(),
			new DungeonOverlay(),
			new FlareOverlay()
		};

		for (Object listener : listeners) {
			MinecraftForge.EVENT_BUS.register(listener);
		}

	}
}
