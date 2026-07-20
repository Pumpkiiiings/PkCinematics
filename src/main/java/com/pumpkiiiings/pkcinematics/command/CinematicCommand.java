package com.pumpkiiiings.pkcinematics.command;

import com.pumpkiiiings.pkcinematics.api.PkCinematics;
import com.pumpkiiiings.pkcinematics.editor.EditorManager;
import com.pumpkiiiings.pkcinematics.editor.EditorSession;
import com.pumpkiiiings.pkcinematics.model.Cinematic;
import com.pumpkiiiings.pkcinematics.model.timeline.CameraKeyframe;
import com.pumpkiiiings.pkcinematics.model.timeline.CameraTrack;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.Location;
import io.papermc.paper.command.brigadier.BasicCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import java.util.Collection;
import java.util.List;
import com.pumpkiiiings.pkcinematics.api.storage.CinematicRepository;
import com.pumpkiiiings.pkcinematics.config.MessageManager;
import com.pumpkiiiings.pkcinematics.core.PlaybackManagerImpl;
import com.pumpkiiiings.pkcinematics.editor.gui.ActionEditorGUI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import org.bukkit.Bukkit;

public class CinematicCommand implements BasicCommand {

    private final EditorManager editorManager;
    private final CinematicRepository repository;

    public CinematicCommand(EditorManager editorManager, CinematicRepository repository) {
        this.editorManager = editorManager;
        this.repository = repository;
    }

