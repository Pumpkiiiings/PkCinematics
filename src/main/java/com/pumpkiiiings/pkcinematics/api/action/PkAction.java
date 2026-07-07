package com.pumpkiiiings.pkcinematics.api.action;

import java.util.Map;

public interface PkAction {
    
    /**
     * Executes the action within a specific context.
     * @param context The action context containing player and variables.
     */
    void execute(ActionContext context);
    
    /**
     * Serializes the action parameters to a map for saving.
     */
    Map<String, Object> serialize();
    
    /**
     * Deserializes the action parameters from a map.
     */
    void deserialize(Map<String, Object> data);
    
    /**
     * Gets the type identifier of this action.
     */
    String getType();
}
