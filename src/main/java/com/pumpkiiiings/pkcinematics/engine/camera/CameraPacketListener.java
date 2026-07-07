package com.pumpkiiiings.pkcinematics.engine.camera;

import com.github.retrooper.packetevents.event.PacketListenerAbstract;
import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.pumpkiiiings.pkcinematics.api.PkCinematics;
import org.bukkit.entity.Player;

import java.util.UUID;

public class CameraPacketListener extends PacketListenerAbstract {

    @Override
    public void onPacketSend(PacketSendEvent event) {
        if (event.getPacketType() == PacketType.Play.Server.PLAYER_POSITION_AND_LOOK) {
            
            Object sender = event.getPlayer();
            if (!(sender instanceof Player)) return;
            
            Player player = (Player) sender;
            UUID uuid = player.getUniqueId();
            
            // If the player is currently in an active cinematic session,
            // we block the server from teleporting their physical body on the client side.
            // This allows us to teleport them server-side to load chunks without shaking the camera!
            
            if (PkCinematics.getApi().getPlaybackManager().isPlaying(player)) {
                // The player is in a cinematic, block the native teleport packet!
                event.setCancelled(true);
            }
        }
    }
}
