package com.golem.skyblockutils.utils;

import com.golem.skyblockutils.command.commands.StatCommand;
import com.golem.skyblockutils.models.AttributePrice;
import logger.Logger;

import static com.golem.skyblockutils.Main.*;

public class AuctionHouse {
	public static long lastKnownLastUpdated = 0;
	public static boolean isRunning = false;


	public void run(){
		while(true) {
			try {
				if (configFile.time_between_checks == 0) continue;
				long sleepTime = lastKnownLastUpdated + 60000L * configFile.time_between_checks - System.currentTimeMillis() + 10000L;
				if (sleepTime > 0) {
					try {
						Logger.debug("Sleep Time:" + sleepTime);
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
					} catch (NullPointerException ignored) {
						Logger.error("Error fetching auctions");
					}
				}).start();
				lastKnownLastUpdated = System.currentTimeMillis();




			} catch (Exception ignored) {}
		}
	}
}