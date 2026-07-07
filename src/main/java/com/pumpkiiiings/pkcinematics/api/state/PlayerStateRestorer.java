package com.pumpkiiiings.pkcinematics.api.state;

import com.pumpkiiiings.pkcinematics.engine.session.PlayerState;
import org.bukkit.entity.Player;
import java.util.UUID;

public interface PlayerStateRestorer {
    
    /**
     * Captures the current state of a player.
     * @param player The player.
     * @return The captured state.
     */
    PlayerState captureState(Player player);
    
    /**
     * Restores the state of a player.
     * @param player The player.
     * @param state The state to restore.
     */
    void restoreState(Player player, PlayerState state);
    
    /**
     * Saves a state to disk for crash recovery.
     * @param sessionUuid The session UUID.
     * @param playerUuid The player UUID.
     * @param state The state to save.
     */
    void saveBackup(UUID sessionUuid, UUID playerUuid, PlayerState state);
    
    /**
     * Deletes a state backup from disk.
     * @param sessionUuid The session UUID.
     */
    void deleteBackup(UUID sessionUuid);
    
    /**
     * Checks if there are any orphaned backups and restores them if the player is online.
     * This is usually called on player join.
     * @param player The player.
     */
    void checkAndRestoreBackups(Player player);
}
