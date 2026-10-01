package com.pumpkiiiings.pkcinematics.gui;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

import java.util.HashMap;
import java.util.Map;

public class PkMenu implements InventoryHolder {

    private final Inventory inventory;
    private final Map<Integer, PkGuiItem> items = new HashMap<>();

    private PkMenu(Component title, int rows) {
        this.inventory = Bukkit.createInventory(this, rows * 9, title);
    }

    public static Builder gui() {
        return new Builder();
    }

    public void setItem(int row, int col, PkGuiItem item) {
        int slot = (row - 1) * 9 + (col - 1);
        items.put(slot, item);
        inventory.setItem(slot, item.getItemStack());
    }

    public void open(Player player) {
        player.openInventory(inventory);
    }

    public void close(Player player) {
        player.closeInventory();
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }

    public void handleClick(InventoryClickEvent event) {
        event.setCancelled(true);
        int slot = event.getRawSlot();
        if (slot < 0 || slot >= inventory.getSize()) return;
        PkGuiItem item = items.get(slot);
        if (item != null) {
            item.handleClick(event);
        }
    }

    public static class Builder {
        private Component title = Component.empty();
        private int rows = 3;

        public Builder title(Component title) {
            this.title = title;
            return this;
        }

        public Builder rows(int rows) {
            this.rows = rows;
            return this;
        }

        public Builder disableAllInteractions() {
            return this;
        }

        public PkMenu create() {
            return new PkMenu(title, rows);
        }
    }
}
