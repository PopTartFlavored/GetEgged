package io.jimbonesjim.getEgged.API;

import io.papermc.paper.entity.CollarColorable;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.*;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import static io.jimbonesjim.getEgged.Managers.DataManager.*;

public class DataSaver {

    public ItemMeta saveData(Entity e, ItemMeta meta){
        PersistentDataContainer PDC = meta.getPersistentDataContainer();
        PDC.set(GETEGGED, PersistentDataType.BOOLEAN, Boolean.TRUE);
        if (e.customName() != null) saveName(e, PDC);
        if (e instanceof Tameable te) saveOwner(te, PDC);
        if (e instanceof Ageable ae) saveBaby(ae, PDC);

        //Variant saving
        if (e instanceof Sheep s) saveEnum(s.getColor(), COLOR, PDC);
        if (e instanceof CollarColorable cce) saveEnum(cce.getCollarColor(), COLLAR, PDC);
        if (e instanceof Axolotl a) saveEnum(a.getVariant(), VARIANT, PDC);

        if (e instanceof Parrot p) saveEnum(p.getVariant(), VARIANT, PDC);
        if (e instanceof Cat c) saveVariant(c.getCatType(), VARIANT, PDC);
        if (e instanceof Frog f) saveVariant(f.getVariant(), VARIANT, PDC);
        if (e instanceof Llama l) saveEnum(l.getColor(), VARIANT, PDC);
        if (e instanceof TropicalFish tf) {
            saveEnum(tf.getPattern(), PATTERN, PDC);
            saveEnum(tf.getBodyColor(), COLOR, PDC);
        }
        if (e instanceof Panda pd) {
            saveEnum(pd.getMainGene(), VARIANT, PDC);
            saveEnum(pd.getHiddenGene(), VARIANT2, PDC); // new key
        }
        if (e instanceof Fox fx) saveEnum(fx.getFoxType(), VARIANT, PDC);

//        if (e instanceof Cow c) saveVariant(c.getVariant(), VARIANT, PDC);
//        if (e instanceof Pig p) saveVariant(p.getVariant(), VARIANT, PDC);
//        if (e instanceof Chicken ch) saveVariant(ch.getVariant(), VARIANT, PDC);
        if (e instanceof Wolf w) saveVariant(w.getVariant(), VARIANT, PDC);

        //special cases
        if (e instanceof AbstractHorse ah) saveAbstractHorseData(ah, PDC);
        if (e instanceof Villager v) saveVillagerData(v, PDC);
        return meta;
    }

    private void saveName(Entity e, PersistentDataContainer PDC) {
        PDC.set(NAME, PersistentDataType.STRING , e.getName());
    }

    private void saveBaby(Ageable ae, PersistentDataContainer PDC) {
        PDC.set(BABY, PersistentDataType.BOOLEAN, !ae.isAdult());
    }

    private void saveOwner(Tameable te, PersistentDataContainer PDC) {
        if (!te.isTamed() || te.getOwnerUniqueId() == null) return;
        PDC.set(OWNER, PersistentDataType.STRING, te.getOwnerUniqueId().toString());
    }

    private void saveAbstractHorseData(AbstractHorse ah, PersistentDataContainer PDC){
        PDC.set(JUMP, PersistentDataType.DOUBLE, ah.getJumpStrength());
        PDC.set(SPEED, PersistentDataType.DOUBLE, ah.getAttribute(Attribute.MOVEMENT_SPEED).getBaseValue());
        if (ah instanceof Horse horse){
            PDC.set(COLOR, PersistentDataType.STRING, horse.getColor().name());
            PDC.set(STYLE, PersistentDataType.STRING, horse.getStyle().name());
        }
    }

    private void saveVariant(Object value, NamespacedKey key, PersistentDataContainer PDC){
        if (value == null) return;
        PDC.set(key, PersistentDataType.STRING, value.toString());
    }

    private void saveEnum(Enum<?> value, NamespacedKey key, PersistentDataContainer PDC) {
        PDC.set(key, PersistentDataType.STRING, value.name());
    }

    private void saveVillagerData(Villager v, PersistentDataContainer PDC){
        PDC.set(PROF, PersistentDataType.STRING, v.getProfession().toString());
        PDC.set(TYPE, PersistentDataType.STRING, v.getVillagerType().toString());
        PDC.set(LEVEL, PersistentDataType.INTEGER, v.getVillagerLevel());
    }
}
