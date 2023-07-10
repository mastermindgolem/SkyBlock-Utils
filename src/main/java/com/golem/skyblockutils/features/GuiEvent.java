package com.golem.skyblockutils.features;

import com.golem.skyblockutils.Main;
import com.golem.skyblockutils.command.commands.StatCommand;
import com.golem.skyblockutils.init.KeybindsInit;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.golem.skyblockutils.Main.mc;

public class GuiEvent {
	List<String> lastPartyChecked = new ArrayList<>();
	Pattern pattern = Pattern.compile("^\\s*(.*?)\\s+\\(");

	@SubscribeEvent
	public void onItemToolTipEvent(ItemTooltipEvent event) {
		if (Main.mc.thePlayer == null || Main.mc.theWorld == null) return;
		if (event.toolTip.size() == 0) return;
		if (!GameSettings.isKeyDown(KeybindsInit.getPartyInfo)) return;
		try {
			GuiScreen gui = Main.mc.currentScreen;
			if (!(gui instanceof GuiChest)) return;
			Container container = ((GuiChest) gui).inventorySlots;
			if (!(container instanceof ContainerChest)) return;
			String chestName = ((ContainerChest) container).getLowerChestInventory().getDisplayName().getUnformattedText();

			if (!Objects.equals(chestName, "Party Finder")) return;

			if (!event.toolTip.get(0).contains("'s Party")) return;

			List<String> currentParty = new ArrayList<>();

			for (String line : event.toolTip) {
				line = line.replaceAll("§.", "");

				Matcher matcher = pattern.matcher(line);
				if (matcher.find()) {
					currentParty.add(matcher.group(0).split(" ")[1]);
				}
			}

			boolean cont = true;
			for (String a : currentParty)
				if (lastPartyChecked.contains(a)) {
					cont = false;
					break;
				}
			if (cont) {
				currentParty.forEach(StatCommand::showPlayerStats);
				lastPartyChecked = currentParty;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static void addChatMessage(String string) {
		mc.thePlayer.addChatMessage(new ChatComponentText(string));
	}
}
