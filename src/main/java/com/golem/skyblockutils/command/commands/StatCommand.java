package com.golem.skyblockutils.command.commands;

import com.golem.skyblockutils.Main;
import com.golem.skyblockutils.utils.RequestUtil;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.event.HoverEvent;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

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
			showPlayerStats(mc.thePlayer.getDisplayNameString());
		}

		if (args.length == 1) {
			if (args[0].equals("help")) {
				sendHelpMessage();
				return;
			}
			showPlayerStats(args[0]);
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

	public static void processProfileData(JsonObject data, String uuid, String ign) {

		IChatComponent msg;
		DecimalFormat formatter = new DecimalFormat("#,###");
		DecimalFormat formatter2 = new DecimalFormat("#,###.####");


		addChatMessage(EnumChatFormatting.RED + "------------------");
		if (data.get("Expert Plus").getAsBoolean()) {
			addChatMessage(EnumChatFormatting.AQUA + "Kuudra Stats for " + ign + EnumChatFormatting.DARK_GREEN + " [Expert Plus]");
		} else if (data.get("Expert Plus").getAsBoolean()) {
			addChatMessage(EnumChatFormatting.AQUA + "Kuudra Stats for " + ign + EnumChatFormatting.DARK_GREEN + " [Expert]");
		} else {
			addChatMessage(EnumChatFormatting.AQUA + "Kuudra Stats for " + ign);
		}
			addChatMessage(EnumChatFormatting.GREEN + "Kuudra Level: " + EnumChatFormatting.YELLOW + data.get("Kuudra Level").getAsInt());
		addChatMessage(EnumChatFormatting.GREEN + "Magical Power: " + EnumChatFormatting.YELLOW + formatter.format(data.get("Magical Power").getAsInt()));


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
				(data.get("Rend Term").getAsBoolean() ? EnumChatFormatting.GREEN : EnumChatFormatting.DARK_RED) + "Rend Terminator"
			))));
		Main.mc.thePlayer.addChatMessage(msg);

		displayItem(data.get("Aurora Chestplate").getAsJsonObject());
		displayItem(data.get("Aurora Leggings").getAsJsonObject());
		displayItem(data.get("Aurora Boots").getAsJsonObject());
		displayItem(data.get("Terror Chestplate").getAsJsonObject());
		displayItem(data.get("Terror Leggings").getAsJsonObject());
		displayItem(data.get("Terror Boots").getAsJsonObject());

		for (JsonElement equipment : data.get("Equipment").getAsJsonArray()) displayItem(equipment.getAsJsonObject());

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
		} catch (Exception e) {e.printStackTrace();}
	}

	public static void showPlayerStats(String name) {
		new Thread(() -> {
			try {
				String ign = name;
				JsonObject uuidData = fetchUUID(ign);
				assert uuidData != null;
				String uuid = uuidData.get("id").getAsString();
				ign = uuidData.get("name").getAsString();
				JsonObject profileData = fetchProfileData(uuid);
				assert profileData != null;
				processProfileData(profileData, uuid, ign);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}).start();
	}

	public static void addChatMessage(String string) {
		mc.thePlayer.addChatMessage(new ChatComponentText(string));
	}
}