package com.pumpkiiiings.pkcinematics.editor.gui.menu;

import com.pumpkiiiings.pkcinematics.api.PkCinematics;
import com.pumpkiiiings.pkcinematics.config.GuiConfigManager;
import com.pumpkiiiings.pkcinematics.editor.EditorSession;
import com.pumpkiiiings.pkcinematics.model.timeline.CameraKeyframe;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import org.bukkit.entity.Player;
import java.util.Comparator;

public class KeyframeEditorGui {

    private static final String[] INTERP_TYPES = {"LINEAR", "CATMULL_ROM"};
    private static final String[] EASING_TYPES = {"LINEAR", "EASE_IN", "EASE_OUT", "SMOOTH"};

    public static void open(Player player, EditorSession session, CameraKeyframe kf, int index) {
        PkCinematics api = PkCinematics.getApi();
        GuiConfigManager config = api.getGuiConfigManager();

        Gui gui = Gui.gui()
                .title(config.getComponent("keyframe_edit.title", "index", String.valueOf(index)))
                .rows(3)
                .disableAllInteractions()
                .create();

        // Time (Tick)
        GuiItem timeItem = config.getItemBuilder("keyframe_edit.items.time", "value", String.valueOf(kf.getTick()))
                .asGuiItem(event -> {
                    api.getChatInputManager().requestInput(player, "§aEscribe el nuevo valor de TICK (tiempo):", input -> {
                        try {
                            int newTick = Integer.parseInt(input);
                            kf.setTick(newTick);
                            session.getCinematic().getTimeline().getCameraTrack().getKeyframes().sort(Comparator.comparingInt(CameraKeyframe::getTick));
                            session.getCinematic().getTimeline().calculateDuration();
                            player.sendMessage("§aTick actualizado a " + newTick);
                        } catch (NumberFormatException e) {
                            player.sendMessage("§cPor favor, escribe un número válido.");
                        }
                        open(player, session, kf, session.getCinematic().getTimeline().getCameraTrack().getKeyframes().indexOf(kf));
                    });
                });
        gui.setItem(1, 3, timeItem);

        // FOV
        GuiItem fovItem = config.getItemBuilder("keyframe_edit.items.fov", "value", String.valueOf(kf.getFov()))
                .asGuiItem(event -> {
                    api.getChatInputManager().requestInput(player, "§aEscribe el nuevo valor de FOV (zoom):", input -> {
                        try {
                            float newFov = Float.parseFloat(input);
                            kf.setFov(newFov);
                            player.sendMessage("§aFOV actualizado a " + newFov);
                        } catch (NumberFormatException e) {
                            player.sendMessage("§cPor favor, escribe un número decimal válido.");
                        }
                        open(player, session, kf, index);
                    });
                });
        gui.setItem(1, 7, fovItem);

        // Interpolation
        GuiItem interpItem = config.getItemBuilder("keyframe_edit.items.interp", "value", kf.getInterpolationType())
                .asGuiItem(event -> {
                    String current = kf.getInterpolationType();
                    int i = 0;
                    for (int j = 0; j < INTERP_TYPES.length; j++) {
                        if (INTERP_TYPES[j].equalsIgnoreCase(current)) i = j;
                    }
                    kf.setInterpolationType(INTERP_TYPES[(i + 1) % INTERP_TYPES.length]);
                    open(player, session, kf, index); // Refresh
                });
        gui.setItem(2, 4, interpItem);

        // Easing
        GuiItem easingItem = config.getItemBuilder("keyframe_edit.items.easing", "value", kf.getEasingType())
                .asGuiItem(event -> {
                    String current = kf.getEasingType();
                    int i = 0;
                    for (int j = 0; j < EASING_TYPES.length; j++) {
                        if (EASING_TYPES[j].equalsIgnoreCase(current)) i = j;
                    }
                    kf.setEasingType(EASING_TYPES[(i + 1) % EASING_TYPES.length]);
                    open(player, session, kf, index); // Refresh
                });
        gui.setItem(2, 6, easingItem);

        // Back
        gui.setItem(3, 5, config.getItemBuilder("nav.back").asGuiItem(e -> KeyframesListGui.open(player, session)));

        gui.open(player);
    }
}
