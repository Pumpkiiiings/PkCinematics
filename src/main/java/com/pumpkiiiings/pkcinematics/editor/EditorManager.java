package com.pumpkiiiings.pkcinematics.editor;

import com.pumpkiiiings.pkcinematics.api.PkCinematics;
import com.pumpkiiiings.pkcinematics.core.PkCinematicsPlugin;
import com.pumpkiiiings.pkcinematics.model.Cinematic;
import org.bukkit.entity.Player;
import java.util.UUID;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import com.pumpkiiiings.pkcinematics.config.Messages;
import org.bukkit.Bukkit;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

public class EditorManager {
    
    private final Map<UUID, EditorSession> activeSessions = new ConcurrentHashMap<>();
    private final PkCinematicsPlugin plugin;

    public EditorManager(PkCinematicsPlugin plugin) {
        this.plugin = plugin;
        
        // Iniciar tarea asíncrona para el Actionbar
        Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, () -> {
            for (Map.Entry<UUID, EditorSession> entry : activeSessions.entrySet()) {
                Player player = Bukkit.getPlayer(entry.getKey());
                if (player != null && player.isOnline()) {
                    Cinematic cin = entry.getValue().getCinematic();
                    int points = cin.getTimeline().getCameraTrack().getKeyframes().size();
                    int duration = cin.getTimeline().getDurationTicks();
                    String msg = "§eCinematic: §f" + cin.getId() + " §8| §ePoints: §f" + points + " §8| §eDuration: §f" + duration + " ticks §8| §cUnsaved";
                    player.sendActionBar(LegacyComponentSerializer.legacySection().deserialize(msg));
                }
            }
        }, 20L, 20L);
    }

    public void startEditing(Player player, Cinematic cinematic) {
        activeSessions.put(player.getUniqueId(), new EditorSession(player, cinematic));
        player.sendMessage(Messages.EDITOR_START.getWithPrefix("name", cinematic.getId()));
    }

    public void stopEditing(Player player) {
        EditorSession session = activeSessions.remove(player.getUniqueId());
        if (session != null) {
            player.sendMessage(Messages.EDITOR_STOP.getWithPrefix("name", session.getCinematic().getId()));
        }
    }

    public EditorSession getSession(Player player) {
        return activeSessions.get(player.getUniqueId());
    }
}
