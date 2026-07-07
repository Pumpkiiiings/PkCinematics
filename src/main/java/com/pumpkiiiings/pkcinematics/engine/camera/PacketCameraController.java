package com.pumpkiiiings.pkcinematics.engine.camera;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.protocol.entity.type.EntityTypes;
import com.github.retrooper.packetevents.util.Vector3d;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerCamera;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerDestroyEntities;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerEntityTeleport;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerEntityHeadLook;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerSpawnEntity;
import com.pumpkiiiings.pkcinematics.api.camera.CameraController;
import com.pumpkiiiings.pkcinematics.model.timeline.CameraKeyframe;
import org.bukkit.entity.Player;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class PacketCameraController implements CameraController {
    
    // Simple way to get unique entity IDs for fake entities
    private static final AtomicInteger ENTITY_ID_COUNTER = new AtomicInteger(100000);
    private final Map<UUID, Integer> playerFakeCameras = new ConcurrentHashMap<>();

    @Override
    public void startSpectating(Player player, CameraKeyframe initialPoint) {
        int fakeEntityId = ENTITY_ID_COUNTER.incrementAndGet();
        playerFakeCameras.put(player.getUniqueId(), fakeEntityId);

        // Spawn fake ArmorStand
        WrapperPlayServerSpawnEntity spawnPacket = new WrapperPlayServerSpawnEntity(
                fakeEntityId,
                Optional.of(UUID.randomUUID()),
                EntityTypes.ARMOR_STAND,
                new Vector3d(initialPoint.getX(), initialPoint.getY(), initialPoint.getZ()),
                initialPoint.getPitch(),
                initialPoint.getYaw(),
                0, 0, Optional.empty()
        );
        PacketEvents.getAPI().getPlayerManager().sendPacket(player, spawnPacket);

        // Set Camera to spectate fake entity
        WrapperPlayServerCamera cameraPacket = new WrapperPlayServerCamera(fakeEntityId);
        PacketEvents.getAPI().getPlayerManager().sendPacket(player, cameraPacket);
    }

    @Override
    public void updatePosition(Player player, CameraKeyframe currentPoint) {
        Integer entityId = playerFakeCameras.get(player.getUniqueId());
        if (entityId == null) return;

        // Teleport the fake entity
        WrapperPlayServerEntityTeleport tpPacket = new WrapperPlayServerEntityTeleport(
                entityId,
                new Vector3d(currentPoint.getX(), currentPoint.getY(), currentPoint.getZ()),
                currentPoint.getYaw(),
                currentPoint.getPitch(),
                true
        );
        PacketEvents.getAPI().getPlayerManager().sendPacket(player, tpPacket);
        
        WrapperPlayServerEntityHeadLook headLook = new WrapperPlayServerEntityHeadLook(
                entityId,
                currentPoint.getYaw()
        );
        PacketEvents.getAPI().getPlayerManager().sendPacket(player, headLook);
        
        // Note: FOV changes might require changing player's attributes or sending a specific packet, 
        // will handle FOV later.
    }

    @Override
    public void stopSpectating(Player player) {
        Integer entityId = playerFakeCameras.remove(player.getUniqueId());
        if (entityId != null) {
            // Restore camera to player
            WrapperPlayServerCamera cameraPacket = new WrapperPlayServerCamera(player.getEntityId());
            PacketEvents.getAPI().getPlayerManager().sendPacket(player, cameraPacket);

            // Destroy fake entity
            WrapperPlayServerDestroyEntities destroyPacket = new WrapperPlayServerDestroyEntities(entityId);
            PacketEvents.getAPI().getPlayerManager().sendPacket(player, destroyPacket);
        }
    }
}
