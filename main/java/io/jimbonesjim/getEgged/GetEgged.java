package io.jimbonesjim.getEgged;

import io.jimbonesjim.getEgged.Commands.GeteggedCommands;
import io.jimbonesjim.getEgged.Keys.GetEggedKeys;
import io.jimbonesjim.getEgged.Listeners.PlayerPlaceListener;
import io.jimbonesjim.getEgged.Listeners.PlayerUseSpawnEggListener;
import io.jimbonesjim.getEgged.Listeners.PlayerInteractEntityListener;
import io.jimbonesjim.getEgged.Managers.ConfigManager;
import io.jimbonesjim.getEgged.Managers.DataManager;
import io.jimbonesjim.getEgged.Managers.EggingToolManager;
import io.jimbonesjim.getEgged.Managers.MessageManager;
import io.jimbonesjim.getEgged.Services.*;
import net.milkbowl.vault.economy.Economy;
import org.bstats.bukkit.Metrics;
import org.bstats.charts.AdvancedBarChart;
import org.bstats.charts.MultiLineChart;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public final class GetEgged extends JavaPlugin {

    private final JavaPlugin plugin = this;
    private static Economy econ = null;
    private File messagesFile = new File(getDataFolder(), "messages.yml");
    private FileConfiguration messagesConfig = YamlConfiguration.loadConfiguration(messagesFile);
    private MessageManager messageManager = new MessageManager(messagesConfig);
    private final ConfigManager configManager = new ConfigManager(plugin);
    private final LoreBuilder loreBuilder = new LoreBuilder();
    private final DataManager dataManager = new DataManager(loreBuilder);
    private final GriefPreventionService griefPreventionService = new GriefPreventionService();
    private final WorldguardService worldguardService = new WorldguardService(messageManager);
    private final EntityCategoryResolver entityCategoryResolver = new EntityCategoryResolver();
    private final EggingRulesService eggingRulesService = new EggingRulesService(configManager, messageManager, entityCategoryResolver, worldguardService, griefPreventionService);
    private final EggingToolManager eggingToolManager = new EggingToolManager(configManager);
    private final EntityInventoryValidator entityInventoryValidator = new EntityInventoryValidator(messageManager);
    private final EggFactory eggFactory = new EggFactory(dataManager);
    private VaultEconomyService vaultEconomyService;

    @Override
    public void onLoad(){
        worldguardService.registerFlags();
    }
    @Override
    public void onEnable() {
        saveDefaultConfig();
        configManager.loadConfig();
        saveResource("messages.yml", false);
        messageManager = new MessageManager(messagesConfig);
        reloadMessages();
        GetEggedKeys.init(plugin);
        DataManager.init();
        int pluginId = 28848;
        Metrics metrics = new Metrics(this, pluginId);
        metrics.addCustomChart(new MultiLineChart("mobs_egged",
                dataManager::getEggCountsSnapshot));
        if (configManager.getEconomyEnabled()){
            if (!setupEconomy()){
                getLogger().severe("Failed to setup Economy");
                vaultEconomyService = new VaultEconomyService(null, configManager, messageManager, entityCategoryResolver);
            } else {
                getLogger().info("Economy connected");
                vaultEconomyService = new VaultEconomyService(econ, configManager, messageManager, entityCategoryResolver);
            }
        } else {
            vaultEconomyService = new VaultEconomyService(null, configManager, messageManager, entityCategoryResolver);
        }
        getCommand("getegged").setExecutor(new GeteggedCommands(this, eggingToolManager, configManager, vaultEconomyService));
        getServer().getPluginManager().registerEvents( new PlayerInteractEntityListener(plugin, dataManager,
                eggingRulesService, eggingToolManager, entityInventoryValidator, eggFactory, vaultEconomyService)
                , this);
        getServer().getPluginManager().registerEvents( new PlayerUseSpawnEggListener(dataManager, worldguardService, griefPreventionService, configManager, messageManager), this);
        getServer().getPluginManager().registerEvents( new PlayerPlaceListener(eggingToolManager), plugin);
        getServer().getPluginManager().registerEvents(new Listener() {
            @EventHandler
            public void onPluginEnable(PluginEnableEvent event) {
                if (event.getPlugin().getName().equalsIgnoreCase("WorldGuard")) {
                    worldguardService.init();
                } else if (event.getPlugin().getName().equalsIgnoreCase("GriefPrevention")) {
                    griefPreventionService.init();
                }
            }
        }, this);
        griefPreventionService.init();
        getLogger().info(griefPreventionService.isEnabled() ? "GriefPrevention Hook enabled!" : "GriefPrevention not found! Skipping Hook!");
        worldguardService.init();
        getLogger().info(worldguardService.enabled() ? "WorldGuard Hook enabled!" : "WorldGuard not found! Skipping Hook!");

        getLogger().info("Fully enabled GetEgged v0.9.3");

        metrics.addCustomChart(new AdvancedBarChart("soft_deps", () -> {
            Map<String, int[]> map = new HashMap<>();
            map.put("Vault",              new int[]{ vaultEconomyService.isEnabled()     ? 1 : 0,
                    vaultEconomyService.isEnabled()     ? 0 : 1 });
            map.put("WorldGuard",         new int[]{ worldguardService.enabled()         ? 1 : 0,
                    worldguardService.enabled()         ? 0 : 1 });
            map.put("GriefPrevention",    new int[]{ griefPreventionService.isEnabled()  ? 1 : 0,
                    griefPreventionService.isEnabled()  ? 0 : 1 });
            return map;
        }));
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

    public void reloadMessages() {
        if (messagesFile == null) {
            messagesFile = new File(getDataFolder(), "messages.yml");
        }

        messagesConfig = YamlConfiguration.loadConfiguration(messagesFile);

        if (messageManager == null) {
            messageManager = new MessageManager(messagesConfig);
        } else {
            messageManager.reload(messagesConfig);
        }
    }

}