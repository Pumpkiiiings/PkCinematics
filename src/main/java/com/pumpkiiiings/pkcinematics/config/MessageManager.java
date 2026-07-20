package com.pumpkiiiings.pkcinematics.config;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

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
        // Usa Kyori Adventure para parsear colores &, incluyendo hex (&#rrggbb)
        net.kyori.adventure.text.Component comp = LegacyComponentSerializer.builder()
                .character('&')
                .hexColors()
                .build()
                .deserialize(text);
        return LegacyComponentSerializer.legacySection().serialize(comp);
    }
}
