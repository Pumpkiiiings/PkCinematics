package com.pumpkiiiings.pkcinematics.editor.gui.menu;

import com.pumpkiiiings.pkcinematics.api.PkCinematics;
import com.pumpkiiiings.pkcinematics.config.GuiConfigManager;
import com.pumpkiiiings.pkcinematics.editor.EditorSession;
import com.pumpkiiiings.pkcinematics.model.timeline.CameraKeyframe;
import com.pumpkiiiings.pkcinematics.model.timeline.CameraTrack;
import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.PaginatedGui;
import dev.triumphteam.gui.guis.GuiItem;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

public class KeyframesListGui {

    public static void open(Player player, EditorSession session) {
        PkCinematics api = PkCinematics.getApi();
        GuiConfigManager config = api.getGuiConfigManager();
        CameraTrack track = session.getCinematic().getTimeline().getCameraTrack();

        PaginatedGui gui = Gui.paginated()
                .title(config.getComponent("keyframes_list.title", "cinematic", session.getCinematic().getId()))
                .rows(6)
                .pageSize(45)
                .disableAllInteractions()
                .create();

        int index = 0;
        for (CameraKeyframe kf : track.getKeyframes()) {
            final int currentIndex = index;
            ItemBuilder itemBuilder = config.getItemBuilder("keyframes_list.item",
                    "index", String.valueOf(index),
                    "tick", String.valueOf(kf.getTick()),
                    "fov", String.valueOf(kf.getFov()),
                    "interp", kf.getInterpolationType(),
                    "easing", kf.getEasingType()
            );

            GuiItem guiItem = itemBuilder.asGuiItem(event -> {
                if (event.getClick() == ClickType.LEFT) {
                    KeyframeEditorGui.open(player, session, kf, currentIndex);
                } else if (event.getClick() == ClickType.RIGHT) {
                    track.removeKeyframe(kf);
                    session.getCinematic().getTimeline().calculateDuration();
                    open(player, session); // Refresh
                }
            });
            gui.addItem(guiItem);
            index++;
        }

        // Navigation
        gui.setItem(6, 3, config.getItemBuilder("nav.prev").asGuiItem(e -> gui.previous()));
        gui.setItem(6, 7, config.getItemBuilder("nav.next").asGuiItem(e -> gui.next()));
        gui.setItem(6, 5, config.getItemBuilder("nav.back").asGuiItem(e -> MainEditorGui.open(player, session)));

        // Add Point Button
        GuiItem addBtn = config.getItemBuilder("keyframes_list.add_point_btn").asGuiItem(event -> {
            player.performCommand("cinematic point");
            open(player, session);
        });
        gui.setItem(6, 9, addBtn);

        gui.open(player);
    }
}
