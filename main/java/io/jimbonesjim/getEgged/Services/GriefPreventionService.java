package io.jimbonesjim.getEgged.Services;

import me.ryanhamshire.GriefPrevention.GriefPrevention;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class GriefPreventionService {

    private GriefPrevention GP;

    public void init() {
        GP = GriefPrevention.instance;
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
