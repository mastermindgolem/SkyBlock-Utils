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
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static com.golem.skyblockutils.Main.modDir;

public class AutoUpdater {

    //Adapted from VolcAddons

    private static final String UPDATE_CHECK_URL = "https://api.github.com/repos/mastermindgolem/Skyblock-Utils/releases";
    private boolean updateChecked = false;
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
                JsonObject releaseJson = jsonParser.parse(jsonResponse).getAsJsonArray().get(0).getAsJsonObject();
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
                            EnumChatFormatting.GREEN + "Click here or run " +
                                    EnumChatFormatting.WHITE + "/updatesbu" +
                                    EnumChatFormatting.GREEN + " to update!\n"
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
        System.out.println(version1 + " " + version2);
        List<String> splitVersion1 = Arrays.asList(version1.split("\\."));
        List<String> splitVersion2 = Arrays.asList(version2.split("\\."));
        int beta1 = splitVersion1.indexOf("Beta");
        int beta2 = splitVersion2.indexOf("Beta");

        boolean majorVersion = Integer.parseInt(splitVersion1.get(0)) > Integer.parseInt(splitVersion2.get(0));
        boolean minorVersion = Integer.parseInt(splitVersion1.get(1)) > Integer.parseInt(splitVersion2.get(1));
        boolean patchVersion = Integer.parseInt(splitVersion1.get(2)) > Integer.parseInt(splitVersion2.get(2));

        if (majorVersion || minorVersion || patchVersion) return true;

        if (beta2 > -1 && beta1 == -1) return true;

        if (beta2 > -1) {
            return Integer.parseInt(splitVersion1.get(4)) > Integer.parseInt(splitVersion2.get(4));
        }

        return false;
    }

    public static void downloadAndExtractUpdate(EntityPlayer player) {
        try {

            URL url = new URL(UPDATE_CHECK_URL);
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
                JsonObject releaseJson = parser.parse(jsonResponse).getAsJsonArray().get(0).getAsJsonObject();
                String changelogs = releaseJson.get("body").getAsString();
                String downloadUrl = releaseJson.getAsJsonArray("assets")
                        .get(0).getAsJsonObject()
                        .get("browser_download_url").getAsString();

                File modulesDir = new File(modDir + File.separator + "mods");
                modulesDir.mkdirs();

                URL fileUrl = new URL(downloadUrl);
                String fileName = fileUrl.getFile().substring(fileUrl.getFile().lastIndexOf('/') + 1);
                File zipFile = new File(modulesDir, fileName);

                try (InputStream inputStream = fileUrl.openStream();
                     FileOutputStream fileOutputStream = new FileOutputStream(zipFile)) {
                    byte[] buffer = new byte[1024];
                    int bytesRead;
                    while ((bytesRead = inputStream.read(buffer)) != -1) {
                        fileOutputStream.write(buffer, 0, bytesRead);
                    }
                    fileOutputStream.getFD().sync();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                System.out.println("File downloaded to: " + zipFile.getAbsolutePath());
                System.out.println("File exists: " + zipFile.exists());

                player.addChatMessage(new ChatComponentText(
                        EnumChatFormatting.GREEN + "Update successful: New version " +
                                EnumChatFormatting.WHITE + "v" + latestVersion +
                                EnumChatFormatting.GREEN + " downloaded!"
                ));
                changelogs = changelogs.replaceAll("(?s)```[\\s\\S]*?```", "")
                        .replaceAll("#+\\s*", "")
                        .replaceAll("!?\\[[^\\]]*?]\\([^)]*?\\)", "")
                        .replaceAll("<[^>]*?>", "")
                        .replaceAll("\\*{1,2}|_{1,2}", "")
                        .replaceAll("~{2}", "")
                        .replaceAll("\\[\\d+\\]", "")
                        .replaceAll("^\\s*[-+*]\\s*", "")
                        .replaceAll("(?m)^\\s{0,3}>\\s?", "")
                        .replaceAll("`", "")
                        .trim();

                String[] lines = changelogs.split("\r\n");

                for (String l : lines) {
                    Main.mc.thePlayer.addChatMessage(new ChatComponentText(l));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
