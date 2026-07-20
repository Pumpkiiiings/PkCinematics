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

        // Estandarizar section signs
        text = text.replace("§", "&");

        // Translate legacy Hex (&#RRGGBB) to MiniMessage (<#RRGGBB>)
        Matcher hexMatcher = HEX_PATTERN.matcher(text);
        StringBuffer buffer = new StringBuffer();
        while (hexMatcher.find()) {
            hexMatcher.appendReplacement(buffer, "<#" + hexMatcher.group(1) + ">");
        }
        hexMatcher.appendTail(buffer);
        text = buffer.toString();

        // Translate legacy color codes to MiniMessage tags
        text = text.replaceAll("(?i)&0", "<black>")
                   .replaceAll("(?i)&1", "<dark_blue>")
                   .replaceAll("(?i)&2", "<dark_green>")
                   .replaceAll("(?i)&3", "<dark_aqua>")
                   .replaceAll("(?i)&4", "<dark_red>")
                   .replaceAll("(?i)&5", "<dark_purple>")
                   .replaceAll("(?i)&6", "<gold>")
                   .replaceAll("(?i)&7", "<gray>")
                   .replaceAll("(?i)&8", "<dark_gray>")
                   .replaceAll("(?i)&9", "<blue>")
                   .replaceAll("(?i)&a", "<green>")
                   .replaceAll("(?i)&b", "<aqua>")
                   .replaceAll("(?i)&c", "<red>")
                   .replaceAll("(?i)&d", "<light_purple>")
                   .replaceAll("(?i)&e", "<yellow>")
                   .replaceAll("(?i)&f", "<white>")
                   .replaceAll("(?i)&k", "<obfuscated>")
                   .replaceAll("(?i)&l", "<bold>")
                   .replaceAll("(?i)&m", "<strikethrough>")
                   .replaceAll("(?i)&n", "<underlined>")
                   .replaceAll("(?i)&o", "<italic>")
                   .replaceAll("(?i)&r", "<reset>");

        return MiniMessage.miniMessage().deserialize(text);
    }
}
