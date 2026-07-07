package com.pumpkiiiings.pkcinematics.api.action;

import com.pumpkiiiings.pkcinematics.engine.session.PlaybackSession;
import org.bukkit.entity.Player;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Nullable;

public class SimpleActionContext implements ActionContext {

    private final Player player;
    private final PlaybackSession session;
    private final Map<String, Object> variables;

    public SimpleActionContext(Player player, @Nullable PlaybackSession session) {
        this.player = player;
        this.session = session;
        this.variables = new HashMap<>();
        this.variables.put("player_name", player.getName());
        this.variables.put("player_uuid", player.getUniqueId().toString());
    }

    @Override
    public Player getPlayer() {
        return player;
    }

    @Nullable
    @Override
    public PlaybackSession getPlaybackSession() {
        return session;
    }

    @Override
    public Map<String, Object> getVariables() {
        return variables;
    }
}
