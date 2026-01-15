package io.jimbonesjim.getEgged.Services;

import com.sk89q.worldedit.util.Location;
import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.flags.Flag;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.flags.registry.FlagConflictException;
import com.sk89q.worldguard.protection.flags.registry.FlagRegistry;
import com.sk89q.worldguard.protection.regions.RegionQuery;
import io.jimbonesjim.getEgged.Managers.MessageManager;
import io.jimbonesjim.getEgged.utils.MiniMessageUtil;
import org.bukkit.entity.Player;

public class WorldguardService {

    private final MessageManager messageManager;
    private boolean wgExists = false;
    private WorldGuardPlugin wgPlugin;
    private RegionQuery query;
    private StateFlag GETEGGED_SPAWN;
    private StateFlag GETEGGED_EGG;

    public WorldguardService(MessageManager messageManager) {
        this.messageManager = messageManager;
    }

    public void init(){
        wgPlugin = WorldGuardPlugin.inst();
        if (wgPlugin == null || !wgPlugin.isEnabled()) {
            wgExists = false;
            return;
        }

        wgExists = true;
        query = WorldGuard.getInstance().getPlatform().getRegionContainer().createQuery();
    }

    public boolean enabled(){
        return wgExists;
    }

    public boolean canEggMob(Player player, Location entLoc){
        if (player.hasPermission("getegged.worldguard.bypass")) return true;
        if (!wgExists || GETEGGED_EGG == null) return true;
        LocalPlayer localPlayer = wgPlugin.wrapPlayer(player);

        StateFlag.State state = query.queryState(entLoc, localPlayer, GETEGGED_EGG);

        if (state == StateFlag.State.DENY) {
            player.sendMessage(MiniMessageUtil.createMessage(messageManager.getDenyWorldGuardEggingMessage()));
        }

        return state == StateFlag.State.ALLOW;
    }

    public boolean canSpawn(Player player, Location entLoc){
        if (player.hasPermission("getegged.worldguard.bypass")) return true;
        if (!wgExists || GETEGGED_SPAWN == null) return true;
        LocalPlayer localPlayer = wgPlugin.wrapPlayer(player);

        StateFlag.State state = query.queryState(entLoc, localPlayer, GETEGGED_SPAWN);

        if (state == StateFlag.State.DENY) {
            player.sendMessage(MiniMessageUtil.createMessage(messageManager.getDenyWorldGuardSpawnMessage()));
        }

        return state == StateFlag.State.ALLOW;
    }

    public void registerFlags() {
        FlagRegistry registry = WorldGuard.getInstance().getFlagRegistry();
        try {
            StateFlag flag = new StateFlag("getegged-egg", true);
            registry.register(flag);
            GETEGGED_EGG = flag;
        } catch (FlagConflictException e) {
            Flag<?> existing = registry.get("getegged-egg");
            if (existing instanceof StateFlag) {
                GETEGGED_EGG = (StateFlag) existing;
            }
        }

        try {
            StateFlag flag = new StateFlag("getegged-spawn", true);
            registry.register(flag);
            GETEGGED_SPAWN = flag;
        } catch (FlagConflictException e) {
            Flag<?> existing = registry.get("getegged-spawn");
            if (existing instanceof StateFlag) {
                GETEGGED_SPAWN = (StateFlag) existing;
            }
        }
    }
}
