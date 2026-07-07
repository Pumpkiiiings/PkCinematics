package com.pumpkiiiings.pkcinematics.condition.impl;

import com.pumpkiiiings.pkcinematics.api.condition.Condition;
import org.bukkit.entity.Player;

import java.util.Map;

public class PermissionCondition implements Condition {

    private String permission;

    @Override
    public boolean test(Player player) {
        if (permission == null || permission.isEmpty()) return true;
        return player.hasPermission(permission);
    }

    @Override
    public void deserialize(Map<String, Object> data) {
        this.permission = (String) data.get("permission");
    }

    @Override
    public String getType() {
        return "permission";
    }
}
