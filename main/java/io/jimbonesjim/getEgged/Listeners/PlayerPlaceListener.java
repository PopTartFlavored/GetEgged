package io.jimbonesjim.getEgged.Listeners;

import io.jimbonesjim.getEgged.Managers.DataManager;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class PlayerPlaceListener implements Listener {

    private final DataManager dataManager;

    public PlayerPlaceListener(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    @EventHandler
    public void onPlace(PlayerInteractEvent e) {
        ItemStack item = e.getItem();
        if (item == null || !item.hasItemMeta()) return;
        ItemMeta meta = item.getItemMeta();
        if (!dataManager.isMobCaptureItem(meta)) return;
        if (e.useItemInHand() == Event.Result.DENY) return;
        e.setUseItemInHand(Event.Result.DENY);
        e.getPlayer().updateInventory();
    }
}
