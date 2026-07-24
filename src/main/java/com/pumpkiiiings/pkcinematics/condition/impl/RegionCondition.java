package com.pumpkiiiings.pkcinematics.condition.impl;

import com.pumpkiiiings.pkcinematics.api.condition.Condition;
import com.pumpkiiiings.pkcinematics.integration.WGIntegration;
import org.bukkit.entity.Player;

import java.util.Map;

public class RegionCondition implements Condition {
    private String region;

    @Override
    public boolean test(Player player) {
        if (player == null || region == null) return false;
        return WGIntegration.isPlayerInRegion(player, region);
    }

    @Override
    public void deserialize(Map<String, Object> config) {
        if (config.containsKey("region")) {
            this.region = (String) config.get("region");
        }
    }
    
    @Override
    public String getType() {
        return "region";
    }
}
