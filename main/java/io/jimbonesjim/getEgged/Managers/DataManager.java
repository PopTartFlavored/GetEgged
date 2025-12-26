package io.jimbonesjim.getEgged.Managers;

import io.jimbonesjim.getEgged.API.DataLoader;
import io.jimbonesjim.getEgged.API.DataSaver;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.DyeColor;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.*;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public class DataManager {

    public static NamespacedKey GETEGGED;
    public static NamespacedKey NAME;
    public static NamespacedKey JUMP;
    public static NamespacedKey SPEED;
    public static NamespacedKey COLOR;
    public static NamespacedKey STYLE;
    public static NamespacedKey BABY;
    public static NamespacedKey PROF;
    public static NamespacedKey TYPE;
    public static NamespacedKey LEVEL;
    public static NamespacedKey OWNER;
    public static NamespacedKey COLLAR;
    public static NamespacedKey VARIANT;
    public static NamespacedKey PATTERN;
    public static NamespacedKey VARIANT2;
    private static DataSaver SAVER;
    private static DataLoader LOADER;

    public static void init(JavaPlugin plugin){
        GETEGGED = new NamespacedKey(plugin, "getegged");
        NAME = new NamespacedKey(plugin, "name");
        JUMP = new NamespacedKey(plugin, "jump");
        SPEED = new NamespacedKey(plugin, "speed");
        COLOR = new NamespacedKey(plugin, "color");
        STYLE = new NamespacedKey(plugin, "style");
        BABY = new NamespacedKey(plugin, "baby");
        PROF = new NamespacedKey(plugin, "profession");
        TYPE = new NamespacedKey(plugin, "type");
        LEVEL = new NamespacedKey(plugin, "level");
        OWNER = new NamespacedKey(plugin, "owner");
        COLLAR = new NamespacedKey(plugin, "collar");
        VARIANT = new NamespacedKey(plugin, "variant");
        VARIANT2 = new NamespacedKey(plugin, "variant2");
        PATTERN = new NamespacedKey(plugin, "pattern");
        SAVER = new DataSaver();
        LOADER = new DataLoader();
    }

    public boolean fromGetEgged(ItemMeta meta){
        PersistentDataContainer PDC = meta.getPersistentDataContainer();
        return PDC.has(GETEGGED, PersistentDataType.BOOLEAN);
    }

    public ItemMeta entityToEgg(Entity e, ItemMeta meta){
        meta = SAVER.saveData(e, meta);
        meta.lore(setLore(e));
        return meta;
    }

    public void eggToEntity(Entity e, ItemMeta meta){
        LOADER.loadData(meta, e);
    }

    public List<Component> setLore(Entity e){
        List<Component> lore = new ArrayList<>();

        lore.add(Component.text("GetEgged Data").color(NamedTextColor.GRAY));
        lore.add(Component.text("────────────").color(NamedTextColor.DARK_GRAY));

        if (e.customName() != null) {
            lore.add(Component.text("Name: ").color(NamedTextColor.WHITE)
                    .append(Component.text(e.getName()).color(NamedTextColor.YELLOW)));
        }

        if (e instanceof Ageable ae) {
            lore.add(Component.text("Baby: ").color(NamedTextColor.WHITE)
                    .append(Component.text(!ae.isAdult() ? "Yes" : "No")
                            .color(!ae.isAdult() ? NamedTextColor.GREEN : NamedTextColor.RED)));
        }

        if (e instanceof Tameable te) {
            if (te.getOwner() != null) {
                lore.add(Component.text("Owner: ").color(NamedTextColor.WHITE)
                        .append(Component.text(te.getOwner().getName()).color(NamedTextColor.YELLOW)));
            }
        }

        if (e instanceof Sheep sheep){
            lore.add(Component.text("Color: ").color(NamedTextColor.WHITE)
                    .append(Component.text(String.valueOf(sheep.getColor())).color(dyeToTextColor(sheep.getColor()))));
        }

        if (e instanceof Fox fox){
            lore.add(Component.text("Color: ").color(NamedTextColor.WHITE)
                    .append(Component.text(fox.getFoxType().name()).color(NamedTextColor.YELLOW)));
        }

        if (e instanceof Axolotl axolotl){
            lore.add(Component.text("Variant: ").color(NamedTextColor.WHITE)
                    .append(Component.text(axolotl.getVariant().name()).color(NamedTextColor.YELLOW)));
        }

        if (e instanceof Frog frog){
            lore.add(Component.text("Variant: ").color(NamedTextColor.WHITE)
                    .append(Component.text(frog.getVariant().toString()).color(NamedTextColor.YELLOW)));
        }

        if (e instanceof Cat cat){
            lore.add(Component.text("Type: ").color(NamedTextColor.WHITE)
                    .append(Component.text(cat.getType().name()).color(NamedTextColor.YELLOW)));
        }

        if (e instanceof Panda panda){
            lore.add(Component.text("Main Gene: ").color(NamedTextColor.WHITE)
                    .append(Component.text(panda.getMainGene().name()).color(NamedTextColor.YELLOW)));
            lore.add(Component.text("Hidden Gene: ").color(NamedTextColor.WHITE)
                    .append(Component.text(panda.getHiddenGene().name()).color(NamedTextColor.YELLOW)));
        }

        if (e instanceof AbstractHorse ah){
            lore.add(Component.text("Jump: ").color(NamedTextColor.WHITE)
                    .append(Component.text(ah.getJumpStrength()).color(NamedTextColor.YELLOW)));
            lore.add(Component.text("Speed: ").color(NamedTextColor.WHITE)
                    .append(Component.text(ah.getAttribute(Attribute.MOVEMENT_SPEED).getBaseValue()).color(NamedTextColor.YELLOW)));
        }

        if (e instanceof Horse horse) {
            lore.add(Component.text("Color: ").color(NamedTextColor.WHITE)
                    .append(Component.text(horse.getColor().toString()).color(NamedTextColor.YELLOW)));
            lore.add(Component.text("Style: ").color(NamedTextColor.WHITE)
                    .append(Component.text(horse.getStyle().toString()).color(NamedTextColor.YELLOW)));
        }

        return lore;
    }

    private NamedTextColor dyeToTextColor(DyeColor dye) {
        return switch (dye) {
            case WHITE -> NamedTextColor.WHITE;
            case ORANGE, BROWN -> NamedTextColor.GOLD;
            case MAGENTA, PINK -> NamedTextColor.LIGHT_PURPLE;
            case LIGHT_BLUE -> NamedTextColor.AQUA;
            case YELLOW -> NamedTextColor.YELLOW;
            case LIME -> NamedTextColor.GREEN;
            case GRAY, BLACK -> NamedTextColor.DARK_GRAY;
            case LIGHT_GRAY -> NamedTextColor.GRAY;
            case CYAN -> NamedTextColor.DARK_AQUA;
            case PURPLE -> NamedTextColor.DARK_PURPLE;
            case BLUE -> NamedTextColor.BLUE;
            case GREEN -> NamedTextColor.DARK_GREEN;
            case RED -> NamedTextColor.RED;
        };
    }
}