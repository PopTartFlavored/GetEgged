package io.jimbonesjim.getEgged.Services;

import io.jimbonesjim.getEgged.Managers.MessageManager;
import io.jimbonesjim.getEgged.utils.MiniMessageUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.entity.*;
import org.bukkit.inventory.InventoryHolder;

public class EntityInventoryValidator {

    private final MessageManager messageManager;

    public EntityInventoryValidator(MessageManager messageManager) {
        this.messageManager = messageManager;
    }

    // Allows saddles / horse armor
    // Blocks chested animals with actual inventories
    public RuleResult hasItems(Entity entity) {
        if (entity instanceof AbstractNautilus) return RuleResult.ok();

        Component deny_message = MiniMessageUtil.createMessage(messageManager.getDenyItemsMessage(),
                Placeholder.component("entity", Component.translatable(entity.getType().translationKey())));

        // Not an inventory holder → safe
        if (!(entity instanceof InventoryHolder holder)) {
            return RuleResult.ok();
        }

        // Any horse WITHOUT a chest is allowed (saddles & armor only)
        if (entity instanceof AbstractHorse horse) {
            if (horse instanceof ChestedHorse chested && chested.isCarryingChest()) {
                return RuleResult.fail(deny_message);
            }
            return RuleResult.ok();
        }

        // All other inventory holders
        if (holder.getInventory() != null && !holder.getInventory().isEmpty()) {
            return RuleResult.fail(deny_message);
        }

        return RuleResult.ok();
    }
}