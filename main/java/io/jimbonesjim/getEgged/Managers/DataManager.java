package io.jimbonesjim.getEgged.Managers;

import io.jimbonesjim.getEgged.API.DataLoader;
import io.jimbonesjim.getEgged.API.DataSaver;
import io.jimbonesjim.getEgged.Services.LoreBuilder;
import org.bukkit.entity.*;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.EnumMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static io.jimbonesjim.getEgged.Keys.GetEggedKeys.GETEGGED;

public class DataManager {

    private static DataSaver SAVER;
    private static DataLoader LOADER;
    private final LoreBuilder loreBuilder;

    public DataManager(LoreBuilder loreBuilder) {
        this.loreBuilder = loreBuilder;
    }

    public static void init(){
        SAVER = new DataSaver();
        LOADER = new DataLoader();
    }

    public boolean fromGetEgged(ItemMeta meta){
        return meta != null &&
                meta.getPersistentDataContainer().has(GETEGGED, PersistentDataType.BOOLEAN);
    }

    public ItemMeta entityToEgg(Entity e, ItemMeta meta){
        meta = SAVER.saveData(e, meta);
        meta.lore(loreBuilder.build(e));
        return meta;
    }

    public void eggToEntity(Entity e, ItemMeta meta){
        LOADER.loadData(meta, e);
    }

    private final Map<EntityType, AtomicInteger> eggCounts = new EnumMap<>(EntityType.class);

    public void recordEgged(EntityType type) {
        eggCounts.computeIfAbsent(type, k -> new AtomicInteger()).incrementAndGet();
    }

    public Map<String, Integer> getEggCountsSnapshot() {
        return eggCounts.entrySet().stream()
                .collect(Collectors.toMap(e -> e.getKey().name(),
                        e -> e.getValue().get()));
    }
}