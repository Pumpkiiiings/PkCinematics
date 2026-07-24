package com.pumpkiiiings.pkcinematics.listener;

import com.pumpkiiiings.pkcinematics.core.UpdateChecker;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class UpdateListener implements Listener {

    private final UpdateChecker updateChecker;

    public UpdateListener(UpdateChecker updateChecker) {
        this.updateChecker = updateChecker;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (player.isOp() && updateChecker.isUpdateAvailable()) {
            if (updateChecker.isImportant()) {
                player.sendMessage(MiniMessage.miniMessage().deserialize("<red>[PkCinematics] An IMPORTANT update is available!</red>"));
            } else {
                player.sendMessage(MiniMessage.miniMessage().deserialize("<yellow>[PkCinematics] An update is available!</yellow>"));
            }
            
            for (Component msg : updateChecker.getUpdateMessages()) {
                player.sendMessage(msg);
            }
            
            player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f);
        }
    }
}
