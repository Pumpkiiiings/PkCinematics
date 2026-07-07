package com.pumpkiiiings.pkcinematics.api.condition;

import java.util.HashMap;
import java.util.Map;

public class ConditionRegistry {
    private final Map<String, Class<? extends Condition>> registry = new HashMap<>();

    public void registerCondition(String type, Class<? extends Condition> conditionClass) {
        registry.put(type.toLowerCase(), conditionClass);
    }
    
    public Class<? extends Condition> getConditionClass(String type) {
        return registry.get(type.toLowerCase());
    }
}
