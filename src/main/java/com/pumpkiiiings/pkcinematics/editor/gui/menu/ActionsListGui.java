package com.pumpkiiiings.pkcinematics.editor.gui.menu;

import com.pumpkiiiings.pkcinematics.api.PkCinematics;
import com.pumpkiiiings.pkcinematics.config.GuiConfigManager;
import com.pumpkiiiings.pkcinematics.editor.EditorSession;
import com.pumpkiiiings.pkcinematics.api.action.PkAction;
import com.pumpkiiiings.pkcinematics.model.timeline.ActionTrack;
import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.PaginatedGui;
import dev.triumphteam.gui.guis.GuiItem;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import java.util.List;

public class ActionsListGui {

    public static void open(Player player, EditorSession session) {
        PkCinematics api = PkCinematics.getApi();
        GuiConfigManager config = api.getGuiConfigManager();
        ActionTrack track = session.getCinematic().getTimeline().getActionTrack();

        PaginatedGui gui = PaginatedGui.gui()
                .title(config.getComponent("actions_list.title", "cinematic", session.getCinematic().getId()))
                .rows(6)
                .pageSize(45)
                .disableAllInteractions()
                .create();

        // Populate Actions
        for (int tick : track.getAllActions().keySet()) {
            List<PkAction> actionsAtTick = track.getAllActions().get(tick);
            for (PkAction action : actionsAtTick) {
                ItemBuilder itemBuilder = config.getItemBuilder("actions_list.item",
                        "type", action.getType(),
                        "tick", String.valueOf(tick)
                );
                
                GuiItem guiItem = itemBuilder.asGuiItem(event -> {
                    if (event.getClick() == ClickType.RIGHT) {
                        track.removeAction(tick, action);
                        session.getCinematic().getTimeline().calculateDuration();
                        open(player, session); // Refresh
                    }
                });
                gui.addItem(guiItem);
            }
        }

        // Navigation
        gui.setItem(6, 3, config.getItemBuilder("nav.prev").asGuiItem(e -> gui.previous()));
        gui.setItem(6, 7, config.getItemBuilder("nav.next").asGuiItem(e -> gui.next()));
        gui.setItem(6, 5, config.getItemBuilder("nav.back").asGuiItem(e -> MainEditorGui.open(player, session)));

        // Add Action Button
        GuiItem addBtn = config.getItemBuilder("actions_list.add_btn").asGuiItem(event -> {
            ActionSelectorGui.open(player, session);
        });
        gui.setItem(6, 9, addBtn);

        gui.open(player);
    }
}
