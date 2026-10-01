package com.pumpkiiiings.pkcinematics.editor.gui.menu;

import com.pumpkiiiings.pkcinematics.api.PkCinematics;
import com.pumpkiiiings.pkcinematics.config.GuiConfigManager;
import com.pumpkiiiings.pkcinematics.editor.EditorSession;
import com.pumpkiiiings.pkcinematics.api.action.PkAction;
import com.pumpkiiiings.pkcinematics.model.timeline.ActionTrack;
import com.pumpkiiiings.pkcinematics.gui.PkItem;
import com.pumpkiiiings.pkcinematics.gui.PkPaginatedMenu;
import com.pumpkiiiings.pkcinematics.gui.PkGuiItem;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import java.util.List;

public class ActionsListGui {

    public static void open(Player player, EditorSession session) {
        PkCinematics api = PkCinematics.getApi();
        GuiConfigManager config = api.getGuiConfigManager();
        ActionTrack track = session.getCinematic().getTimeline().getActionTrack();

        PkPaginatedMenu gui = PkPaginatedMenu.paginated()
                .title(config.getComponent("actions_list.title", "cinematic", session.getCinematic().getId()))
                .rows(6)
                .pageSize(45)
                .create();

        for (int tick : track.getAllActions().keySet()) {
            List<PkAction> actionsAtTick = track.getAllActions().get(tick);
            for (PkAction action : actionsAtTick) {
                PkItem itemBuilder = config.getItemBuilder("actions_list.item",
                        "type", action.getType(),
                        "tick", String.valueOf(tick)
                );
                
                PkGuiItem guiItem = itemBuilder.asGuiItem(event -> {
                    if (event.getClick() == ClickType.RIGHT) {
                        com.pumpkiiiings.pkcinematics.api.action.ActionContext ctx = new com.pumpkiiiings.pkcinematics.api.action.ActionContext() {
                            @Override public Player getPlayer() { return player; }
                            @Override public com.pumpkiiiings.pkcinematics.engine.session.PlaybackSession getPlaybackSession() { return null; }
                            @Override public java.util.Map<String, Object> getVariables() { return java.util.Collections.emptyMap(); }
                        };
                        action.execute(ctx);
                        player.sendMessage("§a[PkCinematics] Previewing action...");
                    } else if (event.getClick() == ClickType.DROP || event.getClick() == ClickType.CONTROL_DROP) {
                        track.removeAction(tick, action);
                        session.getCinematic().getTimeline().calculateDuration();
                        open(player, session);
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
        PkGuiItem addBtn = config.getItemBuilder("actions_list.add_btn").asGuiItem(event -> {
            ActionSelectorGui.open(player, session);
        });
        gui.setItem(6, 9, addBtn);

        gui.open(player);
    }
}
