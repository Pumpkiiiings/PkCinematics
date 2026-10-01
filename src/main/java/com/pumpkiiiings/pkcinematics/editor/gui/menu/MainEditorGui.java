package com.pumpkiiiings.pkcinematics.editor.gui.menu;

import com.pumpkiiiings.pkcinematics.api.PkCinematics;
import com.pumpkiiiings.pkcinematics.config.GuiConfigManager;
import com.pumpkiiiings.pkcinematics.editor.EditorSession;
import com.pumpkiiiings.pkcinematics.gui.PkItem;
import com.pumpkiiiings.pkcinematics.gui.PkMenu;
import com.pumpkiiiings.pkcinematics.gui.PkGuiItem;
import org.bukkit.entity.Player;
import com.pumpkiiiings.pkcinematics.model.Cinematic;
import net.kyori.adventure.text.Component;

public class MainEditorGui {

    public static void open(Player player, EditorSession session) {
        PkCinematics api = PkCinematics.getApi();
        GuiConfigManager config = api.getGuiConfigManager();
        Cinematic cinematic = session.getCinematic();

        Component title = config.getComponent("main.title", "cinematic", cinematic.getId());
        PkMenu gui = PkMenu.gui()
                .title(title)
                .rows(3)
                .create();

        // Info Item
        PkItem infoBuilder = config.getItemBuilder("main.items.info",
                "duration", String.valueOf(cinematic.getTimeline().getDurationTicks()),
                "keyframes", String.valueOf(cinematic.getTimeline().getCameraTrack().getKeyframes().size()),
                "actions", String.valueOf(cinematic.getTimeline().getActionTrack().getAllActions().size()),
                "id", cinematic.getId()
        );
        gui.setItem(1, 5, infoBuilder.asGuiItem());

        // Skipeable Item
        String status = cinematic.isSkipeable() ? config.getString("nav.enabled") : config.getString("nav.disabled");
        PkItem skipeableBuilder = config.getItemBuilder("main.items.skipeable", "status", status);
        PkGuiItem skipeableItem = skipeableBuilder.asGuiItem(event -> {
            cinematic.setSkipeable(!cinematic.isSkipeable());
            open(player, session);
        });
        gui.setItem(2, 3, skipeableItem);

        // Keyframes Item
        PkItem keyframesBuilder = config.getItemBuilder("main.items.keyframes");
        PkGuiItem keyframesItem = keyframesBuilder.asGuiItem(event -> {
            KeyframesListGui.open(player, session);
        });
        gui.setItem(2, 4, keyframesItem);

        // Actions Item
        PkItem actionsBuilder = config.getItemBuilder("main.items.actions");
        PkGuiItem actionsItem = actionsBuilder.asGuiItem(event -> {
            ActionsListGui.open(player, session);
        });
        gui.setItem(2, 5, actionsItem);

        // Play Item
        PkItem playBuilder = config.getItemBuilder("main.items.play");
        PkGuiItem playItem = playBuilder.asGuiItem(event -> {
            gui.close(player);
            api.getPlaybackManager().play(player, cinematic);
        });
        gui.setItem(2, 6, playItem);

        // Save Item
        PkItem saveBuilder = config.getItemBuilder("main.items.save");
        PkGuiItem saveItem = saveBuilder.asGuiItem(event -> {
            gui.close(player);
            player.performCommand("cinematic save");
        });
        gui.setItem(2, 7, saveItem);

        gui.open(player);
    }
}
