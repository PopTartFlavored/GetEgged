package io.jimbonesjim.getEgged.Services;

import me.ryanhamshire.GriefPrevention.GriefPrevention;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class GriefPreventionService {

    private GriefPrevention GP;

    public void init(JavaPlugin plugin) {
        GP = GriefPrevention.instance;
        plugin.getLogger().info("Registered GriefPrevention Services");
    }

    public boolean isEnabled(){
        if (GP == null) return false;
        return GP.isEnabled();
    }

    public boolean canBuild(Player player, Location loc) {
        if (!isEnabled()) return true;
        if (player.hasPermission("getegged.griefprevention.bypass")) return true;
        String allow = GP.allowBuild(player, loc);
        return allow == null;
    }

    public boolean canBreak(Player player, Location loc) {
        if (!isEnabled()) return true;
        if (player.hasPermission("getegged.griefprevention.bypass")) return true;
        String allowed = GP.allowBreak(player, loc.getBlock(), loc);
        return allowed == null;
    }
}
