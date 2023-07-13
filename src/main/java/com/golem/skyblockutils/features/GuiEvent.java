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
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.golem.skyblockutils.Main.mc;

public class GuiEvent {
	List<String> lastPartyChecked = new ArrayList<>();
	Pattern pattern = Pattern.compile("^\\s*(.*?)\\s+\\(");

	public static HashMap<String, long[]> kuudraLevel = new HashMap<>();

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

			List<String> toolTip = new ArrayList<>(event.toolTip);
			for (String line : event.toolTip) {
				String line1 = line.replaceAll("§.", "");

				Matcher matcher = pattern.matcher(line1);
				if (matcher.find()) {
					String ign = matcher.group(0).split(" ")[1];
					currentParty.add(ign);
					int index = event.toolTip.indexOf(line);
					toolTip.remove(line);
					String updatedString = line.replaceAll("\\(§e\\d+§b\\)", "(§e" + kuudraLevel.getOrDefault(ign, new long[]{0, 0})[0] + "§b)");
					System.out.println(updatedString);
					toolTip.add(index, updatedString);
				}
			}


			event.toolTip.clear();
			event.toolTip.addAll(toolTip);


			boolean cont = true;
			for (String a : currentParty)
				if (lastPartyChecked.contains(a)) {
					cont = false;
					break;
				}
			if (cont) {
				currentParty.forEach(member -> StatCommand.showPlayerStats(member, false));
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
