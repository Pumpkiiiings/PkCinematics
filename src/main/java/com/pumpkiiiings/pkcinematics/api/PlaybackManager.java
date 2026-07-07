package com.pumpkiiiings.pkcinematics.api;

import com.pumpkiiiings.pkcinematics.engine.session.PlaybackSession;
import com.pumpkiiiings.pkcinematics.model.Cinematic;
import org.bukkit.entity.Player;
import java.util.Collection;
import java.util.List;

public interface PlaybackManager {
    
    /**
     * Plays a cinematic for a player.
     * @param player The player.
     * @param cinematic The cinematic.
     * @return The created session.
     */
    PlaybackSession play(Player player, Cinematic cinematic);
    
    /**
     * Plays a cinematic for multiple players (shared session if supported, or individual).
     * For now, returns a list of individual sessions.
     * @param players The players.
     * @param cinematic The cinematic.
     * @return A list of created sessions.
     */
    List<PlaybackSession> play(Collection<Player> players, Cinematic cinematic);
    
    void stop(Player player);
    
    void pause(Player player);
    
    void resume(Player player);
    
    void skip(Player player);
    
    boolean isPlaying(Player player);
    
    PlaybackSession getSession(Player player);
}
