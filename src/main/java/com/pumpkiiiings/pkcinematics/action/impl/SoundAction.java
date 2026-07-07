package com.pumpkiiiings.pkcinematics.action.impl;

import com.pumpkiiiings.pkcinematics.api.action.ActionContext;
import com.pumpkiiiings.pkcinematics.api.action.PkAction;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class SoundAction implements PkAction {

    private String soundKey;
    private float volume;
    private float pitch;

    public SoundAction() {}

    public SoundAction(String soundKey, float volume, float pitch) {
        this.soundKey = soundKey;
        this.volume = volume;
        this.pitch = pitch;
    }

    @Override
    public void execute(ActionContext context) {
        Player player = context.getPlayer();
        if (soundKey == null || soundKey.isEmpty()) return;

        Location loc = player.getLocation();
        
        try {
            // Try to parse as native Bukkit Sound Enum
            Sound nativeSound = Sound.valueOf(soundKey.toUpperCase());
            player.playSound(loc, nativeSound, volume, pitch);
        } catch (IllegalArgumentException e) {
            // It's a custom sound (from a ResourcePack)
            player.playSound(loc, soundKey.toLowerCase(), volume, pitch);
        }
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        map.put("type", getType());
        map.put("sound", soundKey);
        map.put("volume", volume);
        map.put("pitch", pitch);
        return map;
    }

    @Override
    public void deserialize(Map<String, Object> data) {
        this.soundKey = (String) data.getOrDefault("sound", "");
        
        Object volObj = data.getOrDefault("volume", 1.0f);
        this.volume = volObj instanceof Number ? ((Number) volObj).floatValue() : 1.0f;
        
        Object pitchObj = data.getOrDefault("pitch", 1.0f);
        this.pitch = pitchObj instanceof Number ? ((Number) pitchObj).floatValue() : 1.0f;
    }

    @Override
    public String getType() {
        return "sound";
    }
}
