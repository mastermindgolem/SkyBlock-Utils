package com.golem.skyblockutils;

import com.golem.skyblockutils.models.Overlay.TextOverlay.*;
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

public class Config extends Vigilant
{
	public static String configFolder;
	public File CONFIG_FILE;
	@Property(
		type = PropertyType.SWITCH,
		name = "Test move overlay",
		description = "Dw abt this",
		category = "General",
		subcategory = "General"
	)
	public boolean testGui = true;

	@Property(
			type = PropertyType.SLIDER,
			name = "Time between AH Checks",
			description = "Time to wait between updating AH data. (0 turns it off)",
			category = "General",
			subcategory = "General",
			min = 5,
			max = 30
	)
	public int time_between_checks = 5;

	@Property(type = PropertyType.SLIDER, name = "Armor/Equipment Minimum Tier", description = "Minimum tier to consider for valuing armor/equipment and showing them in /ap and /ep. 0 will check for exact match", category = "General", subcategory = "Kuudra Pricing", max = 5)
	public int minArmorTier = 0;
	@Property(type = PropertyType.SLIDER, name = "Shard Minimum Tier", description = "Minimum tier to consider for valuing shards and showing them in /ap. 0 will check for exact match", category = "General", subcategory = "Kuudra Pricing", max = 3)
	public int minShardTier = 0;


/*	@Property(
			type = PropertyType.SLIDER,
			name = "Minimum Shard Tier",
			description = "Minimum tier SHARD to consider for finding cheapest price per tier in /ap and /ep when tier is not specified.",
			category = "General",
			subcategory = "Kuudra Pricing",
			min = 1,
			max = 3
	)
	public int min_armor_tier = 1;

	@Property(
		type = PropertyType.SLIDER,
		name = "Minimum Armour Tier",
		description = "Minimum tier ARMOUR or EQUIPMENT to consider for finding cheapest price per tier in /ap and /ep when tier is not specified.",
		category = "General",
		subcategory = "Kuudra Pricing",
		min = 1,
		max = 10
	)
	public int min_shard_tier = 1;*/

	@Property(type = PropertyType.SWITCH, name = "Value Soulbound ARMOR", description = "Whether or not to value starred/soulbound kuudra armor.", category = "General", subcategory = "Kuudra Pricing")
	public boolean valueStarredArmor = true;

	@Property(type = PropertyType.SWITCH, name = "Show LBIN Overlay", description = "Whether or not to show the LBIN overlay for items whose attributes are not worth more than LBIN.", category = "General", subcategory = "Attribute Overlay")
	public boolean showLbinOverlay = true;

	@Property(type = PropertyType.TEXT, name = "Attributes to Prioritize", description = "If the item is not a godroll and one attribute is from this list, it will value it over the other attribute no matter what. Separate multiple by _", category = "General", subcategory = "Kuudra Pricing")
	public String priorityAttributes = "";

	@Property(type = PropertyType.TEXT, name = "Excluded Attributes", description = "Attributes here will not be valued in any circumstance.", category = "General", subcategory = "Kuudra Pricing")
	public String attributesToExclude = "experience, arachno_resistance, blazing_resistance, undead_resistance, life_regeneration, ender_resistance";

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

	@Property(type = PropertyType.BUTTON, name = "Move Main Alerts", description = "Press this button to decide where big alerts like Hype Broken show", category = "Overlays", subcategory = "Main Alerts")
	@SuppressWarnings("unused")
	public void MoveAlertLocation() {
		Main.mc.displayGuiScreen(new MoveGui(AlertOverlay.element));
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
		Main.mc.displayGuiScreen(new MoveGui(AlignOverlay.element));
		Main.display = null;
	}

	@Property(type = PropertyType.SWITCH, name = "Show Splits Overlay", description = "Show splits", category = "Overlays", subcategory = "Splits Overlay")
	public boolean splitsOverlay = false;
	@Property(type = PropertyType.BUTTON, name = "Move Splits Overlay", description = "Test", category = "Overlays", subcategory = "Splits Overlay")
	@SuppressWarnings("unused")
	public void MoveSplits() {
		Main.mc.displayGuiScreen(new MoveGui(SplitsOverlay.element));
		Main.display = null;
	}

