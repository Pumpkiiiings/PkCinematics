package com.pumpkiiiings.pkcinematics.action.impl;

import com.pumpkiiiings.pkcinematics.api.action.ActionContext;
import com.pumpkiiiings.pkcinematics.api.action.PkAction;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class MessageAction implements PkAction {

    private String text;

    public MessageAction() {}

    public MessageAction(String text) {
        this.text = text;
    }

    @Override
    public void execute(ActionContext context) {
        Player player = context.getPlayer();
        String parsedText = this.text != null ? this.text : "";

        for (Map.Entry<String, Object> entry : context.getVariables().entrySet()) {
            parsedText = parsedText.replace("%" + entry.getKey() + "%", String.valueOf(entry.getValue()));
        }

        player.sendMessage(com.pumpkiiiings.pkcinematics.core.FormatUtils.parse(parsedText));
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
        return "message";
    }
}
