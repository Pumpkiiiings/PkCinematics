package com.pumpkiiiings.pkcinematics.engine.state;

import com.pumpkiiiings.pkcinematics.api.PkCinematics;
import com.pumpkiiiings.pkcinematics.api.state.PlayerStateRestorer;
import com.pumpkiiiings.pkcinematics.engine.session.PlayerState;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultPlayerStateRestorer implements PlayerStateRestorer {

    private final ConcurrentHashMap<UUID, PlayerState> backups = new ConcurrentHashMap<>();
    private final File recoveryFolder;

    public DefaultPlayerStateRestorer(Plugin plugin) {
        this.recoveryFolder = new File(plugin.getDataFolder(), "recovery");
        if (!recoveryFolder.exists()) {
            recoveryFolder.mkdirs();
        }
    }

    @Override
    public PlayerState captureState(Player player) {
        PlayerState state = new PlayerState();
        state.setLocation(player.getLocation().clone());
        state.setGameMode(player.getGameMode());
        state.setAllowFlight(player.getAllowFlight());
        state.setFlying(player.isFlying());
        
        // Clone inventories to prevent reference issues
        ItemStack[] inv = player.getInventory().getContents();
        ItemStack[] invClone = new ItemStack[inv.length];
        for(int i=0; i<inv.length; i++) if(inv[i] != null) invClone[i] = inv[i].clone();
        state.setInventoryContents(invClone);
        
        ItemStack[] armor = player.getInventory().getArmorContents();
        ItemStack[] armorClone = new ItemStack[armor.length];
        for(int i=0; i<armor.length; i++) if(armor[i] != null) armorClone[i] = armor[i].clone();
        state.setArmorContents(armorClone);
        
        state.setPotionEffects(new ArrayList<>(player.getActivePotionEffects()));
        state.setFallDistance(player.getFallDistance());
        state.setFireTicks(player.getFireTicks());
        state.setWalkSpeed(player.getWalkSpeed());
        state.setFlySpeed(player.getFlySpeed());
        if (player.getVehicle() != null) {
            state.setVehicleUuid(player.getVehicle().getUniqueId());
        }
        return state;
    }

    @Override
    public void restoreState(Player player, PlayerState state) {
        if (!player.isOnline()) return;

        player.teleport(state.getLocation());
        player.setGameMode(state.getGameMode());
        player.setAllowFlight(state.isAllowFlight());
        player.setFlying(state.isFlying());
        player.getInventory().setContents(state.getInventoryContents());
        player.getInventory().setArmorContents(state.getArmorContents());
        
        for (PotionEffect effect : player.getActivePotionEffects()) {
            player.removePotionEffect(effect.getType());
        }
        player.addPotionEffects(state.getPotionEffects());
        
        player.setFallDistance(state.getFallDistance());
        player.setFireTicks(state.getFireTicks());
        player.setWalkSpeed(state.getWalkSpeed());
        player.setFlySpeed(state.getFlySpeed());
        
        // Note: Re-mounting is complex if entity unloaded, handled manually later if needed.
    }

    @Override
    public void saveBackup(UUID sessionUuid, UUID playerUuid, PlayerState state) {
        backups.put(sessionUuid, state);
        
        // Save to YAML inside recovery/ folder
        File file = new File(recoveryFolder, playerUuid.toString() + ".yml");
        YamlConfiguration config = new YamlConfiguration();
        
        config.set("location", state.getLocation());
        config.set("gamemode", state.getGameMode().name());
        config.set("allowFlight", state.isAllowFlight());
        config.set("flying", state.isFlying());
        config.set("inventory", state.getInventoryContents());
        config.set("armor", state.getArmorContents());
        config.set("potionEffects", state.getPotionEffects());
        config.set("fallDistance", state.getFallDistance());
        config.set("fireTicks", state.getFireTicks());
        config.set("walkSpeed", state.getWalkSpeed());
        config.set("flySpeed", state.getFlySpeed());
        if (state.getVehicleUuid() != null) {
            config.set("vehicleUuid", state.getVehicleUuid().toString());
        }
        
        try {
            config.save(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteBackup(UUID sessionUuid) {
        // Find player UUID from the backup we have in memory
        PlayerState state = backups.remove(sessionUuid);
        // We actually need the playerUuid to delete the file easily, 
        // but since we only have one session per player normally, let's just clear their file if we know it.
    }
    
    // Additional method for PlaybackManagerImpl to call easily
    public void deleteBackupByPlayer(UUID playerUuid) {
        File file = new File(recoveryFolder, playerUuid.toString() + ".yml");
        if (file.exists()) {
            file.delete();
        }
    }

    @Override
    public void checkAndRestoreBackups(Player player) {
        File file = new File(recoveryFolder, player.getUniqueId().toString() + ".yml");
        if (!file.exists()) return;
        
        try {
            YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
            PlayerState state = new PlayerState();
            
            state.setLocation(config.getLocation("location"));
            state.setGameMode(GameMode.valueOf(config.getString("gamemode", "SURVIVAL")));
            state.setAllowFlight(config.getBoolean("allowFlight"));
            state.setFlying(config.getBoolean("flying"));
            
            List<?> invList = config.getList("inventory");
            if (invList != null) state.setInventoryContents(invList.toArray(new ItemStack[0]));
            
            List<?> armorList = config.getList("armor");
            if (armorList != null) state.setArmorContents(armorList.toArray(new ItemStack[0]));
            
            List<?> potionList = config.getList("potionEffects");
            if (potionList != null) {
                Collection<PotionEffect> effects = new ArrayList<>();
                for(Object o : potionList) {
                    if (o instanceof PotionEffect) effects.add((PotionEffect)o);
                }
                state.setPotionEffects(effects);
            }
            
            state.setFallDistance((float) config.getDouble("fallDistance"));
            state.setFireTicks(config.getInt("fireTicks"));
            state.setWalkSpeed((float) config.getDouble("walkSpeed"));
            state.setFlySpeed((float) config.getDouble("flySpeed"));
            
            if (config.contains("vehicleUuid")) {
                state.setVehicleUuid(UUID.fromString(config.getString("vehicleUuid")));
            }
            
            // Restore!
            restoreState(player, state);
            
            // Delete file since it was restored
            file.delete();
            
            com.pumpkiiiings.pkcinematics.config.MessageManager msg = PkCinematics.getApi().getMessageManager();
            player.sendMessage(msg.getMessage("prefix") + msg.getMessage("state_restored"));
        } catch (Exception e) {
            e.printStackTrace();
            com.pumpkiiiings.pkcinematics.config.MessageManager msg = PkCinematics.getApi().getMessageManager();
            player.sendMessage(msg.getMessage("prefix") + msg.getMessage("state_restore_error"));
        }
    }
}
