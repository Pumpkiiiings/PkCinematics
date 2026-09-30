package com.pumpkiiiings.pkcinematics.engine.scheduler;

import com.pumpkiiiings.pkcinematics.api.PkCinematics;
import com.pumpkiiiings.pkcinematics.api.action.PkAction;
import com.pumpkiiiings.pkcinematics.api.action.SimpleActionContext;
import com.pumpkiiiings.pkcinematics.api.camera.CameraController;
import com.pumpkiiiings.pkcinematics.engine.session.PlaybackSession;
import com.pumpkiiiings.pkcinematics.model.timeline.CameraKeyframe;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.plugin.Plugin;
import io.papermc.paper.threadedregions.scheduler.ScheduledTask;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import com.pumpkiiiings.pkcinematics.core.PlaybackManagerImpl;
import org.bukkit.Bukkit;

public class CinematicScheduler {
    private final ConcurrentHashMap<UUID, PlaybackSession> activeSessions = new ConcurrentHashMap<>();
    private final CameraController cameraController;
    /**
     * Shared set of UUIDs of all players currently in a cinematic.
     * Read by CameraPacketListener on every outgoing SPAWN_ENTITY packet — must be thread-safe.
     */
    private final Set<UUID> cinematicPlayerIds = ConcurrentHashMap.newKeySet();
    /**
     * Shared set of entity IDs (int) of players currently in a cinematic.
     * Used by CameraPacketListener to cancel SPAWN_ENTITY packets via getEntityId(),
     * which is always available unlike getEntityUUID() in some PacketEvents builds.
     */
    private final Set<Integer> cinematicEntityIds = ConcurrentHashMap.newKeySet();

    /**
     * Tracks chunk tickets added per session so they can be removed on cleanup.
     * Key: sessionId, Value: set of "worldName:chunkX:chunkZ" strings.
     */
    private final ConcurrentHashMap<UUID, Set<String>> sessionChunkTickets = new ConcurrentHashMap<>();

    private final Plugin plugin;

    public CinematicScheduler(Plugin plugin, CameraController cameraController) {
        this.plugin = plugin;
        this.cameraController = cameraController;
    }

    public Set<UUID> getCinematicPlayerIds() {
        return cinematicPlayerIds;
    }

    public Set<Integer> getCinematicEntityIds() {
        return cinematicEntityIds;
    }

    public void addSession(PlaybackSession session) {
        activeSessions.put(session.getSessionId(), session);
        sessionChunkTickets.put(session.getSessionId(), ConcurrentHashMap.newKeySet());
        // Start spectating immediately at tick 0
        CameraKeyframe initialPoint = session.getCinematic().getTimeline().getCameraTrack().getInterpolatedPoint(0);
        if (initialPoint != null) {
            cameraController.startSpectating(session.getPlayer(), initialPoint);
        }

        // Schedule Folia per-entity task
        ScheduledTask task = session.getPlayer().getScheduler().runAtFixedRate(plugin, (scheduledTask) -> {
            tickSession(session);
        }, null, 1L, 1L);
        session.setScheduledTask(task);
    }

    public void removeSession(UUID sessionId) {
        PlaybackSession session = activeSessions.remove(sessionId);
        if (session != null) {
            cameraController.stopSpectating(session.getPlayer());
            if (session.getScheduledTask() != null) {
                session.getScheduledTask().cancel();
            }
            // Remove all chunk tickets added during this session
            removeChunkTickets(sessionId);
        }
    }

    public PlaybackSession getSession(UUID sessionId) {
        return activeSessions.get(sessionId);
    }
    
    public Collection<PlaybackSession> getActiveSessions() {
        return activeSessions.values();
    }

    private void tickSession(PlaybackSession session) {
        if (session.isPaused()) return;

        int currentTick = session.getCurrentTick();
        
        // 1. Execute actions for current tick
        List<PkAction> actions = session.getCinematic().getTimeline().getActionTrack().getActionsAt(currentTick);
        if (!actions.isEmpty()) {
            SimpleActionContext context = new SimpleActionContext(session.getPlayer(), session);
            for (PkAction action : actions) {
                action.execute(context);
                if (((PlaybackManagerImpl)PkCinematics.getApi().getPlaybackManager()).isDebugEnabled(session.getPlayer())) {
                    session.getPlayer().sendMessage(com.pumpkiiiings.pkcinematics.config.Messages.DEBUG_ACTION_EXECUTED.get("type", action.getClass().getSimpleName(), "tick", String.valueOf(currentTick)));
                }
            }
        }

        // 2. Update Camera
        CameraKeyframe currentPoint = session.getCinematic().getTimeline().getCameraTrack().getInterpolatedPoint(currentTick);
        if (currentPoint != null) {
            cameraController.updatePosition(session.getPlayer(), currentPoint);
            
            // Force chunk loading using chunk tickets instead of teleportAsync.
            // teleportAsync in 1.21.2+/26.x causes teleport confirmation desync because
            // CameraPacketListener cancels PLAYER_POSITION_AND_LOOK, preventing the client
            // from sending TeleportConfirm — leading to rubberbanding or kicks.
            if (currentTick % 10 == 0) {
                World world = Bukkit.getWorld(currentPoint.getWorldName());
                if (world != null) {
                    int chunkX = (int) Math.floor(currentPoint.getX()) >> 4;
                    int chunkZ = (int) Math.floor(currentPoint.getZ()) >> 4;
                    String ticketKey = currentPoint.getWorldName() + ":" + chunkX + ":" + chunkZ;
                    Set<String> tickets = sessionChunkTickets.get(session.getSessionId());
                    if (tickets != null && tickets.add(ticketKey)) {
                        world.addPluginChunkTicket(chunkX, chunkZ, plugin);
                    }
                }
            }
        }

        // 3. Advance time
        session.setCurrentTick(currentTick + 1);
        
        // 4. Debug Action Bar
        if (((PlaybackManagerImpl)PkCinematics.getApi().getPlaybackManager()).isDebugEnabled(session.getPlayer())) {
            String actionBar = "§eCinematic: §b" + session.getCinematic().getId() + " §8| §eTick: §a" + currentTick + " §8| §eSecond: §a" + (currentTick / Double.parseDouble("20.0")) + "s";
            session.getPlayer().sendActionBar(actionBar);
        }

        // 5. Check if finished
        if (session.getCurrentTick() > session.getCinematic().getTimeline().getDurationTicks()) {
            // Remove session and stop spectating
            // Ideally we'd call PlaybackManager.stop() to handle state restoration properly.
            // For now, we'll just let the manager handle it or fire an event.
            PkCinematics.getApi().getPlaybackManager().stop(session.getPlayer());
        }
    }

    /**
     * Removes all plugin chunk tickets that were added during a session.
     */
    private void removeChunkTickets(UUID sessionId) {
        Set<String> tickets = sessionChunkTickets.remove(sessionId);
        if (tickets == null) return;
        for (String ticketKey : tickets) {
            String[] parts = ticketKey.split(":");
            if (parts.length != 3) continue;
            World world = Bukkit.getWorld(parts[0]);
            if (world != null) {
                try {
                    int chunkX = Integer.parseInt(parts[1]);
                    int chunkZ = Integer.parseInt(parts[2]);
                    world.removePluginChunkTicket(chunkX, chunkZ, plugin);
                } catch (NumberFormatException ignored) {}
            }
        }
    }
}
