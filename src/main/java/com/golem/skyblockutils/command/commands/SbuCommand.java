package com.golem.skyblockutils.command.commands;

import com.golem.skyblockutils.Main;
import com.golem.skyblockutils.PersistentData;
import com.golem.skyblockutils.command.Help;
import com.golem.skyblockutils.command.HelpInvocation;
import com.golem.skyblockutils.features.Bestiary.Bestiary;
import com.golem.skyblockutils.features.Bestiary.Mob;
import com.golem.skyblockutils.models.AttributePrice;
import com.golem.skyblockutils.models.CustomItem;
import com.golem.skyblockutils.models.Overlay.TextOverlay.SplitsOverlay;
import com.golem.skyblockutils.utils.AuctionHouse;
import com.golem.skyblockutils.utils.ChatUtils;
import com.golem.skyblockutils.utils.RequestUtil;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import logger.Logger;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.event.HoverEvent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import static com.golem.skyblockutils.Main.*;

public class SbuCommand extends CommandBase {

	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");

	@Override
	public int getRequiredPermissionLevel() {
		return 0;
	}

	@Override
	public String getCommandName() {
		return "sbu";
	}

	@Override
	public List<String> getCommandAliases() {
		List<String> al = new ArrayList<>();
		al.add("skyblockutils");
		return al;
	}

	@Override
	public String getCommandUsage(ICommandSender sender) {
		return "Opens gui, shows help message";
	}

	@Override
	public void processCommand(ICommandSender sender, String[] args) throws CommandException {
		if(args.length == 0) {
			Main.display = configFile.gui();
			return;
		}
		if (args.length == 1) {
			if (Objects.equals(args[0], "help")) {
				Logger.debug("Sending help message!");
				mc.thePlayer.addChatMessage(new ChatComponentText(Help.getChat()).setChatStyle(new ChatStyle().setChatHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ChatComponentText(Help.getHoverChat())))));
				HelpInvocation.sendAll();
				return;
			}
			if (Objects.equals(args[0], "update") || Objects.equals(args[0], "refresh")) {
				new Thread(() -> {
					mc.thePlayer.addChatMessage(new ChatComponentText(EnumChatFormatting.GREEN + "Updating auctions"));
					String urlString = "https://mastermindgolem.pythonanywhere.com/?auctions=mb";
					auctions = new RequestUtil().sendGetRequest(urlString).getJsonAsObject().get("auctions").getAsJsonArray();
					AttributePrice.checkAuctions(auctions);
					bazaar = new RequestUtil().sendGetRequest("https://api.hypixel.net/skyblock/bazaar").getJsonAsObject();
					mc.thePlayer.addChatMessage(new ChatComponentText(EnumChatFormatting.GREEN + "Auctions updated"));
				}).start();
				AuctionHouse.lastKnownLastUpdated = System.currentTimeMillis();
			}
			if (Objects.equals(args[0], "bestiary")) {
				mc.thePlayer.addChatMessage(new ChatComponentText(EnumChatFormatting.GREEN + "Getting Bestiary Data"));
				new Thread(() ->{
					String url = "https://mastermindgolem.pythonanywhere.com/?bestiary=" + Main.mc.getSession().getPlayerID();
					JsonObject be = new RequestUtil().sendGetRequest(url).getJsonAsObject();
					mc.thePlayer.addChatMessage(new ChatComponentText(EnumChatFormatting.GREEN + "Got " + be.entrySet().size() + " bestiary mobs data."));
					for (Mob mob : Bestiary.bestiary.values()) mob.updateKills(be);
					mc.thePlayer.addChatMessage(new ChatComponentText(EnumChatFormatting.GREEN + "Bestiary updated"));
				}).start();
			}

