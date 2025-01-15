package com.golem.skyblockutils.utils;

import com.golem.skyblockutils.NoteForDecompilers;
import com.golem.skyblockutils.features.General.Elite500;
import com.golem.skyblockutils.features.KuudraOverlay;
import com.golem.skyblockutils.models.AttributePrice;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import logger.Logger;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;

import java.util.ArrayList;
import java.util.List;

import static com.golem.skyblockutils.Main.*;

public class AuctionHouse {
	public static long lastKnownLastUpdated = 0;
	public static boolean isRunning = false;
	public static long lastErrorMessage = 0;
	public static double ESSENCE_VALUE = 0;
	private RequestUtil requestUtil = new RequestUtil();


	@NoteForDecompilers("a session variable does not mean i am trying to rat you. I am simply getting the player's info.")
	public void run(){
		while(true) {
			try {
				if (config.getConfig().pricingCategory.timeBetweenChecks == 0) continue;
				if (mc == null || mc.getSession() == null || mc.getSession().getPlayerID() == null) continue;
				long sleepTime = lastKnownLastUpdated + 60000L * config.getConfig().pricingCategory.timeBetweenChecks - System.currentTimeMillis() + 10000L;
				if (sleepTime > 0) {
					try {
						Logger.info("Sleep Time: " + sleepTime);
						Thread.sleep(sleepTime);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				String urlString = "https://mastermindgolem.pythonanywhere.com/?auctions=mb";
				String skyHelperPrices = "https://raw.githubusercontent.com/SkyHelperBot/Prices/master/prices.json";
				new Thread(() -> {
					try {
						JsonObject result = requestUtil.sendGetRequest(urlString).getJsonAsObject();
						auctions = result.get("auctions").getAsJsonArray();
						if (result.has("elite")) {
							JsonArray temp = result.get("elite").getAsJsonArray();

							List<String> newElite = new ArrayList<>();
							for (JsonElement e : temp) newElite.add(e.getAsString());
							Elite500.elite500 = newElite;
						}

						if (result.has("expected_profit")) {
							JsonArray temp = result.get("expected_profit").getAsJsonArray();
							KuudraOverlay.expectedProfit.clear();
							for (JsonElement o : temp) KuudraOverlay.expectedProfit.add(o.getAsInt());
						}

						AttributePrice.checkAuctions(auctions);
						bazaar = requestUtil.sendGetRequest("https://api.hypixel.net/skyblock/bazaar").getJsonAsObject();
						int buy_price = 1000;
						int sell_price = 1000;
						try {
							buy_price = bazaar.get("products").getAsJsonObject().get("ESSENCE_CRIMSON").getAsJsonObject().get("sell_summary").getAsJsonArray().get(0).getAsJsonObject().get("pricePerUnit").getAsInt();
							sell_price = bazaar.get("products").getAsJsonObject().get("ESSENCE_CRIMSON").getAsJsonObject().get("buy_summary").getAsJsonArray().get(0).getAsJsonObject().get("pricePerUnit").getAsInt();
						} catch (Exception ignored) {}
						ESSENCE_VALUE = (buy_price + sell_price) / 2F;

						AttributePrice.processSkyHelperPrices(requestUtil.sendGetRequest(skyHelperPrices).getJsonAsObject());

						Logger.info("Fetched auctions!");
					} catch (NullPointerException ignored) {
						Logger.error("Nom");
						Logger.error("Error fetching auctions");
					}
				}).start();
				lastKnownLastUpdated = System.currentTimeMillis();

			} catch (Exception ignored) {}
		}
	}

	public static boolean CheckIfAuctionsSearched() {
		if (auctions.size() == 0) {
			final IChatComponent msg = new ChatComponentText(EnumChatFormatting.RED + "Auctions not checked yet. If you have logged in more than 5 minutes ago, contact golem.");
			mc.thePlayer.addChatMessage(msg);
			new Thread(() -> {
				String urlString = "https://mastermindgolem.pythonanywhere.com/?auctions=mb";
				auctions = new RequestUtil().sendGetRequest(urlString).getJsonAsObject().get("auctions").getAsJsonArray();
				AttributePrice.checkAuctions(auctions);
				bazaar = new RequestUtil().sendGetRequest("https://api.hypixel.net/skyblock/bazaar").getJsonAsObject();
				int buy_price = 1000;
				int sell_price = 1000;
				try {
					buy_price = bazaar.get("products").getAsJsonObject().get("ESSENCE_CRIMSON").getAsJsonObject().get("sell_summary").getAsJsonArray().get(0).getAsJsonObject().get("pricePerUnit").getAsInt();
					sell_price = bazaar.get("products").getAsJsonObject().get("ESSENCE_CRIMSON").getAsJsonObject().get("buy_summary").getAsJsonArray().get(0).getAsJsonObject().get("pricePerUnit").getAsInt();
				} catch (Exception ignored) {}
				ESSENCE_VALUE = (buy_price + sell_price) / 2F;
				mc.thePlayer.addChatMessage(new ChatComponentText(EnumChatFormatting.GREEN + "Auctions updated"));
			}).start();
			AuctionHouse.lastKnownLastUpdated = System.currentTimeMillis();
			return true;
		}
		return false;
	}
}