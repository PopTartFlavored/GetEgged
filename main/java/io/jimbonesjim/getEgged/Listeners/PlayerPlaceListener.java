package io.jimbonesjim.getEgged.Listeners;

import io.jimbonesjim.getEgged.Managers.EggingToolManager;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class PlayerPlaceListener implements Listener {

    private final EggingToolManager eggingToolManager;

    public PlayerPlaceListener(EggingToolManager eggingToolManager) {
        this.eggingToolManager = eggingToolManager;
    }

    @EventHandler
    public void onPlace(PlayerInteractEvent e) {
        ItemStack item = e.getItem();
        if (item == null || !item.hasItemMeta()) return;
        ItemMeta meta = item.getItemMeta();
        if (!eggingToolManager.isTool(meta)) return;
        if (e.useItemInHand() == Event.Result.DENY) return;
        e.setUseItemInHand(Event.Result.DENY);
        e.getPlayer().updateInventory();
    }
}