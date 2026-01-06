package io.jimbonesjim.getEgged.utils;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.entity.CreatureSpawnEvent;

public class SafeSpawnUtil {


    private SafeSpawnUtil() {}

    public static Entity spawnSafely(EntityType etype, Location baseLoc) {
        Location loc =  baseLoc.clone();

        loc.setX(Math.round(loc.getX()) + 0.5);
        loc.setZ(Math.round(loc.getZ()) + 0.5);

        Entity ent = loc.getWorld().spawnEntity(loc, etype, CreatureSpawnEvent.SpawnReason.CUSTOM, spawned -> {
            spawned.setSilent(true);
            spawned.setInvisible(true);
            spawned.setInvulnerable(true);
            spawned.setNoPhysics(true);
        });

        Location newLoc = loc.clone();
        while (ent.collidesAt(newLoc)) {
            newLoc.add(0, 1, 0);
        }
        if (newLoc.getY() - loc.getY() > 2) {
            ent.remove();
            return null;
        }

        ent.teleport(newLoc);
        return ent;
    }
}