    @Override
    public void execute(CommandSourceStack stack, String[] args) {
        CommandSender sender = stack.getSender();
        if (!(sender instanceof Player)) return;
        Player player = (Player) sender;
        
        MessageManager msg = PkCinematics.getApi().getMessageManager();

        if (!player.hasPermission("pkcinematics.admin")) {
            player.sendMessage(msg.getMessage("prefix") + msg.getMessage("no_permission"));
            return;
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
            return;
        }

        String sub = args[0].toLowerCase();

        if (sub.equals("create")) {
            if (args.length < 2) {
                player.sendMessage(msg.getMessage("prefix") + msg.getMessage("help_create"));
                return;
            }
            String id = args[1];
            if (PkCinematics.getApi().getCinematicManager().getCinematic(id) != null) {
                player.sendMessage(msg.getMessage("prefix") + msg.getMessage("already_exists"));
                return;
            }
            Cinematic cin = new Cinematic(id);
            PkCinematics.getApi().getCinematicManager().registerCinematic(cin);
            editorManager.startEditing(player, cin);
            return;
        }

        if (sub.equals("edit")) {
            if (args.length < 2) {
                player.sendMessage(msg.getMessage("prefix") + msg.getMessage("help_edit"));
                return;
            }
            String id = args[1];
            Cinematic cin = PkCinematics.getApi().getCinematicManager().getCinematic(id);
            if (cin == null) {
                player.sendMessage(msg.getMessage("prefix") + msg.getMessage("not_found"));
                return;
            }
            editorManager.startEditing(player, cin);
            return;
        }

        if (sub.equals("point")) {
            if (args.length > 1 && args[1].equalsIgnoreCase("edit")) {
                EditorSession session = editorManager.getSession(player);
                if (session == null) {
                    player.sendMessage(msg.getMessage("prefix") + msg.getMessage("editor_not_editing"));
                    return;
                }
                if (args.length < 5) {
                    player.sendMessage(msg.getMessage("prefix") + msg.getMessage("help_point_edit"));
                    return;
                }
                int index;
                try { index = Integer.parseInt(args[2]); } catch(Exception e) { return true; }
                
                CameraTrack track = session.getCinematic().getTimeline().getCameraTrack();
                if (index < 0 || index >= track.getKeyframes().size()) {
                    player.sendMessage(msg.getMessage("prefix") + msg.getMessage("editor_invalid_index"));
                    return;
                }
                
                CameraKeyframe kf = track.getKeyframes().get(index);
                String prop = args[3].toLowerCase();
                String val = args[4];
                
                if (prop.equals("time") || prop.equals("tick")) {
                    kf.setTick(Integer.parseInt(val));
                    track.getKeyframes().sort(new Comparator<CameraKeyframe>() {
                        @Override
                        public int compare(CameraKeyframe a, CameraKeyframe b) {
                            return Integer.compare(a.getTick(), b.getTick());
                        }
                    });
                    session.getCinematic().getTimeline().calculateDuration();
                    player.sendMessage(msg.getMessage("prefix") + msg.getMessage("editor_tick_updated", "value", val));
                } else if (prop.equals("fov")) {
                    kf.setFov(Float.parseFloat(val));
                    player.sendMessage(msg.getMessage("prefix") + msg.getMessage("editor_fov_updated", "value", val));
                } else if (prop.equals("interp")) {
                    kf.setInterpolationType(val.toUpperCase());
                    player.sendMessage(msg.getMessage("prefix") + msg.getMessage("editor_interp_updated", "value", val));
                } else if (prop.equals("easing")) {
                    kf.setEasingType(val.toUpperCase());
                    player.sendMessage(msg.getMessage("prefix") + "§aEasing actualizado a: " + val);
                } else {
                    player.sendMessage(msg.getMessage("prefix") + msg.getMessage("editor_invalid_property"));
                }
                return;
            }

            EditorSession session = editorManager.getSession(player);
            if (session == null) {
                player.sendMessage(msg.getMessage("prefix") + msg.getMessage("editor_not_editing"));
                return;
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
            double cameraY = loc.getY() - Double.parseDouble("1.62");
            
            CameraKeyframe kf = new CameraKeyframe(
                newTick, 
                loc.getWorld().getName(),
                loc.getX(), cameraY, loc.getZ(),
                loc.getYaw(), loc.getPitch(),
                Float.parseFloat("70.0"), // Default FOV
                "LINEAR", // Default interpolation
                "LINEAR" // Default easing
            );
            
            track.addKeyframe(kf);
            session.getCinematic().getTimeline().calculateDuration(); // Update total duration
            
            int index = track.getKeyframes().indexOf(kf);
            player.sendMessage(msg.getMessage("prefix") + msg.getMessage("editor_point_added", "index", index, "tick", newTick));
            return;
        }
        
        if (sub.equals("save")) {
            EditorSession session = editorManager.getSession(player);
            if (session == null) {
                player.sendMessage(msg.getMessage("prefix") + msg.getMessage("editor_not_editing"));
                return;
            }
            repository.save(session.getCinematic());
            player.sendMessage(msg.getMessage("prefix") + msg.getMessage("saved", "name", session.getCinematic().getId()));
            editorManager.stopEditing(player);
            return;
        }
        
        if (sub.equals("actions")) {
            EditorSession session = editorManager.getSession(player);
            if (session == null) {
                player.sendMessage(msg.getMessage("prefix") + msg.getMessage("editor_not_editing"));
                return;
            }
            if (args.length < 2) {
                player.sendMessage(msg.getMessage("prefix") + msg.getMessage("help_actions"));
                return;
            }
            try {
                int tick = Integer.parseInt(args[1]);
                ActionEditorGUI.openMainMenu(player, session, tick);
            } catch (Exception e) {
                player.sendMessage(msg.getMessage("prefix") + msg.getMessage("editor_invalid_index"));
            }
            return;
        }
        
        if (sub.equals("play")) {
            if (args.length < 2) {
                player.sendMessage(msg.getMessage("prefix") + msg.getMessage("help_play"));
                return;
            }
            String id = args[1];
            Cinematic cin = PkCinematics.getApi().getCinematicManager().getCinematic(id);
            if (cin == null) {
                player.sendMessage(msg.getMessage("prefix") + msg.getMessage("not_found"));
                return;
            }

            if (args.length >= 3) {
                String targetName = args[2];
                if (targetName.equalsIgnoreCase("all") || targetName.equalsIgnoreCase("todos") || targetName.equals("*")) {
                    List<Player> onlinePlayers = new ArrayList<>(Bukkit.getOnlinePlayers());
                    PkCinematics.getApi().getPlaybackManager().play(onlinePlayers, cin);
                    player.sendMessage(msg.getMessage("prefix") + "§aReproduciendo cinemática " + id + " para todos los jugadores.");
                } else {
                    Player target = Bukkit.getPlayer(targetName);
                    if (target == null) {
                        player.sendMessage(msg.getMessage("prefix") + "§cJugador no encontrado: " + targetName);
                        return;
                    }
                    PkCinematics.getApi().getPlaybackManager().play(target, cin);
                    player.sendMessage(msg.getMessage("prefix") + "§aReproduciendo cinemática " + id + " para " + target.getName() + ".");
                }
            } else {
                PkCinematics.getApi().getPlaybackManager().play(player, cin);
                player.sendMessage(msg.getMessage("prefix") + msg.getMessage("playback_playing"));
            }
            return;
        }

        if (sub.equals("stop")) {
            PkCinematics.getApi().getPlaybackManager().stop(player);
            player.sendMessage(msg.getMessage("prefix") + msg.getMessage("playback_stopped"));
            return;
        }
        
        if (sub.equals("reload")) {
            if (args.length < 2) {
                player.sendMessage(msg.getMessage("prefix") + "§cUso: /cinematic reload <cinematics|triggers|messages|all>");
                return;
            }
            String target = args[1].toLowerCase();
            
            if (target.equals("cinematics") || target.equals("all")) {
                for (Cinematic cin : PkCinematics.getApi().getCinematicManager().getAllCinematics()) {
                    PkCinematics.getApi().getCinematicManager().unregisterCinematic(cin.getId());
                }
                for (Cinematic c : repository.loadAll()) {
                    PkCinematics.getApi().getCinematicManager().registerCinematic(c);
                }
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
            return;
        }

        if (sub.equals("debug")) {
            PlaybackManagerImpl pbManager = (PlaybackManagerImpl) PkCinematics.getApi().getPlaybackManager();
            pbManager.toggleDebug(player);
            if (pbManager.isDebugEnabled(player)) {
                player.sendMessage(msg.getMessage("prefix") + "§aModo debug de cinemáticas ACTIVADO.");
            } else {
                player.sendMessage(msg.getMessage("prefix") + "§cModo debug de cinemáticas DESACTIVADO.");
            }
            return;
        }

    }

    @Override
    public Collection<String> suggest(CommandSourceStack stack, String[] args) {
        CommandSender sender = stack.getSender();
        if (!sender.hasPermission("pkcinematics.admin")) return Collections.emptyList();
        
        if (args.length == 1) {
            List<String> results = new ArrayList<>();
            for (String s : new String[]{"create", "edit", "point", "actions", "save", "play", "stop", "reload", "debug"}) {
                if (s.startsWith(args[0].toLowerCase())) results.add(s);
            }
            return results;
        }
        
        if (args.length == 2) {
            if (args[0].equalsIgnoreCase("reload")) {
                List<String> results = new ArrayList<>();
                for (String s : new String[]{"cinematics", "triggers", "messages", "all"}) {
                    if (s.startsWith(args[1].toLowerCase())) results.add(s);
                }
                return results;
            } else if (args[0].equalsIgnoreCase("play") || args[0].equalsIgnoreCase("edit")) {
                List<String> results = new ArrayList<>();
                for (Cinematic cin : PkCinematics.getApi().getCinematicManager().getAllCinematics()) {
                    if (cin.getId().toLowerCase().startsWith(args[1].toLowerCase())) results.add(cin.getId());
                }
                return results;
            } else if (args[0].equalsIgnoreCase("point")) {
                List<String> results = new ArrayList<>();
                if ("edit".startsWith(args[1].toLowerCase())) results.add("edit");
                return results;
            }
        }
        
        if (args.length == 3) {
            if (args[0].equalsIgnoreCase("play")) {
                List<String> results = new ArrayList<>();
                if ("todos".startsWith(args[2].toLowerCase())) results.add("todos");
                if ("all".startsWith(args[2].toLowerCase())) results.add("all");
                for (Player p : Bukkit.getOnlinePlayers()) {
                    if (p.getName().toLowerCase().startsWith(args[2].toLowerCase())) results.add(p.getName());
                }
                return results;
            }
        }
        
        if (args.length == 4 && args[0].equalsIgnoreCase("point") && args[1].equalsIgnoreCase("edit")) {
            List<String> results = new ArrayList<>();
            for (String s : new String[]{"time", "tick", "fov", "interp", "easing"}) {
                if (s.startsWith(args[3].toLowerCase())) results.add(s);
            }
            return results;
        }
        
        if (args.length == 5 && args[0].equalsIgnoreCase("point") && args[1].equalsIgnoreCase("edit")) {
            if (args[3].equalsIgnoreCase("interp")) {
                List<String> results = new ArrayList<>();
                for (String s : new String[]{"LINEAR", "CATMULL_ROM"}) {
                    if (s.startsWith(args[4].toUpperCase())) results.add(s);
                }
                return results;
            }
            if (args[3].equalsIgnoreCase("easing")) {
                List<String> results = new ArrayList<>();
                for (String s : new String[]{"LINEAR", "EASE_IN", "EASE_OUT", "SMOOTH"}) {
                    if (s.startsWith(args[4].toUpperCase())) results.add(s);
                }
                return results;
            }
        }
        
        return Collections.emptyList();
    }
}
