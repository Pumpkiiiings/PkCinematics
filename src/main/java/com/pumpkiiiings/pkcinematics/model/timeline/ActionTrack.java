package com.pumpkiiiings.pkcinematics.model.timeline;

import com.pumpkiiiings.pkcinematics.api.action.PkAction;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActionTrack {
    private final Map<Integer, List<PkAction>> actionsByTick;

    public ActionTrack() {
        this.actionsByTick = new HashMap<>();
    }

    public void addAction(int tick, PkAction action) {
        actionsByTick.computeIfAbsent(tick, k -> new ArrayList<>()).add(action);
    }
    
    public void removeAction(int tick, PkAction action) {
        List<PkAction> actions = actionsByTick.get(tick);
        if (actions != null) {
            actions.remove(action);
            if (actions.isEmpty()) {
                actionsByTick.remove(tick);
            }
        }
    }

    public List<PkAction> getActionsAt(int tick) {
        return actionsByTick.getOrDefault(tick, new ArrayList<>());
    }

    public Map<Integer, List<PkAction>> getAllActions() {
        return actionsByTick;
    }
}
