package com.pumpkiiiings.pkcinematics.listener;

import com.pumpkiiiings.pkcinematics.api.PkCinematics;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerResourcePackStatusEvent;

public class TriggerListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        org.bukkit.plugin.Plugin plugin = org.bukkit.plugin.java.JavaPlugin.getProvidingPlugin(TriggerListener.class);
        
        // Hide this new player from anyone currently watching a cinematic
        for (com.pumpkiiiings.pkcinematics.engine.session.PlaybackSession session : 
                ((com.pumpkiiiings.pkcinematics.core.PlaybackManagerImpl) PkCinematics.getApi().getPlaybackManager()).getScheduler().getActiveSessions()) {
            org.bukkit.entity.Player cinematicPlayer = session.getPlayer();
            if (cinematicPlayer != null && cinematicPlayer.isOnline()) {
                cinematicPlayer.hideEntity(plugin, event.getPlayer());
            }
        }
        
        org.bukkit.Bukkit.getScheduler().runTaskLater(plugin, () -> {
            if (!event.getPlayer().hasPlayedBefore()) {
                PkCinematics.getApi().getTriggerManager().fire("first_join", event.getPlayer());
            }
            PkCinematics.getApi().getTriggerManager().fire("join", event.getPlayer());
        }, 15L); // 15 ticks = 0.75 seconds delay
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        PkCinematics.getApi().getTriggerManager().fire("quit", event.getPlayer());
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        PkCinematics.getApi().getTriggerManager().fire("death", event.getEntity());
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        PkCinematics.getApi().getTriggerManager().fire("respawn", event.getPlayer());
    }

    @EventHandler
    public void onPlayerChangedWorld(PlayerChangedWorldEvent event) {
        PkCinematics.getApi().getTriggerManager().fire("world_change", event.getPlayer());
    }

    @EventHandler
    public void onResourcePackStatus(PlayerResourcePackStatusEvent event) {
        if (event.getStatus() == PlayerResourcePackStatusEvent.Status.SUCCESSFULLY_LOADED) {
            PkCinematics.getApi().getTriggerManager().fire("resource_pack_loaded", event.getPlayer());
        } else if (event.getStatus() == PlayerResourcePackStatusEvent.Status.DECLINED || event.getStatus() == PlayerResourcePackStatusEvent.Status.FAILED_DOWNLOAD || event.getStatus() == PlayerResourcePackStatusEvent.Status.DISCARDED) {
            PkCinematics.getApi().getTriggerManager().fire("resource_pack_declined", event.getPlayer());
        }
    }
}
