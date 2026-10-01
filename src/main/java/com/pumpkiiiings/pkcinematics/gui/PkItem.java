package com.pumpkiiiings.pkcinematics.gui;

import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.function.Consumer;

public class PkItem {

    private final ItemStack itemStack;

    private PkItem(Material material) {
        this.itemStack = new ItemStack(material);
    }

    public static PkItem of(Material material) {
        return new PkItem(material);
    }

    public PkItem name(Component name) {
        ItemMeta meta = itemStack.getItemMeta();
        if (meta != null) {
            meta.displayName(name);
            itemStack.setItemMeta(meta);
        }
        return this;
    }

    public PkItem lore(List<Component> lore) {
        ItemMeta meta = itemStack.getItemMeta();
        if (meta != null) {
            meta.lore(lore);
            itemStack.setItemMeta(meta);
        }
        return this;
    }

    public ItemStack build() {
        return itemStack.clone();
    }

    public PkGuiItem asGuiItem() {
        return new PkGuiItem(build());
    }

    public PkGuiItem asGuiItem(Consumer<InventoryClickEvent> action) {
        return new PkGuiItem(build(), action);
    }
}
