package com.golem.skyblockutils;

import com.golem.skyblockutils.models.gui.GuiPosition;
import com.google.gson.Gson;
import logger.Logger;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.Map;

import static com.golem.skyblockutils.Config.stonksFolder;

public class PersistentData implements Serializable {
	public boolean isFirstLoad = true;

	public	 static Map<String, GuiPosition> positions = new HashMap<>();

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
		// Implementation for saving data
		try {
			Files.write(configFile.toPath(), new Gson().toJson(new PersistentData()).getBytes());
		} catch (IOException e) {
			Logger.error("An error occurred while saving the data: " + e.getMessage());
		}

	}

	public static PersistentData load() {
		// Implementation for loading data
		return new PersistentData();
	}

//	private static void saveFile() throws IOException {
//	}
}
