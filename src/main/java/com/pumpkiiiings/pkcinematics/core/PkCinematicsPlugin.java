package com.pumpkiiiings.pkcinematics.core;

import com.github.retrooper.packetevents.PacketEvents;
import io.github.retrooper.packetevents.factory.spigot.SpigotPacketEventsBuilder;
import com.pumpkiiiings.pkcinematics.api.PkCinematics;
import com.pumpkiiiings.pkcinematics.api.PkCinematicsProvider;
import com.pumpkiiiings.pkcinematics.api.CinematicManager;
import com.pumpkiiiings.pkcinematics.api.PlaybackManager;
import com.pumpkiiiings.pkcinematics.api.action.ActionRegistry;
import com.pumpkiiiings.pkcinematics.api.condition.ConditionRegistry;
import com.pumpkiiiings.pkcinematics.api.trigger.TriggerManager;
import com.pumpkiiiings.pkcinematics.api.trigger.TriggerRegistry;
import com.pumpkiiiings.pkcinematics.api.camera.CameraController;
import com.pumpkiiiings.pkcinematics.api.state.PlayerStateRestorer;
import com.pumpkiiiings.pkcinematics.command.CinematicCommand;
import com.pumpkiiiings.pkcinematics.config.MessageManager;
import com.pumpkiiiings.pkcinematics.editor.EditorManager;
import com.pumpkiiiings.pkcinematics.editor.gui.ActionEditorGUI;
import com.pumpkiiiings.pkcinematics.engine.camera.CameraPacketListener;
import com.pumpkiiiings.pkcinematics.engine.camera.PacketCameraController;
import com.pumpkiiiings.pkcinematics.core.PlaybackManagerImpl;
import com.pumpkiiiings.pkcinematics.core.CinematicManagerImpl;
import com.pumpkiiiings.pkcinematics.engine.scheduler.CinematicScheduler;
import com.pumpkiiiings.pkcinematics.engine.state.DefaultPlayerStateRestorer;
import com.pumpkiiiings.pkcinematics.storage.impl.YamlCinematicRepository;
import com.pumpkiiiings.pkcinematics.action.impl.TitleAction;
import com.pumpkiiiings.pkcinematics.action.impl.MessageAction;
import com.pumpkiiiings.pkcinematics.action.impl.CommandAction;
import com.pumpkiiiings.pkcinematics.action.impl.PlayCinematicAction;
import com.pumpkiiiings.pkcinematics.action.impl.ActionBarAction;
import com.pumpkiiiings.pkcinematics.action.impl.SoundAction;
import com.pumpkiiiings.pkcinematics.action.impl.ParticleAction;
import com.pumpkiiiings.pkcinematics.action.impl.PotionEffectAction;
import com.pumpkiiiings.pkcinematics.action.impl.TimeAction;
import com.pumpkiiiings.pkcinematics.action.impl.WeatherAction;
import com.pumpkiiiings.pkcinematics.action.impl.ResetEnvironmentAction;
import com.pumpkiiiings.pkcinematics.condition.impl.PermissionCondition;
import com.pumpkiiiings.pkcinematics.condition.impl.WorldCondition;
import com.pumpkiiiings.pkcinematics.condition.impl.GamemodeCondition;
import com.pumpkiiiings.pkcinematics.condition.impl.PlayedBeforeCondition;
import com.pumpkiiiings.pkcinematics.listener.TriggerListener;
import org.bukkit.plugin.java.JavaPlugin;

public class PkCinematicsPlugin extends JavaPlugin implements PkCinematics {
    
    private ActionRegistry actionRegistry;
    private ConditionRegistry conditionRegistry;
    private TriggerRegistry triggerRegistry;
    private TriggerManager triggerManager;
    private CameraController cameraController;
    private CinematicScheduler cinematicScheduler;
    private CinematicManager cinematicManager;
    private PlaybackManager playbackManager;
    private PlayerStateRestorer stateRestorer;
    private YamlCinematicRepository repository;
    private EditorManager editorManager;
    private MessageManager messageManager;

    @Override
    public void onLoad() {
        // Initialize PacketEvents
        PacketEvents.setAPI(SpigotPacketEventsBuilder.build(this));
        PacketEvents.getAPI().getSettings()
            .bStats(true)
            .checkForUpdates(false);
        PacketEvents.getAPI().load();
    }