	@Property(type = PropertyType.SELECTOR, name = "Show Ragnarok Timer", description = "Show ragnarok buff timer and cooldown timer", category = "Overlays", subcategory = "Ragnarok Timer", options = {"Off", "On", "Kuudra Only", "Phase 4 Only"})
	public int ragnarokTimer = 0;
	@Property(type = PropertyType.BUTTON, name = "Move Ragnarok Timer", description = "Test", category = "Overlays", subcategory = "Ragnarok Timer")
	@SuppressWarnings("unused")
	public void MoveRagnarokTimer() {
		if (ragnarokTimer == 0) {
			Main.mc.thePlayer.addChatMessage(new ChatComponentText(EnumChatFormatting.GOLD + "SBU > " + EnumChatFormatting.RED + "You cannot move this since Show Ragnarok Timer is off."));
			return;
		}
		Main.mc.displayGuiScreen(new MoveGui(RagnarokOverlay.element));
		Main.display = null;
	}

	@Property(type = PropertyType.SELECTOR, name = "Show Reaper Timer", description = "Show reaper buff timer and cooldown timer", category = "Overlays", subcategory = "Reaper Timer", options = {"Off", "On", "Kuudra Only", "Phase 4 Only"})
	public int reaperTimer = 0;
	@Property(type = PropertyType.BUTTON, name = "Move Reaper Timer", description = "Test", category = "Overlays", subcategory = "Reaper Timer")
	@SuppressWarnings("unused")
	public void MoveReaperTimer() {
		if (reaperTimer == 0) {
			Main.mc.thePlayer.addChatMessage(new ChatComponentText(EnumChatFormatting.GOLD + "SBU > " + EnumChatFormatting.RED + "You cannot move this since Show Reaper Timer is off."));
			return;
		}
		Main.mc.displayGuiScreen(new MoveGui(ReaperOverlay.element));
		Main.display = null;
	}

	@Property(type = PropertyType.SWITCH, name = "Show Supply Info", description = "Show where supplies are and who got how many supplies", category = "Overlays", subcategory = "Supply Info")
	public boolean supplyInfo = false;
	@Property(type = PropertyType.COLOR, name = "Supply Waypoints Color", description = "Choose the color for supply waypoints", category = "Overlays", subcategory = "Supply Info")
	public Color supplyColor = Color.BLUE;
	@Property(type = PropertyType.BUTTON, name = "Move Supply Info", description = "Test", category = "Overlays", subcategory = "Supply Info")
	@SuppressWarnings("unused")
	public void MoveSupplyInfo() {
		Main.mc.displayGuiScreen(new MoveGui(CratesOverlay.element));
		Main.display = null;
	}

	@Property(type = PropertyType.SWITCH, name = "Supply Ender Pearls", description = "Show where to ender pearl to get to supply", category = "Overlays", subcategory = "Supply Info")
	public boolean enderPearl = false;
	@Property(type = PropertyType.COLOR, name = "Pearl Waypoint Color", description = "Choose the color of pearl waypoints", category = "Overlays", subcategory = "Supply Info")
	public Color enderPearlColor = Color.RED;

	@Property(type = PropertyType.SWITCH, name = "Broken Wither Impact Notification", description = "Notifies when wither impact is broken, and also shows champion XP", category = "Overlays", subcategory = "Broken Wither Impact")
	public boolean brokenHyp = false;
	@Property(type = PropertyType.BUTTON, name = "Move Broken Wither Impact", description = "Test", category = "Overlays", subcategory = "Broken Wither Impact")
	@SuppressWarnings("unused")
	public void MoveWitherImpact() {
		Main.mc.displayGuiScreen(new MoveGui(ChampionOverlay.element));
		Main.display = null;
	}

