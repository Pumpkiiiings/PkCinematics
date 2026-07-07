package com.pumpkiiiings.pkcinematics.api;

import com.pumpkiiiings.pkcinematics.model.Cinematic;
import java.util.Collection;

public interface CinematicManager {
    
    /**
     * Gets a cinematic by its ID (name).
     * @param id The id of the cinematic.
     * @return The cinematic, or null if not found.
     */
    Cinematic getCinematic(String id);
    
    /**
     * Caches a cinematic in memory.
     * @param cinematic The cinematic to cache.
     */
    void registerCinematic(Cinematic cinematic);
    
    /**
     * Unregisters a cinematic from memory.
     * @param id The ID of the cinematic.
     */
    void unregisterCinematic(String id);
    
    /**
     * Gets all registered cinematics.
     * @return A collection of all cinematics.
     */
    Collection<Cinematic> getAllCinematics();
}
