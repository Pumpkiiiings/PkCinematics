package com.pumpkiiiings.pkcinematics.api;

import com.pumpkiiiings.pkcinematics.api.action.ActionRegistry;
import com.pumpkiiiings.pkcinematics.api.condition.ConditionRegistry;
import com.pumpkiiiings.pkcinematics.api.trigger.TriggerManager;
import com.pumpkiiiings.pkcinematics.api.trigger.TriggerRegistry;

public interface PkCinematics {
    
    /**
     * Get the CinematicManager instance.
     * @return the cinematic manager
     */
    CinematicManager getCinematicManager();
    
    /**
     * Get the PlaybackManager instance.
     * @return the playback manager
     */
    PlaybackManager getPlaybackManager();
    
    /**
     * Get the ActionRegistry instance.
     * @return the action registry
     */
    ActionRegistry getActionRegistry();

    /**
     * Get the ConditionRegistry instance.
     * @return the condition registry
     */
    ConditionRegistry getConditionRegistry();

    /**
     * Get the TriggerManager instance.
     * @return the trigger manager
     */
    TriggerManager getTriggerManager();
    
    /**
     * Get the TriggerRegistry instance.
     * @return the trigger registry
     */
    TriggerRegistry getTriggerRegistry();
    
    /**
     * Get the EditorManager instance.
     */
    com.pumpkiiiings.pkcinematics.editor.EditorManager getEditorManager();
    
    /**
     * Get the MessageManager instance.
     */
    com.pumpkiiiings.pkcinematics.config.MessageManager getMessageManager();

    /**
     * Gets the static API instance.
     * @return The API instance
     */
    static PkCinematics getApi() {
        return PkCinematicsProvider.get();
    }
}
