package com.pumpkiiiings.pkcinematics.integration;

import com.pumpkiiiings.pkcinematics.api.PkCinematics;
import com.pumpkiiiings.pkcinematics.api.trigger.PkTrigger;
import io.lumine.mythic.bukkit.events.MythicMobSpawnEvent;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.List;

public class MythicMobsIntegration implements Listener {

    public static void register() {
        try {
            Class.forName("io.lumine.mythic.bukkit.events.MythicMobSpawnEvent");
            Bukkit.getPluginManager().registerEvents(new MythicMobsIntegration(), (org.bukkit.plugin.Plugin) PkCinematics.getApi());
            ((org.bukkit.plugin.Plugin) PkCinematics.getApi()).getLogger().info("MythicMobs integration enabled.");
        } catch (Exception | NoClassDefFoundError e) {
            ((org.bukkit.plugin.Plugin) PkCinematics.getApi()).getLogger().warning("Failed to register MythicMobs handler.");
        }
    }

    @EventHandler
    public void onMythicMobSpawn(MythicMobSpawnEvent event) {
        String mobId = event.getMobType().getInternalName();

        List<PkTrigger> triggers = PkCinematics.getApi().getTriggerManager().getTriggers("mythicmobs_spawn");
        if (triggers == null) return;

        for (PkTrigger trigger : triggers) {
            String requiredMobId = trigger.getProperty("mob_id");
            if (requiredMobId != null && !requiredMobId.equalsIgnoreCase(mobId)) {
                continue;
            }

            // Fire this specific trigger with the location of the mob spawn
            PkCinematics.getApi().getTriggerManager().fireTrigger(trigger, null, event.getLocation());
        }
    }
}
