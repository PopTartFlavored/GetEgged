package io.jimbonesjim.getEgged.API;

import io.papermc.paper.entity.CollarColorable;
import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import net.kyori.adventure.text.Component;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.*;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.UUID;
import java.util.function.Consumer;

import static io.jimbonesjim.getEgged.Managers.DataManager.*;
import static org.bukkit.Bukkit.getOfflinePlayer;

public class DataLoader {

    public void loadData(ItemMeta meta, Entity e) {
        PersistentDataContainer PDC = meta.getPersistentDataContainer();
        if (hasData(PDC, NAME, PersistentDataType.STRING)){
            e.customName(Component.text(PDC.get(NAME, PersistentDataType.STRING)));
        }
        if (e instanceof Ageable ae && hasData(PDC, BABY, PersistentDataType.BOOLEAN)) {
            if (Boolean.TRUE.equals(PDC.get(BABY, PersistentDataType.BOOLEAN))) {
                ae.setBaby();
            } else {
                ae.setAdult();
            }
        }
        if (e instanceof Tameable te && hasData(PDC, OWNER, PersistentDataType.STRING)){
            OfflinePlayer owner = getOfflinePlayer(UUID.fromString(PDC.get(OWNER, PersistentDataType.STRING)));
            if (owner != null) {
                te.setOwner(owner);
            }
        }
        if (hasData(PDC, COLOR, PersistentDataType.STRING)) {
            if (e instanceof Sheep sheep) {
                sheep.setColor(DyeColor.valueOf(PDC.get(COLOR, PersistentDataType.STRING)));
            }
            if (e instanceof Llama llama) {
                llama.setColor(Llama.Color.valueOf(PDC.get(COLOR, PersistentDataType.STRING)));
                if (hasData(PDC, STRENGTH, PersistentDataType.INTEGER)) {
                    llama.setStrength(PDC.get(STRENGTH, PersistentDataType.INTEGER));
                }
            }
        }
        if (e instanceof CollarColorable cce && hasData(PDC, COLLAR, PersistentDataType.STRING)){
            cce.setCollarColor(DyeColor.valueOf(PDC.get(COLLAR, PersistentDataType.STRING)));
        }
        if (hasData(PDC, VARIANT, PersistentDataType.STRING)) {
            if (e instanceof Chicken chicken){
                loadRegistry(PDC, VARIANT, RegistryAccess.registryAccess().getRegistry(RegistryKey.CHICKEN_VARIANT), chicken::setVariant);
            }
            if (e instanceof Cow cow){
                loadRegistry(PDC, VARIANT, RegistryAccess.registryAccess().getRegistry(RegistryKey.COW_VARIANT), cow::setVariant);
            }
            if (e instanceof Pig pig){
                loadRegistry(PDC, VARIANT, RegistryAccess.registryAccess().getRegistry(RegistryKey.PIG_VARIANT), pig::setVariant);
            }
            if (e instanceof Parrot parrot) {
                loadVariant(PDC, VARIANT, Parrot.Variant.class, parrot::setVariant);
            }
            if (e instanceof Fox fox) {
                loadVariant(PDC, VARIANT, Fox.Type.class, fox::setFoxType);
            }
            if (e instanceof Axolotl axolotl) {
                loadVariant(PDC, VARIANT, Axolotl.Variant.class, axolotl::setVariant);
            }
            if (e instanceof Frog frog){
                loadRegistry(PDC, VARIANT, RegistryAccess.registryAccess().getRegistry(RegistryKey.FROG_VARIANT), frog::setVariant);
            }
            if (e instanceof Cat cat){
                if (hasData(PDC, TYPE, PersistentDataType.STRING)) {
                    loadRegistry(PDC, TYPE, RegistryAccess.registryAccess().getRegistry(RegistryKey.CAT_VARIANT), cat::setCatType);
                }
            }
            if (e instanceof Panda panda) {
                loadVariant(PDC, VARIANT, Panda.Gene.class, panda::setMainGene);
                if (hasData(PDC, VARIANT2, PersistentDataType.STRING)) {
                    loadVariant(PDC, VARIANT2, Panda.Gene.class, panda::setHiddenGene);
                }
            }

            if (e instanceof Wolf w){
                loadRegistry(PDC, VARIANT, RegistryAccess.registryAccess().getRegistry(RegistryKey.WOLF_VARIANT), w::setVariant);
            }
        }
        if (e instanceof TropicalFish tf){
            if (hasData(PDC, PATTERN, PersistentDataType.STRING)){
                tf.setPattern(TropicalFish.Pattern.valueOf(PDC.get(PATTERN, PersistentDataType.STRING)));
            }
            if (hasData(PDC, COLOR, PersistentDataType.STRING)) {
                tf.setBodyColor(DyeColor.valueOf(PDC.get(COLOR, PersistentDataType.STRING)));
            }
        }
        if (e instanceof AbstractHorse ah){
            if (hasData(PDC, SPEED, PersistentDataType.DOUBLE)) {
                ah.getAttribute(Attribute.MOVEMENT_SPEED).setBaseValue(PDC.get(SPEED, PersistentDataType.DOUBLE));
            }
            if (hasData(PDC, JUMP, PersistentDataType.DOUBLE)) {
                ah.setJumpStrength(PDC.get(JUMP, PersistentDataType.DOUBLE));
            }
        }
        if (e instanceof Horse horse) {
            if (hasData(PDC, COLOR, PersistentDataType.STRING)) {
                        horse.setColor(Horse.Color.valueOf(PDC.get(COLOR, PersistentDataType.STRING)));
            }
            if (hasData(PDC, STYLE, PersistentDataType.STRING)) {
                        horse.setStyle(Horse.Style.valueOf(PDC.get(STYLE, PersistentDataType.STRING)));
            }
        }
        if (e instanceof Villager v){
            if (hasData(PDC, TYPE, PersistentDataType.STRING)) {
                loadRegistry(PDC, TYPE, Registry.VILLAGER_TYPE, v::setVillagerType);
            }
            if (hasData(PDC, PROF, PersistentDataType.STRING)) {
                loadRegistry(PDC, PROF, Registry.VILLAGER_PROFESSION, v::setProfession);
            }
        }
    }

    private <T, Z> boolean hasData(
            PersistentDataContainer pdc,
            NamespacedKey key,
            PersistentDataType<T, Z> type
    ) {
        return pdc.has(key, type) && pdc.get(key, type) != null;
    }

    private <T extends Keyed> void loadRegistry(
            PersistentDataContainer pdc,
            NamespacedKey key,
            Registry<T> registry,
            Consumer<T> setter
    ) {
        String raw = pdc.get(key, PersistentDataType.STRING);
        if (raw == null) return;

        NamespacedKey k = NamespacedKey.fromString(raw);
        if (k == null) return;

        T value = registry.get(k);
        if (value != null) setter.accept(value);
    }


    private <E extends Enum<E>> void loadVariant(
            PersistentDataContainer pdc,
            NamespacedKey key,
            Class<E> enumClass,
            java.util.function.Consumer<E> setter
    ) {
        String raw = pdc.get(key, PersistentDataType.STRING);
        if (raw == null) return;

        try {
            E value = Enum.valueOf(enumClass, raw);
            setter.accept(value);
        } catch (IllegalArgumentException ignored) {}
    }

}