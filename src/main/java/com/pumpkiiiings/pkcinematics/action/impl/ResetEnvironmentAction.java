package com.pumpkiiiings.pkcinematics.action.impl;

import com.pumpkiiiings.pkcinematics.api.action.ActionContext;
import com.pumpkiiiings.pkcinematics.api.action.PkAction;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class ResetEnvironmentAction implements PkAction {

    public ResetEnvironmentAction() {}

    @Override
    public void execute(ActionContext context) {
        Player player = context.getPlayer();
        player.resetPlayerTime();
        player.resetPlayerWeather();
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        map.put("type", getType());
        return map;
    }

    @Override
    public void deserialize(Map<String, Object> data) {
        // No params needed
    }

    @Override
    public String getType() {
        return "reset_environment";
    }
}
