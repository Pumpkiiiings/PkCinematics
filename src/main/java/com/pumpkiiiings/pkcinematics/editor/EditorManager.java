package com.pumpkiiiings.pkcinematics.editor;

import com.pumpkiiiings.pkcinematics.api.PkCinematics;
import com.pumpkiiiings.pkcinematics.model.Cinematic;
import org.bukkit.entity.Player;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import com.pumpkiiiings.pkcinematics.config.MessageManager;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

public class EditorManager {
    
    private final ConcurrentHashMap<UUID, EditorSession> activeEditors = new ConcurrentHashMap<>();
    private final Plugin plugin;

    public EditorManager(Plugin plugin) {
        this.plugin = plugin;
        startActionBarTask();
    }

    private void startActionBarTask() {
        Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, () -> {
            for (EditorSession session : activeEditors.values()) {
                Player player = session.getPlayer();
                if (player != null && player.isOnline()) {
                    Cinematic cin = session.getCinematic();
                    int points = cin.getTimeline().getCameraTrack().getKeyframes().size();
                    int duration = cin.getTimeline().getDurationTicks();
                    String msg = "§eCinemática: §f" + cin.getId() + " §8| §ePuntos: §f" + points + " §8| §eDuración: §f" + duration + " ticks §8| §cSin guardar";
                    player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(msg));
                }
            }
        }, 20L, 20L);
    }

    public void startEditing(Player player, Cinematic cinematic) {
        activeEditors.put(player.getUniqueId(), new EditorSession(player, cinematic));
        MessageManager msg = PkCinematics.getApi().getMessageManager();
        player.sendMessage(msg.getMessage("prefix") + msg.getMessage("editor_start", "name", cinematic.getId()));
    }

    public void stopEditing(Player player) {
        EditorSession session = activeEditors.remove(player.getUniqueId());
        if (session != null) {
            MessageManager msg = PkCinematics.getApi().getMessageManager();
            player.sendMessage(msg.getMessage("prefix") + msg.getMessage("editor_stop", "name", session.getCinematic().getId()));
        }
    }

    public EditorSession getSession(Player player) {
        return activeEditors.get(player.getUniqueId());
    }
}
