package com.pumpkiiiings.pkcinematics.action.impl;

import com.pumpkiiiings.pkcinematics.api.action.ActionContext;
import com.pumpkiiiings.pkcinematics.api.action.PkAction;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class ParticleAction implements PkAction {

    private Particle particle;
    private int count;
    private double offsetX;
    private double offsetY;
    private double offsetZ;
    private double speed;

    public ParticleAction() {}

    @Override
    public void execute(ActionContext context) {
        Player player = context.getPlayer();
        if (particle == null) return;
        
        Location loc = player.getLocation();
        
        player.spawnParticle(particle, loc, count, offsetX, offsetY, offsetZ, speed);
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        map.put("type", getType());
        if (particle != null) map.put("particle", particle.name());
        map.put("count", count);
        map.put("offsetX", offsetX);
        map.put("offsetY", offsetY);
        map.put("offsetZ", offsetZ);
        map.put("speed", speed);
        return map;
    }

    @Override
    public void deserialize(Map<String, Object> data) {
        String pName = (String) data.get("particle");
        if (pName != null) {
            try {
                this.particle = Particle.valueOf(pName.toUpperCase());
            } catch (IllegalArgumentException ignored) {}
        }
        
        Object cObj = data.getOrDefault("count", 1);
        this.count = cObj instanceof Number ? ((Number) cObj).intValue() : 1;
        
        Object ox = data.getOrDefault("offsetX", 0.0);
        this.offsetX = ox instanceof Number ? ((Number) ox).doubleValue() : 0.0;
        
        Object oy = data.getOrDefault("offsetY", 0.0);
        this.offsetY = oy instanceof Number ? ((Number) oy).doubleValue() : 0.0;
        
        Object oz = data.getOrDefault("offsetZ", 0.0);
        this.offsetZ = oz instanceof Number ? ((Number) oz).doubleValue() : 0.0;
        
        Object sp = data.getOrDefault("speed", 0.0);
        this.speed = sp instanceof Number ? ((Number) sp).doubleValue() : 0.0;
    }

    @Override
    public String getType() {
        return "particle";
    }
}
