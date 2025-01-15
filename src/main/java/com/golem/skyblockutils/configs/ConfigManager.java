package com.golem.skyblockutils.configs;

import com.golem.skyblockutils.Main;
import com.golem.skyblockutils.MoulConfig;
import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import io.github.notenoughupdates.moulconfig.Config;
import io.github.notenoughupdates.moulconfig.gui.GuiScreenElementWrapper;
import io.github.notenoughupdates.moulconfig.gui.MoulConfigEditor;
import io.github.notenoughupdates.moulconfig.observer.PropertyTypeAdapterFactory;
import io.github.notenoughupdates.moulconfig.processor.BuiltinMoulConfigGuis;
import io.github.notenoughupdates.moulconfig.processor.ConfigProcessorDriver;
import io.github.notenoughupdates.moulconfig.processor.MoulConfigProcessor;


import logger.Logger;
import lombok.Getter;
import net.minecraft.client.gui.GuiScreen;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.UUID;
public class ConfigManager {

    private static final Gson GSON = new GsonBuilder()
            .setPrettyPrinting()
            .excludeFieldsWithoutExposeAnnotation()
            .serializeSpecialFloatingPointValues()
            .registerTypeAdapterFactory(new PropertyTypeAdapterFactory())
            .registerTypeAdapter(UUID.class, new TypeAdapter<UUID>() {
                @Override
                public void write(JsonWriter out, UUID value) throws IOException {
                    out.value(value.toString());
                }

                @Override
                public UUID read(JsonReader reader) throws IOException {
                    return UUID.fromString(reader.nextString());
                }
            }.nullSafe())
            .registerTypeAdapter(Runnable.class, (InstanceCreator<Runnable>) type -> () -> {})
            .enableComplexMapKeySerialization()
            .create();

    public static final File configDirectory = new File("config/skyblockutils");
    private final File configFile;
    @Getter
    private MoulConfig config;
    private long lastSaveTime = 0L;

    private static MoulConfigEditor<Config> editor;
    private GuiScreen screenToOpen;

    public ConfigManager() {
        configDirectory.mkdirs();
        configFile = new File(configDirectory, "config.json");

        if (configFile.isFile()) {
            System.out.println("Trying to load the config");
            tryReadConfig();
        }

        if (config == null) {
            System.out.println("Creating a clean config.");
            config = new MoulConfig();
        }

        MoulConfigProcessor<Config> processor = new MoulConfigProcessor<>(config);
        BuiltinMoulConfigGuis.addProcessors(processor);
        new ConfigProcessorDriver(processor).processConfig(config);

        editor = new MoulConfigEditor<>(processor);

        Runtime.getRuntime().addShutdownHook(new Thread(this::save));
    }

    public void openConfigGui() {
        Main.display = new GuiScreenElementWrapper(editor);
    }

    private void tryReadConfig() {
        try {
            InputStreamReader inputStreamReader = new InputStreamReader(Files.newInputStream(configFile.toPath()), StandardCharsets.UTF_8);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            StringBuilder builder = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                builder.append(line).append("\n");
            }

            JsonObject jsonObject = GSON.fromJson(builder.toString(), JsonObject.class);
            //JsonObject newJsonObject = ConfigUpdaterMigrator.fixConfig(jsonObject);
            config = GSON.fromJson(jsonObject, MoulConfig.class);

            Logger.info("Config loaded.");
        } catch (Exception e) {
            throw new RuntimeException("Could not load config", e);
        }
    }

    public void save() {
        lastSaveTime = System.currentTimeMillis();
        if (config == null) {
            throw new IllegalStateException("Cannot save null config.");
        }

        try {
            configDirectory.mkdirs();
            File tempFile = new File(configDirectory, "config.json.write");
            tempFile.createNewFile();

            try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(tempFile), StandardCharsets.UTF_8))) {
                writer.write(GSON.toJson(config));
            }

            Files.move(
                    tempFile.toPath(),
                    configFile.toPath(),
                    StandardCopyOption.REPLACE_EXISTING,
                    StandardCopyOption.ATOMIC_MOVE
            );
        } catch (IOException e) {
            throw new RuntimeException("Could not save config", e);
        }

        Logger.info("Config saved.");
    }

}