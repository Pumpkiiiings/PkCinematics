package com.pumpkiiiings.pkcinematics.integration;

import com.pumpkiiiings.pkcinematics.api.PkCinematics;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import com.sk89q.worldguard.session.Session;
import com.sk89q.worldguard.session.handler.Handler;
import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.session.MoveType;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.Set;

public class WGIntegration {

    public static void registerHandler() {
        try {
            WorldGuard.getInstance().getPlatform().getSessionManager().registerHandler(RegionEnterHandler.FACTORY, null);
            ((org.bukkit.plugin.Plugin) PkCinematics.getApi()).getLogger().info("WorldGuard integration enabled.");
        } catch (Exception e) {
            ((org.bukkit.plugin.Plugin) PkCinematics.getApi()).getLogger().warning("Failed to register WorldGuard handler.");
        }
    }

    public static boolean isPlayerInRegion(Player player, String regionName) {
        try {
            RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
            RegionManager regions = container.get(BukkitAdapter.adapt(player.getWorld()));
            if (regions == null) return false;

            Location loc = player.getLocation();
            BlockVector3 pt = BlockVector3.at(loc.getX(), loc.getY(), loc.getZ());
            ApplicableRegionSet set = regions.getApplicableRegions(pt);

            for (ProtectedRegion region : set) {
                if (region.getId().equalsIgnoreCase(regionName)) {
                    return true;
                }
            }
        } catch (Exception e) {
            // Ignored, WorldGuard might not be present or API mismatch
        }
        return false;
    }

    public static class RegionEnterHandler extends Handler {
        public static final Factory FACTORY = new Factory();

        public static class Factory extends Handler.Factory<RegionEnterHandler> {
            @Override
            public RegionEnterHandler create(Session session) {
                return new RegionEnterHandler(session);
            }
        }

        public RegionEnterHandler(Session session) {
            super(session);
        }

        @Override
        public boolean onCrossBoundary(LocalPlayer player, com.sk89q.worldedit.util.Location from, com.sk89q.worldedit.util.Location to, ApplicableRegionSet toSet, Set<ProtectedRegion> entered, Set<ProtectedRegion> exited, MoveType moveType) {
            if (!entered.isEmpty()) {
                Player bukkitPlayer = Bukkit.getPlayer(player.getUniqueId());
                if (bukkitPlayer != null) {
                    Bukkit.getScheduler().runTask((org.bukkit.plugin.Plugin) PkCinematics.getApi(), () -> {
                        PkCinematics.getApi().getTriggerManager().fire("region_enter", bukkitPlayer);
                    });
                }
            }
            return super.onCrossBoundary(player, from, to, toSet, entered, exited, moveType);
        }
    }
}
