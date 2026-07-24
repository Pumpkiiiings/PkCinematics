package com.pumpkiiiings.pkcinematics.api.trigger;

import com.pumpkiiiings.pkcinematics.api.action.PkAction;
import com.pumpkiiiings.pkcinematics.api.condition.Condition;
import java.util.List;

public class PkTrigger {
    private final String id;
    private final String type;
    private final List<Condition> conditions;
    private final List<PkAction> actions;
    private final boolean runOnce;
    private final String target;
    private final java.util.Map<String, String> properties;

    public PkTrigger(String id, String type, List<Condition> conditions, List<PkAction> actions, boolean runOnce, String target, java.util.Map<String, String> properties) {
        this.id = id;
        this.type = type.toLowerCase();
        this.conditions = conditions;
        this.actions = actions;
        this.runOnce = runOnce;
        this.target = target;
        this.properties = properties;
    }

    public String getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public List<Condition> getConditions() {
        return conditions;
    }

    public List<PkAction> getActions() {
        return actions;
    }
    
    public boolean isRunOnce() {
        return runOnce;
    }
    
    public String getTarget() {
        return target;
    }
    
    public String getProperty(String key) {
        return properties != null ? properties.get(key) : null;
    }
}
