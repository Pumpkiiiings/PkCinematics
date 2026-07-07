package com.pumpkiiiings.pkcinematics.action.impl;

import com.pumpkiiiings.pkcinematics.api.PkCinematics;
import com.pumpkiiiings.pkcinematics.api.action.ActionContext;
import com.pumpkiiiings.pkcinematics.api.action.PkAction;
import com.pumpkiiiings.pkcinematics.model.Cinematic;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class PlayCinematicAction implements PkAction {

    private String cinematicId;

    public PlayCinematicAction() {}

    public PlayCinematicAction(String cinematicId) {
        this.cinematicId = cinematicId;
    }

    @Override
    public void execute(ActionContext context) {
        Player player = context.getPlayer();
        if (cinematicId == null || cinematicId.isEmpty()) return;

        Cinematic cinematic = PkCinematics.getApi().getCinematicManager().getCinematic(cinematicId);
        if (cinematic != null) {
            PkCinematics.getApi().getPlaybackManager().play(player, cinematic);
        }
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        map.put("type", getType());
        map.put("cinematic", cinematicId);
        return map;
    }

    @Override
    public void deserialize(Map<String, Object> data) {
        // user mentioned `cinematic: intro` or `id: intro`. Let's support `cinematic` key.
        this.cinematicId = (String) data.getOrDefault("cinematic", data.get("id"));
    }

    @Override
    public String getType() {
        return "cinematic";
    }
}
