package io.jimbonesjim.getEgged.Services;

import io.jimbonesjim.getEgged.Rules.EntityCategory;
import org.bukkit.entity.*;

public class EntityCategoryResolver {

    public EntityCategory resolve(Entity ent) {

        if (ent instanceof Boss) return EntityCategory.BOSS;
        else if (ent instanceof Animals || ent instanceof Ambient || ent instanceof WaterMob) return EntityCategory.ANIMAL;
        else if (ent instanceof Enemy) return EntityCategory.MONSTER;
        else if (ent instanceof Golem) return EntityCategory.GOLEM;
        else if (ent instanceof AbstractVillager) return EntityCategory.VILLAGER;
        else return EntityCategory.DEFAULT;
    }
}
