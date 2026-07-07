package com.pumpkiiiings.pkcinematics.api.trigger;

import java.util.HashSet;
import java.util.Set;

public class TriggerRegistry {
    private final Set<String> registeredTypes = new HashSet<>();

    public void registerTriggerType(String type) {
        registeredTypes.add(type.toLowerCase());
    }

    public boolean isRegistered(String type) {
        return registeredTypes.contains(type.toLowerCase());
    }
}
