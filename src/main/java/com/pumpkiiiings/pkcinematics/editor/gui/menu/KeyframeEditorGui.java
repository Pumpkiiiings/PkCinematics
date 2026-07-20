package com.pumpkiiiings.pkcinematics.editor.gui.menu;

import com.pumpkiiiings.pkcinematics.api.PkCinematics;
import com.pumpkiiiings.pkcinematics.config.GuiConfigManager;
import com.pumpkiiiings.pkcinematics.editor.EditorSession;
import com.pumpkiiiings.pkcinematics.model.timeline.CameraKeyframe;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import org.bukkit.entity.Player;
import java.util.Comparator;
import com.pumpkiiiings.pkcinematics.config.Messages;

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
                            player.sendMessage(Messages.EDITOR_TICK_UPDATED.getWithPrefix("value", String.valueOf(newTick)));
                        } catch (NumberFormatException e) {
                            player.sendMessage(Messages.EDITOR_INVALID_NUMBER.getWithPrefix());
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
                            player.sendMessage(Messages.EDITOR_FOV_UPDATED.getWithPrefix("value", String.valueOf(newFov)));
                        } catch (NumberFormatException e) {
                            player.sendMessage(Messages.EDITOR_INVALID_DECIMAL.getWithPrefix());
                        }
                        open(player, session, kf, index);
                    });
                });
        gui.setItem(1, 7, fovItem);

        // Waiting
        GuiItem waitingItem = config.getItemBuilder("keyframe_edit.items.waiting", "value", "0")
                .asGuiItem(event -> {
                    api.getChatInputManager().requestInput(player, "§aEscribe los ticks que deseas esperar en este punto:", input -> {
                        try {
                            int waitTicks = Integer.parseInt(input);
                            if (waitTicks <= 0) {
                                player.sendMessage(Messages.EDITOR_INVALID_WAIT_TICKS.getWithPrefix());
                                return;
                            }
                            // Shift all keyframes after this one
                            for (CameraKeyframe other : session.getCinematic().getTimeline().getCameraTrack().getKeyframes()) {
                                if (other.getTick() > kf.getTick()) {
                                    other.setTick(other.getTick() + waitTicks);
                                }
                            }
                            // Shift all actions after this one
                            com.pumpkiiiings.pkcinematics.model.timeline.ActionTrack actionTrack = session.getCinematic().getTimeline().getActionTrack();
                            java.util.Map<Integer, java.util.List<com.pumpkiiiings.pkcinematics.api.action.PkAction>> oldActions = new java.util.HashMap<>(actionTrack.getAllActions());
                            actionTrack.getAllActions().clear();
                            for (java.util.Map.Entry<Integer, java.util.List<com.pumpkiiiings.pkcinematics.api.action.PkAction>> entry : oldActions.entrySet()) {
                                int newActionTick = entry.getKey() > kf.getTick() ? entry.getKey() + waitTicks : entry.getKey();
                                for (com.pumpkiiiings.pkcinematics.api.action.PkAction act : entry.getValue()) {
                                    actionTrack.addAction(newActionTick, act);
                                }
                            }
                            
                            // Insert clone
                            CameraKeyframe clone = new CameraKeyframe(
                                    kf.getTick() + waitTicks,
                                    kf.getWorldName(),
                                    kf.getX(), kf.getY(), kf.getZ(),
                                    kf.getYaw(), kf.getPitch(),
                                    kf.getFov(),
                                    kf.getInterpolationType(),
                                    kf.getEasingType()
                            );
                            session.getCinematic().getTimeline().getCameraTrack().addKeyframe(clone);
                            session.getCinematic().getTimeline().calculateDuration();
                            
                            player.sendMessage(Messages.EDITOR_WAIT_TIME_ADDED.getWithPrefix("ticks", String.valueOf(waitTicks)));
                            player.playSound(player.getLocation(), org.bukkit.Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.2f);
                        } catch (NumberFormatException e) {
                            player.sendMessage(Messages.EDITOR_INVALID_NUMBER.getWithPrefix());
                        }
                        KeyframesListGui.open(player, session);
                    });
                });
        gui.setItem(1, 5, waitingItem);

        // Interpolation
        String curInterp = kf.getInterpolationType();
        GuiItem interpItem = config.getItemBuilder("keyframe_edit.items.interp", 
                "value", curInterp,
                "opt_linear", curInterp.equalsIgnoreCase("LINEAR") ? "§a▶ Linear" : "§7  Linear",
                "opt_catmull_rom", curInterp.equalsIgnoreCase("CATMULL_ROM") ? "§a▶ Catmull_Rom" : "§7  Catmull_Rom"
        )
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
        String curEasing = kf.getEasingType();
        GuiItem easingItem = config.getItemBuilder("keyframe_edit.items.easing", 
                "value", curEasing,
                "opt_linear", curEasing.equalsIgnoreCase("LINEAR") ? "§a▶ Linear" : "§7  Linear",
                "opt_ease_in", curEasing.equalsIgnoreCase("EASE_IN") ? "§a▶ Ease_In" : "§7  Ease_In",
                "opt_ease_out", curEasing.equalsIgnoreCase("EASE_OUT") ? "§a▶ Ease_Out" : "§7  Ease_Out",
                "opt_smooth", curEasing.equalsIgnoreCase("SMOOTH") ? "§a▶ Smooth" : "§7  Smooth"
        )
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
