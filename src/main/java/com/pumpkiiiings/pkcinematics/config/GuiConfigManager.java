package com.pumpkiiiings.pkcinematics.config;

import com.pumpkiiiings.pkcinematics.core.PkCinematicsPlugin;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import dev.triumphteam.gui.builder.item.ItemBuilder;
import org.bukkit.Material;

public class GuiConfigManager {

    private final PkCinematicsPlugin plugin;
    private File file;
    private YamlConfiguration config;

    public GuiConfigManager(PkCinematicsPlugin plugin) {
        this.plugin = plugin;
        reload();
    }

    public void reload() {
        this.file = new File(plugin.getDataFolder(), "gui.yml");
        if (!file.exists()) {
            plugin.saveResource("gui.yml", false);
        }
        this.config = YamlConfiguration.loadConfiguration(file);
    }
    
    public String getString(String path) {
        return colorize(config.getString(path, path));
    }
    
    public String getString(String path, String def) {
        return colorize(config.getString(path, def));
    }

    public Component getComponent(String path) {
        return LegacyComponentSerializer.legacyAmpersand().deserialize(config.getString(path, path));
    }
    
    public Component getComponent(String path, String... placeholders) {
        String raw = config.getString(path, path);
        for (int i = 0; i < placeholders.length; i += 2) {
            raw = raw.replace("%" + placeholders[i] + "%", placeholders[i + 1]);
        }
        return LegacyComponentSerializer.legacyAmpersand().deserialize(raw);
    }
    
    public ItemBuilder getItemBuilder(String path, String... placeholders) {
        String materialName = config.getString(path + ".material", "STONE");
        Material material = Material.matchMaterial(materialName);
        if (material == null) material = Material.STONE;
        
        String name = config.getString(path + ".name", "");
        for (int i = 0; i < placeholders.length; i += 2) {
            name = name.replace("%" + placeholders[i] + "%", placeholders[i + 1]);
        }
        
        ItemBuilder builder = ItemBuilder.from(material).name(LegacyComponentSerializer.legacyAmpersand().deserialize(name));
        
        List<String> lore = config.getStringList(path + ".lore");
        List<Component> compLore = new ArrayList<>();
        for (String line : lore) {
            for (int i = 0; i < placeholders.length; i += 2) {
                line = line.replace("%" + placeholders[i] + "%", placeholders[i + 1]);
            }
            compLore.add(LegacyComponentSerializer.legacyAmpersand().deserialize(line));
        }
        builder.lore(compLore);
        
        return builder;
    }

    private String colorize(String str) {
        return str.replace("&", "§");
    }
}
