package com.pumpkiiiings.pkcinematics.config;

import com.pumpkiiiings.pkcinematics.api.PkCinematics;

public enum Messages {
    PREFIX("prefix"),
    NO_PERMISSION("no_permission"),
    ALREADY_EXISTS("already_exists"),
    NOT_FOUND("not_found"),
    SAVED("saved"),
    
    EDITOR_START("editor_start"),
    EDITOR_STOP("editor_stop"),
    EDITOR_NOT_EDITING("editor_not_editing"),
    EDITOR_POINT_ADDED("editor_point_added"),
    EDITOR_INVALID_INDEX("editor_invalid_index"),
    EDITOR_INVALID_PROPERTY("editor_invalid_property"),
    EDITOR_TICK_UPDATED("editor_tick_updated"),
    EDITOR_FOV_UPDATED("editor_fov_updated"),
    EDITOR_INTERP_UPDATED("editor_interp_updated"),
    EDITOR_INVALID_NUMBER("editor_invalid_number"),
    EDITOR_INVALID_DECIMAL("editor_invalid_decimal"),
    EDITOR_INVALID_WAIT_TICKS("editor_invalid_wait_ticks"),
    EDITOR_WAIT_TIME_ADDED("editor_wait_time_added"),
    EDITOR_ACTION_ADDED("editor_action_added"),
    EDITOR_ACTION_INVALID_TICK("editor_action_invalid_tick"),
    
    GUI_MAIN_TITLE("gui.main_title"),
    GUI_ADD_TITLE("gui.add_title"),
    GUI_ADD_ACTION_BTN("gui.add_action_btn"),
    GUI_ACTION_ITEM_NAME("gui.action_item_name"),
    GUI_ADD_TITLE_BTN("gui.add_title_btn"),
    
    CHAT_INPUT_TITLE("chat_input_title"),
    CHAT_INPUT_CANCEL_HINT("chat_input_cancel_hint"),
    CHAT_INPUT_CANCELLED("chat_input_cancelled"),
    CHAT_INPUT_SUCCESS("chat_input_success"),
    ACTION_REMOVED("action_removed"),
    
    PLAYBACK_PLAYING("playback_playing"),
    PLAYBACK_STOPPED("playback_stopped"),
    STATE_RESTORED("state_restored"),
    STATE_RESTORE_ERROR("state_restore_error"),
    
    HELP_HEADER("help_header"),
    HELP_CREATE("help_create"),
    HELP_EDIT("help_edit"),
    HELP_POINT("help_point"),
    HELP_POINT_EDIT("help_point_edit"),
    HELP_ACTIONS("help_actions"),
    HELP_SAVE("help_save"),
    HELP_PLAY("help_play"),
    HELP_STOP("help_stop"),
    HELP_RELOAD("help_reload"),
    
    DEBUG_ACTION_EXECUTED("debug_action_executed"),
    RELOADED("reloaded");

    private final String path;

    Messages(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    public String get() {
        return PkCinematics.getApi().getMessageManager().getMessage(path);
    }

    public String get(Object... replacements) {
        return PkCinematics.getApi().getMessageManager().getMessage(path, replacements);
    }
    
    public String getWithPrefix() {
        return PREFIX.get() + get();
    }
    
    public String getWithPrefix(Object... replacements) {
        return PREFIX.get() + get(replacements);
    }
}
