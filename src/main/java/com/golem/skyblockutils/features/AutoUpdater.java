package com.golem.skyblockutils.features;

import com.golem.skyblockutils.Main;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.event.ClickEvent;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static com.golem.skyblockutils.Main.modDir;

public class AutoUpdater {

    private static final String UPDATE_CHECK_URL = "https://api.github.com/repos/mastermindgolem/Skyblock-Utils/releases/latest";
    private boolean updateChecked = false;
    private static String changelogs = "";
    private static String latestVersion = "1.0.0";

    @SubscribeEvent
    public void onClientConnected(FMLNetworkEvent.ClientConnectedToServerEvent event) {
        if (!updateChecked) {
            ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

            // Schedule checkForUpdates() after a delay of 3 seconds
            scheduler.schedule(() -> {
                final EntityPlayer player = Main.mc.thePlayer;
                checkForUpdates(player);
                updateChecked = true;
            }, 3, TimeUnit.SECONDS);

            // Shutdown the scheduler when it's no longer needed
            scheduler.shutdown();
        }
    }

    private void checkForUpdates(EntityPlayer player) {
        try {
            URL url = new URL(UPDATE_CHECK_URL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();

            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line;
                StringBuilder response = new StringBuilder();

                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }

                reader.close();
                connection.disconnect();

                String jsonResponse = response.toString();
                JsonParser jsonParser = new JsonParser();
                JsonObject releaseJson = jsonParser.parse(jsonResponse).getAsJsonObject();
                latestVersion = releaseJson.get("tag_name").getAsString();
                latestVersion = latestVersion.startsWith("v") ? latestVersion.substring(1) : latestVersion;

                String currentVersion = Main.VERSION;

                if (isNewerVersion(latestVersion, currentVersion)) {
                    player.addChatMessage(new ChatComponentText(
                                    EnumChatFormatting.GOLD + "\nSkyblock Utils > " +
                                    EnumChatFormatting.YELLOW + "A new update is available: " +
                                    EnumChatFormatting.WHITE + EnumChatFormatting.BOLD + "v" + latestVersion
                    ));
                    player.addChatMessage(new ChatComponentText(
                            EnumChatFormatting.GREEN + "Click here or run '" +
                                    EnumChatFormatting.WHITE + "/updatesbu" +
                                    EnumChatFormatting.GREEN + "' to update!\n"
                    ).setChatStyle(new ChatStyle().setChatClickEvent(
                            new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/updatesbu") {
                                @Override
                                public Action getAction() {
                                    //custom behavior
                                    return Action.RUN_COMMAND;
                                }
                            })
                    ));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean isNewerVersion(String version1, String version2) {
        String[] splitVersion1 = version1.split("\\.");
        String[] splitVersion2 = version2.split("\\.");

        for (int i = 0; i < Math.min(splitVersion1.length, splitVersion2.length); i++) {
            int v1 = Integer.parseInt(splitVersion1[i]);
            int v2 = Integer.parseInt(splitVersion2[i]);

            if (v1 > v2) {
                return true;
            } else if (v1 < v2) {
                return false;
            }
        }

        return splitVersion1.length > splitVersion2.length;
    }

    public static void downloadAndExtractUpdate(EntityPlayer player) {
        try {
            String releasesUrl = "https://api.github.com/repos/mastermindgolem/SkyBlock-Utils/releases/latest";

            URL url = new URL(releasesUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/vnd.github.v3+json");
            connection.connect();

            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line;
                StringBuilder response = new StringBuilder();

                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }

                reader.close();
                connection.disconnect();

                String jsonResponse = response.toString();
                JsonParser parser = new JsonParser();
                JsonObject releaseJson = parser.parse(jsonResponse).getAsJsonObject();
                changelogs = releaseJson.get("body").getAsString();
                String downloadUrl = releaseJson.getAsJsonArray("assets")
                        .get(0).getAsJsonObject()
                        .get("browser_download_url").getAsString();

                File modulesDir = new File(modDir + File.separator + "mods");
                modulesDir.mkdirs();

                URL fileUrl = new URL(downloadUrl);
                String fileName = fileUrl.getFile().substring(fileUrl.getFile().lastIndexOf('/') + 1);
                File zipFile = new File(modulesDir, fileName);

                try (InputStream inputStream = fileUrl.openStream(); OutputStream outputStream = Files.newOutputStream(zipFile.toPath())) {
                    byte[] buffer = new byte[1024];
                    int bytesRead;
                    while ((bytesRead = inputStream.read(buffer)) != -1) {
                        outputStream.write(buffer, 0, bytesRead);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

                // Notify the player about the update status
                player.addChatMessage(new ChatComponentText(
                        EnumChatFormatting.GREEN + "Update successful: New version " +
                                EnumChatFormatting.WHITE + "v" + latestVersion +
                                EnumChatFormatting.GREEN + " downloaded!"
                ));
                changelogs = changelogs.replaceAll("(?s)```[\\s\\S]*?```", "")         // Remove code blocks (```...```)
                        .replaceAll("#+\\s*", "")                     // Remove headings (#, ##, ###, etc.)
                        .replaceAll("!?\\[[^\\]]*?]\\([^)]*?\\)", "")  // Remove images and links (![...](...), [...](...))
                        .replaceAll("<[^>]*?>", "")                   // Remove HTML-like tags (<...>)
                        .replaceAll("\\*{1,2}|_{1,2}", "")            // Remove bold and italic markers (*, **, _, __)
                        .replaceAll("~{2}", "")                       // Remove strikethrough markers (~~)
                        .replaceAll("\\[\\d+\\]", "")                 // Remove numbered lists ([1], [2], etc.)
                        .replaceAll("^\\s*[-+*]\\s*", "")             // Remove bullet lists (-, +, *)
                        .replaceAll("(?m)^\\s{0,3}>\\s?", "")         // Remove block quotes (>)
                        .replaceAll("`", "")                          // Remove inline code (`...`)
                        .trim();                                                        // Trim leading and trailing whitespace
                // Split the plain text into individual lines
                String[] lines = changelogs.split("\r\n");

                // Send each line as a separate chat message
                for (String l : lines) {
                    // Assuming you have a way to send chat messages, replace the line below with your implementation
                    Main.mc.thePlayer.addChatMessage(new ChatComponentText(l));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}