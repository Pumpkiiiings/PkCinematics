package com.pumpkiiiings.pkcinematics.api.action;

import com.pumpkiiiings.pkcinematics.engine.session.PlaybackSession;
import org.bukkit.entity.Player;
import javax.annotation.Nullable;
import java.util.Map;

public interface ActionContext {

    /**
     * @return The player involved in the action.
     */
    Player getPlayer();

    /**
     * @return The playback session if this action is part of a cinematic, null otherwise.
     */
    @Nullable
    PlaybackSession getPlaybackSession();

    /**
     * @return Context variables available for placeholder parsing.
     */
    Map<String, Object> getVariables();

}
