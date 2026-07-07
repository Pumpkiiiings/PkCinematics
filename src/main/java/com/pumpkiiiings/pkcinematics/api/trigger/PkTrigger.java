package com.pumpkiiiings.pkcinematics.api.trigger;

import com.pumpkiiiings.pkcinematics.api.action.PkAction;
import com.pumpkiiiings.pkcinematics.api.condition.Condition;
import java.util.List;

public class PkTrigger {
    private final String id;
    private final String type;
    private final List<Condition> conditions;
    private final List<PkAction> actions;

    public PkTrigger(String id, String type, List<Condition> conditions, List<PkAction> actions) {
        this.id = id;
        this.type = type.toLowerCase();
        this.conditions = conditions;
        this.actions = actions;
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
}
