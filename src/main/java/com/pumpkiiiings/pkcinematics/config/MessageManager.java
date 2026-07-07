package com.pumpkiiiings.pkcinematics.config;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MessageManager {
    
    private final Plugin plugin;
    private FileConfiguration config;
    private File file;

    public MessageManager(Plugin plugin) {
        this.plugin = plugin;
        this.file = new File(plugin.getDataFolder(), "messages.yml");
        if (!file.exists()) {
            plugin.saveResource("messages.yml", false);
        }
        this.config = YamlConfiguration.loadConfiguration(file);
    }

    public void reload() {
        this.config = YamlConfiguration.loadConfiguration(file);
    }

    public String getMessage(String path) {
        String msg = config.getString(path);
        if (msg == null) return "§cMessage not found: " + path;
        return format(msg);
    }
    
    public String getMessage(String path, Object... replacements) {
        String msg = config.getString(path);
        if (msg == null) return "§cMessage not found: " + path;
        
        for (int i = 0; i < replacements.length; i += 2) {
            if (i + 1 < replacements.length) {
                String key = String.valueOf(replacements[i]);
                String value = String.valueOf(replacements[i + 1]);
                msg = msg.replace("{" + key + "}", value);
            }
        }
        return format(msg);
    }

    private String format(String text) {
        // Soporte básico para colores & y Hex (&#RRGGBB)
        Pattern pattern = Pattern.compile("&#[a-fA-F0-9]{6}");
        Matcher matcher = pattern.matcher(text);
        while (matcher.find()) {
            String color = text.substring(matcher.start(), matcher.end());
            text = text.replace(color, net.md_5.bungee.api.ChatColor.of(color.replace("&", "")) + "");
            matcher = pattern.matcher(text);
        }
        return ChatColor.translateAlternateColorCodes('&', text);
    }
}
