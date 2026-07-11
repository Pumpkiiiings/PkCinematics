package com.pumpkiiiings.pkcinematics.engine.camera;

import com.github.retrooper.packetevents.event.PacketListenerAbstract;
import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerSpawnEntity;
import com.pumpkiiiings.pkcinematics.api.PkCinematics;
import com.pumpkiiiings.pkcinematics.core.PlaybackManagerImpl;
import com.pumpkiiiings.pkcinematics.engine.scheduler.CinematicScheduler;
import org.bukkit.entity.Player;

import java.util.Set;
import java.util.UUID;

public class CameraPacketListener extends PacketListenerAbstract {

    @Override
    public void onPacketSend(PacketSendEvent event) {
        Object sender = event.getPlayer();
        if (!(sender instanceof Player)) return;
        Player receiver = (Player) sender;

        // --- Block server teleport jitter during cinematic ---
        if (event.getPacketType() == PacketType.Play.Server.PLAYER_POSITION_AND_LOOK) {
            if (PkCinematics.getApi().getPlaybackManager().isPlaying(receiver)) {
                event.setCancelled(true);
            }
            return;
        }

        // --- Block spectator body visibility between cinematic players ---
        // If the receiver is in a cinematic and the server tries to spawn another
        // cinematic player's entity, cancel the packet.
        // Uses entity ID (always available) instead of UUID (API varies by PacketEvents build).
        if (event.getPacketType() == PacketType.Play.Server.SPAWN_ENTITY) {
            CinematicScheduler scheduler = getScheduler();
            Set<UUID> cinematicIds = scheduler.getCinematicPlayerIds();

            // Only apply filter when the receiver is themselves in a cinematic
            if (!cinematicIds.contains(receiver.getUniqueId())) return;

            WrapperPlayServerSpawnEntity spawnPacket = new WrapperPlayServerSpawnEntity(event);
            int spawnedEntityId = spawnPacket.getEntityId();

            if (scheduler.getCinematicEntityIds().contains(spawnedEntityId)) {
                // The entity being spawned is another player in cinematic — block it
                event.setCancelled(true);
            }
        }
    }

    private CinematicScheduler getScheduler() {
        return ((PlaybackManagerImpl) PkCinematics.getApi().getPlaybackManager()).getScheduler();
    }
}
