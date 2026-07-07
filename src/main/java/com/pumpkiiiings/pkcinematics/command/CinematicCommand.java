package com.pumpkiiiings.pkcinematics.command;

import com.pumpkiiiings.pkcinematics.api.PkCinematics;
import com.pumpkiiiings.pkcinematics.editor.EditorManager;
import com.pumpkiiiings.pkcinematics.editor.EditorSession;
import com.pumpkiiiings.pkcinematics.model.Cinematic;
import com.pumpkiiiings.pkcinematics.model.timeline.CameraKeyframe;
import com.pumpkiiiings.pkcinematics.model.timeline.CameraTrack;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.Location;
import java.util.List;

public class CinematicCommand implements CommandExecutor, TabCompleter {

    private final EditorManager editorManager;
    private final com.pumpkiiiings.pkcinematics.api.storage.CinematicRepository repository;

    public CinematicCommand(EditorManager editorManager, com.pumpkiiiings.pkcinematics.api.storage.CinematicRepository repository) {
        this.editorManager = editorManager;
        this.repository = repository;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) return true;
        Player player = (Player) sender;
        
        com.pumpkiiiings.pkcinematics.config.MessageManager msg = PkCinematics.getApi().getMessageManager();

        if (!player.hasPermission("pkcinematics.admin")) {
            player.sendMessage(msg.getMessage("prefix") + msg.getMessage("no_permission"));
            return true;
        }

        if (args.length == 0) {
            player.sendMessage(msg.getMessage("help_header"));
            player.sendMessage(msg.getMessage("help_create"));
            player.sendMessage(msg.getMessage("help_edit"));
            player.sendMessage(msg.getMessage("help_point"));
            player.sendMessage(msg.getMessage("help_point_edit"));
            player.sendMessage(msg.getMessage("help_actions"));
            player.sendMessage(msg.getMessage("help_save"));
            player.sendMessage(msg.getMessage("help_play"));
            player.sendMessage(msg.getMessage("help_stop"));
            player.sendMessage("§e/cinematic reload (cinematics|triggers|messages|all) §7- Recarga configuraciones");
            return true;
        }

        String sub = args[0].toLowerCase();

        if (sub.equals("create")) {
            if (args.length < 2) {
                player.sendMessage(msg.getMessage("prefix") + msg.getMessage("help_create"));
                return true;
            }
            String id = args[1];
            if (PkCinematics.getApi().getCinematicManager().getCinematic(id) != null) {
                player.sendMessage(msg.getMessage("prefix") + msg.getMessage("already_exists"));
                return true;
            }
            Cinematic cin = new Cinematic(id);
            PkCinematics.getApi().getCinematicManager().registerCinematic(cin);
            editorManager.startEditing(player, cin);
            return true;
        }

        if (sub.equals("edit")) {
            if (args.length < 2) {
                player.sendMessage(msg.getMessage("prefix") + msg.getMessage("help_edit"));
                return true;
            }
            String id = args[1];
            Cinematic cin = PkCinematics.getApi().getCinematicManager().getCinematic(id);
            if (cin == null) {
                player.sendMessage(msg.getMessage("prefix") + msg.getMessage("not_found"));
                return true;
            }
            editorManager.startEditing(player, cin);
            return true;
        }

