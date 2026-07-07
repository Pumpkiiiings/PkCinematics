package com.pumpkiiiings.pkcinematics.editor;

import com.pumpkiiiings.pkcinematics.api.PkCinematics;
import com.pumpkiiiings.pkcinematics.model.Cinematic;
import org.bukkit.entity.Player;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class EditorManager {
    
    private final ConcurrentHashMap<UUID, EditorSession> activeEditors = new ConcurrentHashMap<>();

    public void startEditing(Player player, Cinematic cinematic) {
        activeEditors.put(player.getUniqueId(), new EditorSession(player, cinematic));
        com.pumpkiiiings.pkcinematics.config.MessageManager msg = PkCinematics.getApi().getMessageManager();
        player.sendMessage(msg.getMessage("prefix") + msg.getMessage("editor_start", "name", cinematic.getId()));
    }

    public void stopEditing(Player player) {
        EditorSession session = activeEditors.remove(player.getUniqueId());
        if (session != null) {
            com.pumpkiiiings.pkcinematics.config.MessageManager msg = PkCinematics.getApi().getMessageManager();
            player.sendMessage(msg.getMessage("prefix") + msg.getMessage("editor_stop", "name", session.getCinematic().getId()));
        }
    }

    public EditorSession getSession(Player player) {
        return activeEditors.get(player.getUniqueId());
    }
}