	@Property(type = PropertyType.SWITCH, name = "Fishing Overlay", description = "Notifies when wither impact is broken, and also shows champion XP", category = "Overlays", subcategory = "Fishing")
	public boolean fishingOverlay = false;
	@Property(type = PropertyType.BUTTON, name = "Move Fishing Overlay", description = "Test", category = "Overlays", subcategory = "Fishing")
	@SuppressWarnings("unused")
	public void MoveFishingOverlay() {
		Main.mc.displayGuiScreen(new MoveGui(FishingOverlay.element));
		Main.display = null;
	}
	@Property(type = PropertyType.SELECTOR, name = "Fatal Tempo Overlay", description = "Shows Fatal Tempo bonus in Phase 4 of Kuudra", category = "Overlays", subcategory = "Fatal Tempo", options = {"Off", "On", "Kuudra Only", "Phase 4 Only"})
	public int ftOverlay = 0;
	@Property(type = PropertyType.BUTTON, name = "Move Fatal Tempo Overlay", description = "Test", category = "Overlays", subcategory = "Fatal Tempo")
	@SuppressWarnings("unused")
	public void MoveFTOverlay() {
		if (ftOverlay == 0) {
			Main.mc.thePlayer.addChatMessage(new ChatComponentText(EnumChatFormatting.GOLD + "SBU > " + EnumChatFormatting.RED + "You cannot move this since Fatal Tempo Overlay is off."));
			return;
		}
		Main.mc.displayGuiScreen(new MoveGui(FatalTempoOverlay.element));
		Main.display = null;
	}
	@Property(type = PropertyType.SELECTOR, name = "Profit Overlay", description = "Shows Coins / Hour and other profit info for Kuudra", category = "Overlays", subcategory = "Profit", options = {"Off", "On", "Kuudra Only", "End of Run only"})
	public int profitOverlay = 0;
	@Property(type = PropertyType.BUTTON, name = "Move Profit Overlay", description = "Test", category = "Overlays", subcategory = "Profit")
	@SuppressWarnings("unused")
	public void MoveProfitOverlay() {
		if (profitOverlay == 0) {
			Main.mc.thePlayer.addChatMessage(new ChatComponentText(EnumChatFormatting.GOLD + "SBU > " + EnumChatFormatting.RED + "You cannot move this since Profit Overlay is off."));
			return;
		}
		Main.mc.displayGuiScreen(new MoveGui(ProfitOverlay.element));
		Main.display = null;
	}

	@Property(type = PropertyType.BUTTON, name = "Reset Profit Overlay", description = "Test", category = "Overlays", subcategory = "Profit")
	@SuppressWarnings("unused")
	public void ResetProfitOverlay() {
		if (profitOverlay == 0) {
			Main.mc.thePlayer.addChatMessage(new ChatComponentText(EnumChatFormatting.GOLD + "SBU > " + EnumChatFormatting.RED + "You cannot move this since Profit Overlay is off."));
			return;
		}
		ProfitOverlay.reset();
		Main.mc.thePlayer.addChatMessage(new ChatComponentText(EnumChatFormatting.GOLD + "SBU > " + EnumChatFormatting.GREEN + "Profit Overlay Data reset."));
	}

	@Property(type = PropertyType.SELECTOR, name = "Damage Bonus Indicator", description = "Shows an indicator to show whether dominance / lifeline is active.", category = "Overlays", subcategory = "Damage Bonus", options = {"None", "Dominance", "Lifeline"})
	public int damageOverlay = 0;
	@Property(type = PropertyType.BUTTON, name = "Move Damage Overlay", description = "Test", category = "Overlays", subcategory = "Damage Bonus")
	@SuppressWarnings("unused")
	public void MoveDamageOverlay() {
		if (damageOverlay == 0) {
			Main.mc.thePlayer.addChatMessage(new ChatComponentText(EnumChatFormatting.GOLD + "SBU > " + EnumChatFormatting.RED + "You cannot move this since Damage Bonus Indicator is off."));
			return;
		}
		Main.mc.displayGuiScreen(new MoveGui(DamageOverlay.element));
		Main.display = null;
	}



	@Property(
			type = PropertyType.SLIDER,
			name = "Min. Godroll Price",
			description = "Minimum Price for a combo to be considered a godroll (in millions).",
			category = "General",
			subcategory = "Attribute Overlay",
			min = 1,
			max = 300
	)
	public int min_godroll_price = 50;

	@Property(type = PropertyType.SWITCH, name = "Display Attribute Overlay", description = "Show the best attribute on any attribute item (Will also show if it's a godroll).", category = "General", subcategory = "Attribute Overlay")
	public boolean attribute_overlay = true;

	@Property(type = PropertyType.SELECTOR, name = "Combine Helper", description = "temp", category = "General", subcategory = "Other", options = {"", "lifeline", "breeze", "speed", "experience", "mana_pool", "life_regeneration", "blazing_resistance", "arachno_resistance", "undead_resistance", "blazing_fortune", "fishing_experience", "double_hook", "infection", "trophy_hunter", "fisherman", "hunter", "fishing_speed", "life_recovery", "ignition", "combo", "attack_speed", "midas_touch", "mana_regeneration", "veteran", "mending", "ender_resistance", "dominance", "ender", "mana_steal", "blazing", "elite", "arachno", "undead", "warrior", "deadeye", "fortitude", "magic_find"})
	public int combineAttribute = 0;

