package io.jimbonesjim.getEgged.Listeners;

import io.jimbonesjim.getEgged.Managers.DataManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.CreatureSpawnEvent;
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
        if (!dataManager.fromGetEgged(egg.getItemMeta())) return;
        if (!(egg.getItemMeta() instanceof SpawnEggMeta eggMeta)) return;
        if (e.getAction() != Action.RIGHT_CLICK_AIR){
            if (e.getClickedBlock().getType().isInteractable()) return;
        }
        e.setCancelled(true);
        EntityType etype = eggMeta.getCustomSpawnedType();
        if (etype == null) {
            etype = EntityType.valueOf(egg.getType().name().replace("_SPAWN_EGG", ""));
        }
        Location loc = e.getClickedBlock() != null
                ? e.getClickedBlock().getLocation().add(0, 1, 0)
                : p.getLocation().add(p.getLocation().getDirection());
        loc.setX(Math.round(loc.getX()) + 0.5);
        loc.setZ(Math.round(loc.getZ()) + 0.5);
        Entity ent = loc.getWorld().spawnEntity(loc, etype, CreatureSpawnEvent.SpawnReason.CUSTOM, spawned -> {
            spawned.setSilent(true);
            spawned.setInvisible(true);
            spawned.setInvulnerable(true);
            spawned.setNoPhysics(true);
        });
        Location newLoc = loc.clone();
        while (ent.collidesAt(newLoc)) {
            newLoc.add(0, 1, 0);
        }
        if (newLoc.getY() - loc.getY() > 2){
            ent.remove();
            p.sendMessage(Component.text("This is not a safe location for your " +
                    etype.name().toLowerCase() + " to spawn!").color(NamedTextColor.RED));
            return;
        }
        ent.teleport(newLoc);
        dataManager.eggToEntity(ent, egg.getItemMeta());
        ent.setSilent(false);
        ent.setInvulnerable(false);
        ent.setNoPhysics(false);
        ent.setInvisible(false);
        //Removes egg if not in creative
        if (p.getGameMode() != GameMode.CREATIVE) {
            if (egg.getAmount() > 1){
                egg.setAmount(egg.getAmount() - 1);
            } else {
                p.getInventory().setItemInMainHand(null);
            }
        }
        e.getPlayer().updateInventory();
    }
}