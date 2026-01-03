package io.jimbonesjim.getEgged.Managers;

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class ConfigManager {

    private final JavaPlugin PLUGIN;
    private FileConfiguration CONFIG;
    private Material item_mat;
    private String item_name;
    private String item_lore;
    private boolean item_glow;
    private boolean captureTamed;

    public ConfigManager(JavaPlugin plugin) {
        PLUGIN = plugin;
        CONFIG = plugin.getConfig();
    }

    public void reloadConfig(){
        PLUGIN.reloadConfig();
        CONFIG = PLUGIN.getConfig();
        loadConfig();
    }

    public void loadConfig() {
        //Gets material from config or sets as default
        Material configMat = Material.getMaterial(CONFIG.getString("capture-item.material").toUpperCase());
        item_mat =  configMat != null ? configMat :
                Material.SLIME_BALL; // default value

        //Gets name string from config or sets as default
        item_name =  CONFIG.getString(
                "capture-item.name",
                "Mob Capture Tool" // default value
        );

        //gets lore string from config or sets as default
        item_lore = CONFIG.getString(
                "capture-item.lore",
                "Right click me on a mob to capture it!" // default value
        );

        //gets enchant glow from config or sets as default
        item_glow = CONFIG.getBoolean(
                "capture-item.enchant-glow",
                true // default value
        );

        //gets if allowed to capture other's tamed mobs from config or sets as default
        captureTamed = CONFIG.getBoolean(
                "capture.allow-tamed-capture",
                 false // default value
        );
    }

    public Material  getItemMaterial() {
        return item_mat;
    }
    public String getItemName() {
        return item_name;
    }

    public String getItemLore() {
        return item_lore;
    }

    public boolean getItemGlow() {
        return item_glow;
    }

    public boolean getCaptureTamed() {
        return captureTamed;
    }
}