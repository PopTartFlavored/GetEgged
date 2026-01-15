package io.jimbonesjim.getEgged.Managers;

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public class ConfigManager {

    public enum UsageMode {
        NONE,
        DURABILITY,
        CONSUME
    }
    private final JavaPlugin PLUGIN;
    private FileConfiguration CONFIG;
    private Material toolMaterial;
    private String toolName;
    private String toolLore;
    private boolean toolGlow;
    private UsageMode usageMode;
    private int maxUses;
    private double toolPrice;
    private double defaultPrice;
    private double animalsPrice;
    private double monstersPrice;
    private double golemsPrice;
    private double villagerPrice;
    private double bossPrice;
    private boolean eggTamed;
    private boolean economyEnabled;
    private boolean renaming;
    private boolean renaming_permNeeded;
    private List<String> blacklist;

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
        Material configMat = Material.getMaterial(CONFIG.getString("egging-item.material").toUpperCase());
        toolMaterial =  configMat != null ? configMat :
                Material.SLIME_BALL; // default value

        //Gets name string from config or sets as default
        toolName =  CONFIG.getString(
                "egging-item.name",
                "Egging Tool" // default value
        );

        //gets lore string from config or sets as default
        toolLore = CONFIG.getString(
                "egging-item.lore",
                "Right click me on a mob to egg it!" // default value
        );

        //gets enchant glow from config or sets as default
        toolGlow = CONFIG.getBoolean(
                "egging-item.enchant-glow",
                true // default value
        );

        //gets if allowed to egg other's tamed mobs from config or sets as default
        eggTamed = CONFIG.getBoolean(
                "egging.allow-tamed-egging",
                 false // default value
        );

        renaming = CONFIG.getBoolean(
                "egging.renaming.allowed",
                true // default value
        );

        renaming_permNeeded = CONFIG.getBoolean(
                "egging.renaming.require-permission",
                false
        );


        // Gets egging tool usage mode or sets as default
        String modeString = CONFIG.getString("egging-item.usage.mode",
                "NONE" // default value
        );

        try {
            usageMode = UsageMode.valueOf(modeString.toUpperCase());
        } catch  (Exception exception){
            usageMode = UsageMode.NONE;
        }

        // Gets max uses of egging tool or sets as default
        maxUses = CONFIG.getInt("egging-item.usage.durability.max-uses", 25);

        economyEnabled = CONFIG.getBoolean("egging.economy.enabled", false);

        toolPrice = CONFIG.getDouble("egging.economy.get-command-price", 1000.00);

        defaultPrice = CONFIG.getDouble("egging.economy.default-price", 100.00);

        animalsPrice = CONFIG.getDouble("egging.economy.animals-price", 100.00);

        monstersPrice = CONFIG.getDouble("egging.economy.monsters-price", 500.00);

        golemsPrice = CONFIG.getDouble("egging.economy.golems-price", 250.00);

        villagerPrice = CONFIG.getDouble("egging.economy.villager-price", 250.00);

        bossPrice = CONFIG.getDouble("egging.economy.boss-price", 10000.00);

        blacklist = CONFIG.getStringList("egging.black-list");
    }

    public Material getToolMaterial() {
        return toolMaterial;
    }

    public String getToolName() {
        return toolName;
    }

    public String getToolLore() {
        return toolLore;
    }

    public boolean getToolGlow() {
        return toolGlow;
    }

    public boolean getEggTamed() {
        return eggTamed;
    }

    public boolean getRenaming() {
        return renaming;
    }

    public boolean  getRenaming_permNeeded() {
        return renaming_permNeeded;
    }

    public UsageMode getUsageMode() {
        return usageMode;
    }

    public int getMaxUses() {
        return maxUses;
    }

    public boolean getEconomyEnabled() {
        return economyEnabled;
    }

    public double getToolPrice() {
        return toolPrice;
    }

    public double getDefaultPrice() {
        return defaultPrice;
    }

    public double  getAnimalsPrice() {
        return animalsPrice;
    }

    public double getMonstersPrice() {
        return monstersPrice;
    }

    public double getGolemsPrice() {
        return golemsPrice;
    }

    public double getVillagerPrice() {
        return villagerPrice;
    }

    public double getBossPrice() {
        return bossPrice;
    }

    public List<String> getBlacklist() {
        return blacklist;
    }
}