package com.golem.skyblockutils.command.commands;

import com.golem.skyblockutils.Main;
import com.golem.skyblockutils.features.GuiEvent;
import com.golem.skyblockutils.utils.RequestUtil;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.event.ClickEvent;
import net.minecraft.event.HoverEvent;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.*;

import static com.golem.skyblockutils.Main.mc;

public class StatCommand extends CommandBase implements Help {

	private final List<String> helpStrings;

	public StatCommand() {
		helpStrings = new ArrayList<>();
	}

	@Override
	public Help getHelp() {
		return this;
	}

	@Override
	public int getRequiredPermissionLevel() {
		return 0;
	}

	@Override
	public List<String> getHelpStrings() {
		return helpStrings;
	}

	@Override
	public List<String> getHoverStrings() {
		return Arrays.asList(
			EnumChatFormatting.BLUE + "====================Kuudra help menu!====================",
			EnumChatFormatting.RESET + "\n",
			example() + "                  Checks kuudra stats of a person!                  ",
			EnumChatFormatting.RESET + "\n",
			EnumChatFormatting.GOLD + "/ks help" +
			EnumChatFormatting.GRAY + " ⬅ Shows this elaborate menu.",
			EnumChatFormatting.RESET + "\n",
			EnumChatFormatting.RESET + "\n",
			EnumChatFormatting.GOLD + "/ks [ign]" +
			EnumChatFormatting.GRAY + " ⬅ Shows player's kuudra stats unless [ign] is specified",
			EnumChatFormatting.RESET + "\n",
			example() + "E.g. /ks duophug",
			EnumChatFormatting.RESET + "\n",
			EnumChatFormatting.RESET + "\n",
			EnumChatFormatting.RED + "Legend:" +
			EnumChatFormatting.RESET + "\n  " +
			EnumChatFormatting.DARK_AQUA + "ign: Pheiaa" +
			EnumChatFormatting.RESET + "\n",
			EnumChatFormatting.BLUE + " ======================================================== "
		);
	}

	@Override
	public String getHelpMessage() {
		return
			EnumChatFormatting.GRAY + "▶ " +
			EnumChatFormatting.GOLD + "/kuudrastats " +
			EnumChatFormatting.AQUA + "ign" +
			example() + "(Aliases: /kuudra /ks)" +
			EnumChatFormatting.RESET + "\n" +
			EnumChatFormatting.WHITE + "Try it out! /ks drfie" +
			EnumChatFormatting.RESET + "\n";
	}

	@Override
	public void addHelpString() {
		helpStrings.add(getHelpMessage());
	}

	@Override
	public String getCommandName() {
		return "kuudrastats";
	}

	@Override
	public List<String> getCommandAliases() {
		List<String> al = new ArrayList<>();
		al.add("kuudra");
		al.add("ks");
		return al;
	}

	@Override
	public String getCommandUsage(ICommandSender sender) {
		return "/kuudrastats help for more information" +
			"\n" +
			"Try it out! /ks drfie";
	}

	@Override
	public void processCommand(ICommandSender sender, String[] args) {
		if (args.length == 0) {
			showPlayerStats(mc.getSession().getUsername(), false);
		}

		if (args.length == 1) {
			if (args[0].equals("help")) {
				sendHelpMessage();
				return;
			}
			showPlayerStats(args[0], false);
		}
	}

	public String example() {
		return EnumChatFormatting.GRAY + " " + EnumChatFormatting.ITALIC;
	}

