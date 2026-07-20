package com.pumpkiiiings.pkcinematics.editor.gui.menu;

import com.pumpkiiiings.pkcinematics.api.PkCinematics;
import com.pumpkiiiings.pkcinematics.config.GuiConfigManager;
import com.pumpkiiiings.pkcinematics.editor.EditorSession;
import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import org.bukkit.entity.Player;
import com.pumpkiiiings.pkcinematics.model.Cinematic;
import net.kyori.adventure.text.Component;

public class MainEditorGui {

    public static void open(Player player, EditorSession session) {
        PkCinematics api = PkCinematics.getApi();
        GuiConfigManager config = api.getGuiConfigManager();
        Cinematic cinematic = session.getCinematic();

        Component title = config.getComponent("main.title", "cinematic", cinematic.getId());
        Gui gui = Gui.gui()
                .title(title)
                .rows(3)
                .disableAllInteractions()
                .create();

        // Info Item
        ItemBuilder infoBuilder = config.getItemBuilder("main.items.info",
                "duration", String.valueOf(cinematic.getTimeline().getDurationTicks()),
                "keyframes", String.valueOf(cinematic.getTimeline().getCameraTrack().getKeyframes().size()),
                "actions", String.valueOf(cinematic.getTimeline().getActionTrack().getAllActions().size()),
                "id", cinematic.getId()
        );
        gui.setItem(1, 5, infoBuilder.asGuiItem());

        // Skipeable Item
        String status = cinematic.isSkipeable() ? config.getString("nav.enabled") : config.getString("nav.disabled");
        ItemBuilder skipeableBuilder = config.getItemBuilder("main.items.skipeable", "status", status);
        GuiItem skipeableItem = skipeableBuilder.asGuiItem(event -> {
            cinematic.setSkipeable(!cinematic.isSkipeable());
            open(player, session); // Refresh
        });
        gui.setItem(2, 3, skipeableItem);

        // Keyframes Item
        ItemBuilder keyframesBuilder = config.getItemBuilder("main.items.keyframes");
        GuiItem keyframesItem = keyframesBuilder.asGuiItem(event -> {
            KeyframesListGui.open(player, session);
        });
        gui.setItem(2, 4, keyframesItem);

        // Actions Item
        ItemBuilder actionsBuilder = config.getItemBuilder("main.items.actions");
        GuiItem actionsItem = actionsBuilder.asGuiItem(event -> {
            ActionsListGui.open(player, session);
        });
        gui.setItem(2, 5, actionsItem);

        // Play Item
        ItemBuilder playBuilder = config.getItemBuilder("main.items.play");
        GuiItem playItem = playBuilder.asGuiItem(event -> {
            gui.close(player);
            api.getPlaybackManager().play(player, cinematic);
        });
        gui.setItem(2, 6, playItem);

        // Save Item
        ItemBuilder saveBuilder = config.getItemBuilder("main.items.save");
        GuiItem saveItem = saveBuilder.asGuiItem(event -> {
            gui.close(player);
            // Save logic should ideally be triggered here or user runs /cinematic save
            player.performCommand("cinematic save");
        });
        gui.setItem(2, 7, saveItem);

        gui.open(player);
    }
}
