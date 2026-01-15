package io.jimbonesjim.getEgged.Listeners;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import io.jimbonesjim.getEgged.Managers.ConfigManager;
import io.jimbonesjim.getEgged.Managers.DataManager;
import io.jimbonesjim.getEgged.Managers.MessageManager;
import io.jimbonesjim.getEgged.Services.GriefPreventionService;
import io.jimbonesjim.getEgged.Services.WorldguardService;
import io.jimbonesjim.getEgged.utils.InventoryUtil;
import io.jimbonesjim.getEgged.utils.MiniMessageUtil;
import io.jimbonesjim.getEgged.utils.SafeSpawnUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SpawnEggMeta;

public class PlayerUseSpawnEggListener implements Listener {

    private final DataManager dataManager;
    private final ConfigManager configManager;
    private final MessageManager messageManager;
    private final WorldguardService worldguardService;
    private final GriefPreventionService griefPreventionService;

    public PlayerUseSpawnEggListener(DataManager dataManager, WorldguardService worldguardService, GriefPreventionService griefPreventionService, ConfigManager configManager, MessageManager messageManager) {
        this.dataManager = dataManager;
        this.configManager = configManager;
        this.messageManager = messageManager;
        this.worldguardService = worldguardService;
        this.griefPreventionService = griefPreventionService;
    }
    @EventHandler (priority= EventPriority.LOWEST)
    public void onSpawn(PlayerInteractEvent e){
        if (e.getAction().isLeftClick()) return;
        Player p = e.getPlayer();
        ItemStack egg = p.getInventory().getItemInMainHand();

        if (!dataManager.fromGetEgged(egg.getItemMeta())) return;
        if (!(egg.getItemMeta() instanceof SpawnEggMeta eggMeta)) return;
        if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (e.getClickedBlock().getType().isInteractable()) return;
        }

        e.setCancelled(true);
        e.setUseInteractedBlock(Event.Result.DENY);
        e.setUseItemInHand(Event.Result.DENY);

        Component newName = null;

        if (egg.getItemMeta().hasDisplayName()){
            newName = egg.getItemMeta().displayName();
        }

        EntityType etype = eggMeta.getCustomSpawnedType();
        if (etype == null) {
            etype = EntityType.valueOf(egg.getType().name().replace("_SPAWN_EGG", ""));
        }

        Location loc = e.getClickedBlock() != null
                ? e.getClickedBlock().getLocation().add(0, 1, 0)
                : p.getLocation().add(p.getLocation().getDirection());

        Entity ent = SafeSpawnUtil.spawnSafely(etype, loc);
        if (ent == null) {
            p.sendMessage(MiniMessageUtil.createMessage(messageManager.getUnsafeSpawnMessage(),
                    Placeholder.component("entity", Component.translatable(etype.translationKey()))));
            return;
        }

        if (!worldguardService.canSpawn(p, BukkitAdapter.adapt(ent.getLocation()))) {
            ent.remove();
            return;
        }

        if (!griefPreventionService.canBuild(p, ent.getLocation())) {
            ent.remove();
            return;
        }

        dataManager.eggToEntity(ent, egg.getItemMeta());

        if (configManager.getRenaming()
                && newName != null
                && (!configManager.getRenaming_permNeeded() || p.hasPermission("getegged.rename"))) {
            ent.customName(newName);
        }


        ent.setSilent(false);
        ent.setNoPhysics(false);
        ent.setInvulnerable(false);
        ent.setInvisible(false);

        //Removes egg if not in creative
        if (p.getGameMode() != GameMode.CREATIVE) {
            InventoryUtil.removeOneFromMainHand(p, egg);
        }
    }
}