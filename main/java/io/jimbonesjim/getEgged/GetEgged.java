package io.jimbonesjim.getEgged;

import io.jimbonesjim.getEgged.Commands.GiveToolCommand;
import io.jimbonesjim.getEgged.Listeners.PlayerUseSpawnEggListener;
import io.jimbonesjim.getEgged.Listeners.PlayerInteractEntityListener;
import io.jimbonesjim.getEgged.Managers.DataManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class GetEgged extends JavaPlugin {

    private final DataManager dataManager = new DataManager();
    @Override
    public void onEnable() {
        saveDefaultConfig();
        DataManager.init(this);
        getCommand("getegged").setExecutor(new GiveToolCommand(this));
        getServer().getPluginManager().registerEvents( new PlayerInteractEntityListener(dataManager), this);
        getServer().getPluginManager().registerEvents( new PlayerUseSpawnEggListener(dataManager), this);

        getLogger().info("Fully enabled GetEgged v0.8");

    }
}