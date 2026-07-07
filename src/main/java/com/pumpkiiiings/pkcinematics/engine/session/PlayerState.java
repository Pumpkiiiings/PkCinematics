package com.pumpkiiiings.pkcinematics.engine.session;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import java.util.Collection;

/**
 * Represents the complete state of a player to be restored after a cinematic.
 */
public class PlayerState {
    private Location location;
    private GameMode gameMode;
    private boolean allowFlight;
    private boolean flying;
    private ItemStack[] inventoryContents;
    private ItemStack[] armorContents;
    private Collection<PotionEffect> potionEffects;
    private float fallDistance;
    private int fireTicks;
    private float walkSpeed;
    private float flySpeed;
    // We will save entity mount uuid instead of the entity itself to avoid memory leaks
    private java.util.UUID vehicleUuid; 

    public PlayerState() {}

    public Location getLocation() { return location; }
    public void setLocation(Location location) { this.location = location; }

    public GameMode getGameMode() { return gameMode; }
    public void setGameMode(GameMode gameMode) { this.gameMode = gameMode; }

    public boolean isAllowFlight() { return allowFlight; }
    public void setAllowFlight(boolean allowFlight) { this.allowFlight = allowFlight; }

    public boolean isFlying() { return flying; }
    public void setFlying(boolean flying) { this.flying = flying; }

    public ItemStack[] getInventoryContents() { return inventoryContents; }
    public void setInventoryContents(ItemStack[] inventoryContents) { this.inventoryContents = inventoryContents; }

    public ItemStack[] getArmorContents() { return armorContents; }
    public void setArmorContents(ItemStack[] armorContents) { this.armorContents = armorContents; }

    public Collection<PotionEffect> getPotionEffects() { return potionEffects; }
    public void setPotionEffects(Collection<PotionEffect> potionEffects) { this.potionEffects = potionEffects; }

    public float getFallDistance() { return fallDistance; }
    public void setFallDistance(float fallDistance) { this.fallDistance = fallDistance; }

    public int getFireTicks() { return fireTicks; }
    public void setFireTicks(int fireTicks) { this.fireTicks = fireTicks; }

    public float getWalkSpeed() { return walkSpeed; }
    public void setWalkSpeed(float walkSpeed) { this.walkSpeed = walkSpeed; }

    public float getFlySpeed() { return flySpeed; }
    public void setFlySpeed(float flySpeed) { this.flySpeed = flySpeed; }

    public java.util.UUID getVehicleUuid() { return vehicleUuid; }
    public void setVehicleUuid(java.util.UUID vehicleUuid) { this.vehicleUuid = vehicleUuid; }
}
