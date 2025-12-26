package io.jimbonesjim.getEgged;

import io.jimbonesjim.getEgged.Listeners.EntitySpawnListener;
import io.jimbonesjim.getEgged.Listeners.PlayerInteractEntityListener;
import io.jimbonesjim.getEgged.Managers.DataManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class GetEgged extends JavaPlugin {

    private final DataManager dataManager = new DataManager();
    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents( new PlayerInteractEntityListener(dataManager), this);
        getServer().getPluginManager().registerEvents( new EntitySpawnListener(dataManager), this);
        DataManager.init(this);

        getLogger().info("Fully enabled GetEgged v0.7.2");

    }
}