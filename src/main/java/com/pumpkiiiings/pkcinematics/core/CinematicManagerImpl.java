package com.pumpkiiiings.pkcinematics.core;

import com.pumpkiiiings.pkcinematics.api.CinematicManager;
import com.pumpkiiiings.pkcinematics.model.Cinematic;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

public class CinematicManagerImpl implements CinematicManager {
    
    private final ConcurrentHashMap<String, Cinematic> cache = new ConcurrentHashMap<>();

    @Override
    public Cinematic getCinematic(String id) {
        return cache.get(id.toLowerCase());
    }

    @Override
    public void registerCinematic(Cinematic cinematic) {
        cache.put(cinematic.getId().toLowerCase(), cinematic);
    }

    @Override
    public void unregisterCinematic(String id) {
        cache.remove(id.toLowerCase());
    }

    @Override
    public Collection<Cinematic> getAllCinematics() {
        return cache.values();
    }
}
