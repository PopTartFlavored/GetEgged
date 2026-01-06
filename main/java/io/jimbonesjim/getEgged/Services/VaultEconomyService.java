package io.jimbonesjim.getEgged.Services;

import io.jimbonesjim.getEgged.Managers.ConfigManager;
import io.jimbonesjim.getEgged.Rules.EntityCategory;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.GameMode;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.*;

public class VaultEconomyService {

    private final Economy econ;
    private final ConfigManager configManager;
    private final EntityCategoryResolver entityCategoryResolver;

    public VaultEconomyService(Economy economy, ConfigManager configManager,  EntityCategoryResolver entityCategoryResolver) {
        econ = economy;
        this.configManager = configManager;
        this.entityCategoryResolver = entityCategoryResolver;
    }

    public boolean isEnabled(){
        return configManager.getEconomyEnabled() && econ != null && econ.isEnabled();
    }

    public RuleResult withdrawAmount(OfflinePlayer p, Entity entity){
        // Returns if economy is disabled
        if (!isEnabled()) return RuleResult.ok();

        // Returns if player isn't online
        if (!(p instanceof Player player)) return RuleResult.ok();

        // Returns if player is in creative or has bypass permission
        if (player.getGameMode() == GameMode.CREATIVE) return RuleResult.ok();
        if (player.hasPermission("getegged.economybypass")) return RuleResult.ok();

        // Gets cost of egging based on type of entity
        double cost = getPrice(entity);

        // Tries to withdraw cost
        EconomyResponse result = econ.withdrawPlayer(p, cost);


        if (result.transactionSuccess()) {
            // Succesfully withdrawn, formatted message sent to player
            player.sendMessage(Component.text("You have paid ", NamedTextColor.YELLOW)
                    .append(Component.text(econ.format(cost), NamedTextColor.AQUA))
                            .append(Component.text(" to egg a ", NamedTextColor.YELLOW))
                    .append(Component.text(entity.getType().name(), NamedTextColor.DARK_PURPLE)));
            return RuleResult.ok();
        } else {
            // Not enough money
            return RuleResult.fail(
                    Component.text("You don't have enough money to egg a ", NamedTextColor.RED)
                            .append(Component.text(entity.getType().name(), NamedTextColor.DARK_RED)));
        }
    }

    public double getPrice(Entity entity){
        EntityCategory category = entityCategoryResolver.resolve(entity);
        double cost;
        switch (category){
            case ANIMAL -> cost = configManager.getAnimalsPrice();
            case MONSTER -> cost =  configManager.getMonstersPrice();
            case GOLEM -> cost = configManager.getGolemsPrice();
            case VILLAGER -> cost = configManager.getVillagerPrice();
            default ->  cost = configManager.getDefaultPrice();
        }
        return cost;
    }
}
