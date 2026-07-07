package com.pumpkiiiings.pkcinematics.api.storage;

import com.pumpkiiiings.pkcinematics.model.Cinematic;
import java.util.Collection;

public interface CinematicRepository {
    
    /**
     * Loads all cinematics from the storage.
     * @return A collection of loaded cinematics.
     */
    Collection<Cinematic> loadAll();
    
    /**
     * Loads a specific cinematic.
     * @param id The cinematic ID.
     * @return The cinematic, or null if it doesn't exist.
     */
    Cinematic load(String id);
    
    /**
     * Saves a cinematic to the storage.
     * @param cinematic The cinematic.
     */
    void save(Cinematic cinematic);
    
    /**
     * Deletes a cinematic from the storage.
     * @param id The cinematic ID.
     */
    void delete(String id);
}
