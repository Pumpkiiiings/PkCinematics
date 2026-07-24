package com.pumpkiiiings.pkcinematics.api.trigger;

import com.pumpkiiiings.pkcinematics.api.PkCinematics;
import com.pumpkiiiings.pkcinematics.api.action.ActionContext;
import com.pumpkiiiings.pkcinematics.api.action.PkAction;
import com.pumpkiiiings.pkcinematics.api.action.SimpleActionContext;
import com.pumpkiiiings.pkcinematics.api.condition.Condition;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.io.FilenameFilter;

public class TriggerManager {

    private final Map<String, List<PkTrigger>> triggersByType = new HashMap<>();
    private final File triggersFolder;
    private final Logger logger;

    public TriggerManager(File dataFolder, Logger logger) {
        this.triggersFolder = new File(dataFolder, "triggers");
        this.logger = logger;
        if (!this.triggersFolder.exists()) {
            this.triggersFolder.mkdirs();
        }
    }

    public void loadAll() {
        triggersByType.clear();
        File[] files = triggersFolder.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith(".yml");
            }
        });
        if (files == null) return;

        for (File file : files) {
            YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
            
            // Assume single trigger per file or multiple triggers. Let's support a list if needed,
            // or just read the root level as one trigger.
            // A good practice is either root properties for one trigger, or a list of triggers.
            // We'll support root properties for one trigger per file.
            
            String id = config.getString("id", file.getName().replace(".yml", ""));
            
            ConfigurationSection triggerSec = config.getConfigurationSection("trigger");
            if (triggerSec == null) {
                logger.warning("No 'trigger' section found in " + file.getName());
                continue;
            }
            String type = triggerSec.getString("type");
            if (type == null) {
                logger.warning("No trigger type defined in " + file.getName());
                continue;
            }
            
            boolean runOnce = triggerSec.getBoolean("run-once", false);
            String target = triggerSec.getString("target", "@trigger");
            
            java.util.Map<String, String> properties = new java.util.HashMap<>();
            for (String key : triggerSec.getKeys(false)) {
                if (!key.equals("type") && !key.equals("run-once") && !key.equals("target")) {
                    properties.put(key, triggerSec.getString(key));
                }
            }

            // Conditions
            List<Condition> conditions = new ArrayList<>();
            List<Map<?, ?>> condList = config.getMapList("conditions");
            for (Map<?, ?> rawCond : condList) {
                String cType = (String) rawCond.get("type");
                if (cType != null) {
                    Class<? extends Condition> cClass = PkCinematics.getApi().getConditionRegistry().getConditionClass(cType);
                    if (cClass != null) {
                        try {
                            Condition condition = cClass.getDeclaredConstructor().newInstance();
                            
                            // Safe casting
                            Map<String, Object> stringMap = new HashMap<>();
                            for (Map.Entry<?, ?> entry : rawCond.entrySet()) {
                                if (entry.getKey() instanceof String) {
                                    stringMap.put((String) entry.getKey(), entry.getValue());
                                }
                            }
                            
                            condition.deserialize(stringMap);
                            conditions.add(condition);
                        } catch (Exception e) {
                            logger.severe("Failed to instantiate condition " + cType + " in " + file.getName());
                            e.printStackTrace();
                        }
                    } else {
                        logger.warning("Unknown condition type " + cType + " in " + file.getName());
                    }
                }
            }

            // Actions
            List<PkAction> actions = new ArrayList<>();
            List<Map<?, ?>> actList = config.getMapList("actions");
            for (Map<?, ?> rawAct : actList) {
                String aType = (String) rawAct.get("type");
                if (aType != null) {
                    Class<? extends PkAction> aClass = PkCinematics.getApi().getActionRegistry().getActionClass(aType);
                    if (aClass != null) {
                        try {
                            PkAction action = aClass.getDeclaredConstructor().newInstance();
                            
                            // Safe casting
                            Map<String, Object> stringMap = new HashMap<>();
                            for (Map.Entry<?, ?> entry : rawAct.entrySet()) {
                                if (entry.getKey() instanceof String) {
                                    stringMap.put((String) entry.getKey(), entry.getValue());
                                }
                            }
                            
                            action.deserialize(stringMap);
                            actions.add(action);
                        } catch (Exception e) {
                            logger.severe("Failed to instantiate action " + aType + " in " + file.getName());
                            e.printStackTrace();
                        }
                    } else {
                        logger.warning("Unknown action type " + aType + " in " + file.getName());
                    }
                }
            }

            PkTrigger trigger = new PkTrigger(id, type, conditions, actions, runOnce, target, properties);
            List<PkTrigger> list = triggersByType.get(type.toLowerCase());
            if (list == null) {
                list = new ArrayList<>();
                triggersByType.put(type.toLowerCase(), list);
            }
            list.add(trigger);
            logger.info("Loaded trigger " + id + " of type " + type);
        }
    }

    public void fire(String type, Player player) {
        fire(type, player, player != null ? player.getLocation() : null);
    }
    
    public void fire(String type, org.bukkit.Location location) {
        fire(type, null, location);
    }

    public void fire(String type, Player player, org.bukkit.Location location) {
        List<PkTrigger> triggers = triggersByType.get(type.toLowerCase());
        if (triggers == null) return;

        for (PkTrigger trigger : triggers) {
            fireTrigger(trigger, player, location);
        }
    }
    
    public List<PkTrigger> getTriggers(String type) {
        return triggersByType.get(type.toLowerCase());
    }
    
    public void fireTrigger(PkTrigger trigger, Player triggerPlayer, org.bukkit.Location location) {
        List<Player> targets = resolveTargets(trigger.getTarget(), triggerPlayer, location);
        
        for (Player targetPlayer : targets) {
            if (trigger.isRunOnce() && hasRunOnce(targetPlayer, trigger.getId())) {
                continue;
            }

            boolean conditionsMet = true;
            for (Condition condition : trigger.getConditions()) {
                if (!condition.test(targetPlayer)) {
                    conditionsMet = false;
                    break;
                }
            }

            if (conditionsMet) {
                if (trigger.isRunOnce()) {
                    markRunOnce(targetPlayer, trigger.getId());
                }
                ActionContext context = new SimpleActionContext(targetPlayer, null);
                for (PkAction action : trigger.getActions()) {
                    action.execute(context);
                }
            }
        }
    }
    
    private List<Player> resolveTargets(String targetStr, Player triggerPlayer, org.bukkit.Location location) {
        List<Player> list = new ArrayList<>();
        if (targetStr == null) return list;
        
        targetStr = targetStr.toLowerCase();
        
        if (targetStr.equals("@trigger")) {
            if (triggerPlayer != null) list.add(triggerPlayer);
        } else if (targetStr.equals("@all")) {
            list.addAll(org.bukkit.Bukkit.getOnlinePlayers());
        } else if (targetStr.startsWith("@world:")) {
            String worldName = targetStr.substring(7);
            org.bukkit.World world = org.bukkit.Bukkit.getWorld(worldName);
            if (world != null) {
                list.addAll(world.getPlayers());
            }
        } else if (targetStr.startsWith("@radius:")) {
            try {
                double radius = Double.parseDouble(targetStr.substring(8));
                if (location != null) {
                    for (Player p : location.getWorld().getPlayers()) {
                        if (p.getLocation().distanceSquared(location) <= radius * radius) {
                            list.add(p);
                        }
                    }
                }
            } catch (NumberFormatException ignored) {}
        }
        return list;
    }
    
    private boolean hasRunOnce(Player player, String triggerId) {
        org.bukkit.persistence.PersistentDataContainer pdc = player.getPersistentDataContainer();
        org.bukkit.NamespacedKey key = new org.bukkit.NamespacedKey((org.bukkit.plugin.Plugin) PkCinematics.getApi(), "trigger_" + triggerId.toLowerCase());
        return pdc.has(key, org.bukkit.persistence.PersistentDataType.BYTE);
    }

    private void markRunOnce(Player player, String triggerId) {
        org.bukkit.persistence.PersistentDataContainer pdc = player.getPersistentDataContainer();
        org.bukkit.NamespacedKey key = new org.bukkit.NamespacedKey((org.bukkit.plugin.Plugin) PkCinematics.getApi(), "trigger_" + triggerId.toLowerCase());
        pdc.set(key, org.bukkit.persistence.PersistentDataType.BYTE, (byte) 1);
    }

    public int getLoadedTriggersCount() {
        int count = 0;
        for (List<PkTrigger> list : triggersByType.values()) {
            count += list.size();
        }
        return count;
    }
}
