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
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerSoundEffect;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerEntitySoundEffect;
import com.github.retrooper.packetevents.protocol.sound.SoundCategory;
import com.github.retrooper.packetevents.protocol.entity.type.EntityTypes;

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
        // If the receiver is in a cinematic, hide ALL other players visually and mute their sounds.
        if (PkCinematics.getApi().getPlaybackManager().isPlaying(receiver)) {
            
            // Block spawning of other players (1.20.2+ uses SPAWN_ENTITY for players)
            if (event.getPacketType() == PacketType.Play.Server.SPAWN_ENTITY) {
                WrapperPlayServerSpawnEntity spawnPacket = new WrapperPlayServerSpawnEntity(event);
                if (spawnPacket.getEntityType() == EntityTypes.PLAYER) {
                    event.setCancelled(true);
                    return;
                }
            }
            
            // Block spawning of other players (older versions)
            if (event.getPacketType() == PacketType.Play.Server.SPAWN_PLAYER) {
                event.setCancelled(true);
                return;
            }
            
            // Block sounds produced by other players
            if (event.getPacketType() == PacketType.Play.Server.SOUND_EFFECT) {
                WrapperPlayServerSoundEffect sound = new WrapperPlayServerSoundEffect(event);
                if (sound.getSoundCategory() == SoundCategory.PLAYER) {
                    event.setCancelled(true);
                    return;
                }
            }
            if (event.getPacketType() == PacketType.Play.Server.ENTITY_SOUND_EFFECT) {
                WrapperPlayServerEntitySoundEffect sound = new WrapperPlayServerEntitySoundEffect(event);
                if (sound.getSoundCategory() == SoundCategory.PLAYER) {
                    event.setCancelled(true);
                    return;
                }
            }
        }
    }

    private CinematicScheduler getScheduler() {
        return ((PlaybackManagerImpl) PkCinematics.getApi().getPlaybackManager()).getScheduler();
    }
}
