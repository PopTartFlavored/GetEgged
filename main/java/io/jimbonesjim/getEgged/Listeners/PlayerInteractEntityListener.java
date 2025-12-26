package io.jimbonesjim.getEgged.Listeners;

import io.jimbonesjim.getEgged.Managers.DataManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class PlayerInteractEntityListener implements Listener {

    private final DataManager dataManager;

    public PlayerInteractEntityListener(DataManager dataManager){
        this.dataManager = dataManager;
    }

    @EventHandler
    public void onInteract(PlayerInteractEntityEvent e){
        Entity entity = e.getRightClicked();
        Player p = e.getPlayer();
        if (!p.getInventory().getItemInMainHand().getType().equals(Material.STICK)) return;

        switch (entity) {
            case Animals animal -> {
                if (!p.hasPermission("getegged.animal." + animal.getType()) && !p.hasPermission("getegged.animal.*")) {
                    p.sendMessage(Component.text("You do not have permission to egg a " + entity.getType()).color(NamedTextColor.RED));
                    return;
                }
            }
            case Monster monster -> {
                if (!p.hasPermission("getegged.monster." + monster.getType()) && !p.hasPermission("getegged.monster.*")) {
                    p.sendMessage(Component.text("You do not have permission to egg a " + entity.getType()).color(NamedTextColor.RED));
                    return;
                }
            }
            case AbstractVillager abstractVillager -> {
                if (!p.hasPermission("getegged.villager")) {
                    p.sendMessage(Component.text("You do not have permission to egg a " + entity.getType()).color(NamedTextColor.RED));
                    return;
                }
            }
            default -> {
                return;
            }
        }

        ItemStack egg = ItemStack.of(Material.getMaterial(entity.getType() + "_SPAWN_EGG"));
        if (egg == null){
            p.sendMessage(Component.text("Could not find spawn egg for entity " + entity.getType()).color(NamedTextColor.RED));
            return;
        }
        ItemMeta meta = egg.getItemMeta();
        meta = dataManager.entityToEgg(entity, meta);
        egg.setItemMeta(meta);

        e.setCancelled(true);
        entity.getWorld().spawnParticle(Particle.WHITE_SMOKE, entity.getLocation(), 10);
        entity.getWorld().playSound(entity.getLocation(), Sound.ENTITY_CHICKEN_EGG, 1f, 1f);
        entity.remove();
        p.give(egg);
    }
}