        if (sub.equals("point")) {
            if (args.length > 1 && args[1].equalsIgnoreCase("edit")) {
                EditorSession session = editorManager.getSession(player);
                if (session == null) {
                    player.sendMessage(msg.getMessage("prefix") + msg.getMessage("editor_not_editing"));
                    return true;
                }
                if (args.length < 5) {
                    player.sendMessage(msg.getMessage("prefix") + msg.getMessage("help_point_edit"));
                    return true;
                }
                int index;
                try { index = Integer.parseInt(args[2]); } catch(Exception e) { return true; }
                
                CameraTrack track = session.getCinematic().getTimeline().getCameraTrack();
                if (index < 0 || index >= track.getKeyframes().size()) {
                    player.sendMessage(msg.getMessage("prefix") + msg.getMessage("editor_invalid_index"));
                    return true;
                }
                
                CameraKeyframe kf = track.getKeyframes().get(index);
                String prop = args[3].toLowerCase();
                String val = args[4];
                
                if (prop.equals("time") || prop.equals("tick")) {
                    kf.setTick(Integer.parseInt(val));
                    track.getKeyframes().sort((a,b) -> Integer.compare(a.getTick(), b.getTick()));
                    session.getCinematic().getTimeline().calculateDuration();
                    player.sendMessage(msg.getMessage("prefix") + msg.getMessage("editor_tick_updated", "value", val));
                } else if (prop.equals("fov")) {
                    kf.setFov(Float.parseFloat(val));
                    player.sendMessage(msg.getMessage("prefix") + msg.getMessage("editor_fov_updated", "value", val));
                } else if (prop.equals("interp")) {
                    kf.setInterpolationType(val.toUpperCase());
                    player.sendMessage(msg.getMessage("prefix") + msg.getMessage("editor_interp_updated", "value", val));
                } else {
                    player.sendMessage(msg.getMessage("prefix") + msg.getMessage("editor_invalid_property"));
                }
                return true;
            }

            EditorSession session = editorManager.getSession(player);
            if (session == null) {
                player.sendMessage(msg.getMessage("prefix") + msg.getMessage("editor_not_editing"));
                return true;
            }
            
            CameraTrack track = session.getCinematic().getTimeline().getCameraTrack();
            int currentMaxTick = 0;
            if (!track.getKeyframes().isEmpty()) {
                currentMaxTick = track.getKeyframes().get(track.getKeyframes().size() - 1).getTick();
            }
            
            // Add 60 ticks (3 seconds) by default if there is a previous point
            int newTick = track.getKeyframes().isEmpty() ? 0 : currentMaxTick + 60;
            
            Location loc = player.getEyeLocation();
            
            // Minecraft ArmorStand Eye Height is approximately 1.62.
            // If we want the camera exactly at the player's eyes, we must spawn the entity 1.62 blocks below.
            double cameraY = loc.getY() - 1.62;
            
            CameraKeyframe kf = new CameraKeyframe(
                newTick, 
                loc.getWorld().getName(),
                loc.getX(), cameraY, loc.getZ(),
                loc.getYaw(), loc.getPitch(),
                70.0f, // Default FOV
                "LINEAR" // Default interpolation
            );
            
            track.addKeyframe(kf);
            session.getCinematic().getTimeline().calculateDuration(); // Update total duration
            
            int index = track.getKeyframes().indexOf(kf);
            player.sendMessage(msg.getMessage("prefix") + msg.getMessage("editor_point_added", "index", index, "tick", newTick));
            return true;
        }
        
        if (sub.equals("save")) {
            EditorSession session = editorManager.getSession(player);
            if (session == null) {
                player.sendMessage(msg.getMessage("prefix") + msg.getMessage("editor_not_editing"));
                return true;
            }
            repository.save(session.getCinematic());
            player.sendMessage(msg.getMessage("prefix") + msg.getMessage("saved", "name", session.getCinematic().getId()));
            editorManager.stopEditing(player);
            return true;
        }
        
        if (sub.equals("actions")) {
            EditorSession session = editorManager.getSession(player);
            if (session == null) {
                player.sendMessage(msg.getMessage("prefix") + msg.getMessage("editor_not_editing"));
                return true;
            }
            if (args.length < 2) {
                player.sendMessage(msg.getMessage("prefix") + msg.getMessage("help_actions"));
                return true;
            }
            try {
                int tick = Integer.parseInt(args[1]);
                com.pumpkiiiings.pkcinematics.editor.gui.ActionEditorGUI.openMainMenu(player, session, tick);
            } catch (Exception e) {
                player.sendMessage(msg.getMessage("prefix") + msg.getMessage("editor_invalid_index"));
            }
            return true;
        }
        
