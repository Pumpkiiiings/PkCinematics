package com.pumpkiiiings.pkcinematics.action.impl;

import com.pumpkiiiings.pkcinematics.api.action.ActionContext;
import com.pumpkiiiings.pkcinematics.api.action.PkAction;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class TimeAction implements PkAction {

    private long time;
    private boolean relative;

    public TimeAction() {}

    @Override
    public void execute(ActionContext context) {
        Player player = context.getPlayer();
        player.setPlayerTime(time, relative);
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        map.put("type", getType());
        map.put("time", time);
        map.put("relative", relative);
        return map;
    }

    @Override
    public void deserialize(Map<String, Object> data) {
        Object tObj = data.getOrDefault("time", 0L);
        this.time = tObj instanceof Number ? ((Number) tObj).longValue() : 0L;
        
        Object rObj = data.getOrDefault("relative", false);
        this.relative = rObj instanceof Boolean ? (Boolean) rObj : false;
    }

    @Override
    public String getType() {
        return "time";
    }
}
