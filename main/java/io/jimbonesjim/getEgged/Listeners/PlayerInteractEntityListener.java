package io.jimbonesjim.getEgged.Listeners;

import io.jimbonesjim.getEgged.Managers.DataManager;
import io.jimbonesjim.getEgged.Managers.EggingToolManager;
import io.jimbonesjim.getEgged.Services.*;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class PlayerInteractEntityListener implements Listener {

    private final JavaPlugin plugin;
    private final DataManager dataManager;
    private final EggingToolManager eggingToolManager;
    private final EggingRulesService eggingRulesService;
    private final EntityInventoryValidator entityInventoryValidator;
    private final EggFactory eggFactory;
    private final VaultEconomyService vaultEconomyService;

    public PlayerInteractEntityListener(JavaPlugin plugin, DataManager dataManager,
                                        EggingRulesService eggingRulesService, EggingToolManager eggingToolManager,
                                        EntityInventoryValidator entityInventoryValidator, EggFactory eggFactory,
                                        VaultEconomyService vaultEconomyService) {
        this.plugin = plugin;
        this.dataManager = dataManager;
        this.eggingRulesService = eggingRulesService;
        this.eggingToolManager = eggingToolManager;
        this.entityInventoryValidator = entityInventoryValidator;
        this.eggFactory = eggFactory;
        this.vaultEconomyService = vaultEconomyService;
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
        // Checks if item in main hand is tool made by GetEgged
        // returns if not made by GetEgged
        if (!eggingToolManager.isTool(mainHand.getItemMeta())) return;

        // Checks if player has permission to egg entity
        // returns if player doesn't have permission
        if (fail(p, eggingRulesService.canEgg(p, entity))) return;

        // Checks if entity has items in its inventory
        // returns if entity has items
        if (fail(p, entityInventoryValidator.hasItems(entity))) return;

        ItemStack egg = eggFactory.createEgg(entity);
        if (egg == null){
            p.sendMessage(Component.text("Could not find spawn egg for entity " + entity.getType()).color(NamedTextColor.RED));
            return;
        }

        // Checks if economy is enabled and withdraws money from player if they have enough money
        // returns if player doesn't have enough money and economy is enabled
        if (fail(p, vaultEconomyService.withdrawAmount(p, entity))) return;

        e.setCancelled(true);

        eggingToolManager.applyUsage(mainHand, p);

        // Visual and audible effects of mob being removed from world and put into egg
        entity.getWorld().spawnParticle(Particle.WHITE_SMOKE, entity.getLocation(), 10);
        entity.getWorld().playSound(entity.getLocation(), Sound.ENTITY_CHICKEN_EGG, 1f, 1f);
        entity.remove();

        // Gives spawn egg to player after a second
        plugin.getServer().getScheduler().runTask(plugin, () -> p.give(egg));
    }

    //Sends message to player if RuleResult was not allowed
    private boolean fail(Player p, RuleResult result) {
        if (!result.allowed()) {
            p.sendMessage(result.message());
            return true;
        }
        return false;
    }
}