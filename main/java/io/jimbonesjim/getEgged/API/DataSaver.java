package io.jimbonesjim.getEgged.API;

import io.papermc.paper.entity.CollarColorable;
import io.papermc.paper.entity.Shearable;
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
        PDC.set(GETEGGED, PersistentDataType.BOOLEAN, true);
        if (e.customName() != null) saveName(e, PDC);
        if (e instanceof Tameable te) saveOwner(te, PDC);
        if (e instanceof Ageable ae) saveBaby(ae, PDC);

        //Variant saving
        if (e instanceof Pig pig) saveVariant(pig.getVariant().getKey().toString(), VARIANT, PDC);
        if (e instanceof Chicken chicken) saveVariant(chicken.getVariant().getKey().toString(), VARIANT, PDC);
        //if (e instanceof Cow cow) saveVariant(cow.getVariant().getKey().toString(), VARIANT, PDC);
        if (e instanceof Sheep s) {
            saveEnum(s.getColor(), COLOR, PDC);
            saveSheared(s, PDC);
        }
        if (e instanceof CollarColorable cce) saveEnum(cce.getCollarColor(), COLLAR, PDC);
        if (e instanceof Axolotl a) saveEnum(a.getVariant(), VARIANT, PDC);
        if (e instanceof Parrot p) saveEnum(p.getVariant(), VARIANT, PDC);
        if (e instanceof Cat c) saveVariant(c.getCatType().getKey().toString(), CAT_TYPE, PDC);
        if (e instanceof Frog f) saveVariant(f.getVariant().getKey().getKey(), VARIANT, PDC);
        if (e instanceof Llama l) saveEnum(l.getColor(), COLOR, PDC);
        if (e instanceof TropicalFish tf) {
            saveEnum(tf.getPattern(), PATTERN, PDC);
            saveEnum(tf.getBodyColor(), COLOR, PDC);
        }
        if (e instanceof Panda pd) {
            saveEnum(pd.getMainGene(), VARIANT, PDC);
            saveEnum(pd.getHiddenGene(), VARIANT2, PDC); // new key
        }
        if (e instanceof Fox fx) saveEnum(fx.getFoxType(), TYPE, PDC);
        if (e instanceof Rabbit rabbit) saveEnum(rabbit.getRabbitType(), TYPE, PDC);
        if (e instanceof Goat goat) saveHorns(goat, PDC);
        if (e instanceof Creeper creeper) savePowered(creeper, PDC);
        if (e instanceof Slime slime) saveSize(slime, PDC);
        if (e instanceof Wolf w) saveVariant(w.getVariant().getKey().toString(), VARIANT, PDC);

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

    private void saveSheared(Shearable se, PersistentDataContainer PDC) {
        PDC.set(SHEARED, PersistentDataType.BOOLEAN, !se.readyToBeSheared());
    }

    private void savePowered(Creeper creeper, PersistentDataContainer PDC) {
        PDC.set(POWERED, PersistentDataType.BOOLEAN, creeper.isPowered());
    }

    private void saveSize(Slime slime, PersistentDataContainer PDC) {
        PDC.set(SIZE, PersistentDataType.INTEGER, slime.getSize());
    }

    private void saveHorns(Goat goat,  PersistentDataContainer PDC) {
        PDC.set(RIGHT_HORN, PersistentDataType.BOOLEAN, goat.hasRightHorn());
        PDC.set(LEFT_HORN, PersistentDataType.BOOLEAN, goat.hasLeftHorn());
    }

    private void saveAbstractHorseData(AbstractHorse ah, PersistentDataContainer PDC){
        if (!(ah instanceof Llama)) {
            PDC.set(JUMP, PersistentDataType.DOUBLE, ah.getJumpStrength());
            PDC.set(SPEED, PersistentDataType.DOUBLE, ah.getAttribute(Attribute.MOVEMENT_SPEED).getValue());
        }
        if (ah instanceof Horse horse){
            PDC.set(COLOR, PersistentDataType.STRING, horse.getColor().name());
            PDC.set(STYLE, PersistentDataType.STRING, horse.getStyle().name());
        }
        if (ah instanceof Llama llama){
            PDC.set(COLOR, PersistentDataType.STRING, llama.getColor().name());
            PDC.set(STRENGTH, PersistentDataType.INTEGER, llama.getStrength());
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
        PDC.set(PROF, PersistentDataType.STRING, v.getProfession().getKey().toString());
        PDC.set(TYPE, PersistentDataType.STRING, v.getVillagerType().getKey().toString());
        PDC.set(LEVEL, PersistentDataType.INTEGER, v.getVillagerLevel());
    }
}