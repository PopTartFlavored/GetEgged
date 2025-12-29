package io.jimbonesjim.getEgged.Listeners;

import io.jimbonesjim.getEgged.Managers.DataManager;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SpawnEggMeta;

public class PlayerUseSpawnEggListener implements Listener {

    private final DataManager dataManager;

    public PlayerUseSpawnEggListener(DataManager dataManager){
        this.dataManager = dataManager;
    }
    @EventHandler
    public void onSpawn(PlayerInteractEvent e){
        if (e.getAction().isLeftClick()) return;
        Player p = e.getPlayer();
        ItemStack egg = p.getInventory().getItemInMainHand();
        if (!egg.getType().name().endsWith("_SPAWN_EGG")) return;
        if (!dataManager.fromGetEgged(egg.getItemMeta())) return;
        if (!(egg.getItemMeta() instanceof SpawnEggMeta eggMeta)) return;
        e.setCancelled(true);
        EntityType etype = eggMeta.getCustomSpawnedType();
        if (etype == null) {
            etype = EntityType.valueOf(egg.getType().name().replace("_SPAWN_EGG", ""));
        }
        Location loc = e.getClickedBlock() != null
                ? e.getClickedBlock().getLocation().add(0, 1, 0)
                : p.getLocation().add(p.getLocation().getDirection());
        Entity ent = loc.getWorld().spawnEntity(loc, etype);
        dataManager.eggToEntity(ent, egg.getItemMeta());
        //Removes egg if not in creative
        if (p.getGameMode() != GameMode.CREATIVE) {
            if (egg.getAmount() > 1){
                egg.setAmount(egg.getAmount() - 1);
            } else {
                p.getInventory().setItemInMainHand(null);
            }
        }
    }
}