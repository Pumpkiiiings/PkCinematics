package com.pumpkiiiings.pkcinematics.action.impl;

import com.pumpkiiiings.pkcinematics.api.action.ActionContext;
import com.pumpkiiiings.pkcinematics.api.action.PkAction;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class TitleAction implements PkAction {
    private String title;
    private String subtitle;
    private int fadeIn, stay, fadeOut;

    public TitleAction() {} // For registry instantiation

    public TitleAction(String title, String subtitle, int fadeIn, int stay, int fadeOut) {
        this.title = title;
        this.subtitle = subtitle;
        this.fadeIn = fadeIn;
        this.stay = stay;
        this.fadeOut = fadeOut;
    }

    @Override
    public void execute(ActionContext context) {
        Player player = context.getPlayer();
        String parsedTitle = this.title != null ? this.title : "";
        String parsedSubtitle = this.subtitle != null ? this.subtitle : "";

        // Basic placeholder replacing
        for (Map.Entry<String, Object> entry : context.getVariables().entrySet()) {
            String key = "%" + entry.getKey() + "%";
            String value = String.valueOf(entry.getValue());
            parsedTitle = parsedTitle.replace(key, value);
            parsedSubtitle = parsedSubtitle.replace(key, value);
        }

        net.kyori.adventure.title.Title.Times times = net.kyori.adventure.title.Title.Times.times(
            net.kyori.adventure.util.Ticks.duration(fadeIn),
            net.kyori.adventure.util.Ticks.duration(stay),
            net.kyori.adventure.util.Ticks.duration(fadeOut)
        );
        net.kyori.adventure.title.Title titleObj = net.kyori.adventure.title.Title.title(
            com.pumpkiiiings.pkcinematics.core.FormatUtils.parse(parsedTitle),
            com.pumpkiiiings.pkcinematics.core.FormatUtils.parse(parsedSubtitle),
            times
        );

        player.showTitle(titleObj);
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        map.put("title", title);
        map.put("subtitle", subtitle);
        map.put("fadeIn", fadeIn);
        map.put("stay", stay);
        map.put("fadeOut", fadeOut);
        return map;
    }

    @Override
    public void deserialize(Map<String, Object> data) {
        this.title = (String) data.getOrDefault("title", "");
        this.subtitle = (String) data.getOrDefault("subtitle", "");
        this.fadeIn = (int) data.getOrDefault("fadeIn", 10);
        this.stay = (int) data.getOrDefault("stay", 40);
        this.fadeOut = (int) data.getOrDefault("fadeOut", 10);
    }

    @Override
    public String getType() {
        return "TITLE";
    }
}
