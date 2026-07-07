package com.pumpkiiiings.pkcinematics.action.impl;

import com.pumpkiiiings.pkcinematics.api.action.ActionContext;
import com.pumpkiiiings.pkcinematics.api.action.PkAction;
import org.bukkit.Bukkit;

import java.util.HashMap;
import java.util.Map;

public class CommandAction implements PkAction {

    private String command;
    private boolean asConsole;

    public CommandAction() {}

    public CommandAction(String command, boolean asConsole) {
        this.command = command;
        this.asConsole = asConsole;
    }

    @Override
    public void execute(ActionContext context) {
        String parsedCommand = this.command;
        for (Map.Entry<String, Object> entry : context.getVariables().entrySet()) {
            parsedCommand = parsedCommand.replace("%" + entry.getKey() + "%", String.valueOf(entry.getValue()));
        }

        if (asConsole) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), parsedCommand);
        } else {
            context.getPlayer().performCommand(parsedCommand);
        }
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        map.put("type", getType());
        map.put("command", command);
        map.put("console", asConsole);
        return map;
    }

    @Override
    public void deserialize(Map<String, Object> data) {
        this.command = (String) data.getOrDefault("command", "");
        this.asConsole = (Boolean) data.getOrDefault("console", false);
    }

    @Override
    public String getType() {
        return "command";
    }
}
