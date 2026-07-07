package com.pumpkiiiings.pkcinematics.editor;

import com.pumpkiiiings.pkcinematics.model.Cinematic;
import org.bukkit.entity.Player;
import java.util.UUID;

public class EditorSession {
    private final Player player;
    private final Cinematic cinematic;
    
    // For tracking which node they are currently inserting after, or defaults to end.
    private int currentEditingTick = -1; 

    public EditorSession(Player player, Cinematic cinematic) {
        this.player = player;
        this.cinematic = cinematic;
    }

    public Player getPlayer() {
        return player;
    }

    public Cinematic getCinematic() {
        return cinematic;
    }

    public int getCurrentEditingTick() {
        return currentEditingTick;
    }

    public void setCurrentEditingTick(int currentEditingTick) {
        this.currentEditingTick = currentEditingTick;
    }
}
