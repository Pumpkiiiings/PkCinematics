package com.pumpkiiiings.pkcinematics.condition.impl;

import com.pumpkiiiings.pkcinematics.api.condition.Condition;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

import java.util.Map;

public class GamemodeCondition implements Condition {

    private GameMode gameMode;

    @Override
    public boolean test(Player player) {
        if (gameMode == null) return true;
        return player.getGameMode() == gameMode;
    }

    @Override
    public void deserialize(Map<String, Object> data) {
        String gmStr = (String) data.getOrDefault("gamemode", data.get("value"));
        if (gmStr != null) {
            try {
                this.gameMode = GameMode.valueOf(gmStr.toUpperCase());
            } catch (IllegalArgumentException ignored) {
            }
        }
    }

    @Override
    public String getType() {
        return "gamemode";
    }
}
