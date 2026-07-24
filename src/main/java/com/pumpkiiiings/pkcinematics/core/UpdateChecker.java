package com.pumpkiiiings.pkcinematics.core;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class UpdateChecker {

    private final JavaPlugin plugin;
    private final String urlString;
    private boolean updateAvailable = false;
    private List<Component> updateMessages = new ArrayList<>();
    private boolean important = false;

    public UpdateChecker(JavaPlugin plugin, String urlString) {
        this.plugin = plugin;
        this.urlString = urlString;
    }

    public void check() {
        if (urlString == null || urlString.isEmpty()) return;

        CompletableFuture.runAsync(() -> {
            try {
                URL url = new URL(urlString);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setConnectTimeout(5000);
                connection.setReadTimeout(5000);

                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    InputStreamReader reader = new InputStreamReader(connection.getInputStream());
                    Gson gson = new Gson();
                    JsonObject json = gson.fromJson(reader, JsonObject.class);
                    
                    if (json != null && json.has("version")) {
                        String latestVersion = json.get("version").getAsString();
                        String currentVersion = plugin.getDescription().getVersion();
                        
                        if (!currentVersion.equalsIgnoreCase(latestVersion)) {
                            updateAvailable = true;
                            if (json.has("important")) {
                                important = json.get("important").getAsBoolean();
                            }
                            
                            if (json.has("message")) {
                                JsonElement msgElement = json.get("message");
                                MiniMessage miniMessage = MiniMessage.miniMessage();
                                
                                if (msgElement.isJsonArray()) {
                                    JsonArray array = msgElement.getAsJsonArray();
                                    for (JsonElement element : array) {
                                        String line = element.getAsString();
                                        // Support legacy & codes alongside minimessage by converting & to section signs
                                        // Note: we just pass it to MiniMessage, if users use &, they should use <color> tags instead for best results.
                                        // To truly support both we'd convert & to legacy, then legacy to MiniMessage, but it's simpler to just replace & with § and let the client handle it if they aren't using MiniMessage tags, OR just use MiniMessage only.
                                        // We will parse with MiniMessage. For legacy '&' support, we can use LegacyComponentSerializer.
                                        // But wait, Adventure can do both. For now we will use MiniMessage, which handles hex, hover, click, etc.
                                        updateMessages.add(miniMessage.deserialize(line));
                                    }
                                } else {
                                    updateMessages.add(miniMessage.deserialize(msgElement.getAsString()));
                                }
                            }
                            
                            // Log to console if it's an update
                            Bukkit.getLogger().info("[PkCinematics] New update available! Version: " + latestVersion);
                            for (Component comp : updateMessages) {
                                String plain = PlainTextComponentSerializer.plainText().serialize(comp);
                                Bukkit.getLogger().info("[PkCinematics] " + plain);
                            }
                        }
                    }
                    reader.close();
                }
            } catch (Exception e) {
                plugin.getLogger().warning("Could not check for updates: " + e.getMessage());
            }
        });
    }

    public boolean isUpdateAvailable() {
        return updateAvailable;
    }

    public List<Component> getUpdateMessages() {
        return updateMessages;
    }

    public boolean isImportant() {
        return important;
    }
}
