package io.jimbonesjim.getEgged.Services;

import io.jimbonesjim.getEgged.Managers.DataManager;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class EggFactory {

    private final DataManager dataManager;

    public EggFactory(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    public ItemStack createEgg(Entity entity){
        Material eggMat = Material.getMaterial(entity.getType().name() + "_SPAWN_EGG");
        if (eggMat == null) return null;
        ItemStack egg = new ItemStack(eggMat);
        ItemMeta meta = egg.getItemMeta();
        if (meta == null) return null;
        egg.setItemMeta(dataManager.entityToEgg(entity, meta));
        return egg;
    }
}
