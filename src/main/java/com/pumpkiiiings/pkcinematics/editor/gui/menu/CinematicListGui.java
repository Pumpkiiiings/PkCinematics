package com.pumpkiiiings.pkcinematics.editor.gui.menu;

import com.pumpkiiiings.pkcinematics.api.PkCinematics;
import com.pumpkiiiings.pkcinematics.config.GuiConfigManager;
import com.pumpkiiiings.pkcinematics.model.Cinematic;
import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.PaginatedGui;
import dev.triumphteam.gui.guis.GuiItem;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import com.pumpkiiiings.pkcinematics.config.Messages;

public class CinematicListGui {

    public static void open(Player player) {
        PkCinematics api = PkCinematics.getApi();
        GuiConfigManager config = api.getGuiConfigManager();

        PaginatedGui gui = Gui.paginated()
                .title(config.getComponent("cinematics_list.title"))
                .rows(6)
                .pageSize(45)
                .disableAllInteractions()
                .create();

        for (Cinematic cinematic : api.getCinematicManager().getAllCinematics()) {
            int duration = cinematic.getTimeline().getDurationTicks();
            int keys = cinematic.getTimeline().getCameraTrack().getKeyframes().size();
            int actions = cinematic.getTimeline().getActionTrack().getAllActions().size();
            
            ItemBuilder itemBuilder = config.getItemBuilder("cinematics_list.item",
                    "id", cinematic.getId(),
                    "duration", String.valueOf(duration),
                    "keyframes", String.valueOf(keys),
                    "actions", String.valueOf(actions)
            );

            GuiItem guiItem = itemBuilder.asGuiItem(event -> {
                if (event.getClick() == ClickType.LEFT) {
                    api.getEditorManager().startEditing(player, cinematic);
                    MainEditorGui.open(player, api.getEditorManager().getSession(player));
                }
            });
            gui.addItem(guiItem);
        }

        // Navigation
        gui.setItem(6, 3, config.getItemBuilder("nav.prev").asGuiItem(e -> gui.previous()));
        gui.setItem(6, 7, config.getItemBuilder("nav.next").asGuiItem(e -> gui.next()));
        
        // Add create button
        GuiItem createBtn = config.getItemBuilder("cinematics_list.create_btn").asGuiItem(e -> {
            String prompt = Messages.PREFIX.get() + "§aEscribe el ID (nombre) para la nueva cinemática en el chat:";
            api.getChatInputManager().requestInput(player, prompt, input -> {
                String id = input.trim();
                if (id.contains(" ")) {
                    player.sendMessage(Messages.PREFIX.get() + "§cEl ID no puede contener espacios.");
                    return;
                }
                if (api.getCinematicManager().getCinematic(id) != null) {
                    player.sendMessage(Messages.ALREADY_EXISTS.getWithPrefix());
                    return;
                }
                Cinematic newCin = new Cinematic(id);
                api.getCinematicManager().registerCinematic(newCin);
                api.getEditorManager().startEditing(player, newCin);
                player.playSound(player.getLocation(), org.bukkit.Sound.BLOCK_NOTE_BLOCK_PLING, 1.0f, 1.0f);
                MainEditorGui.open(player, api.getEditorManager().getSession(player));
            });
        });
        gui.setItem(6, 5, createBtn);

        gui.open(player);
    }
}
