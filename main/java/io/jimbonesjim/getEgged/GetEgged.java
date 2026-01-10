package io.jimbonesjim.getEgged;

import io.jimbonesjim.getEgged.Commands.GeteggedCommands;
import io.jimbonesjim.getEgged.Keys.GetEggedKeys;
import io.jimbonesjim.getEgged.Listeners.PlayerPlaceListener;
import io.jimbonesjim.getEgged.Listeners.PlayerUseSpawnEggListener;
import io.jimbonesjim.getEgged.Listeners.PlayerInteractEntityListener;
import io.jimbonesjim.getEgged.Managers.ConfigManager;
import io.jimbonesjim.getEgged.Managers.DataManager;
import io.jimbonesjim.getEgged.Managers.EggingToolManager;
import io.jimbonesjim.getEgged.Services.*;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public final class GetEgged extends JavaPlugin {

    private static Economy econ = null;
    private final ConfigManager configManager = new ConfigManager(this);
    private final LoreBuilder loreBuilder = new LoreBuilder();
    private final DataManager dataManager = new DataManager(loreBuilder);
    private final GriefPreventionService griefPreventionService = new GriefPreventionService();
    private final WorldguardService worldguardService = new WorldguardService();
    private final EntityCategoryResolver entityCategoryResolver = new EntityCategoryResolver();
    private final EggingRulesService eggingRulesService = new EggingRulesService(configManager, entityCategoryResolver, worldguardService, griefPreventionService);
    private final EggingToolManager eggingToolManager = new EggingToolManager(configManager);
    private final EntityInventoryValidator entityInventoryValidator = new EntityInventoryValidator();
    private final EggFactory eggFactory = new EggFactory(dataManager);
    private VaultEconomyService vaultEconomyService;
    private final JavaPlugin plugin = this;

    @Override
    public void onLoad(){
        worldguardService.registerFlags();
    }
    @Override
    public void onEnable() {
        saveDefaultConfig();
        configManager.loadConfig();
        GetEggedKeys.init(this);
        DataManager.init();
        if (configManager.getEconomyEnabled()){
            if (!setupEconomy()){
                getLogger().severe("Failed to setup Economy");
                vaultEconomyService = new VaultEconomyService(null, configManager, entityCategoryResolver);
            } else {
                getLogger().info("Economy connected");
                vaultEconomyService = new VaultEconomyService(econ, configManager, entityCategoryResolver);
            }
        } else {
            vaultEconomyService = new VaultEconomyService(null, configManager, entityCategoryResolver);
        }
        getCommand("getegged").setExecutor(new GeteggedCommands(eggingToolManager, configManager, vaultEconomyService));
        getServer().getPluginManager().registerEvents( new PlayerInteractEntityListener(this, dataManager,
                eggingRulesService, eggingToolManager, entityInventoryValidator, eggFactory, vaultEconomyService)
                , this);
        getServer().getPluginManager().registerEvents( new PlayerUseSpawnEggListener(dataManager, worldguardService, griefPreventionService), this);
        getServer().getPluginManager().registerEvents( new PlayerPlaceListener(eggingToolManager), this);
        getServer().getPluginManager().registerEvents(new Listener() {
            @EventHandler
            public void onPluginEnable(PluginEnableEvent event) {
                if (event.getPlugin().getName().equalsIgnoreCase("WorldGuard")) {
                    worldguardService.init();
                } else if (event.getPlugin().getName().equalsIgnoreCase("GriefPrevention")) {
                    griefPreventionService.init(plugin);
                }
            }
        }, this);
        griefPreventionService.init(plugin);
        getLogger().info(griefPreventionService.isEnabled() ? "GriefPrevention Hook enabled!" : "GriefPrevention not found! Skipping Hook!");
        worldguardService.init();
        getLogger().info(worldguardService.enabled() ? "WorldGuard Hook enabled!" : "WorldGuard not found! Skipping Hook!");

        getLogger().info("Fully enabled GetEgged v0.9.2");
    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }
}