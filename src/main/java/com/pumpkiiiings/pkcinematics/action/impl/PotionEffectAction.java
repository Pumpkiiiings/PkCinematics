package com.pumpkiiiings.pkcinematics.action.impl;

import com.pumpkiiiings.pkcinematics.api.action.ActionContext;
import com.pumpkiiiings.pkcinematics.api.action.PkAction;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;
import java.util.Map;

public class PotionEffectAction implements PkAction {

    private PotionEffectType effectType;
    private int durationTicks;
    private int amplifier;

    public PotionEffectAction() {}

    @Override
    public void execute(ActionContext context) {
        Player player = context.getPlayer();
        if (effectType == null) return;
        
        player.addPotionEffect(new PotionEffect(effectType, durationTicks, amplifier, false, false, false));
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        map.put("type", getType());
        if (effectType != null) map.put("effect", effectType.getName());
        map.put("duration", durationTicks);
        map.put("amplifier", amplifier);
        return map;
    }

    @Override
    public void deserialize(Map<String, Object> data) {
        String eName = (String) data.get("effect");
        if (eName != null) {
            this.effectType = PotionEffectType.getByName(eName.toUpperCase());
        }
        
        Object dObj = data.getOrDefault("duration", 20);
        this.durationTicks = dObj instanceof Number ? ((Number) dObj).intValue() : 20;
        
        Object aObj = data.getOrDefault("amplifier", 0);
        this.amplifier = aObj instanceof Number ? ((Number) aObj).intValue() : 0;
    }

    @Override
    public String getType() {
        return "potion_effect";
    }
}