    @Override
    public void onEnable() {
        PacketEvents.getAPI().init();
        
        // Register API provider
        PkCinematicsProvider.register(this);
        
        // Register packet listeners
        PacketEvents.getAPI().getEventManager().registerListener(new CameraPacketListener());
        
        // Initialize Core Components
        this.messageManager = new MessageManager(this);
        this.actionRegistry = new ActionRegistry();
        this.conditionRegistry = new ConditionRegistry();
        this.triggerRegistry = new TriggerRegistry();
        this.triggerManager = new TriggerManager(getDataFolder(), getLogger());
        this.cameraController = new PacketCameraController();
        this.cinematicScheduler = new CinematicScheduler(this.cameraController);
        this.repository = new YamlCinematicRepository(this);
        this.cinematicManager = new CinematicManagerImpl();
        this.stateRestorer = new DefaultPlayerStateRestorer(this);
        this.playbackManager = new PlaybackManagerImpl(this.cinematicScheduler, this.stateRestorer);
        this.editorManager = new EditorManager();
        
        // Load cinematics from disk
        this.repository.loadAll().forEach(this.cinematicManager::registerCinematic);
        
        // Register default actions
        this.actionRegistry.registerAction("title", TitleAction.class);
        this.actionRegistry.registerAction("message", MessageAction.class);
        this.actionRegistry.registerAction("command", CommandAction.class);
        this.actionRegistry.registerAction("cinematic", PlayCinematicAction.class);
        this.actionRegistry.registerAction("actionbar", ActionBarAction.class);
        this.actionRegistry.registerAction("sound", SoundAction.class);
        this.actionRegistry.registerAction("particle", ParticleAction.class);
        this.actionRegistry.registerAction("potion_effect", PotionEffectAction.class);
        this.actionRegistry.registerAction("time", TimeAction.class);
        this.actionRegistry.registerAction("weather", WeatherAction.class);
        this.actionRegistry.registerAction("reset_environment", ResetEnvironmentAction.class);
        
        // Register default conditions
        this.conditionRegistry.registerCondition("permission", PermissionCondition.class);
        this.conditionRegistry.registerCondition("world", WorldCondition.class);
        this.conditionRegistry.registerCondition("gamemode", GamemodeCondition.class);
        this.conditionRegistry.registerCondition("played_before", PlayedBeforeCondition.class);
        
        // Register default trigger types
        this.triggerRegistry.registerTriggerType("first_join");
        this.triggerRegistry.registerTriggerType("join");
        this.triggerRegistry.registerTriggerType("quit");
        this.triggerRegistry.registerTriggerType("respawn");
        this.triggerRegistry.registerTriggerType("death");
        this.triggerRegistry.registerTriggerType("world_change");
        this.triggerRegistry.registerTriggerType("resource_pack_loaded");
        this.triggerRegistry.registerTriggerType("resource_pack_declined");
        
        // Load triggers
        this.triggerManager.loadAll();
        
        // Start Scheduler
        this.cinematicScheduler.runTaskTimer(this, 1L, 1L);
        
        // Register commands
        CinematicCommand commandExecutor = new CinematicCommand(this.editorManager, this.repository);
        getCommand("cinematic").setExecutor(commandExecutor);
        getCommand("cinematic").setTabCompleter(commandExecutor);
        
        // Register events
        getServer().getPluginManager().registerEvents(new ActionEditorGUI(), this);
        getServer().getPluginManager().registerEvents(new TriggerListener(), this);
        
        getLogger().info("PkCinematics (v2 Timeline Architecture) enabled!");
    }

    @Override
    public void onDisable() {
        PacketEvents.getAPI().terminate();
        PkCinematicsProvider.unregister();
        // Restore all players in sessions
        this.cinematicScheduler.getActiveSessions().forEach(session -> {
            this.playbackManager.stop(session.getPlayer());
        });
    }

    @Override
    public CinematicManager getCinematicManager() {
        return cinematicManager;
    }

    @Override
    public PlaybackManager getPlaybackManager() {
        return playbackManager;
    }

    @Override
    public ActionRegistry getActionRegistry() {
        return actionRegistry;
    }

    @Override
    public ConditionRegistry getConditionRegistry() {
        return conditionRegistry;
    }

    @Override
    public TriggerRegistry getTriggerRegistry() {
        return triggerRegistry;
    }

    @Override
    public TriggerManager getTriggerManager() {
        return triggerManager;
    }
    
    @Override
    public EditorManager getEditorManager() {
        return editorManager;
    }
    
    @Override
    public MessageManager getMessageManager() {
        return messageManager;
    }
}
