package io.jimbonesjim.getEgged.Keys;

import org.bukkit.NamespacedKey;
import org.bukkit.plugin.java.JavaPlugin;

public final class GetEggedKeys {

    private GetEggedKeys() {}

    public static NamespacedKey GETEGGED;
    public static NamespacedKey EGGING_TOOL;
    public static NamespacedKey TOOL_DURABILITY;
    public static NamespacedKey TOOL_MODE;

    public static final class EntityKeys{
        public static NamespacedKey NAME;
        public static NamespacedKey COLOR;
        public static NamespacedKey STYLE;
        public static NamespacedKey BABY;
        public static NamespacedKey TYPE;
        public static NamespacedKey OWNER;
        public static NamespacedKey COLLAR;
        public static NamespacedKey VARIANT;
        public static NamespacedKey PATTERN;
        public static NamespacedKey VARIANT2;
        public static NamespacedKey SHEARED;
        public static NamespacedKey POWERED;
        public static NamespacedKey SIZE;
        public static NamespacedKey RIGHT_HORN;
        public static NamespacedKey LEFT_HORN;
        public static NamespacedKey CAT_TYPE;
    }

    public static final class HorseKeys {
        public static NamespacedKey JUMP;
        public static NamespacedKey SPEED;
        public static NamespacedKey STRENGTH;
        public static NamespacedKey CHESTED;
        public static NamespacedKey SADDLED;
        public static NamespacedKey ARMOR;
    }

    public static final class VillagerKeys {
        public static NamespacedKey PROF;
        public static NamespacedKey LEVEL;
    }

    public static void init(JavaPlugin plugin){
        GETEGGED = key(plugin, "getegged");
        EGGING_TOOL = key(plugin, "egging_tool");
        TOOL_DURABILITY = key(plugin, "tool_durability");
        TOOL_MODE = key(plugin, "tool_mode");

        //ENTITY
        EntityKeys.NAME = key(plugin, "name");
        EntityKeys.COLOR = key(plugin, "color");
        EntityKeys.STYLE = key(plugin, "style");
        EntityKeys.BABY = key(plugin, "baby");
        EntityKeys.TYPE = key(plugin, "type");
        EntityKeys.OWNER = key(plugin, "owner");
        EntityKeys.COLLAR = key(plugin, "collar");
        EntityKeys.VARIANT = key(plugin, "variant");
        EntityKeys.VARIANT2 = key(plugin, "variant2");
        EntityKeys.PATTERN = key(plugin, "pattern");
        EntityKeys.SHEARED = key(plugin, "sheared");
        EntityKeys.POWERED = key(plugin, "powered");
        EntityKeys.SIZE = key(plugin, "size");
        EntityKeys.RIGHT_HORN = key(plugin, "right_horn");
        EntityKeys.LEFT_HORN = key(plugin, "left_horn");
        EntityKeys.CAT_TYPE = key(plugin, "cat_type");

        //HORSE
        HorseKeys.JUMP = key(plugin, "jump");
        HorseKeys.SPEED = key(plugin, "speed");
        HorseKeys.STRENGTH = key(plugin, "strength");
        HorseKeys.CHESTED = key(plugin, "chested");
        HorseKeys.SADDLED = key(plugin, "saddled");
        HorseKeys.ARMOR = key(plugin, "armor");

        //VILLAGER
        VillagerKeys.PROF = key(plugin, "profession");
        VillagerKeys.LEVEL =  key(plugin, "level");
    }

    private static NamespacedKey key(JavaPlugin plugin, String key) {
        return new NamespacedKey(plugin, key);
    }

}
