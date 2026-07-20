package com.pumpkiiiings.pkcinematics.config;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import net.kyori.adventure.text.Component;
import com.pumpkiiiings.pkcinematics.core.FormatUtils;

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

    public Component getMessage(String path) {
        String msg = config.getString(path);
        if (msg == null) return FormatUtils.parse("&cMessage not found: " + path);
        return FormatUtils.parse(msg);
    }
    
    public Component getMessage(String path, Object... replacements) {
        String msg = config.getString(path);
        if (msg == null) return FormatUtils.parse("&cMessage not found: " + path);
        
        for (int i = 0; i < replacements.length; i += 2) {
            if (i + 1 < replacements.length) {
                String key = String.valueOf(replacements[i]);
                String value = String.valueOf(replacements[i + 1]);
                msg = msg.replace("{" + key + "}", value);
            }
        }
        return FormatUtils.parse(msg);
    }
}
