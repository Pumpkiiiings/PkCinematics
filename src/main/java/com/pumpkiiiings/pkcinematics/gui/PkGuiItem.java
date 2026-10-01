package com.pumpkiiiings.pkcinematics.gui;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.function.Consumer;

public class PkGuiItem {

    private final ItemStack itemStack;
    private final Consumer<InventoryClickEvent> action;

    public PkGuiItem(ItemStack itemStack) {
        this(itemStack, null);
    }

    public PkGuiItem(ItemStack itemStack, Consumer<InventoryClickEvent> action) {
        this.itemStack = itemStack;
        this.action = action;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public void handleClick(InventoryClickEvent event) {
        if (action != null) {
            action.accept(event);
        }
    }
}
