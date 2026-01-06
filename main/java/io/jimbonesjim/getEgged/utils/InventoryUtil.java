package io.jimbonesjim.getEgged.utils;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public final class InventoryUtil {

    private InventoryUtil() {}

    public static void removeOneFromMainHand(Player player, ItemStack item) {
        if (item == null || item.getType().isAir()) return;
        if (item.getAmount() > 1) {
            item.setAmount(item.getAmount() - 1);
        } else {
            player.getInventory().setItemInMainHand(null);
        }
    }
}
