package com.golem.skyblockutils;

import com.golem.skyblockutils.init.GuiInit;
import com.golem.skyblockutils.models.gui.GuiPosition;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import logger.Logger;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.Map;

import static com.golem.skyblockutils.Config.stonksFolder;

public class PersistentData implements Serializable {
	public boolean isFirstLoad = true;

	public static Map<String, GuiPosition> positions = new HashMap<>();



	private static final File configFile = new File(stonksFolder, "positions.json");

	public PersistentData() {
		positions = new HashMap<>();
	}

	public PersistentData(Map<String, GuiPosition> positions) {
		PersistentData.positions = positions;
	}

	public static Map<String, GuiPosition> getPositions() {
		return positions;
	}

	public void save() {
		JsonObject json = new JsonObject();
		json.add("Overlays", new Gson().toJsonTree(positions).getAsJsonObject());
		try {
			String jsonData = json.toString();
			Files.write(configFile.toPath(), jsonData.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
		} catch (IOException e) {
			Logger.error("An error occurred while saving the data: " + e.getMessage());
		}
	}

	public static void load() {
		// Implementation for loading data
		try {
			if (configFile.exists()) {
				byte[] jsonData = Files.readAllBytes(configFile.toPath());
				String json = new String(jsonData);
				JsonObject JsonData = new Gson().fromJson(json, JsonObject.class);
				JsonObject OverlayData = (JsonData.has("Overlays") ? JsonData.get("Overlays").getAsJsonObject() : new JsonObject());
				for (Map.Entry<String, JsonElement> entry : OverlayData.entrySet()) {
					if (entry.getValue() instanceof JsonObject) positions.put(entry.getKey(), new GuiPosition(entry.getValue().getAsJsonObject()));
				}
				Main.StaticPosition = GuiInit.getOverlayLoaded();
				return;
			}
		} catch (IOException e) {
			Logger.error("An error occurred while loading the data: " + e.getMessage());
		}
		new PersistentData();
	}
}
