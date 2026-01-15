package io.jimbonesjim.getEgged.utils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;

public class MiniMessageUtil {

    private static final MiniMessage MM =  MiniMessage.miniMessage();

    private MiniMessageUtil() {}

    public static Component createMessage(String message, TagResolver... resolvers) {
        return MM.deserialize(message, resolvers);
    }
}
