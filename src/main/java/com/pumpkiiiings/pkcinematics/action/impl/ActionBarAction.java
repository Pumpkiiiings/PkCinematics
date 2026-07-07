package com.pumpkiiiings.pkcinematics.action.impl;

import com.pumpkiiiings.pkcinematics.api.action.ActionContext;
import com.pumpkiiiings.pkcinematics.api.action.PkAction;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class ActionBarAction implements PkAction {

    private String text;

    public ActionBarAction() {}

    public ActionBarAction(String text) {
        this.text = text;
    }

    @Override
    public void execute(ActionContext context) {
        Player player = context.getPlayer();
        if (this.text == null || this.text.isEmpty()) return;

        String parsedText = this.text.replace("&", "§");
        for (Map.Entry<String, Object> entry : context.getVariables().entrySet()) {
            parsedText = parsedText.replace("%" + entry.getKey() + "%", String.valueOf(entry.getValue()));
        }

        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(parsedText));
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        map.put("type", getType());
        map.put("text", text);
        return map;
    }

    @Override
    public void deserialize(Map<String, Object> data) {
        this.text = (String) data.getOrDefault("text", "");
    }

    @Override
    public String getType() {
        return "actionbar";
    }
}