			if (args[0].equals("glint")) {
				ItemStack item = mc.thePlayer.getHeldItem();
				if (item == null) {
					ChatUtils.addChatMessage(EnumChatFormatting.RED + "You must be holding an item.", true);
					return;
				}
				NBTTagCompound extraAttribute = item.serializeNBT().getCompoundTag("tag").getCompoundTag("ExtraAttributes");
				if (!extraAttribute.hasKey("uuid")) {
					ChatUtils.addChatMessage(EnumChatFormatting.RED + "This item does not have a UUID.", true);
					return;
				}

				String uuid = extraAttribute.getString("uuid");

				CustomItem customItem = Main.customItems.get(uuid);
				if (customItem != null) {
					if (customItem.newGlint == 1) {
						customItem.newGlint = 0;
						ChatUtils.addChatMessage(EnumChatFormatting.GREEN + "Enchant glint set to default for this item.", true);
					} else {
						customItem.newGlint = 1;
						ChatUtils.addChatMessage(EnumChatFormatting.GREEN + "Enchant glint added to item.", true);
					}
				} else {
					CustomItem newCustomItem = new CustomItem();
					newCustomItem.newGlint = 1;
					customItems.put(uuid, newCustomItem);
					ChatUtils.addChatMessage(EnumChatFormatting.GREEN + "Enchant glint added to item.", true);
				}
				PersistentData.saveCustomItems();
			}
			if (args[0].equals("unglint")) {
				ItemStack item = mc.thePlayer.getHeldItem();
				if (item == null) {
					ChatUtils.addChatMessage(EnumChatFormatting.RED + "You must be holding an item.", true);
					return;
				}
				NBTTagCompound extraAttribute = item.serializeNBT().getCompoundTag("tag").getCompoundTag("ExtraAttributes");
				if (!extraAttribute.hasKey("uuid")) {
					ChatUtils.addChatMessage(EnumChatFormatting.RED + "This item does not have a UUID.", true);
					return;
				}

				String uuid = extraAttribute.getString("uuid");

				CustomItem customItem = Main.customItems.get(uuid);
				if (customItem != null) {
					if (customItem.newGlint == -1) {
						customItem.newGlint = 0;
						ChatUtils.addChatMessage(EnumChatFormatting.GREEN + "Enchant glint set to default for this item.", true);
					} else {
						customItem.newGlint = -1;
						ChatUtils.addChatMessage(EnumChatFormatting.GREEN + "Enchant glint removed from this item.", true);
					}
				} else {
					CustomItem newCustomItem = new CustomItem();
					newCustomItem.newGlint = -1;
					customItems.put(uuid, newCustomItem);
					ChatUtils.addChatMessage(EnumChatFormatting.GREEN + "Enchant glint removed from this item.", true);
				}
				PersistentData.saveCustomItems();
			}
		}

		if (args.length >= 2) {
			if (args[0].equals("rename")) {
				renameItem(args);
			}

			if (args[0].equals("retexture")) {
				retextureItem(args);
			}
		}

		if (args.length == 2) {
			if (Objects.equals(args[0], "split") || Objects.equals(args[0], "splits")) {
				if (Objects.equals(args[1], "best")) {
					JsonObject bestOverall = null;
					int bestTheoretical = 0;
					for (int i = 0; i < 4; i++) {
						JsonObject best = null;
						for (JsonElement s : PersistentData.splits) {
							JsonObject splitData = s.getAsJsonObject();
							if (!Objects.equals(splitData.get("tier").getAsString(), "5")) continue;
							if (best == null || splitData.get("splits").getAsJsonArray().get(i).getAsInt() < best.get("splits").getAsJsonArray().get(i).getAsInt()) {
								best = splitData;
							}
						}
						if (best == null) {
							mc.thePlayer.addChatMessage(new ChatComponentText(EnumChatFormatting.RED + "No data found for Phase " + (i+1)));
							return;
						}
						bestTheoretical += best.get("splits").getAsJsonArray().get(i).getAsInt();
						mc.thePlayer.addChatMessage( new ChatComponentText(getPhaseName(i+1) + EnumChatFormatting.WHITE + SplitsOverlay.format(best.get("splits").getAsJsonArray().get(i).getAsInt()/60000F))
								.setChatStyle(new ChatStyle().setChatHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ChatComponentText(displaySplit(best))))));
					}
					for (JsonElement s : PersistentData.splits) {
						JsonObject splitData = s.getAsJsonObject();
						if (!Objects.equals(splitData.get("tier").getAsString(), "5")) continue;
						if (bestOverall == null || bestOverall.get("overall").getAsInt() > splitData.get("overall").getAsInt()) bestOverall = splitData;
					}
					if (bestOverall == null) {
						mc.thePlayer.addChatMessage(new ChatComponentText(EnumChatFormatting.RED + "No data found for Overall"));
						return;
					}
					mc.thePlayer.addChatMessage( new ChatComponentText(EnumChatFormatting.GOLD + "Overall: " + EnumChatFormatting.WHITE + SplitsOverlay.format(bestOverall.get("overall").getAsInt()/60000F))
							.setChatStyle(new ChatStyle().setChatHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ChatComponentText(displaySplit(bestOverall))))));
					mc.thePlayer.addChatMessage(new ChatComponentText(EnumChatFormatting.GOLD + "Theoretical: " + EnumChatFormatting.WHITE + SplitsOverlay.format(bestTheoretical/60000F)));
				}
				if (Objects.equals(args[1], "today")) {
					JsonObject bestOverall = null;
					int bestTheoretical = 0;
					for (int i = 0; i < 4; i++) {
						JsonObject best = null;
						for (JsonElement s : PersistentData.splits) {
							JsonObject splitData = s.getAsJsonObject();
							if (!Objects.equals(splitData.get("tier").getAsString(), "5")) continue;
							if (System.currentTimeMillis() - splitData.get("time").getAsLong() > 86400000) continue;
							if (best == null || splitData.get("splits").getAsJsonArray().get(i).getAsInt() < best.get("splits").getAsJsonArray().get(i).getAsInt()) {
								best = splitData;
							}
						}
						if (best == null) {
							mc.thePlayer.addChatMessage(new ChatComponentText(EnumChatFormatting.RED + "No data found for Phase " + (i+1)));
							return;
						}
						bestTheoretical += best.get("splits").getAsJsonArray().get(i).getAsInt();
						mc.thePlayer.addChatMessage( new ChatComponentText(getPhaseName(i+1) + EnumChatFormatting.WHITE + SplitsOverlay.format(best.get("splits").getAsJsonArray().get(i).getAsInt()/60000F))
								.setChatStyle(new ChatStyle().setChatHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ChatComponentText(displaySplit(best))))));
					}
					for (JsonElement s : PersistentData.splits) {
						JsonObject splitData = s.getAsJsonObject();
						if (!Objects.equals(splitData.get("tier").getAsString(), "5")) continue;
						if (System.currentTimeMillis() - splitData.get("time").getAsLong() > 86400000) continue;
						if (bestOverall == null || bestOverall.get("overall").getAsInt() > splitData.get("overall").getAsInt()) bestOverall = splitData;
					}
					if (bestOverall == null) {
						mc.thePlayer.addChatMessage(new ChatComponentText(EnumChatFormatting.RED + "No data found for Overall"));
						return;
					}
					mc.thePlayer.addChatMessage( new ChatComponentText(EnumChatFormatting.GOLD + "Overall: " + EnumChatFormatting.WHITE + SplitsOverlay.format(bestOverall.get("overall").getAsInt()/60000F))
							.setChatStyle(new ChatStyle().setChatHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ChatComponentText(displaySplit(bestOverall))))));
					mc.thePlayer.addChatMessage(new ChatComponentText(EnumChatFormatting.GOLD + "Theoretical: " + EnumChatFormatting.WHITE + SplitsOverlay.format(bestTheoretical/60000F)));
				}
				if (Objects.equals(args[1], "last")) {
					JsonObject best = null;
					for (JsonElement s : PersistentData.splits) {
						if (!Objects.equals(s.getAsJsonObject().get("tier").getAsString(), "5")) continue;
						if (best == null || best.get("time").getAsLong() < s.getAsJsonObject().get("time").getAsLong())
							best = s.getAsJsonObject();
					}
					if (best == null) {
						mc.thePlayer.addChatMessage(new ChatComponentText(EnumChatFormatting.DARK_RED + "No data found for last run."));
						return;
					}
					mc.thePlayer.addChatMessage(new ChatComponentText(displaySplit(best)));
				}
			}

			if (args[0].equals("recolor")) {
				ItemStack item = mc.thePlayer.getHeldItem();
				if (item == null) {
					ChatUtils.addChatMessage(EnumChatFormatting.RED + "You must be holding an item to recolor it.", true);
					return;
				}
				NBTTagCompound extraAttribute = item.serializeNBT().getCompoundTag("tag").getCompoundTag("ExtraAttributes");
				if (!extraAttribute.hasKey("uuid")) {
					ChatUtils.addChatMessage(EnumChatFormatting.RED + "This item cannot be recolored.", true);
					return;
				}
				if (!(item.getItem() instanceof ItemArmor) && (getDataForItem(item) == null || !(Item.getByNameOrId(getDataForItem(item).newItem) instanceof ItemArmor))) {
					ChatUtils.addChatMessage(EnumChatFormatting.RED + "This item cannot be recolored.", true);
					return;
				}
				String uuid = extraAttribute.getString("uuid");
				String newColor = args[1];
				if (newColor.equals("null")) {
					CustomItem customItem = customItems.get(uuid);
					if (customItem != null) customItem.newColor = -1;
					ChatUtils.addChatMessage(EnumChatFormatting.GREEN + "Item color reset", true);
					PersistentData.saveCustomItems();
					return;
				}
				CustomItem customItem = Main.customItems.get(uuid);
				if (customItem != null) {
					customItem.newColor = Integer.parseInt(newColor, 16);
				} else {
					CustomItem newCustomItem = new CustomItem();
					newCustomItem.newColor = Integer.parseInt(newColor, 16);
					customItems.put(uuid, newCustomItem);
				}
				PersistentData.saveCustomItems();
				ChatUtils.addChatMessage(EnumChatFormatting.GREEN + "Item recolored to " + newColor, true);
			}
		}
		if (args.length == 3) {
			if (Objects.equals(args[0], "split") || Objects.equals(args[0], "splits")) {
				if (Objects.equals(args[1], "best")) {
					JsonObject bestOverall = null;
					int bestTheoretical = 0;
					for (int i = 0; i < 4; i++) {
						JsonObject best = null;
						for (JsonElement s : PersistentData.splits) {
							JsonObject splitData = s.getAsJsonObject();
							if (!Objects.equals(splitData.get("tier").getAsString(), args[2])) continue;
							if (best == null || splitData.get("splits").getAsJsonArray().get(i).getAsInt() < best.get("splits").getAsJsonArray().get(i).getAsInt()) {
								best = splitData;
							}
						}
						if (best == null) {
							mc.thePlayer.addChatMessage(new ChatComponentText(EnumChatFormatting.RED + "No data found for Phase " + (i+1)));
							return;
						}
						bestTheoretical += best.get("splits").getAsJsonArray().get(i).getAsInt();
						mc.thePlayer.addChatMessage( new ChatComponentText(getPhaseName(i+1) + EnumChatFormatting.WHITE + SplitsOverlay.format(best.get("splits").getAsJsonArray().get(i).getAsInt()/60000F))
								.setChatStyle(new ChatStyle().setChatHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ChatComponentText(displaySplit(best))))));
					}
					for (JsonElement s : PersistentData.splits) {
						JsonObject splitData = s.getAsJsonObject();
						if (!Objects.equals(splitData.get("tier").getAsString(), args[2])) continue;
						if (bestOverall == null || bestOverall.get("overall").getAsInt() > splitData.get("overall").getAsInt()) bestOverall = splitData;
					}
					if (bestOverall == null) {
						mc.thePlayer.addChatMessage(new ChatComponentText(EnumChatFormatting.RED + "No data found for Overall"));
						return;
					}
					mc.thePlayer.addChatMessage( new ChatComponentText(EnumChatFormatting.GOLD + "Overall: " + EnumChatFormatting.WHITE + SplitsOverlay.format(bestOverall.get("overall").getAsInt()/60000F))
							.setChatStyle(new ChatStyle().setChatHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ChatComponentText(displaySplit(bestOverall))))));
					mc.thePlayer.addChatMessage(new ChatComponentText(EnumChatFormatting.GOLD + "Theoretical: " + EnumChatFormatting.WHITE + SplitsOverlay.format(bestTheoretical/60000F)));
				}
				if (Objects.equals(args[1], "today")) {
					JsonObject bestOverall = null;
					int bestTheoretical = 0;
					for (int i = 0; i < 4; i++) {
						JsonObject best = null;
						for (JsonElement s : PersistentData.splits) {
							JsonObject splitData = s.getAsJsonObject();
							if (!Objects.equals(splitData.get("tier").getAsString(), args[2])) continue;
							if (System.currentTimeMillis() - splitData.get("time").getAsLong() > 86400000) continue;
							if (best == null || splitData.get("splits").getAsJsonArray().get(i).getAsInt() < best.get("splits").getAsJsonArray().get(i).getAsInt()) {
								best = splitData;
							}
						}
						if (best == null) {
							mc.thePlayer.addChatMessage(new ChatComponentText(EnumChatFormatting.RED + "No data found for Phase " + (i+1)));
							return;
						}
						bestTheoretical += best.get("splits").getAsJsonArray().get(i).getAsInt();
						mc.thePlayer.addChatMessage( new ChatComponentText(getPhaseName(i+1) + EnumChatFormatting.WHITE + SplitsOverlay.format(best.get("splits").getAsJsonArray().get(i).getAsInt()/60000F))
								.setChatStyle(new ChatStyle().setChatHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ChatComponentText(displaySplit(best))))));
					}
					for (JsonElement s : PersistentData.splits) {
						JsonObject splitData = s.getAsJsonObject();
						if (System.currentTimeMillis() - splitData.get("time").getAsLong() > 86400000) continue;
						if (!Objects.equals(splitData.get("tier").getAsString(), args[2])) continue;
						if (bestOverall == null || bestOverall.get("overall").getAsInt() > splitData.get("overall").getAsInt()) bestOverall = splitData;
					}
					if (bestOverall == null) {
						mc.thePlayer.addChatMessage(new ChatComponentText(EnumChatFormatting.RED + "No data found for Overall"));
						return;
					}
					mc.thePlayer.addChatMessage( new ChatComponentText(EnumChatFormatting.GOLD + "Overall: " + EnumChatFormatting.WHITE + SplitsOverlay.format(bestOverall.get("overall").getAsInt()/60000F))
							.setChatStyle(new ChatStyle().setChatHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ChatComponentText(displaySplit(bestOverall))))));
					mc.thePlayer.addChatMessage(new ChatComponentText(EnumChatFormatting.GOLD + "Theoretical: " + EnumChatFormatting.WHITE + SplitsOverlay.format(bestTheoretical/60000F)));
				}
				if (Objects.equals(args[1], "last")) {
					JsonObject best = null;
					for (JsonElement s : PersistentData.splits) {
						if (!Objects.equals(s.getAsJsonObject().get("tier").getAsString(), args[2])) continue;
						if (best == null || best.get("time").getAsLong() < s.getAsJsonObject().get("time").getAsLong())
							best = s.getAsJsonObject();
					}
					if (best == null) {
						mc.thePlayer.addChatMessage(new ChatComponentText(EnumChatFormatting.DARK_RED + "No data found for last run."));
						return;
					}
					mc.thePlayer.addChatMessage(new ChatComponentText(displaySplit(best)));

				}
			}
		}
	}

	private static String displaySplit(JsonObject split) {
		List<String> sb = new ArrayList<>();
		if (split.has("time")) {
			sb.add(EnumChatFormatting.YELLOW + "Time: "  + EnumChatFormatting.WHITE + sdf.format(new Date(split.get("time").getAsLong())));
		}
		if (split.has("party")) {
			StringBuilder resultBuilder = new StringBuilder();
			for (JsonElement element : split.get("party").getAsJsonArray()) {
				if (resultBuilder.length() > 0) {
					resultBuilder.append(", ");
				}
				resultBuilder.append(element.getAsString());
			}

			String result = resultBuilder.toString();
			sb.add(EnumChatFormatting.YELLOW + "Party: "  + EnumChatFormatting.WHITE + result);
		}
		JsonArray sData = split.get("splits").getAsJsonArray();
		for (int i = 0; i < 4; i++) {
			sb.add(getPhaseName(i+1) + EnumChatFormatting.WHITE + SplitsOverlay.format(sData.get(i).getAsInt()/60000F));
		}
		if (split.has("overall")) {
			sb.add(EnumChatFormatting.AQUA + "Overall: "  + EnumChatFormatting.WHITE + SplitsOverlay.format(split.get("overall").getAsInt()/60000F));
		}
		return String.join("\n", sb);

	}

	private static String getPhaseName(int p) {
		switch (p) {
			case 1:
				return EnumChatFormatting.AQUA + "Supplies: ";
			case 2:
				return EnumChatFormatting.AQUA + "Build: ";
			case 3:
				return EnumChatFormatting.AQUA + "Fuel/Stun: ";
			case 4:
				return EnumChatFormatting.AQUA + "Kuudra Kill: ";
			default:
				return EnumChatFormatting.DARK_RED + "Unknown: ";
		}
	}

	private static void renameItem(String[] args) {
		ItemStack item = mc.thePlayer.getHeldItem();
		if (item == null) {
			ChatUtils.addChatMessage(EnumChatFormatting.RED + "You must be holding an item to rename it.", true);
			return;
		}
		NBTTagCompound extraAttribute = item.serializeNBT().getCompoundTag("tag").getCompoundTag("ExtraAttributes");
		if (!extraAttribute.hasKey("uuid")) {
			ChatUtils.addChatMessage(EnumChatFormatting.RED + "This item cannot be renamed.", true);
			return;
		}
		String uuid = extraAttribute.getString("uuid");
		String newName = "§r" + Arrays.stream(args).skip(1).collect(Collectors.joining(" "));
		if (newName.equals("§rnull")) {
			CustomItem customItem = customItems.get(uuid);
			if (customItem != null) customItem.newName = "";
			ChatUtils.addChatMessage(EnumChatFormatting.GREEN + "Item name reset", true);
			PersistentData.saveCustomItems();
			return;
		}
		CustomItem customItem = customItems.get(uuid);
		if (customItem != null) {
			customItem.newName = newName;
		} else {
			CustomItem newCustomItem = new CustomItem();
			newCustomItem.newName = newName;
			customItems.put(uuid, newCustomItem);
		}
		PersistentData.saveCustomItems();
		ChatUtils.addChatMessage(EnumChatFormatting.GREEN + "Item renamed to " + newName, true);
	}

	private static void retextureItem(String[] args) {
		ItemStack item = mc.thePlayer.getHeldItem();
		if (item == null) {
			ChatUtils.addChatMessage(EnumChatFormatting.RED + "You must be holding an item to retexture it.", true);
			return;
		}
		NBTTagCompound extraAttribute = item.serializeNBT().getCompoundTag("tag").getCompoundTag("ExtraAttributes");
		if (!extraAttribute.hasKey("uuid")) {
			ChatUtils.addChatMessage(EnumChatFormatting.RED + "This item cannot be retextured.", true);
			return;
		}
		String uuid = extraAttribute.getString("uuid");
		String newItem = Arrays.stream(args).skip(1).collect(Collectors.joining("_"));
		if (newItem.equals("null")) {
			CustomItem customItem = customItems.get(uuid);
			if (customItem != null) customItem.newItem = "";
			ChatUtils.addChatMessage(EnumChatFormatting.GREEN + "Item texture reset", true);
			PersistentData.saveCustomItems();
			return;
		}
		if (Item.getByNameOrId(newItem) == null) {
			ChatUtils.addChatMessage(EnumChatFormatting.RED + "Invalid item name", true);
			return;
		}
		CustomItem customItem = Main.customItems.get(uuid);
		if (customItem != null) {
			customItem.newItem = newItem;
		} else {
			CustomItem newCustomItem = new CustomItem();
			newCustomItem.newItem = newItem;
			customItems.put(uuid, newCustomItem);
		}
		PersistentData.saveCustomItems();
		ChatUtils.addChatMessage(EnumChatFormatting.GREEN + "Item retextured to " + newItem, true);
	}

	public static CustomItem getDataForItem(ItemStack stack) {
		if (stack == null) return null;
		NBTTagCompound extraAttributes = stack.serializeNBT().getCompoundTag("tag").getCompoundTag("ExtraAttributes");
		if (!extraAttributes.hasKey("uuid")) return null;
		String uuid = extraAttributes.getString("uuid");
		return Main.customItems.get(uuid);
	}
}
