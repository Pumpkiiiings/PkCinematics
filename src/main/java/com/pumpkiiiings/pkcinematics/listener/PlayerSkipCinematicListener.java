package com.pumpkiiiings.pkcinematics.listener;

import com.pumpkiiiings.pkcinematics.api.PkCinematics;
import com.pumpkiiiings.pkcinematics.core.PlaybackManagerImpl;
import com.pumpkiiiings.pkcinematics.engine.session.PlaybackSession;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;

public class PlayerSkipCinematicListener implements Listener {

    @EventHandler
    public void onPlayerSwapHandItems(PlayerSwapHandItemsEvent event) {
        if (PkCinematics.getApi().getPlaybackManager().isPlaying(event.getPlayer())) {
            PlaybackSession session = PkCinematics.getApi().getPlaybackManager().getSession(event.getPlayer());
            if (session != null && session.getCinematic().isSkipeable()) {
                event.setCancelled(true);
                PkCinematics.getApi().getPlaybackManager().skip(event.getPlayer());
            } else if (session != null) {
                // Cancel the event anyway if they are in a cinematic
                event.setCancelled(true);
            }
        }
    }

    /**
     * Cleans up sessions and shared maps when a player disconnects mid-cinematic.
     * State restoration is skipped since the player is offline — only map cleanup is performed
     * to prevent memory leaks from stale Player object references in activeSessions,
     * cinematicPlayerIds, and debugPlayers.
     */
    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerQuit(PlayerQuitEvent event) {
        if (PkCinematics.getApi().getPlaybackManager().isPlaying(event.getPlayer())) {
            ((PlaybackManagerImpl) PkCinematics.getApi().getPlaybackManager())
                    .forceCleanup(event.getPlayer().getUniqueId());
        }
    }
}
