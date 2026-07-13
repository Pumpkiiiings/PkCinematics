package com.pumpkiiiings.pkcinematics.core;

import com.pumpkiiiings.pkcinematics.api.PlaybackManager;
import com.pumpkiiiings.pkcinematics.api.state.PlayerStateRestorer;
import com.pumpkiiiings.pkcinematics.engine.scheduler.CinematicScheduler;
import com.pumpkiiiings.pkcinematics.engine.session.PlaybackSession;
import com.pumpkiiiings.pkcinematics.engine.session.PlayerState;
import com.pumpkiiiings.pkcinematics.model.Cinematic;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class PlaybackManagerImpl implements PlaybackManager {
    
    private final CinematicScheduler scheduler;
    private final PlayerStateRestorer stateRestorer;
    private final Set<UUID> debugPlayers = ConcurrentHashMap.newKeySet();

    public PlaybackManagerImpl(CinematicScheduler scheduler, PlayerStateRestorer stateRestorer) {
        this.scheduler = scheduler;
        this.stateRestorer = stateRestorer;
    }

    public CinematicScheduler getScheduler() {
        return scheduler;
    }

    @Override
    public PlaybackSession play(Player player, Cinematic cinematic) {
        if (isPlaying(player)) {
            stop(player);
        }

        PlaybackSession session = new PlaybackSession(player, cinematic);
        
        // 1. Capture State
        PlayerState state = stateRestorer.captureState(player);
        session.setSavedState(state);
        
        // 2. Save Backup for Crash Recovery
        stateRestorer.saveBackup(session.getSessionId(), player.getUniqueId(), state);
        
        // 3. Prepare Player for Cinematic
        if (player.getVehicle() != null) {
            player.getVehicle().removePassenger(player);
        }
        player.setGameMode(GameMode.SPECTATOR);

        // 3b. Register in the cinematic sets — CameraPacketListener will block
        //     SPAWN_ENTITY packets for this player from reaching other cinematic players,
        //     preventing spectator bodies from being visible. No loops needed here.
        scheduler.getCinematicPlayerIds().add(player.getUniqueId());
        scheduler.getCinematicEntityIds().add(player.getEntityId());
        
        // 3c. Hide all other players from the cinematic player (keeps them in Tab)
        int[] entityIds = org.bukkit.Bukkit.getOnlinePlayers().stream()
                .filter(p -> !p.equals(player))
                .mapToInt(Player::getEntityId)
                .toArray();
        if (entityIds.length > 0) {
            com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerDestroyEntities destroyPacket = 
                    new com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerDestroyEntities(entityIds);
            com.github.retrooper.packetevents.PacketEvents.getAPI().getPlayerManager().sendPacket(player, destroyPacket);
        }

        
        // 4. Add to scheduler (which starts the camera)
        scheduler.addSession(session);
        
        return session;
    }

    @Override
    public List<PlaybackSession> play(Collection<Player> players, Cinematic cinematic) {
        List<PlaybackSession> sessions = new ArrayList<>();
        for (Player p : players) {
            sessions.add(play(p, cinematic));
        }
        return sessions;
    }

    @Override
    public void stop(Player player) {
        PlaybackSession session = getSession(player);
        if (session != null) {
            // Remove from cinematic sets first so packet filter stops blocking
            scheduler.getCinematicPlayerIds().remove(player.getUniqueId());
            scheduler.getCinematicEntityIds().remove(player.getEntityId());

            // Remove from scheduler (also stops camera)
            scheduler.removeSession(session.getSessionId());
            
            // Restore State
            stateRestorer.restoreState(player, session.getSavedState());
            
            // Restore visibility of other players
            // Note: Since we use PacketEvents to hide them purely client-side, 
            // Bukkit still thinks they are tracked. They will naturally reappear 
            // when the player is teleported back by the stateRestorer (chunk refresh).
            
            // Delete Backup
            stateRestorer.deleteBackup(session.getSessionId());
        }
    }

    /**
     * Called when a player disconnects mid-cinematic.
     * Skips state restoration (player is offline) but cleans up all maps to prevent memory leaks.
     */
    public void forceCleanup(UUID playerUuid) {
        scheduler.getCinematicPlayerIds().remove(playerUuid);
        debugPlayers.remove(playerUuid);
        // Find and remove the session from the scheduler directly
        for (PlaybackSession session : scheduler.getActiveSessions()) {
            if (session.getPlayer().getUniqueId().equals(playerUuid)) {
                scheduler.getCinematicEntityIds().remove(session.getPlayer().getEntityId());
                scheduler.removeSession(session.getSessionId());
                stateRestorer.deleteBackup(session.getSessionId());
                break;
            }
        }
    }

    @Override
    public void pause(Player player) {
        PlaybackSession session = getSession(player);
        if (session != null) {
            session.setPaused(true);
        }
    }

    @Override
    public void resume(Player player) {
        PlaybackSession session = getSession(player);
        if (session != null) {
            session.setPaused(false);
        }
    }

    @Override
    public void skip(Player player) {
        PlaybackSession session = getSession(player);
        if (session != null) {
            session.setCurrentTick(session.getCinematic().getTimeline().getDurationTicks());
            // In the next tick, the scheduler will finish it.
        }
    }

    @Override
    public boolean isPlaying(Player player) {
        return getSession(player) != null;
    }

    @Override
    public PlaybackSession getSession(Player player) {
        for (PlaybackSession session : scheduler.getActiveSessions()) {
            if (session.getPlayer().getUniqueId().equals(player.getUniqueId())) {
                return session;
            }
        }
        return null;
    }

    public boolean isDebugEnabled(Player player) {
        return debugPlayers.contains(player.getUniqueId());
    }

    public void toggleDebug(Player player) {
        if (isDebugEnabled(player)) {
            debugPlayers.remove(player.getUniqueId());
        } else {
            debugPlayers.add(player.getUniqueId());
        }
    }
}
