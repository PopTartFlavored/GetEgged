package io.jimbonesjim.getEgged;

import io.jimbonesjim.getEgged.Commands.GeteggedCommands;
import io.jimbonesjim.getEgged.Listeners.PlayerPlaceListener;
import io.jimbonesjim.getEgged.Listeners.PlayerUseSpawnEggListener;
import io.jimbonesjim.getEgged.Listeners.PlayerInteractEntityListener;
import io.jimbonesjim.getEgged.Managers.ConfigManager;
import io.jimbonesjim.getEgged.Managers.DataManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class GetEgged extends JavaPlugin {

    private final ConfigManager configManager = new ConfigManager(this);
    private final DataManager dataManager = new DataManager(configManager);
    @Override
    public void onEnable() {
        saveDefaultConfig();
        configManager.loadConfig();
        DataManager.init(this);
        getCommand("getegged").setExecutor(new GeteggedCommands(dataManager, configManager));
        getServer().getPluginManager().registerEvents( new PlayerInteractEntityListener(dataManager, configManager), this);
        getServer().getPluginManager().registerEvents( new PlayerUseSpawnEggListener(dataManager), this);
        getServer().getPluginManager().registerEvents( new PlayerPlaceListener(dataManager), this);

        getLogger().info("Fully enabled GetEgged v0.8.3");

    }
}