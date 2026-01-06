package io.jimbonesjim.getEgged.Services;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.AbstractHorse;
import org.bukkit.entity.ChestedHorse;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.InventoryHolder;

public class EntityInventoryValidator {

    // Allows saddles / horse armor
    // Blocks chested animals with actual inventories
    public RuleResult hasItems(Entity entity) {

        // Not an inventory holder → safe
        if (!(entity instanceof InventoryHolder holder)) {
            return RuleResult.ok();
        }

        // Any horse WITHOUT a chest is allowed (saddles & armor only)
        if (entity instanceof AbstractHorse horse) {
            if (horse instanceof ChestedHorse chested && chested.isCarryingChest()) {
                return RuleResult.fail(
                        Component.text("You can't egg animals with chest inventories.")
                                .color(NamedTextColor.RED)
                );
            }
            return RuleResult.ok();
        }

        // All other inventory holders
        if (holder.getInventory() != null && !holder.getInventory().isEmpty()) {
            return RuleResult.fail(
                    Component.text("This entity has items in inventory and cannot be egged.")
                            .color(NamedTextColor.RED)
            );
        }

        return RuleResult.ok();
    }
}
