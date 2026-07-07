package com.pumpkiiiings.pkcinematics.api.camera;

import com.pumpkiiiings.pkcinematics.model.timeline.CameraKeyframe;
import org.bukkit.entity.Player;

public interface CameraController {
    
    /**
     * Prepares the camera for the player.
     * Often puts them in spectator mode and assigns a fake entity.
     * @param player The player.
     * @param initialPoint The starting camera point.
     */
    void startSpectating(Player player, CameraKeyframe initialPoint);
    
    /**
     * Updates the camera position and rotation.
     * @param player The player.
     * @param currentPoint The current calculated point.
     */
    void updatePosition(Player player, CameraKeyframe currentPoint);
    
    /**
     * Stops spectating and removes any fake entities.
     * @param player The player.
     */
    void stopSpectating(Player player);
}
