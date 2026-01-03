package io.jimbonesjim.getEgged.Listeners;

import io.jimbonesjim.getEgged.Managers.ConfigManager;
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
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class PlayerInteractEntityListener implements Listener {

    private final DataManager dataManager;
    private final ConfigManager configManager;

    public PlayerInteractEntityListener(DataManager dataManager, ConfigManager configManager) {
        this.dataManager = dataManager;
        this.configManager = configManager;
    }

    @EventHandler
    public void onInteract(PlayerInteractEntityEvent e){
        Entity entity = e.getRightClicked();
        Player p = e.getPlayer();
        ItemStack mainHand = p.getInventory().getItemInMainHand();
        if(mainHand.getType().name().endsWith("_SPAWN_EGG") && dataManager.fromGetEgged(mainHand.getItemMeta())){
            e.setCancelled(true);
            return;
        }
        if (!dataManager.isMobCaptureItem(mainHand.getItemMeta())) return;

        if (entity instanceof Tameable te){
            boolean allowOthers = configManager.getCaptureTamed();

            if (te.isTamed() && te.getOwnerUniqueId() != null &&
                    !p.getUniqueId().equals(te.getOwnerUniqueId()) && !allowOthers && !p.hasPermission("getegged.tamed")){
                p.sendMessage(Component.text("You cannot egg someone else's " + entity.getType().name()).color(NamedTextColor.RED));
                return;
            }
        }

        switch (entity) {
            case Animals animal -> {
                if (!p.hasPermission("getegged.animal." + animal.getType()) && !p.hasPermission("getegged.animal.*")) {
                    p.sendMessage(Component.text("You do not have permission to egg a " + entity.getType().name()).color(NamedTextColor.RED));
                    return;
                }
            }
            case Monster monster -> {
                if (!p.hasPermission("getegged.monster." + monster.getType()) && !p.hasPermission("getegged.monster.*")) {
                    p.sendMessage(Component.text("You do not have permission to egg a " + monster.getType().name()).color(NamedTextColor.RED));
                    return;
                }
            }
            case Enemy enemy -> {
                if (!p.hasPermission("getegged.monster." + enemy.getType()) && !p.hasPermission("getegged.monster.*")) {
                    p.sendMessage(Component.text("You do not have permission to egg a " + enemy.getType().name()).color(NamedTextColor.RED));
                    return;
                }
            }
            case AbstractVillager abstractVillager -> {
                if (!p.hasPermission("getegged.villager")) {
                    p.sendMessage(Component.text("You do not have permission to egg a " + abstractVillager.getType().name()).color(NamedTextColor.RED));
                    return;
                }
            }
            case Ambient ambient -> {
                    if (!p.hasPermission("getegged.animal." + ambient.getType()) && !p.hasPermission("getegged.animal.*")) {
                        p.sendMessage(Component.text("You do not have permission to egg a " + ambient.getType().name()).color(NamedTextColor.RED));
                        return;
                    }
            }
            case WaterMob watermob -> {
                if (!p.hasPermission("getegged.animal." + watermob.getType()) && !p.hasPermission("getegged.animal.*")) {
                    p.sendMessage(Component.text("You do not have permission to egg a " + watermob.getType().name()).color(NamedTextColor.RED));
                    return;
                }
            }
            case Golem golem -> {
                if (!p.hasPermission("getegged.golem." + golem.getType()) && !p.hasPermission("getegged.golem.*")) {
                    p.sendMessage(Component.text("You do not have permission to egg a " + golem.getType().name()).color(NamedTextColor.RED));
                    return;
                }
            }
            default -> {
                return;
            }
        }

        if (entity instanceof InventoryHolder ih) {
            if (dataManager.hasItems(ih)){
                if (ih instanceof ChestedHorse) {
                    p.sendMessage(Component.text("Cannot egg something with equipments or items").color(NamedTextColor.RED));
                } else {
                    p.sendMessage(Component.text("Cannot egg something with items in their inventory").color(NamedTextColor.RED));
                }
                return;
            }
        }

        Material eggMat = Material.getMaterial(entity.getType().name() + "_SPAWN_EGG");
        if (eggMat == null) {
            p.sendMessage(Component.text("Could not find spawn egg for entity " + entity.getType()).color(NamedTextColor.RED));
            return;
        }
        ItemStack egg = new ItemStack(eggMat);
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