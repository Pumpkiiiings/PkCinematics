package com.pumpkiiiings.pkcinematics.condition.impl;

import com.pumpkiiiings.pkcinematics.api.condition.Condition;
import org.bukkit.entity.Player;

import java.util.Map;

public class WorldCondition implements Condition {

    private String worldName;

    @Override
    public boolean test(Player player) {
        if (worldName == null || worldName.isEmpty()) return true;
        return player.getWorld().getName().equalsIgnoreCase(worldName);
    }

    @Override
    public void deserialize(Map<String, Object> data) {
        // Can be defined as `world: world_nether` or `value: world_nether`
        this.worldName = (String) data.getOrDefault("world", data.get("value"));
    }

    @Override
    public String getType() {
        return "world";
    }
}
