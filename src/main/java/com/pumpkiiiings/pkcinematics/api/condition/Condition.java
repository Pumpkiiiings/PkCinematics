package com.pumpkiiiings.pkcinematics.api.condition;

import org.bukkit.entity.Player;
import java.util.Map;

public interface Condition {
    /**
     * Test the condition against a player.
     * @return true if the condition is met, false otherwise.
     */
    boolean test(Player player);

    /**
     * Deserialize configuration parameters for this condition.
     */
    void deserialize(Map<String, Object> data);

    /**
     * Get the type identifier of this condition.
     */
    String getType();
}
