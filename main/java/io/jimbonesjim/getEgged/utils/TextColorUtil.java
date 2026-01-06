package io.jimbonesjim.getEgged.utils;

import net.kyori.adventure.text.format.NamedTextColor;

public class TextColorUtil {

    private TextColorUtil() {}

    public static NamedTextColor stringToTextColor(String color) {
        return switch (color.toUpperCase()) {
            case "WHITE" -> NamedTextColor.WHITE;
            case "ORANGE", "BROWN", "GOLDEN" -> NamedTextColor.GOLD;
            case "MAGENTA", "PINK" -> NamedTextColor.LIGHT_PURPLE;
            case "LIGHT_BLUE", "DIAMOND" -> NamedTextColor.AQUA;
            case "LIME" -> NamedTextColor.GREEN;
            case "GRAY", "BLACK", "NETHERITE" -> NamedTextColor.DARK_GRAY;
            case "LIGHT_GRAY", "IRON" -> NamedTextColor.GRAY;
            case "CYAN" -> NamedTextColor.DARK_AQUA;
            case "PURPLE" -> NamedTextColor.DARK_PURPLE;
            case "BLUE" -> NamedTextColor.BLUE;
            case "GREEN" -> NamedTextColor.DARK_GREEN;
            case "RED", "COPPER", "LEATHER" -> NamedTextColor.RED;
            default -> NamedTextColor.YELLOW;
        };
    }
}
