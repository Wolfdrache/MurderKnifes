package com.wolfdrache.murderknifes;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

class MessageHelper {
    public static Component createComponent(String text) {
        return LegacyComponentSerializer.legacySection().deserialize(text).decoration(TextDecoration.ITALIC, false);
    }
}