	private void sendHelpMessage() {
		StringBuilder sb = new StringBuilder();
		for (String str : getHoverStrings()) {
			sb.append(str);
		}
		String hover = sb.toString();
		Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText(getHelpMessage()).setChatStyle(new ChatStyle().setChatHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ChatComponentText(hover)))));
	}

	public static JsonObject fetchProfileData(String uuid) {
		try {
			String urlString = "https://mastermindgolem.pythonanywhere.com/?kuudra=" + uuid;


			return new RequestUtil().sendGetRequest(urlString).getJsonAsObject();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static JsonObject fetchUUID(String ign) {
		try {
			URL url = new URL("https://api.mojang.com/users/profiles/minecraft/" + ign);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");

			int responseCode = connection.getResponseCode();
			JsonObject jsonObject = new JsonObject();
			if (responseCode == HttpURLConnection.HTTP_OK) {
				BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
				StringBuilder response = new StringBuilder();
				String line;

				while ((line = reader.readLine()) != null) {
					response.append(line);
				}

				reader.close();

				String jsonResponse = response.toString();
				JsonParser parser = new JsonParser();
				jsonObject = parser.parse(jsonResponse).getAsJsonObject();

				return jsonObject;
			} else {
				System.out.println(jsonObject);
				System.out.println("Request failed with response code: " + responseCode);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}


		return null;
	}

	public static void processProfileData(JsonObject data, String ign, boolean partyFinder) {

		IChatComponent msg;
		DecimalFormat formatter = new DecimalFormat("#,###");
		DecimalFormat formatter2 = new DecimalFormat("#,###.####");


		addChatMessage(EnumChatFormatting.RED + "------------------");
		if (data.get("Expert Plus").getAsBoolean()) {
			addChatMessage(EnumChatFormatting.AQUA + "Kuudra Stats for " + ign + EnumChatFormatting.DARK_GREEN + " [Expert Plus]", "/pv " + ign);
		} else if (data.get("Expert").getAsBoolean()) {
			addChatMessage(EnumChatFormatting.AQUA + "Kuudra Stats for " + ign + EnumChatFormatting.DARK_GREEN + " [Expert]", "/pv " + ign);
		} else {
			addChatMessage(EnumChatFormatting.AQUA + "Kuudra Stats for " + ign, "/pv " + ign);
		}
		addChatMessage(EnumChatFormatting.GREEN + "Kuudra Level: " + EnumChatFormatting.YELLOW + data.get("Kuudra Level").getAsInt());
		addChatMessage(EnumChatFormatting.GREEN + "Magical Power: " + EnumChatFormatting.YELLOW + formatter.format(data.get("Magical Power").getAsInt()));

		if (!GuiEvent.kuudraLevel.containsKey(ign) || (GuiEvent.kuudraLevel.containsKey(ign) && Main.time.getCurrentMS() - GuiEvent.kuudraLevel.getOrDefault(ign, new long[]{0L, 0L})[1] > 900)) {
			GuiEvent.kuudraLevel.put(ign, new long[]{data.get("Kuudra Level").getAsInt(), Main.time.getCurrentMS()});
		}



		msg = new ChatComponentText(
			EnumChatFormatting.AQUA + "Kuudra Completions: " + EnumChatFormatting.GRAY + "(Hover)"
		).setChatStyle(new ChatStyle().setChatHoverEvent(
			new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ChatComponentText(
					EnumChatFormatting.GOLD + "Basic: " + EnumChatFormatting.YELLOW + formatter.format(data.get("Basic Comps").getAsInt()) + EnumChatFormatting.GRAY + " (" + formatter.format(data.get("Basic Key Amt").getAsInt()) + " keys)" + "\n" +
					EnumChatFormatting.GOLD + "Hot: " + EnumChatFormatting.YELLOW + formatter.format(data.get("Hot Comps").getAsInt()) + EnumChatFormatting.GRAY + " (" + formatter.format(data.get("Hot Key Amt").getAsInt()) + " keys)" + "\n" +
					EnumChatFormatting.GOLD + "Burning: " + EnumChatFormatting.YELLOW + formatter.format(data.get("Burning Comps").getAsInt()) + EnumChatFormatting.GRAY + " (" + formatter.format(data.get("Burning Key Amt").getAsInt()) + " keys)" + "\n" +
					EnumChatFormatting.GOLD + "Fiery: " + EnumChatFormatting.YELLOW + formatter.format(data.get("Fiery Comps").getAsInt()) + EnumChatFormatting.GRAY + " (" + formatter.format(data.get("Fiery Key Amt").getAsInt()) + " keys)" + "\n" +
					EnumChatFormatting.GOLD + "Infernal: " + EnumChatFormatting.YELLOW + formatter.format(data.get("Infernal Comps").getAsInt()) + EnumChatFormatting.GRAY + " (" + formatter.format(data.get("Infernal Key Amt").getAsInt()) + " keys)"
			))));
		Main.mc.thePlayer.addChatMessage(msg);

		String ragnarokText = "";
		int chimeraLevel = data.get("Ragnarok Chimera").getAsInt();
		if (chimeraLevel == 0) {
			ragnarokText = EnumChatFormatting.DARK_RED + "Chimera Ragnarok Axe";
		} else if (chimeraLevel < 3) {
			ragnarokText = EnumChatFormatting.YELLOW + "Chimera " + chimeraLevel + " Ragnarok Axe";
		} else {
			ragnarokText = EnumChatFormatting.GREEN + "Chimera " + chimeraLevel + " Ragnarok Axe";
		}

		msg = new ChatComponentText(
			EnumChatFormatting.AQUA + "Important Items: " + EnumChatFormatting.GRAY + "(Hover)"
		).setChatStyle(new ChatStyle().setChatHoverEvent(
			new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ChatComponentText(
				(data.get("Wither Impact Weapon").getAsBoolean() ? EnumChatFormatting.GREEN : EnumChatFormatting.DARK_RED) + "Wither Impact Weapon\n" +
				(data.get("Precursor Eye").getAsBoolean() ? EnumChatFormatting.GREEN : EnumChatFormatting.DARK_RED) + "Precursor Eye\n" +
				(data.get("Gyrokinetic Wand").getAsBoolean() ? EnumChatFormatting.GREEN : EnumChatFormatting.DARK_RED) + "Gyrokinetic Wand\n" +
				(data.get("Warden Helmet").getAsBoolean() ? EnumChatFormatting.GREEN : EnumChatFormatting.DARK_RED) + "Warden Helmet\n" +
				(data.get("1B Bank").getAsBoolean() ? EnumChatFormatting.GREEN : EnumChatFormatting.DARK_RED) + "1 Billion Bank\n" +
				(data.get("Level 200 Golden Dragon").getAsBoolean() ? EnumChatFormatting.GREEN : EnumChatFormatting.DARK_RED) + "Level 200 Golden Dragon\n" +
				(data.get("Reaper Armor").getAsBoolean() ? EnumChatFormatting.GREEN : EnumChatFormatting.DARK_RED) + "Reaper Armor\n" +
				(data.get("Duplex Term").getAsBoolean() ? EnumChatFormatting.GREEN : EnumChatFormatting.DARK_RED) + "Duplex Terminator\n" +
				(data.get("FT Term").getAsBoolean() ? EnumChatFormatting.GREEN : EnumChatFormatting.DARK_RED) + "Fatal Tempo Terminator\n" +
				(data.get("Rend Term").getAsBoolean() ? EnumChatFormatting.GREEN : EnumChatFormatting.DARK_RED) + "Rend Terminator\n" +
				ragnarokText

			))));
		Main.mc.thePlayer.addChatMessage(msg);


		displayItem(data.get("Aurora Chestplate").getAsJsonObject());
		displayItem(data.get("Aurora Leggings").getAsJsonObject());
		displayItem(data.get("Aurora Boots").getAsJsonObject());
		displayItem(data.get("Terror Chestplate").getAsJsonObject());
		displayItem(data.get("Terror Leggings").getAsJsonObject());
		displayItem(data.get("Terror Boots").getAsJsonObject());

		for (JsonElement equipment : data.get("Equipment").getAsJsonArray()) displayItem(equipment.getAsJsonObject());

		if (data.get("Dominance").getAsInt() > data.get("Lifeline").getAsInt()) {
			addChatMessage(EnumChatFormatting.GREEN + "Dominance: " + EnumChatFormatting.YELLOW + data.get("Dominance").getAsInt());
		} else {
			addChatMessage(EnumChatFormatting.GREEN + "Lifeline: " + EnumChatFormatting.YELLOW + data.get("Lifeline").getAsInt());
		}

		displayItem(data.get("Support Item").getAsJsonObject());

		if (!data.get("InventoryAPI").getAsBoolean()) addChatMessage(EnumChatFormatting.RED + "This player has their inventory API disabled.");

		msg = new ChatComponentText(
			EnumChatFormatting.AQUA + "General Information: " + EnumChatFormatting.GRAY + "(Hover)"
		).setChatStyle(new ChatStyle().setChatHoverEvent(
			new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ChatComponentText(
				EnumChatFormatting.GOLD + "Kuudra Score: " + EnumChatFormatting.YELLOW + formatter.format(data.get("Kuudra Score").getAsInt()) + "\n" +
					EnumChatFormatting.GOLD + "Vanquisher Chance: " + EnumChatFormatting.YELLOW + formatter2.format(data.get("Vanquisher Chance").getAsDouble()) + "%" + "\n" +
					EnumChatFormatting.GOLD + "Mage Reputation: " + EnumChatFormatting.YELLOW + formatter.format(data.get("Mage Rep").getAsInt()) + "\n" +
					EnumChatFormatting.GOLD + "Barbarian Reputation: " + EnumChatFormatting.YELLOW + formatter.format(data.get("Barb Rep").getAsInt()) + "\n" +
					EnumChatFormatting.GOLD + "Nether Stars: " + EnumChatFormatting.YELLOW + formatter.format(data.get("Stars Amount").getAsInt())
			))));
		Main.mc.thePlayer.addChatMessage(msg);

		addChatMessage(EnumChatFormatting.RED + "------------------");

		if (partyFinder) {

			if (Main.configFile.autoKick && data.get("Kuudra Level").getAsInt() < ParseInt(Main.configFile.minKuudraLevel)) {
				addChatMessage(EnumChatFormatting.RED + "Kicking player for low Kuudra Level");
				mc.thePlayer.sendChatMessage("/party kick " + ign);
				return;
			}
			if (Main.configFile.autoKick && data.get("Magical Power").getAsInt() < ParseInt(Main.configFile.minMagicalPower)) {
				addChatMessage(EnumChatFormatting.RED + "Kicking player for low Magical Power");
				mc.thePlayer.sendChatMessage("/party kick " + ign);
				return;
			}
			int comps = data.get("Basic Comps").getAsInt();
			switch (Main.configFile.minCompsTier) {
				case 1:
					comps = data.get("Hot Comps").getAsInt();
					break;
				case 2:
					comps = data.get("Burning Comps").getAsInt();
					break;
				case 3:
					comps = data.get("Fiery Comps").getAsInt();
					break;
				case 4:
					comps = data.get("Infernal Comps").getAsInt();
					break;
			}

			if (Main.configFile.autoKick && comps < ParseInt(Main.configFile.minComps)) {
				addChatMessage(EnumChatFormatting.RED + "Kicking player for low Completions");
				mc.thePlayer.sendChatMessage("/party kick " + ign);
				return;
			}

			if (Main.configFile.autoKick && (!data.get("InventoryAPI").getAsBoolean() || !Main.configFile.kickAPIoff)) {
				if (data.get("Dominance").getAsInt() < Main.configFile.minDomLevel) {
					addChatMessage(EnumChatFormatting.RED + "Kicking player for low Dominance Levels");
					mc.thePlayer.sendChatMessage("/party kick " + ign);
					return;
				}

				if (Main.configFile.minAuroraTier > 0) {
					if (Main.configFile.autoKick && (!data.get("Aurora Chestplate").getAsJsonObject().has("stars") || data.get("Aurora Chestplate").getAsJsonObject().get("stars").getAsInt() < 10 * Main.configFile.minAuroraTier)) {
						addChatMessage(EnumChatFormatting.RED + "Kicking player for low Aurora Tier");
						mc.thePlayer.sendChatMessage("/party kick " + ign);
						return;
					}
					if (Main.configFile.autoKick && (!data.get("Aurora Leggings").getAsJsonObject().has("stars") || data.get("Aurora Leggings").getAsJsonObject().get("stars").getAsInt() < 10 * Main.configFile.minAuroraTier)) {
						addChatMessage(EnumChatFormatting.RED + "Kicking player for low Aurora Tier");
						mc.thePlayer.sendChatMessage("/party kick " + ign);
						return;
					}
					if (Main.configFile.autoKick && (!data.get("Aurora Boots").getAsJsonObject().has("stars") || data.get("Aurora Boots").getAsJsonObject().get("stars").getAsInt() < 10 * Main.configFile.minAuroraTier)) {
						addChatMessage(EnumChatFormatting.RED + "Kicking player for low Aurora Tier");
						mc.thePlayer.sendChatMessage("/party kick " + ign);
						return;
					}
				}
				if (Main.configFile.minTerrorTier > 0) {
					if (Main.configFile.autoKick && (!data.get("Terror Chestplate").getAsJsonObject().has("stars") || data.get("Terror Chestplate").getAsJsonObject().get("stars").getAsInt() < 10 * Main.configFile.minTerrorTier)) {
						addChatMessage(EnumChatFormatting.RED + "Kicking player for low Terror Tier");
						mc.thePlayer.sendChatMessage("/party kick " + ign);
						System.out.println(data.get("Terror Chestplate").getAsJsonObject().get("stars").getAsInt());
						return;
					}
					if (Main.configFile.autoKick && (!data.get("Terror Leggings").getAsJsonObject().has("stars") || data.get("Terror Leggings").getAsJsonObject().get("stars").getAsInt() < 10 * Main.configFile.minTerrorTier)) {
						addChatMessage(EnumChatFormatting.RED + "Kicking player for low Terror Tier");
						mc.thePlayer.sendChatMessage("/party kick " + ign);
						System.out.println(data.get("Terror Leggings").getAsJsonObject().get("stars").getAsInt());

						return;
					}
					if (Main.configFile.autoKick && (!data.get("Terror Boots").getAsJsonObject().has("stars") || data.get("Terror Boots").getAsJsonObject().get("stars").getAsInt() < 10 * Main.configFile.minTerrorTier)) {
						addChatMessage(EnumChatFormatting.RED + "Kicking player for low Terror Tier");
						mc.thePlayer.sendChatMessage("/party kick " + ign);
						return;
					}
				}
			}


			mc.thePlayer.addChatMessage(new ChatComponentText(EnumChatFormatting.DARK_RED + "[Kick Player]").setChatStyle(new ChatStyle().setChatClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/party kick " + ign))));
		}

	}

	public static void displayItem(JsonObject item) {
		try {
			final IChatComponent msg = new ChatComponentText(
				item.get("item_name").getAsString()
			).setChatStyle(new ChatStyle().setChatHoverEvent(
				new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ChatComponentText(
					item.get("item_name").getAsString()
						+ "\n"
						+ item.get("item_lore").getAsString()))));
			Main.mc.thePlayer.addChatMessage(msg);
		} catch (Exception e) {}
	}

	public static void showPlayerStats(String name, boolean partyFinder) {
		new Thread(() -> {
			try {
				String ign = name;
				JsonObject uuidData = fetchUUID(ign);
				assert uuidData != null;
				String uuid = uuidData.get("id").getAsString();
				ign = uuidData.get("name").getAsString();
				JsonObject profileData = fetchProfileData(uuid);
				assert profileData != null;
				processProfileData(profileData, ign, partyFinder);

			} catch (Exception e) {
				e.printStackTrace();
			}
		}).start();
	}

	private static int ParseInt(String s) {
		try {
			return Integer.parseInt(s);
		} catch (Exception ignored) {return 0;}
	}
	public static void addChatMessage(String string) {
		mc.thePlayer.addChatMessage(new ChatComponentText(string));
	}
	public static void addChatMessage(String string, String command) {
		IChatComponent msg = new ChatComponentText(string).setChatStyle(new ChatStyle().setChatClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, command)));
		mc.thePlayer.addChatMessage(msg);
	}
}