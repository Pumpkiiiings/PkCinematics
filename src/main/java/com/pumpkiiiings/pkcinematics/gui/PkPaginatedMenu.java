package com.pumpkiiiings.pkcinematics.gui;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PkPaginatedMenu implements InventoryHolder {

    private final Component title;
    private final int rows;
    private final int pageSize;
    private final List<PkGuiItem> pageItems = new ArrayList<>();
    private final Map<Integer, PkGuiItem> fixedItems = new HashMap<>();
    private int currentPage = 0;
    private Inventory inventory;

    private PkPaginatedMenu(Component title, int rows, int pageSize) {
        this.title = title;
        this.rows = rows;
        this.pageSize = pageSize;
        this.inventory = Bukkit.createInventory(this, rows * 9, title);
    }

    public static Builder paginated() {
        return new Builder();
    }

    public void addItem(PkGuiItem item) {
        pageItems.add(item);
    }

    public void setItem(int row, int col, PkGuiItem item) {
        int slot = (row - 1) * 9 + (col - 1);
        fixedItems.put(slot, item);
    }

    public void previous() {
        if (currentPage > 0) {
            currentPage--;
            updateInventory();
        }
    }

    public void next() {
        int maxPage = Math.max(0, (int) Math.ceil((double) pageItems.size() / pageSize) - 1);
        if (currentPage < maxPage) {
            currentPage++;
            updateInventory();
        }
    }

    public void open(Player player) {
        updateInventory();
        player.openInventory(inventory);
    }

    private void updateInventory() {
        inventory.clear();

        // Place paginated items for current page
        int start = currentPage * pageSize;
        int end = Math.min(start + pageSize, pageItems.size());
        for (int i = start; i < end; i++) {
            inventory.setItem(i - start, pageItems.get(i).getItemStack());
        }

        // Place fixed items (navigation buttons, etc.)
        for (Map.Entry<Integer, PkGuiItem> entry : fixedItems.entrySet()) {
            inventory.setItem(entry.getKey(), entry.getValue().getItemStack());
        }
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }

    public void handleClick(InventoryClickEvent event) {
        event.setCancelled(true);
        int slot = event.getRawSlot();
        if (slot < 0 || slot >= inventory.getSize()) return;

        // Check fixed items first (nav buttons)
        PkGuiItem fixed = fixedItems.get(slot);
        if (fixed != null) {
            fixed.handleClick(event);
            return;
        }

        // Check paginated items
        int itemIndex = currentPage * pageSize + slot;
        if (slot < pageSize && itemIndex >= 0 && itemIndex < pageItems.size()) {
            pageItems.get(itemIndex).handleClick(event);
        }
    }

    public static class Builder {
        private Component title = Component.empty();
        private int rows = 6;
        private int pageSize = 45;

        public Builder title(Component title) {
            this.title = title;
            return this;
        }

        public Builder rows(int rows) {
            this.rows = rows;
            return this;
        }

        public Builder pageSize(int pageSize) {
            this.pageSize = pageSize;
            return this;
        }

        public Builder disableAllInteractions() {
            return this;
        }

        public PkPaginatedMenu create() {
            return new PkPaginatedMenu(title, rows, pageSize);
        }
    }
}