	@Property(
			type = PropertyType.SWITCH,
			name = "Display Kuudra Overlay",
			description = "Accurate Kuudra Profit Overlay.",
			category = "General",
			subcategory = "Kuudra Profit Overlay"
	)
	public boolean kuudra_overlay = true;

	@Property(type = PropertyType.SELECTOR, name = "Container Value", description = "Turn this on to be able to use the keybind (in controls) to show value of all items in any chest (backpack, ender chest, etc.).", category = "Overlays", subcategory = "Container Value", options = {"Off", "Next to GUI", "Custom"})
	public int container_value = 0;
	@Property(type = PropertyType.SWITCH, name = "Compact Container Value", description = "Compacts text by removing \"Terror\", \"Aurora\", etc. when it doesn't affect value", category = "Overlays", subcategory = "Container Value")
	public boolean compactContainerValue = true;
	@Property(type = PropertyType.SWITCH, name = "Sell Shards as Equipment", description = "Use Equipment prices to price attribute shards", category = "Overlays", subcategory = "Container Value")
	public boolean shardEquipmentPricing = true;
	@Property(type = PropertyType.SELECTOR, name = "Sorting of Container Value", description = "Decide how container value display is sorted.", category = "Overlays", subcategory = "Container Value", options = {"Descending Price", "Ascending Price", "Alphabetical", "Attribute Tier", "Item Type"})
	public int containerSorting = 0;
	@Property(type = PropertyType.SELECTOR, name = "Data Source", description = "Decide where the data is gotten from for valuing items.", category = "Overlays", subcategory = "Container Value", options = {"Auction House", "Cofl"})
	public int dataSource = 0;
	@Property(type = PropertyType.BUTTON, name = "Move Container Value", description = "Test", category = "Overlays", subcategory = "Container Value")
	@SuppressWarnings("unused")
	public void MoveContainerValue() {
		if (container_value == 0) {
			Main.mc.thePlayer.addChatMessage(new ChatComponentText(EnumChatFormatting.GOLD + "SBU > " + EnumChatFormatting.RED + "You cannot move this since Container Value is off."));
			return;
		}
		Main.mc.displayGuiScreen(new MoveGui(ContainerOverlay.element));
		Main.display = null;
	}

	@Property(
			type = PropertyType.SELECTOR,
			name = "Kuudra Pet Rarity",
			description = "Enter Kuudra Pet Rarity to include the extra essence in calculation",
			category = "General",
			subcategory = "Kuudra Profit Overlay",
			options = {"Common", "Uncommon", "Rare", "Epic", "Legendary"}
	)
	public int kuudraPetRarity = 0;

	@Property(
			type = PropertyType.SLIDER,
			name = "Kuudra Pet Level",
			description = "Enter Kuudra Pet Level to include the extra essence in calculation",
			category = "General",
			subcategory = "Kuudra Profit Overlay",
			max = 100
	)
	public int kuudraPetLevel = 0;

	@Property(
			type = PropertyType.SELECTOR,
			name = "Book Valuation",
			description = "Choose whether books are insta-sold/sell offer.\n(Hardened Mana is always insta-sold)",
			category = "General",
			subcategory = "Kuudra Profit Overlay",
			options = {"Instant Sell", "Sell Offer"}
	)
	public int book_sell_method = 0;

	@Property(
			type = PropertyType.SWITCH,
			name = "Sell Essence",
			description = "Choose whether essence is considered in the value and profit of a chest.",
			category = "General",
			subcategory = "Kuudra Profit Overlay"
	)
	public boolean considerEssenceValue = true;

	@Property(type = PropertyType.SELECTOR, name = "Faction", description = "Needed to calculate key cost for kuudra.", category = "General", options = {"Mage", "Barbarian"}, subcategory = "Kuudra Profit Overlay")
	public int faction = 0;

	@Property(type = PropertyType.SELECTOR, name = "Auto-Updater", description = "Notifies when a new mod version is available.", category = "Updater", options = {"Off", "Full Release Only", "Full & Beta Release"}, subcategory = "Updater")
	public int autoUpdater = 0;

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
			addDependency("MoveWitherImpact", "brokenHyp");
			addDependency("MoveSupplyInfo", "supplyInfo");
			addDependency("MoveSplits", "splitsOverlay");
			addDependency("MoveFishingOverlay", "fishingOverlay");
		} catch (Exception ignored) {}
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

		private final List<String> categories = Arrays.asList("General", "Overlays", "Updater"); //
	}
}