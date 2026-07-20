package com.pumpkiiiings.pkcinematics.editor.gui.menu;

import com.pumpkiiiings.pkcinematics.api.PkCinematics;
import com.pumpkiiiings.pkcinematics.config.GuiConfigManager;
import com.pumpkiiiings.pkcinematics.editor.EditorSession;
import com.pumpkiiiings.pkcinematics.action.impl.TitleAction;
import com.pumpkiiiings.pkcinematics.action.impl.MessageAction;
import com.pumpkiiiings.pkcinematics.action.impl.SoundAction;
import com.pumpkiiiings.pkcinematics.action.impl.ParticleAction;
import com.pumpkiiiings.pkcinematics.action.impl.TimeAction;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import org.bukkit.entity.Player;
import java.util.function.Consumer;
import com.pumpkiiiings.pkcinematics.config.Messages;

public class ActionSelectorGui {

    public static void open(Player player, EditorSession session) {
        PkCinematics api = PkCinematics.getApi();
        GuiConfigManager config = api.getGuiConfigManager();

        Gui gui = Gui.gui()
                .title(config.getComponent("action_selector.title"))
                .rows(3)
                .disableAllInteractions()
                .create();

        // Title
        GuiItem titleItem = config.getItemBuilder("action_selector.items.title").asGuiItem(event -> {
            askTickAndExecute(player, session, tick -> {
                api.getChatInputManager().requestInput(player, "§aEscribe el título (o 'none' para dejar vacío):", titleInput -> {
                    api.getChatInputManager().requestInput(player, "§aEscribe el subtítulo (o 'none' para dejar vacío):", subtitleInput -> {
                        String t = titleInput.equalsIgnoreCase("none") ? "" : titleInput;
                        String s = subtitleInput.equalsIgnoreCase("none") ? "" : subtitleInput;
                        TitleAction action = new TitleAction(t, s, 10, 60, 10);
                        session.getCinematic().getTimeline().getActionTrack().addAction(tick, action);
                        session.getCinematic().getTimeline().calculateDuration();
                        player.sendMessage(Messages.EDITOR_ACTION_ADDED.getWithPrefix("type", "Title", "tick", String.valueOf(tick)));
                        ActionsListGui.open(player, session);
                    });
                });
            });
        });
        gui.setItem(2, 2, titleItem);

        // Message
        GuiItem msgItem = config.getItemBuilder("action_selector.items.message").asGuiItem(event -> {
            askTickAndExecute(player, session, tick -> {
                api.getChatInputManager().requestInput(player, "§aEscribe el mensaje:", text -> {
                    MessageAction action = new MessageAction(text);
                    session.getCinematic().getTimeline().getActionTrack().addAction(tick, action);
                    session.getCinematic().getTimeline().calculateDuration();
                    player.sendMessage(Messages.EDITOR_ACTION_ADDED.getWithPrefix("type", "Message", "tick", String.valueOf(tick)));
                    ActionsListGui.open(player, session);
                });
            });
        });
        gui.setItem(2, 4, msgItem);

        // Sound
        GuiItem soundItem = config.getItemBuilder("action_selector.items.sound").asGuiItem(event -> {
            askTickAndExecute(player, session, tick -> {
                api.getChatInputManager().requestInput(player, "§aEscribe el nombre del sonido (ej. ENTITY_PLAYER_LEVELUP):", text -> {
                    SoundAction action = new SoundAction(text, 1.0f, 1.0f);
                    session.getCinematic().getTimeline().getActionTrack().addAction(tick, action);
                    session.getCinematic().getTimeline().calculateDuration();
                    player.sendMessage(Messages.EDITOR_ACTION_ADDED.getWithPrefix("type", "Sound", "tick", String.valueOf(tick)));
                    ActionsListGui.open(player, session);
                });
            });
        });
        gui.setItem(2, 6, soundItem);

        // Particle
        GuiItem particleItem = config.getItemBuilder("action_selector.items.particle").asGuiItem(event -> {
            askTickAndExecute(player, session, tick -> {
                api.getChatInputManager().requestInput(player, "§aEscribe el nombre de la partícula (ej. FLAME):", text -> {
                    ParticleAction action = new ParticleAction(text, 10, 1.0f, 1.0f, 1.0f, 0.1f);
                    session.getCinematic().getTimeline().getActionTrack().addAction(tick, action);
                    session.getCinematic().getTimeline().calculateDuration();
                    player.sendMessage(Messages.EDITOR_ACTION_ADDED.getWithPrefix("type", "Particle", "tick", String.valueOf(tick)));
                    ActionsListGui.open(player, session);
                });
            });
        });
        gui.setItem(2, 8, particleItem);

        // Time
        GuiItem timeItem = config.getItemBuilder("action_selector.items.time").asGuiItem(event -> {
            askTickAndExecute(player, session, tick -> {
                api.getChatInputManager().requestInput(player, "§aEscribe el tiempo (ej. day, night, noon, midnight, o en ticks como 6000):", text -> {
                    long t = 0;
                    switch(text.toLowerCase()) {
                        case "day": t = 1000; break;
                        case "noon": 
                        case "afternoon": t = 6000; break;
                        case "sunset": 
                        case "dusk": t = 12000; break;
                        case "night": t = 13000; break;
                        case "midnight": t = 18000; break;
                        case "sunrise": 
                        case "dawn": t = 23000; break;
                        default:
                            try {
                                t = Long.parseLong(text);
                            } catch (Exception ex) {
                                player.sendMessage("§cValor inválido, usando 0 (alba).");
                                t = 0;
                            }
                            break;
                    }
                    TimeAction action = new TimeAction(t);
                    session.getCinematic().getTimeline().getActionTrack().addAction(tick, action);
                    session.getCinematic().getTimeline().calculateDuration();
                    player.sendMessage(Messages.EDITOR_ACTION_ADDED.getWithPrefix("type", "Time", "tick", String.valueOf(tick)));
                    ActionsListGui.open(player, session);
                });
            });
        });
        gui.setItem(1, 5, timeItem);

        // Back
        gui.setItem(3, 5, config.getItemBuilder("nav.back").asGuiItem(e -> ActionsListGui.open(player, session)));

        gui.open(player);
    }

    private static void askTickAndExecute(Player player, EditorSession session, Consumer<Integer> onSuccess) {
        PkCinematics.getApi().getChatInputManager().requestInput(player, "§a¿En qué TICK quieres ejecutar esta acción?", input -> {
            try {
                int tick = Integer.parseInt(input);
                onSuccess.accept(tick);
            } catch (NumberFormatException e) {
                player.sendMessage(Messages.EDITOR_ACTION_INVALID_TICK.getWithPrefix());
                ActionsListGui.open(player, session);
            }
        });
    }
}
