package com.golem.skyblockutils;

import com.golem.skyblockutils.init.CommandInit;
import com.golem.skyblockutils.init.EventInit;
import com.golem.skyblockutils.init.HelpInit;
import com.golem.skyblockutils.init.KeybindsInit;
import com.golem.skyblockutils.models.CustomItem;
import com.golem.skyblockutils.models.gui.GuiElement;
import com.golem.skyblockutils.utils.AuctionHouse;
import com.golem.skyblockutils.utils.TimeHelper;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import logger.Logger;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;

import java.io.File;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;

@Mod(modid = Main.MODID, version = Main.VERSION, clientSideOnly = true, acceptedMinecraftVersions = "[1.8.9]")
public class Main
{
	public static final String MODID = "SkyblockUtils";
	private static final String[] c = new String[]{"k", "m", "b"};
	public static HashMap<String, CustomItem> customItems = new HashMap<>();
	private AuctionHouse all_auctions;
	public static JsonArray auctions = new JsonArray();
	public static JsonObject bazaar = new JsonObject();
	public static final String VERSION = "1.1.1";
	public static Config configFile;
	public static GuiScreen display;
	public static final Minecraft mc;
	public static final TimeHelper time = new TimeHelper();
	public static List<GuiElement> StaticPosition;
	public static File jarFile = null;

	public static PersistentData persistentData = new PersistentData();

	public static String modDir = "";

	@Mod.EventHandler
	public void preInit(final FMLPreInitializationEvent event) {
		time.setLastMS();
		jarFile = event.getSourceFile();
		modDir = event.getModConfigurationDirectory().getAbsolutePath().replace("/config", "");
	}
	@Mod.EventHandler
	public void init(final FMLInitializationEvent event) {
		// Note: HelpInit.registerHelp() needs to be called last for proper initialization
		KeybindsInit.registerKeyBinds();
		CommandInit.registerCommands();
		EventInit.registerEvents();
		HelpInit.registerHelp();

	}

	@Mod.EventHandler
	public void post(FMLPostInitializationEvent event) {
		configFile = new Config();
		PersistentData.load();
		Logger.debug("Successfully loaded data");
		EventInit.registerOverlays();
		Logger.info("Time taken to reach post initialization: " + time.getDelay());
		if (!AuctionHouse.isRunning) {
			AuctionHouse.isRunning = true;
			all_auctions = new AuctionHouse();
			new Thread(getAuctions()::run, "fetch-auctions").start();
		}

	}

	@SubscribeEvent
	public void join(FMLNetworkEvent.ClientConnectedToServerEvent event) {
	}


	@SubscribeEvent
	public void leave(final PlayerEvent.PlayerLoggedOutEvent e) {
	}

	@SubscribeEvent
	public void tick(final TickEvent.ClientTickEvent event) {
		if (event.phase != TickEvent.Phase.START) {
			return;
		}
		if (Main.display != null) {
			Main.mc.displayGuiScreen(Main.display);
			Main.display = null;
		}
	}

	public static String coolFormat(final double n, final int iteration) {
		final double d = (long) n / 100L / 10.0;
		final boolean isRound = d * 10.0 % 10.0 == 0.0;
		return d < 1000.0 ? (isRound || d > 9.99 ? Integer.valueOf((int) d * 10 / 10) : d + "") + "" + c[iteration] : coolFormat(d, iteration + 1);
	}

	public static String formatNumber(float number) {
		return formatNumber((double) number);
	}

	public static String formatNumber(double number) {
		double thousand = 1000.0;
		double million = 1000000.0;
		double billion = 1000000000.0;

		String prefix = "";

		if (number < 0) {
			prefix = "-";
			number = 0 - number;
		}

		if (number < thousand) {
			return prefix + number;
		} else if (number < million) {
			return prefix + formatWithSuffix(number, thousand, "k");
		} else if (number < billion) {
			return prefix + formatWithSuffix(number, million, "m");
		} else {
			return prefix + formatWithSuffix(number, billion, "b");
		}
	}

	private static String formatWithSuffix(double number, double divisor, String suffix) {
		double quotient = number / divisor;
		return String.format("%.1f%s", quotient, suffix);
	}

	private static String formatWithSuffix(BigInteger number, BigInteger divisor, String suffix) {
		BigInteger[] ans = number.divideAndRemainder(divisor);
		double quotient = ans[0].doubleValue();
		double remainder = ans[1].doubleValue() / divisor.doubleValue();
		double formattedNumber = quotient + remainder;
		return String.format("%.1f%s", formattedNumber, suffix);
	}

	public AuctionHouse getAuctions() {
		return all_auctions;
	}

	static {
		Main.display = null;
		mc = Minecraft.getMinecraft();
	}
}
