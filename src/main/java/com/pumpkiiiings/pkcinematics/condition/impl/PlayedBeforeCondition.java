package com.pumpkiiiings.pkcinematics.condition.impl;

import com.pumpkiiiings.pkcinematics.api.condition.Condition;
import org.bukkit.entity.Player;
import java.util.Map;

public class PlayedBeforeCondition implements Condition {

    private boolean expectedValue = true;

    @Override
    public boolean test(Player player) {
        return player.hasPlayedBefore() == expectedValue;
    }

    @Override
    public void deserialize(Map<String, Object> data) {
        if (data.containsKey("value")) {
            Object val = data.get("value");
            if (val instanceof Boolean) {
                this.expectedValue = (boolean) val;
            } else if (val instanceof String) {
                this.expectedValue = Boolean.parseBoolean((String) val);
            }
        }
    }

    @Override
    public String getType() {
        return "played_before";
    }
}
