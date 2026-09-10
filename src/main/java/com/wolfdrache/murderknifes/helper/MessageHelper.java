package com.wolfdrache.murderknifes.helper;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

public class MessageHelper {
    public Component createComponent(String text) {
        return LegacyComponentSerializer.legacySection().deserialize(text);
    }
}
