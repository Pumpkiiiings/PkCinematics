package com.pumpkiiiings.pkcinematics.command;

import com.pumpkiiiings.pkcinematics.api.PkCinematics;
import com.pumpkiiiings.pkcinematics.editor.EditorManager;
import com.pumpkiiiings.pkcinematics.editor.EditorSession;
import com.pumpkiiiings.pkcinematics.editor.gui.menu.MainEditorGui;
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
import com.pumpkiiiings.pkcinematics.config.Messages;
import com.pumpkiiiings.pkcinematics.core.PlaybackManagerImpl;
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
        
        if (!sender.hasPermission("pkcinematics.admin")) {
            sender.sendMessage(Messages.NO_PERMISSION.getWithPrefix());
            return;
        }

        if (args.length == 0) {
            sender.sendMessage(Messages.HELP_HEADER.get());
            sender.sendMessage(Messages.HELP_CREATE.get());
            sender.sendMessage(Messages.HELP_EDIT.get());
            sender.sendMessage(Messages.HELP_POINT.get());
            sender.sendMessage(Messages.HELP_POINT_EDIT.get());
            sender.sendMessage(Messages.HELP_ACTIONS.get());
            sender.sendMessage(Messages.HELP_SAVE.get());
            sender.sendMessage(Messages.HELP_PLAY.get());
            sender.sendMessage(Messages.HELP_STOP.get());
            sender.sendMessage(Messages.HELP_RELOAD.get());
            return;
        }

        String sub = args[0].toLowerCase();
        
        // Commands allowed for console
        if (sub.equals("play")) {
            if (args.length < 2) {
                sender.sendMessage(Messages.HELP_PLAY.getWithPrefix());
                return;
            }
            String id = args[1];
            Cinematic cin = PkCinematics.getApi().getCinematicManager().getCinematic(id);
            if (cin == null) {
                sender.sendMessage(Messages.NOT_FOUND.getWithPrefix());
                return;
            }

            if (args.length >= 3) {
                String targetName = args[2];
                if (targetName.equalsIgnoreCase("all") || targetName.equalsIgnoreCase("todos") || targetName.equals("*")) {
                    List<Player> onlinePlayers = new ArrayList<>(Bukkit.getOnlinePlayers());
                    PkCinematics.getApi().getPlaybackManager().play(onlinePlayers, cin);
                    sender.sendMessage(Messages.PREFIX.get() + "§aPlaying cinematic " + id + " for all players.");
                } else {
                    Player target = Bukkit.getPlayer(targetName);
                    if (target == null) {
                        sender.sendMessage(Messages.PREFIX.get() + "§cPlayer not found: " + targetName);
                        return;
                    }
                    PkCinematics.getApi().getPlaybackManager().play(target, cin);
                    sender.sendMessage(Messages.PREFIX.get() + "§aPlaying cinematic " + id + " for " + target.getName() + ".");
                }
            } else {
                if (!(sender instanceof Player)) {
                    sender.sendMessage(Messages.PREFIX.get() + "§cYou must specify a player from the console. Example: /pkcinematics play <id> <player>");
                    return;
                }
                Player player = (Player) sender;
                PkCinematics.getApi().getPlaybackManager().play(player, cin);
                sender.sendMessage(Messages.PLAYBACK_PLAYING.getWithPrefix());
            }
            return;
        }

        if (sub.equals("stop")) {
            if (args.length >= 2) {
                String targetName = args[1];
                if (targetName.equalsIgnoreCase("all") || targetName.equalsIgnoreCase("todos") || targetName.equals("*")) {
                    for (Player target : Bukkit.getOnlinePlayers()) {
                        PkCinematics.getApi().getPlaybackManager().stop(target);
                    }
                    sender.sendMessage(Messages.PREFIX.get() + "§aCinematics stopped for all players.");
                } else {
                    Player target = Bukkit.getPlayer(targetName);
                    if (target == null) {
                        sender.sendMessage(Messages.PREFIX.get() + "§cPlayer not found: " + targetName);
                        return;
                    }
                    PkCinematics.getApi().getPlaybackManager().stop(target);
                    sender.sendMessage(Messages.PREFIX.get() + "§aCinematic stopped for " + target.getName() + ".");
                }
            } else {
                if (!(sender instanceof Player)) {
                    sender.sendMessage(Messages.PREFIX.get() + "§cYou must specify a player from the console. Example: /pkcinematics stop <player>");
                    return;
                }
                Player player = (Player) sender;
                PkCinematics.getApi().getPlaybackManager().stop(player);
                sender.sendMessage(Messages.PLAYBACK_STOPPED.getWithPrefix());
            }
            return;
        }
        
        if (sub.equals("reload")) {
            if (args.length < 2) {
                sender.sendMessage(Messages.HELP_RELOAD.getWithPrefix());
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
                sender.sendMessage(Messages.RELOADED.getWithPrefix("type", "Cinematics"));
            }
            if (target.equals("triggers") || target.equals("all")) {
                PkCinematics.getApi().getTriggerManager().loadAll();
                sender.sendMessage(Messages.RELOADED.getWithPrefix("type", "Triggers"));
            }
            if (target.equals("messages") || target.equals("all")) {
                PkCinematics.getApi().getMessageManager().reload();
                sender.sendMessage(Messages.RELOADED.getWithPrefix("type", "Messages"));
            }
            return;
        }

        // --- Commands requiring a Player ---
        if (!(sender instanceof Player)) {
            sender.sendMessage(Messages.PREFIX.get() + "§cThis command can only be executed by a player.");
            return;
        }
        
        Player player = (Player) sender;

        if (sub.equals("create")) {
            if (args.length < 2) {
                player.sendMessage(Messages.HELP_CREATE.getWithPrefix());
                return;
            }
            String id = args[1];
            if (PkCinematics.getApi().getCinematicManager().getCinematic(id) != null) {
                player.sendMessage(Messages.ALREADY_EXISTS.getWithPrefix());
                return;
            }
            Cinematic cin = new Cinematic(id);
            PkCinematics.getApi().getCinematicManager().registerCinematic(cin);
            editorManager.startEditing(player, cin);
            player.playSound(player.getLocation(), org.bukkit.Sound.BLOCK_NOTE_BLOCK_PLING, 1.0f, 1.0f);
            MainEditorGui.open(player, editorManager.getSession(player));
            return;
        }

        if (sub.equals("edit") || sub.equals("menu")) {
            if (args.length < 2) {
                com.pumpkiiiings.pkcinematics.editor.gui.menu.CinematicListGui.open(player);
                return;
            }
            String id = args[1];
            Cinematic cin = PkCinematics.getApi().getCinematicManager().getCinematic(id);
            if (cin == null) {
                player.sendMessage(Messages.NOT_FOUND.getWithPrefix());
                return;
            }
            editorManager.startEditing(player, cin);
            MainEditorGui.open(player, editorManager.getSession(player));
            return;
        }

        if (sub.equals("point")) {
            if (args.length > 1 && args[1].equalsIgnoreCase("edit")) {
                EditorSession session = editorManager.getSession(player);
                if (session == null) {
                    player.sendMessage(Messages.EDITOR_NOT_EDITING.getWithPrefix());
                    return;
                }
                if (args.length < 5) {
                    player.sendMessage(Messages.HELP_POINT_EDIT.getWithPrefix());
                    return;
                }
                int index;
                try { index = Integer.parseInt(args[2]); } catch(Exception e) { return; }
                
                CameraTrack track = session.getCinematic().getTimeline().getCameraTrack();
                if (index < 0 || index >= track.getKeyframes().size()) {
                    player.sendMessage(Messages.EDITOR_INVALID_INDEX.getWithPrefix());
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
                    player.sendMessage(Messages.EDITOR_TICK_UPDATED.getWithPrefix("value", val));
                } else if (prop.equals("fov")) {
                    kf.setFov(Float.parseFloat(val));
                    player.sendMessage(Messages.EDITOR_FOV_UPDATED.getWithPrefix("value", val));
                } else if (prop.equals("interp")) {
                    kf.setInterpolationType(val.toUpperCase());
                    player.sendMessage(Messages.EDITOR_INTERP_UPDATED.getWithPrefix("value", val));
                } else if (prop.equals("easing")) {
                    kf.setEasingType(val.toUpperCase());
                    player.sendMessage(Messages.EDITOR_INTERP_UPDATED.getWithPrefix("value", val));
                } else {
                    player.sendMessage(Messages.EDITOR_INVALID_PROPERTY.getWithPrefix());
                }
                return;
            }

            EditorSession session = editorManager.getSession(player);
            if (session == null) {
                player.sendMessage(Messages.EDITOR_NOT_EDITING.getWithPrefix());
                return;
            }
            
            CameraTrack track = session.getCinematic().getTimeline().getCameraTrack();
            int currentMaxTick = 0;
            if (!track.getKeyframes().isEmpty()) {
                currentMaxTick = track.getKeyframes().get(track.getKeyframes().size() - 1).getTick();
            }
            
            int newTick = track.getKeyframes().isEmpty() ? 0 : currentMaxTick + 60;
            
            Location loc = player.getEyeLocation();
            
            double cameraY = loc.getY() - Double.parseDouble("1.62");
            
            CameraKeyframe kf = new CameraKeyframe(
                newTick, 
                loc.getWorld().getName(),
                loc.getX(), cameraY, loc.getZ(),
                loc.getYaw(), loc.getPitch(),
                Float.parseFloat("70.0"),
                "LINEAR",
                "LINEAR"
            );
            
            track.addKeyframe(kf);
            session.getCinematic().getTimeline().calculateDuration();
            
            int index = track.getKeyframes().indexOf(kf);
            player.playSound(player.getLocation(), org.bukkit.Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.2f);
            player.sendMessage(Messages.EDITOR_POINT_ADDED.getWithPrefix("index", String.valueOf(index), "tick", String.valueOf(newTick)));
            return;
        }
        
        if (sub.equals("save")) {
            EditorSession session = editorManager.getSession(player);
            if (session == null) {
                player.sendMessage(Messages.EDITOR_NOT_EDITING.getWithPrefix());
                return;
            }
            repository.save(session.getCinematic());
            player.playSound(player.getLocation(), org.bukkit.Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f);
            player.sendMessage(Messages.SAVED.getWithPrefix("name", session.getCinematic().getId()));
            editorManager.stopEditing(player);
            return;
        }
        
        if (sub.equals("actions")) {
            EditorSession session = editorManager.getSession(player);
            if (session == null) {
                player.sendMessage(Messages.EDITOR_NOT_EDITING.getWithPrefix());
                return;
            }
            MainEditorGui.open(player, session);
            return;
        }
        
        if (sub.equals("debug")) {
            PlaybackManagerImpl pbManager = (PlaybackManagerImpl) PkCinematics.getApi().getPlaybackManager();
            pbManager.toggleDebug(player);
            if (pbManager.isDebugEnabled(player)) {
                player.sendMessage(Messages.PREFIX.get() + "§aCinematics debug mode ENABLED.");
            } else {
                player.sendMessage(Messages.PREFIX.get() + "§cCinematics debug mode DISABLED.");
            }
            return;
        }

    }

    @Override
    public Collection<String> suggest(CommandSourceStack stack, String[] args) {
        CommandSender sender = stack.getSender();
        if (!sender.hasPermission("pkcinematics.admin")) return Collections.emptyList();
        
        List<String> results = new ArrayList<>();
        
        if (args.length == 1) {
            for (String s : new String[]{"menu", "create", "edit", "point", "actions", "save", "play", "stop", "reload", "debug"}) {
                if (s.toLowerCase().startsWith(args[0].toLowerCase())) results.add(s);
            }
            return results;
        }
        
        if (args.length == 2) {
            String sub = args[0].toLowerCase();
            if (sub.equals("reload")) {
                for (String s : new String[]{"cinematics", "triggers", "messages", "all"}) {
                    if (s.toLowerCase().startsWith(args[1].toLowerCase())) results.add(s);
                }
            } else if (sub.equals("play") || sub.equals("edit")) {
                for (Cinematic cin : PkCinematics.getApi().getCinematicManager().getAllCinematics()) {
                    if (cin.getId().toLowerCase().startsWith(args[1].toLowerCase())) results.add(cin.getId());
                }
            } else if (sub.equals("point")) {
                if ("edit".startsWith(args[1].toLowerCase())) results.add("edit");
            } else if (sub.equals("stop")) {
                if ("todos".startsWith(args[1].toLowerCase())) results.add("todos");
                if ("all".startsWith(args[1].toLowerCase())) results.add("all");
                for (Player p : Bukkit.getOnlinePlayers()) {
                    if (p.getName().toLowerCase().startsWith(args[1].toLowerCase())) results.add(p.getName());
                }
            }
            return results;
        }
        
        if (args.length == 3) {
            String sub = args[0].toLowerCase();
            if (sub.equals("play")) {
                if ("todos".startsWith(args[2].toLowerCase())) results.add("todos");
                if ("all".startsWith(args[2].toLowerCase())) results.add("all");
                for (Player p : Bukkit.getOnlinePlayers()) {
                    if (p.getName().toLowerCase().startsWith(args[2].toLowerCase())) results.add(p.getName());
                }
            } else if (sub.equals("point") && args[1].equalsIgnoreCase("edit")) {
                if (sender instanceof Player) {
                    EditorSession session = editorManager.getSession((Player) sender);
                    if (session != null) {
                        int size = session.getCinematic().getTimeline().getCameraTrack().getKeyframes().size();
                        for (int i = 0; i < size; i++) {
                            if (String.valueOf(i).startsWith(args[2])) results.add(String.valueOf(i));
                        }
                    }
                }
            }
            return results;
        }
        
        if (args.length == 4) {
            if (args[0].equalsIgnoreCase("point") && args[1].equalsIgnoreCase("edit")) {
                for (String s : new String[]{"time", "tick", "fov", "interp", "easing"}) {
                    if (s.toLowerCase().startsWith(args[3].toLowerCase())) results.add(s);
                }
            }
            return results;
        }
        
        if (args.length == 5) {
            if (args[0].equalsIgnoreCase("point") && args[1].equalsIgnoreCase("edit")) {
                String prop = args[3].toLowerCase();
                if (prop.equals("interp")) {
                    for (String s : new String[]{"LINEAR", "CATMULL_ROM"}) {
                        if (s.toLowerCase().startsWith(args[4].toLowerCase())) results.add(s);
                    }
                } else if (prop.equals("easing")) {
                    for (String s : new String[]{"LINEAR", "EASE_IN", "EASE_OUT", "SMOOTH"}) {
                        if (s.toLowerCase().startsWith(args[4].toLowerCase())) results.add(s);
                    }
                }
            }
            return results;
        }
        
        return results;
    }
}
