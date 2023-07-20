package com.golem.skyblockutils.init;

import com.golem.skyblockutils.ChatListener;
import com.golem.skyblockutils.Main;
import com.golem.skyblockutils.features.*;
import com.golem.skyblockutils.features.KuudraFight.Kuudra;
import com.golem.skyblockutils.features.KuudraFight.Waypoints;
import com.golem.skyblockutils.models.Overlay.TextOverlay.*;
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
			new BrokenHyp()
		};

		for (Object listener : listeners) {
			MinecraftForge.EVENT_BUS.register(listener);
		}
	}

	public static void registerOverlays() {
		Object[] listeners = {
				new AlignOverlay(),
				new RagnarokOverlay(),
				new CratesOverlay(),
				new ChampionOverlay()
		};

		for (Object listener : listeners) {
			MinecraftForge.EVENT_BUS.register(listener);
		}
	}
}
