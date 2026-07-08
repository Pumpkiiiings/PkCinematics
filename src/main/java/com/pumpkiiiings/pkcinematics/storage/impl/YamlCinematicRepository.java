package com.pumpkiiiings.pkcinematics.storage.impl;

import com.pumpkiiiings.pkcinematics.api.storage.CinematicRepository;
import com.pumpkiiiings.pkcinematics.model.Cinematic;
import com.pumpkiiiings.pkcinematics.model.timeline.CameraKeyframe;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.pumpkiiiings.pkcinematics.api.PkCinematics;
import com.pumpkiiiings.pkcinematics.api.action.PkAction;

public class YamlCinematicRepository implements CinematicRepository {
    
    private final File folder;

    public YamlCinematicRepository(Plugin plugin) {
        this.folder = new File(plugin.getDataFolder(), "cinematics");
        if (!folder.exists()) {
            folder.mkdirs();
        }
    }

    @Override
    public Collection<Cinematic> loadAll() {
        List<Cinematic> cinematics = new ArrayList<>();
        File[] files = folder.listFiles(new java.io.FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith(".yml");
            }
        });
        if (files != null) {
            for (File file : files) {
                String id = file.getName().replace(".yml", "");
                Cinematic cin = load(id);
                if (cin != null) {
                    cinematics.add(cin);
                }
            }
        }
        return cinematics;
    }

    @Override
    public Cinematic load(String id) {
        File file = new File(folder, id + ".yml");
        if (!file.exists()) return null;

        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
        Cinematic cinematic = new Cinematic(id);
        
        // Load duration
        cinematic.getTimeline().setDurationTicks(config.getInt("duration", 0));
        
        // Load Camera Track
        if (config.contains("camera")) {
            for (String key : config.getConfigurationSection("camera").getKeys(false)) {
                int tick = Integer.parseInt(key);
                String path = "camera." + key + ".";
                CameraKeyframe kf = new CameraKeyframe(
                        tick,
                        config.getString(path + "world", "world"),
                        config.getDouble(path + "x"),
                        config.getDouble(path + "y"),
                        config.getDouble(path + "z"),
                        (float) config.getDouble(path + "yaw"),
                        (float) config.getDouble(path + "pitch"),
                        (float) config.getDouble(path + "fov", Double.parseDouble("70.0")),
                        config.getString(path + "interpolation", "LINEAR")
                );
                cinematic.getTimeline().getCameraTrack().addKeyframe(kf);
            }
        }
        
        // Load Actions
        if (config.contains("actions")) {
            for (String key : config.getConfigurationSection("actions").getKeys(false)) {
                int tick = Integer.parseInt(key);
                List<Map<?, ?>> actList = config.getMapList("actions." + key);
                for (Map<?, ?> rawAct : actList) {
                    String aType = (String) rawAct.get("type");
                    if (aType != null) {
                        Class<? extends PkAction> aClass = PkCinematics.getApi().getActionRegistry().getActionClass(aType);
                        if (aClass != null) {
                            try {
                                PkAction action = aClass.getDeclaredConstructor().newInstance();
                                Map<String, Object> stringMap = new HashMap<>();
                                for (Map.Entry<?, ?> entry : rawAct.entrySet()) {
                                    if (entry.getKey() instanceof String) {
                                        stringMap.put((String) entry.getKey(), entry.getValue());
                                    }
                                }
                                action.deserialize(stringMap);
                                cinematic.getTimeline().getActionTrack().addAction(tick, action);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        }
        
        // Auto-calculate duration if it was missing or too short
        int loadedDuration = cinematic.getTimeline().getDurationTicks();
        cinematic.getTimeline().calculateDuration();
        int calculatedMax = cinematic.getTimeline().getDurationTicks();
        
        // Keep the largest (so users can still manually make it longer if they want)
        cinematic.getTimeline().setDurationTicks(Math.max(loadedDuration, calculatedMax));
        
        return cinematic;
    }

    @Override
    public void save(Cinematic cinematic) {
        File file = new File(folder, cinematic.getId() + ".yml");
        YamlConfiguration config = new YamlConfiguration();
        
        cinematic.getTimeline().calculateDuration();
        config.set("duration", cinematic.getTimeline().getDurationTicks());
        
        // Save Camera Track
        for (CameraKeyframe kf : cinematic.getTimeline().getCameraTrack().getKeyframes()) {
            String path = "camera." + kf.getTick() + ".";
            config.set(path + "world", kf.getWorldName());
            config.set(path + "x", kf.getX());
            config.set(path + "y", kf.getY());
            config.set(path + "z", kf.getZ());
            config.set(path + "yaw", kf.getYaw());
            config.set(path + "pitch", kf.getPitch());
            config.set(path + "fov", kf.getFov());
            config.set(path + "interpolation", kf.getInterpolationType());
        }
        
        // Save Actions
        for (Integer tick : cinematic.getTimeline().getActionTrack().getAllActions().keySet()) {
            List<PkAction> actionsAtTick = cinematic.getTimeline().getActionTrack().getActionsAt(tick);
            List<Map<String, Object>> serializedActions = new ArrayList<>();
            for (PkAction act : actionsAtTick) {
                serializedActions.add(act.serialize());
            }
            if (!serializedActions.isEmpty()) {
                config.set("actions." + tick, serializedActions);
            }
        }
        
        try {
            config.save(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(String id) {
        File file = new File(folder, id + ".yml");
        if (file.exists()) {
            file.delete();
        }
    }
}
