package io.jimbonesjim.getEgged.Services;

import io.jimbonesjim.getEgged.Managers.ConfigManager;
import io.jimbonesjim.getEgged.Rules.EntityCategory;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.GameMode;
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

    private boolean bypassEconomy(Player player){
        return !isEnabled() || player.getGameMode() == GameMode.CREATIVE || player.hasPermission("getegged.economybypass");
    }

    public RuleResult withdrawForTool(Player player){

        // checks if economy is enabled or player has permission to bypass economy
        if (bypassEconomy(player)) return RuleResult.ok();

        // Gets cost of getting and egging tool
        double cost = configManager.getToolPrice();
        if (cost <= 0) return RuleResult.ok();

        EconomyResponse response = econ.withdrawPlayer(player, cost);

        if (response.transactionSuccess()) {
            // Succesfully withdrawn, formatted message sent to player
            Component paidMessage = MiniMessage.miniMessage().deserialize(
                    "<yellow>You have paid <green><cost><yellow> to get an <dark_purple>Egging Tool.",
                    Placeholder.parsed("cost", econ.format(cost)));
            player.sendMessage(paidMessage);
            return RuleResult.ok();
        } else {
            // Not enough money
            return RuleResult.fail(Component.text("You don't have enough money to get an egging tool!", NamedTextColor.RED));
        }
    }

    public RuleResult withdrawAmount(Player player, Entity entity){
        // checks if economy is enabled or player has permission to bypass economy
        if (bypassEconomy(player)) return RuleResult.ok();

        // Gets cost of egging based on type of entity
        double cost = getPrice(entity);
        if (cost <= 0) return RuleResult.ok();

        // Tries to withdraw cost
        EconomyResponse response = econ.withdrawPlayer(player, cost);

        if (response.transactionSuccess()) {
            // Succesfully withdrawn, formatted message sent to player
            Component paidMessage = MiniMessage.miniMessage().deserialize(
                    "<yellow>You have paid <green><cost><yellow> to egg that <dark_purple><entity>",
                    Placeholder.parsed("cost", econ.format(cost)),
                    Placeholder.component("entity", Component.translatable(entity.getType().translationKey())));
            player.sendMessage(paidMessage);
            return RuleResult.ok();
        } else {
            // Not enough money
            return RuleResult.fail(
                    Component.text("You don't have enough money to egg a ", NamedTextColor.RED)
                            .append(Component.translatable(entity.getType().translationKey(), NamedTextColor.DARK_RED)));
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
            case BOSS -> cost = configManager.getBossPrice();
            default ->  cost = configManager.getDefaultPrice();
        }
        return cost;
    }
}
