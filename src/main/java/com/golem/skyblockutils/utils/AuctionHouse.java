package com.golem.skyblockutils.utils;

import com.golem.skyblockutils.models.AttributePrice;
import logger.Logger;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;

import static com.golem.skyblockutils.Main.*;
import static com.golem.skyblockutils.models.AttributePrice.AttributePrices;

public class AuctionHouse {
	public static long lastKnownLastUpdated = 0;
	public static boolean isRunning = false;
	public static long lastErrorMessage = 0;


	public void run(){
		while(true) {
			try {
				if (configFile.time_between_checks == 0) continue;
				long sleepTime = lastKnownLastUpdated + 60000L * configFile.time_between_checks - System.currentTimeMillis() + 10000L;
				if (sleepTime > 0) {
					try {
						Logger.info("Sleep Time: " + sleepTime);
						Thread.sleep(sleepTime);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				String urlString = "https://mastermindgolem.pythonanywhere.com/?auctions=mb";
				new Thread(() -> {
					try {
						auctions = new RequestUtil().sendGetRequest(urlString).getJsonAsObject().get("auctions").getAsJsonArray();
						AttributePrice.checkAuctions(auctions);
						bazaar = new RequestUtil().sendGetRequest("https://api.hypixel.net/skyblock/bazaar").getJsonAsObject();
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
		System.out.println("Checking " + auctions.size() + " auctions");
		System.out.println(AttributePrices.keySet());
		if (auctions.size() == 0) {
			final IChatComponent msg = new ChatComponentText(EnumChatFormatting.RED + "Auctions not checked yet. If you have logged in more than 5 minutes ago, contact golem.");
			mc.thePlayer.addChatMessage(msg);
			new Thread(() -> {
				String urlString = "https://mastermindgolem.pythonanywhere.com/?auctions=mb";
				auctions = new RequestUtil().sendGetRequest(urlString).getJsonAsObject().get("auctions").getAsJsonArray();
				AttributePrice.checkAuctions(auctions);
				bazaar = new RequestUtil().sendGetRequest("https://api.hypixel.net/skyblock/bazaar").getJsonAsObject();
				mc.thePlayer.addChatMessage(new ChatComponentText(EnumChatFormatting.GREEN + "Auctions updated"));
			}).start();
			AuctionHouse.lastKnownLastUpdated = System.currentTimeMillis();
			return true;
		}
		return false;
	}
}