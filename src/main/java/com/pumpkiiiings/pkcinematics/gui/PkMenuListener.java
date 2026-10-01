package com.pumpkiiiings.pkcinematics.gui;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.InventoryHolder;

public class PkMenuListener implements Listener {

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        InventoryHolder holder = event.getInventory().getHolder();
        if (holder instanceof PkMenu menu) {
            menu.handleClick(event);
        } else if (holder instanceof PkPaginatedMenu menu) {
            menu.handleClick(event);
        }
    }

    @EventHandler
    public void onDrag(InventoryDragEvent event) {
        InventoryHolder holder = event.getInventory().getHolder();
        if (holder instanceof PkMenu || holder instanceof PkPaginatedMenu) {
            event.setCancelled(true);
        }
    }
}
