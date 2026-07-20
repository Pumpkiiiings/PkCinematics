package com.pumpkiiiings.pkcinematics.editor.gui;

import com.pumpkiiiings.pkcinematics.api.PkCinematics;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import com.pumpkiiiings.pkcinematics.core.PkCinematicsPlugin;

public class ChatInputManager implements Listener {

    private final Map<UUID, Consumer<String>> pendingInputs = new ConcurrentHashMap<>();

    public void requestInput(Player player, String promptMessage, Consumer<String> onInput) {
        player.closeInventory();
        player.sendMessage(promptMessage);
        player.sendMessage(com.pumpkiiiings.pkcinematics.config.Messages.CHAT_INPUT_CANCEL_HINT.getWithPrefix());
        pendingInputs.put(player.getUniqueId(), onInput);
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        if (pendingInputs.containsKey(player.getUniqueId())) {
            event.setCancelled(true);
            Consumer<String> callback = pendingInputs.remove(player.getUniqueId());
            String message = event.getMessage();
            
            Bukkit.getScheduler().runTask(PkCinematicsPlugin.getPlugin(PkCinematicsPlugin.class), () -> {
                if (message.equalsIgnoreCase("cancelar")) {
                    player.sendMessage(com.pumpkiiiings.pkcinematics.config.Messages.CHAT_INPUT_CANCELLED.getWithPrefix());
                    // Reopen main menu? We would need to pass it, but for now we just cancel.
                    return;
                }
                callback.accept(message);
            });
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        pendingInputs.remove(event.getPlayer().getUniqueId());
    }
}
