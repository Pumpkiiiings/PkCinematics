package com.pumpkiiiings.pkcinematics.action.impl;

import com.pumpkiiiings.pkcinematics.api.action.ActionContext;
import com.pumpkiiiings.pkcinematics.api.action.PkAction;
import org.bukkit.WeatherType;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class WeatherAction implements PkAction {

    private WeatherType weatherType;

    public WeatherAction() {}

    @Override
    public void execute(ActionContext context) {
        Player player = context.getPlayer();
        if (weatherType == null) return;
        player.setPlayerWeather(weatherType);
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        map.put("type", getType());
        if (weatherType != null) map.put("weather", weatherType.name());
        return map;
    }

    @Override
    public void deserialize(Map<String, Object> data) {
        String wName = (String) data.get("weather");
        if (wName != null) {
            try {
                this.weatherType = WeatherType.valueOf(wName.toUpperCase());
            } catch (IllegalArgumentException ignored) {}
        }
    }

    @Override
    public String getType() {
        return "weather";
    }
}
