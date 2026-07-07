package com.pumpkiiiings.pkcinematics.api.action;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ActionRegistry {
    private final Map<String, Class<? extends PkAction>> registry = new HashMap<>();

    public void registerAction(String type, Class<? extends PkAction> actionClass) {
        registry.put(type.toLowerCase(), actionClass);
    }
    
    public Class<? extends PkAction> getActionClass(String type) {
        return registry.get(type.toLowerCase());
    }
    
    public Set<String> getRegisteredTypes() {
        return registry.keySet();
    }
}
