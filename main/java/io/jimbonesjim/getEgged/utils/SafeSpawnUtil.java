package io.jimbonesjim.getEgged.utils;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.entity.CreatureSpawnEvent;

import java.util.EnumSet;

public class SafeSpawnUtil {

    private static final EnumSet<Material> DANGEROUS_BLOCKS = EnumSet.of(
            Material.LAVA,
            Material.MAGMA_BLOCK,
            Material.FIRE,
            Material.CAMPFIRE,
            Material.SOUL_CAMPFIRE,
            Material.WITHER_ROSE,
            Material.CACTUS,
            Material.SWEET_BERRY_BUSH
    );


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
        while (ent.collidesAt(newLoc) && newLoc.getY() - loc.getY() < 5) {
            newLoc.add(0, 1, 0);
        }
        if (ent.collidesAt(newLoc)){
            ent.remove();
            return null;
        }
        if (newLoc.clone().add(0, -1, 0).getBlock().getType().isAir()) {
            while (newLoc.getBlock().getType() == Material.AIR && Math.abs(newLoc.getY() - loc.getY()) < 5) {
                newLoc.add(0, -1, 0);
            }
        }
        Material blockMat = newLoc.getBlock().getType();
        Material belowMat = newLoc.clone().add(0, -1, 0).getBlock().getType();
        if (isDangerous(blockMat, belowMat) || belowMat.isAir() || newLoc.getY() - loc.getY() > 3 || loc.getY() - newLoc.getY() > 3 || ent.collidesAt(newLoc)) {
            ent.remove();
            return null;
        }

        ent.teleport(newLoc);
        return ent;
    }

    private static boolean isDangerous(Material mat, Material mat2) {
        return DANGEROUS_BLOCKS.contains(mat) ||  DANGEROUS_BLOCKS.contains(mat2);
    }
}
