package com.golem.skyblockutils;

import com.golem.skyblockutils.init.GuiInit;
import com.golem.skyblockutils.models.Overlay.TextOverlay.*;
import com.golem.skyblockutils.models.gui.GuiElement;
import com.golem.skyblockutils.models.gui.MoveGui;
import gg.essential.vigilance.Vigilant;
import gg.essential.vigilance.data.*;
import logger.Logger;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class Config extends Vigilant
{
	public static String configFolder;


	public File CONFIG_FILE;

	@Property(type = PropertyType.SWITCH,
			name = "Enable GUI Elements",
			description = "Switch to disable/enable all GUI related elements",
			category = "Overlays", subcategory = "Locations")
	public boolean testGui = true;

	@Property(
			type = PropertyType.SLIDER,
			name = "Time between AH Checks",
			description = "Time to wait between updating AH data. (0 turns it off)",
			category = "General",
			subcategory = "Auction House",
			min = 5,
			max = 30
	)
	public int time_between_checks = 5;

	@Property(
			type = PropertyType.SWITCH,
			name = "Show Item Value",
			description = "Show's approximate item value in lore, based on Cofl data.",
			category = "General",
			subcategory = "Auction House"
	)
	public boolean showItemValue = true;
	@Property(type = PropertyType.SWITCH, name = "Hide Sack Message", description = "Hides the message that shows how many items were added/removed from sack", category = "General", subcategory = "General")
	public boolean hideSackMessage = false;
	@Property(type = PropertyType.SWITCH, name = "Elite 500", description = "Shows ranking of player if they are in the Elite 500 (Top 500 SB Level)", category = "General", subcategory = "General")
	public boolean showElite500 = false;
	@Property(type = PropertyType.SLIDER, name = "Timer", description = "0 = off, timer in seconds to alert you", category = "General", subcategory = "General", max = 1200)
	public int timerAmount = 0;

	@Property(type = PropertyType.BUTTON, name = "Move Timer", description = "Test", category = "General", subcategory = "General")
	@SuppressWarnings("unused")
	public void MoveTimerOverlay() {
		if (timerAmount == 0) {
			Main.mc.thePlayer.addChatMessage(new ChatComponentText(EnumChatFormatting.GOLD + "SBU > " + EnumChatFormatting.RED + "You cannot move this since Timer is off."));
			return;
		}
		Main.mc.displayGuiScreen(new MoveGui(new GuiElement[]{TimerOverlay.element}));
	}

	@Property(type = PropertyType.SWITCH, name = "FPS Overlay", description = "Enable Frames Per Second Overlay", category = "Overlays", subcategory = "Game Settings")
	public boolean fps = false;

	@Property(type = PropertyType.SELECTOR, name = "Custom Kuudra Profit Overlay",
			description = "Set the Kuudra Profit overlay to be set to a custom position. Note: Custom will take the position of the Container overlay",
			category = "Kuudra", subcategory = "Profit", options = {"Off", "Next to GUI", "Custom"})
	public int customProfitOverlay = 1;


	@Property(type = PropertyType.BUTTON,
			name = "Edit FPS Overlay",
			description = "Edit GUI Location for FPS Overlay",
			category = "Overlays",
			subcategory = "Game Settings", placeholder = "Edit")
	@SuppressWarnings("unused")
	public void MoveFPSOverlay() {
		Main.mc.displayGuiScreen(new MoveGui(new GuiElement[]{FPSOverlay.element}));
		Main.display = null;
	}

	@Property(type = PropertyType.SWITCH,
			name = "TPS Overlay",
			description = "Enable Server TPS overlay",
			category = "Overlays",
			subcategory = "Game Settings")
	public boolean tps = false;
	@Property(type = PropertyType.BUTTON,
			name = "Edit TPS Overlay",
			description = "Edit GUI Location for TPS Overlay",
			category = "Overlays",
			subcategory = "Game Settings", placeholder = "Edit")
	@SuppressWarnings("unused")
	public void MoveTPSOverlay() {
		Main.mc.displayGuiScreen(new MoveGui(new GuiElement[]{TPSOverlay.element}));
		Main.display = null;
	}

	@Property(type = PropertyType.SWITCH, name = "Send Kuudra Chest Data", description = "Sends data for any kuudra chest you buy with a key for future features such as better pricing system and notifying when to sell for best value.", category = "General", subcategory = "General")
	public boolean sendProfitData = true;

	@Property(type = PropertyType.SLIDER, name = "Armor/Equipment Minimum Tier", description = "Minimum tier to consider for valuing armor/equipment and showing them in /ap and /ep. 0 will check for exact match", category = "General", subcategory = "Kuudra Pricing", max = 5)
	public int minArmorTier = 0;
	@Property(type = PropertyType.SLIDER, name = "Shard Minimum Tier", description = "Minimum tier to consider for valuing shards and showing them in /ap. 0 will check for exact match", category = "General", subcategory = "Kuudra Pricing", max = 3)
	public int minShardTier = 0;

	@Property(type = PropertyType.SWITCH, name = "Value Soulbound ARMOR", description = "Whether or not to value starred/soulbound kuudra armor.", category = "General", subcategory = "Kuudra Pricing")
	public boolean valueStarredArmor = true;
	@Property(type = PropertyType.SWITCH, name = "Value Attribute Shards", description = "Whether or not to value shards in Kuudra chests.", category = "General", subcategory = "Kuudra Pricing")
	public boolean valueShards = true;

	@Property(type = PropertyType.SWITCH, name = "Show LBIN Overlay", description = "Whether or not to show the LBIN overlay for items whose attributes are not worth more than LBIN.", category = "General", subcategory = "Attribute Overlay")
	public boolean showLbinOverlay = true;

	@Property(type = PropertyType.TEXT, name = "Attributes to Prioritize", description = "If the item is not a godroll and one attribute is from this list, it will value it over the other attribute no matter what. Separate multiple by _", category = "General", subcategory = "Kuudra Pricing")
	public String priorityAttributes = "";

	@Property(type = PropertyType.TEXT, name = "Excluded Attributes (Armor)", description = "Attributes here will not be valued in any circumstance for armor pieces.", category = "General", subcategory = "Kuudra Pricing")
	public String attributesToExcludeArmor = "experience, arachno_resistance, blazing_resistance, undead_resistance, life_regeneration, ender_resistance";

	@Property(type = PropertyType.TEXT, name = "Excluded Attributes (Equipment)", description = "Attributes here will not be valued in any circumstance for equipment.", category = "General", subcategory = "Kuudra Pricing")
	public String attributesToExcludeEquip = "experience, arachno_resistance, blazing_resistance, undead_resistance, life_regeneration, ender_resistance";

	@Property(type = PropertyType.SWITCH, name = "Show Player Info", description = "Gives a summary on players joining kuudra parties.", category = "General", subcategory = "Party Finder")
	public boolean showKuudraPlayerInfo = false;

	@Property(type = PropertyType.SWITCH, name = "Show Own Player Info", description = "Shows your own player info when you join a party finder party.", category = "General", subcategory = "Party Finder")
	public boolean showOwnPlayerInfo = true;

	@Property(type = PropertyType.TEXT, name = "Minimum Kuudra Level", description = "Set a minimum kuudra level to allow people into party, players below this will be instakicked.", category = "General", subcategory = "Party Finder")
	public String minKuudraLevel = "";

	@Property(type = PropertyType.TEXT, name = "Minimum Magical Power", description = "Set a minimum magical power to allow people into party, players below this will be instakicked.", category = "General", subcategory = "Party Finder")
	public String minMagicalPower = "";

	@Property(type = PropertyType.SELECTOR, name = "Minimum Comps (Tier)", description = "Choose what tier Minimum Comps config value should check for.", category = "General", subcategory = "Party Finder", options = {"Basic", "Hot", "Burning", "Fiery", "Infernal"})
	public int minCompsTier = 0;

	@Property(type = PropertyType.TEXT, name = "Minimum Comps", description = "Set a minimum number of completions to allow people into party, players below this will be instakicked.", category = "General", subcategory = "Party Finder")
	public String minComps = "";

	@Property(type = PropertyType.SELECTOR, name = "Minimum Terror Tier", description = "Choose how upgraded the player's terror must be, players will be instakicked if req is not met.", category = "General", subcategory = "Party Finder", options = {"None", "Hot", "Burning", "Fiery", "Infernal"})
	public int minTerrorTier = 0;

	@Property(type = PropertyType.SELECTOR, name = "Minimum Aurora Tier", description = "Choose how upgraded the player's aurora must be, players will be instakicked if req is not met.", category = "General", subcategory = "Party Finder", options = {"None", "Hot", "Burning", "Fiery", "Infernal"})
	public int minAuroraTier = 0;

	@Property(type = PropertyType.SLIDER, name = "Minimum Dominance Level", description = "Set the minimum number of dominance levels a player must have, players below this will be instakicked.", category = "General", subcategory = "Party Finder", max = 80)
	public int minDomLevel = 0;

	@Property(type = PropertyType.SWITCH, name = "Possibly kick API off players", description = "If this is on and Aurora/Terror/Dominance requirement is set, API off players may get instakicked even if they meet requirements.", category = "General", subcategory = "Party Finder")
	public boolean kickAPIoff = false;

	@Property(type = PropertyType.SWITCH, name = "Show Unfinished Supplies Waypoints", description = "temp", category = "General", subcategory = "Kuudra")
	public boolean showSupplyWaypoint = false;
	@Property(type = PropertyType.SWITCH, name = "Show Unfinished Build Waypoints", description = "temp", category = "General", subcategory = "Kuudra")
	public boolean showBuildWaypoint = false;
	@Property(type = PropertyType.SWITCH, name = "TAP Reminder", description = "Warns when you enter a run without TAP.", category = "General", subcategory = "Kuudra")
	public boolean TapWarning = false;
	@Property(type = PropertyType.SWITCH, name = "Fresh Tools Alert", description = "Alert you when fresh tools procs.", category = "General", subcategory = "Kuudra")
	public boolean freshAlert = false;
	@Property(type = PropertyType.SWITCH, name = "Notify party of fresh tools", description = "Allows other SBU users to know when fresh tools procs.", category = "General", subcategory = "Kuudra")
	public boolean freshNotify = false;
	@Property(type = PropertyType.TEXT, name = "Fresh Alert Custom Message", description = "Add a custom message when notifying your party that your fresh tools procced.", category = "General", subcategory = "Kuudra")
	public String freshMessage = "";
	@Property(type = PropertyType.SWITCH, name = "Show Instastun Block", description = "Highlights the block to etherwarp to and the block to mine to instastun easily. Thanks to @Magma_Cao for this.", category = "General", subcategory = "Kuudra")
	public boolean showStunLocation = false;

	@Property(type = PropertyType.SWITCH, name = "Display Kuudra HP on Boss", description = "Shows Kuudra's HP on the magma cube entity.", category = "Kuudra", subcategory = "Boss")
	public boolean showKuudraHP = false;
	@Property(type = PropertyType.SWITCH, name = "Display Kuudra HP in Boss Bar", description = "Shows Kuudra's HP in the boss bar.", category = "Kuudra", subcategory = "Boss")
	public boolean showKuudraBossBar = false;
	@Property(type = PropertyType.SWITCH, name = "Display Kuudra Outline", description = "Creates a box around Kuudra in Phase 4.", category = "Kuudra", subcategory = "Boss")
	public boolean showKuudraOutline = false;
	@Property(type = PropertyType.SWITCH, name = "Draw Line to Kuudra", description = "Draws a line to Kuudra in Phase 4.", category = "Kuudra", subcategory = "Boss")
	public boolean showKuudraLine = false;

	@Property(type = PropertyType.SWITCH, name = "Display Kuudra Location", description = "Says whether Kuudra is FRONT!, BACK!, RIGHT!, or LEFT! in Phase 4.", category = "Kuudra", subcategory = "Boss")
	public boolean showKuudraLocation = false;

	@Property(type = PropertyType.SWITCH, name = "Custom Main Alert Location", description = "If off, main alerts will show in the middle of screen.", category = "Overlays", subcategory = "Main Alerts")
	public boolean mainAlert = false;
	@Property(type = PropertyType.BUTTON, name = "Move Main Alert", description = "Test", category = "Overlays", subcategory = "Main Alerts")
	@SuppressWarnings("unused")
	public void MoveMainAlert() {
		Main.mc.displayGuiScreen(new MoveGui(new GuiElement[]{AlertOverlay.element}));
		Main.display = null;
	}

	@Property(type = PropertyType.SELECTOR, name = "Show Align Timer", description = "Show time till cells alignment runs out", category = "Overlays", subcategory = "Align Timer", options = {"Off", "On", "Kuudra Only", "Phase 4 Only"})
	public int alignTimer = 0;
	@Property(type = PropertyType.BUTTON, name = "Move Align Timer", description = "Test", category = "Overlays", subcategory = "Align Timer")
	@SuppressWarnings("unused")
	public void MoveAlignTimer() {
		if (alignTimer == 0) {
			Main.mc.thePlayer.addChatMessage(new ChatComponentText(EnumChatFormatting.GOLD + "SBU > " + EnumChatFormatting.RED + "You cannot move this since Show Align Timer is off."));
			return;
		}
		Main.mc.displayGuiScreen(new MoveGui(new GuiElement[]{AlignOverlay.element}));
	}

	@Property(type = PropertyType.SWITCH, name = "Ping Overlay", description = "Enable Ping overlay", category = "Overlays", subcategory = "Game Settings")
	public boolean ping = false;
	@Property(type = PropertyType.BUTTON, name = "Edit Ping Overlay", description = "Edit GUI Location for Ping Overlay", category = "Overlays", subcategory = "Game Settings", placeholder = "Edit")
	@SuppressWarnings("unused")
	public void MovePingOverlay() {
		Main.mc.displayGuiScreen(new MoveGui(new GuiElement[]{PingOverlay.element}));
		Main.display = null;
	}



    @Property(type = PropertyType.BUTTON, name = "Edit Overlay Locations", description = "Move every GUI overlay option", category = "Overlays", subcategory = "Locations", placeholder = "Edit")
	@SuppressWarnings("unused")
	public void MoveGuis() {
		Main.mc.displayGuiScreen(new MoveGui(GuiInit.getOverlayLoaded().toArray(new GuiElement[0])));
		Main.display = null;
	}

	@Property(type = PropertyType.SWITCH,
			name = "Show Splits Overlay",
			description = "Show splits",
			category = "Overlays",
			subcategory = "Splits")
	public boolean splitsOverlay = false;
	@Property(type = PropertyType.BUTTON,
			name = "Edit Splits Overlay",
			description = "Edit Splits Overlay GUI Location",
			category = "Overlays",
			subcategory = "Splits", placeholder = "Edit")
	@SuppressWarnings("unused")
	public void MoveSplits() {
		Main.mc.displayGuiScreen(new MoveGui(new GuiElement[]{SplitsOverlay.element}));
		Main.display = null;
	}

	@Property(type = PropertyType.SELECTOR,
			name = "Ragnarok Axe Cooldown",
			description = "Show ragnarok buff timer and cooldown timer",
			category = "Overlays",
			subcategory = "Ragnarok Axe",
			options = {"Off", "On", "Kuudra Only", "Phase 4 Only"})
	public int ragnarokTimer = 0;
	@Property(type = PropertyType.BUTTON, name = "Edit Ragnarok Timer Overlay",
			description = "Edit the GUI location for the raganarock axe timer",
			category = "Overlays",
			subcategory = "Ragnarok Axe", placeholder = "Edit")
	@SuppressWarnings("unused")
	public void MoveRagnarokTimer() {
		if (ragnarokTimer == 0) {
			Main.mc.thePlayer.addChatMessage(new ChatComponentText(EnumChatFormatting.GOLD + "SBU > " + EnumChatFormatting.RED + "You cannot move this since Show Ragnarok Timer is off."));
			return;
		}
		Main.mc.displayGuiScreen(new MoveGui(new GuiElement[]{RagnarokOverlay.element}));
		Main.display = null;
	}

	@Property(type = PropertyType.SELECTOR, name = "Show End Stone Sword Timer", description = "Show end stone sword buff timer", category = "Overlays", subcategory = "End Stone Sword Timer", options = {"Off", "On", "Kuudra Only", "Phase 4 Only"})
	public int endstoneTimer = 0;
	@Property(type = PropertyType.BUTTON, name = "Move End Stone Sword Timer", description = "Test", category = "Overlays", subcategory = "End Stone Sword Timer")
	@SuppressWarnings("unused")
	public void MoveEndstoneTimer() {
		if (endstoneTimer == 0) {
			Main.mc.thePlayer.addChatMessage(new ChatComponentText(EnumChatFormatting.GOLD + "SBU > " + EnumChatFormatting.RED + "You cannot move this since Show End Stone Sword Timer is off."));
			return;
		}
		Main.mc.displayGuiScreen(new MoveGui(new GuiElement[]{EndstoneOverlay.element}));
		Main.display = null;
	}

	@Property(type = PropertyType.SELECTOR, name = "Show Reaper Timer", description = "Show reaper buff timer and cooldown timer", category = "Overlays", subcategory = "Reaper Timer", options = {"Off", "On", "Kuudra Only", "Phase 4 Only"})
	public int reaperTimer = 0;
	@Property(type = PropertyType.BUTTON,
			name = "Edit Reaper Timer",
			description = "Edit GUI Location for Reaper Armor Timer",
			category = "Overlays",
			subcategory = "Reaper Armor", placeholder = "Edit")
	@SuppressWarnings("unused")
	public void MoveReaperTimer() {
		if (reaperTimer == 0) {
			Main.mc.thePlayer.addChatMessage(new ChatComponentText(EnumChatFormatting.GOLD + "SBU > " + EnumChatFormatting.RED + "You cannot move this since Show Reaper Timer is off."));
			return;
		}
		Main.mc.displayGuiScreen(new MoveGui(new GuiElement[]{ReaperOverlay.element}));
		Main.display = null;
	}

	@Property(type = PropertyType.SWITCH,
			name = "Show Run Info",
			description = "Shows information for every phase (Except 3 because idk what to put there)",
			category = "Overlays",
			subcategory = "Run Overview")
	public boolean runInfo = false;
	@Property(type = PropertyType.COLOR,
			name = "Supply Waypoints Color",
			description = "Choose the color for supply waypoints",
			category = "Overlays",
			subcategory = "Kuudra")
	public Color supplyColor = Color.BLUE;

	@Property(type = PropertyType.SWITCH,
			name = "Supply Ender Pearls",
			description = "Show where to ender pearl to get to supply",
			category = "Overlays",
			subcategory = "Kuudra")
	public boolean enderPearl = false;
	@Property(type = PropertyType.COLOR,
			name = "Pearl Waypoint Color",
			description = "Choose the color of pearl waypoints",
			category = "Overlays",
			subcategory = "Kuudra")
	public Color enderPearlColor = Color.RED;

	@Property(type = PropertyType.SWITCH, name = "Kuudra Player Alert", description = "Alerts when all 4 players are not in run.", category = "Kuudra", subcategory = "General")
	public boolean kuudraPlayerAlert = false;
	@Property(type = PropertyType.SWITCH, name = "Dungeon Player Alert", description = "Alerts when all 4 players are not in run.", category = "Dungeons", subcategory = "General")
	public boolean dungeonPlayerAlert = false;

	@Property(type = PropertyType.SWITCH,
			name = "Fishing Overlay",
			description = "Shows fishing overlay",
			category = "Overlays",
			subcategory = "Fishing")
	public boolean fishingOverlay = false;

	@Property(type = PropertyType.BUTTON, name = "Edit Fishing Overlay",
			description = "Edit Fishing Overlay Location",
			category = "Overlays",
			subcategory = "Fishing", placeholder = "Edit")
	@SuppressWarnings("unused")
	public void MoveFishingOverlay() {
		Main.mc.displayGuiScreen(new MoveGui(new GuiElement[]{FishingOverlay.element}));
		Main.display = null;
	}
	@Property(type = PropertyType.SELECTOR,
			name = "Fatal Tempo Overlay",
			description = "Shows Fatal Tempo bonus in Phase 4 of Kuudra",
			category = "Overlays",
			subcategory = "Fatal Tempo",
			options = {"Off", "On", "Kuudra Only", "Phase 4 Only"})
	public int ftOverlay = 0;
	@Property(type = PropertyType.BUTTON,
			name = "Edit Fatal Tempo Overlay",
			description = "Edit Fatal Tempo GUI Location",
			category = "Overlays", subcategory = "Fatal Tempo")
	@SuppressWarnings("unused")
	public void MoveFTOverlay() {
		if (ftOverlay == 0) {
			Main.mc.thePlayer.addChatMessage(new ChatComponentText(EnumChatFormatting.GOLD + "SBU > " + EnumChatFormatting.RED + "You cannot move this since Fatal Tempo Overlay is off."));
			return;
		}
		Main.mc.displayGuiScreen(new MoveGui(new GuiElement[]{FatalTempoOverlay.element}));
		Main.display = null;
	}
	@Property(type = PropertyType.SELECTOR,
			name = "Profit Overlay",
			description = "Shows Coins / Hour and other profit info for Kuudra",
			category = "Overlays",
			subcategory = "Kuudra Profit",
			options = {"Off", "On", "Kuudra Only", "End of Run only"})
	public int profitOverlay = 0;
	@Property(type = PropertyType.BUTTON,
			name = "Move Profit Overlay",
			description = "Test",
			category = "Overlays", subcategory = "Kuudra Profit")
	@SuppressWarnings("unused")
	public void MoveProfitOverlay() {
		if (profitOverlay == 0) {
			Main.mc.thePlayer.addChatMessage(new ChatComponentText(EnumChatFormatting.GOLD + "SBU > " + EnumChatFormatting.RED + "You cannot move this since Profit Overlay is off."));
			return;
		}
		Main.mc.displayGuiScreen(new MoveGui(new GuiElement[]{ProfitOverlay.element}));
		Main.display = null;
	}

	@Property(type = PropertyType.BUTTON,
			name = "Reset Profit Overlay",
			description = "Rest Profit Overlay back to it's original position",
			category = "Overlays",
			subcategory = "Profit",
			placeholder = "Reset")
	@SuppressWarnings("unused")
	public void ResetProfitOverlay() {
		if (profitOverlay == 0) {
			Main.mc.thePlayer.addChatMessage(new ChatComponentText(EnumChatFormatting.GOLD + "SBU > " + EnumChatFormatting.RED + "You cannot move this since Profit Overlay is off."));
			return;
		}
		ProfitOverlay.reset();
		Main.mc.thePlayer.addChatMessage(new ChatComponentText(EnumChatFormatting.GOLD + "SBU > " + EnumChatFormatting.GREEN + "Profit Overlay Data reset."));
	}

	@Property(type = PropertyType.SELECTOR,
			name = "Damage Bonus Indicator",
			description = "Shows an indicator to show whether dominance / lifeline is active.",
			category = "Overlays",
			subcategory = "Damage Bonus",
			options = {"None", "Dominance", "Lifeline"})
	public int damageOverlay = 0;
	@Property(type = PropertyType.BUTTON,
			name = "Edit Damage Overlay",
			description = "Edit GUI location for Damage Bonus overlay",
			category = "Overlays",
			subcategory = "Damage Bonus")
	@SuppressWarnings("unused")
	public void MoveDamageOverlay() {
		if (damageOverlay == 0) {
			Main.mc.thePlayer.addChatMessage(new ChatComponentText(EnumChatFormatting.GOLD + "SBU > " + EnumChatFormatting.RED + "You cannot move this since Damage Bonus Indicator is off."));
			return;
		}
		Main.mc.displayGuiScreen(new MoveGui(new GuiElement[]{DamageOverlay.element}));
		Main.display = null;
	}

	@Property(
			type = PropertyType.SLIDER,
			name = "Required Godroll Price",
			description = "Set the minimum price for an armor/equipment to be considered a god roll (in millions).",
			category = "Kuudra",
			subcategory = "Attributes",
			min = 1,
			max = 300
	)
	public int min_godroll_price = 50;

	@Property(type = PropertyType.SWITCH,
			name = "Display Attribute Overlay",
			description = "Show the best attribute on any attribute item (Will also show if it's a godroll).",
			category = "Overlays",
			subcategory = "Attributes")
	public boolean attribute_overlay = true;

	@Property(type = PropertyType.SELECTOR,
			name = "Combine Helper",
			description = "temp",
			category = "General",
			subcategory = "Other",
			options = {"", "lifeline", "breeze", "speed", "experience", "mana_pool", "life_regeneration", "blazing_resistance", "arachno_resistance", "undead_resistance", "blazing_fortune", "fishing_experience", "double_hook", "infection", "trophy_hunter", "fisherman", "hunter", "fishing_speed", "life_recovery", "ignition", "combo", "attack_speed", "midas_touch", "mana_regeneration", "veteran", "mending", "ender_resistance", "dominance", "ender", "mana_steal", "blazing", "elite", "arachno", "undead", "warrior", "deadeye", "fortitude", "magic_find"})
	public int combineAttribute = 0;

	@Property(
			type = PropertyType.SWITCH,
			name = "Kuudra Profit Overlay",
			description = "Accurate Kuudra Profit Overlay.",
			category = "Overlays",
			subcategory = "Profit"
	)
	public boolean kuudra_overlay = true;



	@Property(type = PropertyType.SELECTOR,
			name = "Container Value Overlay",
			description = "Turn this on to be able to use the keybind (in controls) to show value of all items in any chest (backpack, ender chest, etc.).",
			category = "Overlays",
			subcategory = "Container Value",
			options = {"Off", "Next to GUI", "Custom"})
	public int container_value = 0;
	@Property(type = PropertyType.BUTTON,
			name = "Edit Container Value",
			description = "Edit GUI Location for Container Value",
			category = "Overlays",
			subcategory = "Container Value")
	@SuppressWarnings("unused")
	public void MoveContainerValue() {
		if (container_value == 0) {
			Main.mc.thePlayer.addChatMessage(new ChatComponentText(EnumChatFormatting.GOLD + "SBU > " + EnumChatFormatting.RED + "You cannot move this since Container Value is off."));
			return;
		}
		Main.mc.displayGuiScreen(new MoveGui(new GuiElement[]{ContainerOverlay.element}));
		Main.display = null;
	}
	@Property(type = PropertyType.SWITCH,
			name = "Compact Container Value",
			description = "Compacts text by removing \"Terror\", \"Aurora\", etc. when it doesn't affect value",
			category = "General",
			subcategory = "Containers")
	public boolean compactContainerValue = true;
	@Property(type = PropertyType.SWITCH,
			name = "Sell Shards as Equipment",
			description = "Use Equipment prices to price attribute shards",
			category = "General",
			subcategory = "Containers")
	public boolean shardEquipmentPricing = true;
	@Property(type = PropertyType.SELECTOR,
			name = "Sorting of Container Value",
			description = "Decide how container value display is sorted.",
			category = "General",
			subcategory = "Containers",
			options = {"Descending Price", "Ascending Price", "Alphabetical", "Attribute Tier", "Item Type"})
	public int containerSorting = 0;
	@Property(type = PropertyType.SELECTOR,
			name = "Data Source",
			description = "Decide where the data is gotten from for valuing items.",
			category = "General",
			subcategory = "Containers",
			options = {"Auction House", "Cofl"})
	public int dataSource = 0;

	@Property(
			type = PropertyType.SELECTOR,
			name = "Kuudra Pet Rarity",
			description = "Enter Kuudra Pet Rarity to include the extra essence in calculation",
			category = "Kuudra",
			subcategory = "Profit",
			options = {"Common", "Uncommon", "Rare", "Epic", "Legendary"}
	)
	public int kuudraPetRarity = 0;

	@Property(
			type = PropertyType.SLIDER,
			name = "Kuudra Pet Level",
			description = "Enter Kuudra Pet Level to include the extra essence in calculation",
			category = "Kuudra",
			subcategory = "Profit",
			max = 100
	)
	public int kuudraPetLevel = 0;

	@Property(
			type = PropertyType.SELECTOR,
			name = "Book Valuation",
			description = "Choose whether books are insta-sold/sell offer.\n(Hardened Mana is always insta-sold)",
			category = "Kuudra",
			subcategory = "Profit",
			options = {"Instant Sell", "Sell Offer"}
	)
	public int book_sell_method = 0;

	@Property(
			type = PropertyType.SWITCH,
			name = "Sell Essence",
			description = "Choose whether essence is considered in the value and profit of a chest.",
			category = "Kuudra",
			subcategory = "Profit"
	)
	public boolean considerEssenceValue = true;


  @Property(type = PropertyType.SELECTOR,
			name = "Faction",
			description = "Needed to calculate key cost for kuudra.",
			category = "Kuudra",
			subcategory = "Profit",
			options = {"Mage", "Barbarian", "Cheapest"})
	public int faction = 0;

	@Property(type = PropertyType.SLIDER, name = "Show Chat Waypoints", description = "Select delay before waypoints when coordinates sent in chat disappear. 0 = off", category = "General", subcategory = "General", max = 60)
	public int showWaypoints = 0;
	@Property(type = PropertyType.SWITCH, name = "Remove Selfie Mode", description = "Remove Selfie mode when toggling perspective", category = "General", subcategory = "General")
	public boolean removeSelfie = false;
	@Property(type = PropertyType.SWITCH, name = "Supply Safe Spots", description = "Show safe spots", category = "Kuudra", subcategory = "Instance QoL")
	public boolean safeSpots = false;
	@Property(type = PropertyType.SWITCH, name = "Allow Custom Emotes", description = "Allow you to use MVP++/Gifting emotes without the requirement", category = "General", subcategory = "General")
	public boolean customEmotes = false;

	@Property(type = PropertyType.SWITCH, name = "Auto Kick Players", description = "Auto kick players based on their armor, kuudra related stats, etc.", category = "Kuudra", subcategory = "Party Finder")
	public boolean autoKick = false;
	private void checkFolderExists() {
		Path directory = Paths.get(configFolder);
		if (!Files.exists(directory)) {
			try {
				Logger.warn("Created directory!", "\n", "Potentially config issue being initialized twice or first time using mod.");
				Files.createDirectory(directory);
			}
			catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public Config() {
		super(new File(Config.configFolder + "config.toml"), "SkyblockUtils", new JVMAnnotationPropertyCollector(),
				new ConfigSorting());

		this.checkFolderExists();
		this.CONFIG_FILE = new File(Config.configFolder + "config.toml");
		this.initialize();

		try {
			addDependency("MoveWitherImpact", "brokenHyp"); // it does exist
			addDependency("MoveSplits", "splitsOverlay");
			addDependency("MoveFishingOverlay", "fishingOverlay");
			addDependency("MoveMainAlert", "mainAlert");
			addDependency("MoveFPSOverlay", "fps");
			addDependency("MoveTPSOverlay", "tps");
			addDependency("MovePingOverlay", "ping");
			addDependency("minKuudraLevel", "autoKick");
			addDependency("minMagicalPower", "autoKick");
			addDependency("minCompsTier", "autoKick");
			addDependency("minComps", "autoKick");
			addDependency("minTerrorTier", "autoKick");
			addDependency("minAuroraTier", "autoKick");
			addDependency("minDomLevel", "autoKick");
			addDependency("kickAPIoff", "autoKick");
		} catch (Exception e) {
			Logger.error(e);
		}
	}


	static {
		Config.configFolder = "config/SkyblockUtils/";
	}

	public static class ConfigSorting extends SortingBehavior {
		@NotNull
		@Override
		public Comparator<Category> getCategoryComparator() {
			return Comparator.comparingInt(o -> this.categories.indexOf(o.getName()));

		}

		private final List<String> categories = Arrays.asList("General", "Kuudra", "Dungeons", "Crimson Isles", "Overlays");
		@NotNull
		@Override
		public Comparator<? super Map.Entry<String, ? extends List<PropertyData>>> getSubcategoryComparator() {
			return Comparator.comparing(entry -> ("Locations".equals(entry.getKey()) || "Player Stats".equals(entry.getKey())) ? "" : entry.getKey());
		}
	}
}