        if (sub.equals("play")) {
            if (args.length < 2) {
                player.sendMessage(msg.getMessage("prefix") + msg.getMessage("help_play"));
                return true;
            }
            String id = args[1];
            Cinematic cin = PkCinematics.getApi().getCinematicManager().getCinematic(id);
            if (cin == null) {
                player.sendMessage(msg.getMessage("prefix") + msg.getMessage("not_found"));
                return true;
            }
            PkCinematics.getApi().getPlaybackManager().play(player, cin);
            player.sendMessage(msg.getMessage("prefix") + msg.getMessage("playback_playing"));
            return true;
        }

        if (sub.equals("stop")) {
            PkCinematics.getApi().getPlaybackManager().stop(player);
            player.sendMessage(msg.getMessage("prefix") + msg.getMessage("playback_stopped"));
            return true;
        }
        
        if (sub.equals("reload")) {
            if (args.length < 2) {
                player.sendMessage(msg.getMessage("prefix") + "§cUso: /cinematic reload <cinematics|triggers|messages|all>");
                return true;
            }
            String target = args[1].toLowerCase();
            
            if (target.equals("cinematics") || target.equals("all")) {
                for (Cinematic cin : PkCinematics.getApi().getCinematicManager().getAllCinematics()) {
                    PkCinematics.getApi().getCinematicManager().unregisterCinematic(cin.getId());
                }
                repository.loadAll().forEach(PkCinematics.getApi().getCinematicManager()::registerCinematic);
                player.sendMessage(msg.getMessage("prefix") + "§aCinemáticas recargadas exitosamente.");
            }
            if (target.equals("triggers") || target.equals("all")) {
                PkCinematics.getApi().getTriggerManager().loadAll();
                player.sendMessage(msg.getMessage("prefix") + "§aTriggers recargados exitosamente.");
            }
            if (target.equals("messages") || target.equals("all")) {
                PkCinematics.getApi().getMessageManager().reload();
                player.sendMessage(msg.getMessage("prefix") + "§aMensajes recargados exitosamente.");
            }
            return true;
        }

        if (sub.equals("debug")) {
            com.pumpkiiiings.pkcinematics.core.PlaybackManagerImpl pbManager = (com.pumpkiiiings.pkcinematics.core.PlaybackManagerImpl) PkCinematics.getApi().getPlaybackManager();
            pbManager.toggleDebug(player);
            if (pbManager.isDebugEnabled(player)) {
                player.sendMessage(msg.getMessage("prefix") + "§aModo debug de cinemáticas ACTIVADO.");
            } else {
                player.sendMessage(msg.getMessage("prefix") + "§cModo debug de cinemáticas DESACTIVADO.");
            }
            return true;
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (!sender.hasPermission("pkcinematics.admin")) return java.util.Collections.emptyList();
        
        if (args.length == 1) {
            return java.util.stream.Stream.of("create", "edit", "point", "actions", "save", "play", "stop", "reload", "debug")
                    .filter(s -> s.startsWith(args[0].toLowerCase()))
                    .collect(java.util.stream.Collectors.toList());
        }
        
        if (args.length == 2) {
            if (args[0].equalsIgnoreCase("reload")) {
                return java.util.stream.Stream.of("cinematics", "triggers", "messages", "all")
                        .filter(s -> s.startsWith(args[1].toLowerCase()))
                        .collect(java.util.stream.Collectors.toList());
            } else if (args[0].equalsIgnoreCase("play") || args[0].equalsIgnoreCase("edit")) {
                return PkCinematics.getApi().getCinematicManager().getAllCinematics().stream()
                        .map(Cinematic::getId)
                        .filter(s -> s.toLowerCase().startsWith(args[1].toLowerCase()))
                        .collect(java.util.stream.Collectors.toList());
            } else if (args[0].equalsIgnoreCase("point")) {
                return java.util.stream.Stream.of("edit")
                        .filter(s -> s.startsWith(args[1].toLowerCase()))
                        .collect(java.util.stream.Collectors.toList());
            }
        }
        
        if (args.length == 4 && args[0].equalsIgnoreCase("point") && args[1].equalsIgnoreCase("edit")) {
            return java.util.stream.Stream.of("time", "tick", "fov", "interp")
                    .filter(s -> s.startsWith(args[3].toLowerCase()))
                    .collect(java.util.stream.Collectors.toList());
        }
        
        return java.util.Collections.emptyList();
    }
}
