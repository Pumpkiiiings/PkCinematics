package com.pumpkiiiings.pkcinematics.engine.scheduler;

import com.pumpkiiiings.pkcinematics.api.PkCinematics;
import com.pumpkiiiings.pkcinematics.api.action.PkAction;
import com.pumpkiiiings.pkcinematics.api.action.SimpleActionContext;
import com.pumpkiiiings.pkcinematics.api.camera.CameraController;
import com.pumpkiiiings.pkcinematics.engine.session.PlaybackSession;
import com.pumpkiiiings.pkcinematics.model.timeline.CameraKeyframe;
import org.bukkit.Location;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class CinematicScheduler extends BukkitRunnable {
    private final ConcurrentHashMap<UUID, PlaybackSession> activeSessions = new ConcurrentHashMap<>();
    private final CameraController cameraController;

    public CinematicScheduler(CameraController cameraController) {
        this.cameraController = cameraController;
    }

    public void addSession(PlaybackSession session) {
        activeSessions.put(session.getSessionId(), session);
        // Start spectating immediately at tick 0
        CameraKeyframe initialPoint = session.getCinematic().getTimeline().getCameraTrack().getInterpolatedPoint(0);
        if (initialPoint != null) {
            cameraController.startSpectating(session.getPlayer(), initialPoint);
        }
    }

    public void removeSession(UUID sessionId) {
        PlaybackSession session = activeSessions.remove(sessionId);
        if (session != null) {
            cameraController.stopSpectating(session.getPlayer());
        }
    }

    public PlaybackSession getSession(UUID sessionId) {
        return activeSessions.get(sessionId);
    }
    
    public Collection<PlaybackSession> getActiveSessions() {
        return activeSessions.values();
    }

    @Override
    public void run() {
        for (PlaybackSession session : activeSessions.values()) {
            if (session.isPaused()) continue;

            int currentTick = session.getCurrentTick();
            
            // 1. Execute actions for current tick
            List<PkAction> actions = session.getCinematic().getTimeline().getActionTrack().getActionsAt(currentTick);
            if (!actions.isEmpty()) {
                SimpleActionContext context = new SimpleActionContext(session.getPlayer(), session);
                for (PkAction action : actions) {
                    action.execute(context);
                    if (((com.pumpkiiiings.pkcinematics.core.PlaybackManagerImpl)PkCinematics.getApi().getPlaybackManager()).isDebugEnabled(session.getPlayer())) {
                        session.getPlayer().sendMessage("§8[§cDebug§8] §eAcción §b" + action.getClass().getSimpleName() + " §eejecutada en tick §a" + currentTick);
                    }
                }
            }

            // 2. Update Camera
            CameraKeyframe currentPoint = session.getCinematic().getTimeline().getCameraTrack().getInterpolatedPoint(currentTick);
            if (currentPoint != null) {
                cameraController.updatePosition(session.getPlayer(), currentPoint);
                
                // Force chunk loading by teleporting the physical body every 10 ticks (0.5s)
                // The client won't jitter because CameraPacketListener blocks the position packet!
                if (currentTick % 10 == 0) {
                    Location loc = new Location(
                        org.bukkit.Bukkit.getWorld(currentPoint.getWorldName()),
                        currentPoint.getX(),
                        currentPoint.getY(),
                        currentPoint.getZ(),
                        currentPoint.getYaw(),
                        currentPoint.getPitch()
                    );
                    if (loc.getWorld() != null) {
                        session.getPlayer().teleport(loc);
                    }
                }
            }

            // 3. Advance time
            session.setCurrentTick(currentTick + 1);
            
            // 4. Debug Action Bar
            if (((com.pumpkiiiings.pkcinematics.core.PlaybackManagerImpl)PkCinematics.getApi().getPlaybackManager()).isDebugEnabled(session.getPlayer())) {
                String actionBar = "§eCinemática: §b" + session.getCinematic().getId() + " §8| §eTick: §a" + currentTick + " §8| §eSegundo: §a" + (currentTick / Double.parseDouble("20.0")) + "s";
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
    }
}
