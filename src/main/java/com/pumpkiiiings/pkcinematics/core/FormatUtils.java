package com.pumpkiiiings.pkcinematics.core;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FormatUtils {

    private static final Pattern HEX_PATTERN = Pattern.compile("&#([a-fA-F0-9]{6})");

    public static Component parse(String text) {
        if (text == null || text.isEmpty()) {
            return Component.empty();
        }

        // Translate legacy Hex (&#RRGGBB) to MiniMessage (<#RRGGBB>)
        Matcher hexMatcher = HEX_PATTERN.matcher(text);
        StringBuffer buffer = new StringBuffer();
        while (hexMatcher.find()) {
            hexMatcher.appendReplacement(buffer, "<#" + hexMatcher.group(1) + ">");
        }
        hexMatcher.appendTail(buffer);
        text = buffer.toString();

        // Translate legacy color codes to MiniMessage tags
        text = text.replace("&0", "<black>")
                   .replace("&1", "<dark_blue>")
                   .replace("&2", "<dark_green>")
                   .replace("&3", "<dark_aqua>")
                   .replace("&4", "<dark_red>")
                   .replace("&5", "<dark_purple>")
                   .replace("&6", "<gold>")
                   .replace("&7", "<gray>")
                   .replace("&8", "<dark_gray>")
                   .replace("&9", "<blue>")
                   .replace("&a", "<green>")
                   .replace("&b", "<aqua>")
                   .replace("&c", "<red>")
                   .replace("&d", "<light_purple>")
                   .replace("&e", "<yellow>")
                   .replace("&f", "<white>")
                   .replace("&k", "<obfuscated>")
                   .replace("&l", "<bold>")
                   .replace("&m", "<strikethrough>")
                   .replace("&n", "<underlined>")
                   .replace("&o", "<italic>")
                   .replace("&r", "<reset>");

        return MiniMessage.miniMessage().deserialize(text);
    }
}
