package com.pumpkiiiings.pkcinematics.editor.gui;

import com.pumpkiiiings.pkcinematics.api.PkCinematics;
import com.pumpkiiiings.pkcinematics.action.impl.TitleAction;
import com.pumpkiiiings.pkcinematics.editor.EditorSession;
import com.pumpkiiiings.pkcinematics.api.action.PkAction;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import com.pumpkiiiings.pkcinematics.core.PkCinematicsPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class ActionEditorGUI implements Listener {

    // Stores what tick a player is currently trying to add a title for
    private static final Map<UUID, Integer> pendingTitleInput = new ConcurrentHashMap<>();

    public static void openMainMenu(Player player, EditorSession session, int tick) {
        com.pumpkiiiings.pkcinematics.config.MessageManager msg = PkCinematics.getApi().getMessageManager();
        String mainTitle = msg.getMessage("gui.main_title", "tick", tick);
        Inventory inv = Bukkit.createInventory(null, 54, mainTitle);
        
        List<PkAction> actions = session.getCinematic().getTimeline().getActionTrack().getActionsAt(tick);
        
        int slot = 0;
        for (PkAction action : actions) {
            ItemStack item = new ItemStack(Material.PAPER);
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(msg.getMessage("gui.action_item_name", "type", action.getType()));
            meta.setLore(PkCinematicsPlugin.getPlugin(PkCinematicsPlugin.class).getConfig().getStringList("gui.action_item_lore")); // Simplified, we will just use a static lore or fetch from manager. 
            // Better to use MessageManager for lists if supported, but for simplicity let's do:
            List<String> lore = new ArrayList<>();
            lore.add(msg.getMessage("gui.action_item_lore[0]")); // Fallback if list not supported
            meta.setLore(lore);
            item.setItemMeta(meta);
            inv.setItem(slot++, item);
        }
        
        ItemStack addBtn = new ItemStack(Material.EMERALD_BLOCK);
        ItemMeta addMeta = addBtn.getItemMeta();
        addMeta.setDisplayName(msg.getMessage("gui.add_action_btn"));
        addBtn.setItemMeta(addMeta);
        inv.setItem(53, addBtn);
        
        player.openInventory(inv);
    }
    
    public static void openAddMenu(Player player, int tick) {
        com.pumpkiiiings.pkcinematics.config.MessageManager msg = PkCinematics.getApi().getMessageManager();
        String addTitle = msg.getMessage("gui.add_title") + " - " + tick;
        Inventory inv = Bukkit.createInventory(null, 27, addTitle);
        
        ItemStack title = new ItemStack(Material.NAME_TAG);
        ItemMeta tMeta = title.getItemMeta();
        tMeta.setDisplayName(msg.getMessage("gui.add_title_btn"));
        List<String> lore = new ArrayList<>();
        lore.add(msg.getMessage("gui.add_title_lore[0]")); // Fallback
        tMeta.setLore(lore);
        title.setItemMeta(tMeta);
        inv.setItem(13, title);
        
        player.openInventory(inv);
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) return;
        Player player = (Player) event.getWhoClicked();
        String title = event.getView().getTitle();
        com.pumpkiiiings.pkcinematics.config.MessageManager msg = PkCinematics.getApi().getMessageManager();
        
        if (title.contains("Acciones - Tick: ")) {
            event.setCancelled(true);
            if (event.getCurrentItem() == null) return;
            
            // Extract tick correctly
            String[] parts = title.split(" ");
            int tick = Integer.parseInt(parts[parts.length - 1]);
            
            EditorSession session = PkCinematics.getApi().getEditorManager().getSession(player);
            if (session == null) {
                player.closeInventory();
                return;
            }

            if (event.getRawSlot() == 53) { // Añadir
                openAddMenu(player, tick);
            } else if (event.getRawSlot() < 53 && event.getCurrentItem().getType() == Material.PAPER) {
                if (event.isRightClick()) {
                    List<PkAction> actions = session.getCinematic().getTimeline().getActionTrack().getActionsAt(tick);
                    if (event.getRawSlot() < actions.size()) {
                        PkAction toRemove = actions.get(event.getRawSlot());
                        session.getCinematic().getTimeline().getActionTrack().removeAction(tick, toRemove);
                        player.sendMessage(msg.getMessage("prefix") + msg.getMessage("action_removed"));
                        openMainMenu(player, session, tick);
                    }
                }
            }
        } else if (title.contains(msg.getMessage("gui.add_title").replace("&8", "§8"))) {
            event.setCancelled(true);
            if (event.getCurrentItem() == null) return;
            
            String[] parts = title.split(" ");
            int tick = Integer.parseInt(parts[parts.length - 1]);
            
            if (event.getCurrentItem().getType() == Material.NAME_TAG) {
                player.closeInventory();
                pendingTitleInput.put(player.getUniqueId(), tick);
                player.sendMessage(msg.getMessage("prefix") + msg.getMessage("chat_input_title"));
            }
        }
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        if (pendingTitleInput.containsKey(player.getUniqueId())) {
            event.setCancelled(true);
            int tick = pendingTitleInput.remove(player.getUniqueId());
            com.pumpkiiiings.pkcinematics.config.MessageManager msg = PkCinematics.getApi().getMessageManager();
            
            if (event.getMessage().equalsIgnoreCase("cancelar")) {
                player.sendMessage(msg.getMessage("prefix") + msg.getMessage("chat_input_cancelled"));
                return;
            }
            
            String text = event.getMessage();
            
            Bukkit.getScheduler().runTask(Bukkit.getPluginManager().getPlugin("PkCinematics"), new Runnable() {
                @Override
                public void run() {
                    EditorSession session = PkCinematics.getApi().getEditorManager().getSession(player);
                    if (session != null) {
                        TitleAction action = new TitleAction(text, "", 10, 60, 10);
                        session.getCinematic().getTimeline().getActionTrack().addAction(tick, action);
                        session.getCinematic().getTimeline().calculateDuration();
                        player.sendMessage(msg.getMessage("prefix") + msg.getMessage("chat_input_success", "tick", tick));
                        openMainMenu(player, session, tick);
                    }
                }
            });
        }
    }
}
