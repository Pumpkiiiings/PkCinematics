package com.pumpkiiiings.pkcinematics.engine.session;

import com.pumpkiiiings.pkcinematics.model.Cinematic;
import org.bukkit.entity.Player;
import java.util.UUID;

public class PlaybackSession {
    private final UUID sessionId;
    private final Player player;
    private final Cinematic cinematic;
    private PlayerState savedState;
    private int currentTick;
    private boolean paused;

    public PlaybackSession(Player player, Cinematic cinematic) {
        this.sessionId = UUID.randomUUID();
        this.player = player;
        this.cinematic = cinematic;
        this.currentTick = 0;
        this.paused = false;
    }

    public UUID getSessionId() {
        return sessionId;
    }

    public Player getPlayer() {
        return player;
    }

    public Cinematic getCinematic() {
        return cinematic;
    }

    public PlayerState getSavedState() {
        return savedState;
    }

    public void setSavedState(PlayerState savedState) {
        this.savedState = savedState;
    }

    public int getCurrentTick() {
        return currentTick;
    }

    public void setCurrentTick(int currentTick) {
        this.currentTick = currentTick;
    }

    public boolean isPaused() {
        return paused;
    }

    public void setPaused(boolean paused) {
        this.paused = paused;
    }
}
