package com.wolfdrache.murderknifes;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

class ItemHelper {

    public static ItemStack createFillerItem(Material material) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.displayName(MessageHelper.createComponent(" "));
            item.setItemMeta(meta);
        }
        return item;
    }

    public static ItemStack createNextArrow(boolean next) {
        ItemStack item = new ItemStack(Material.ARROW);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            if (next) {
                meta.displayName(MessageHelper.createComponent("§aNexte Seite"));
            } else {
                meta.displayName(MessageHelper.createComponent("§aVorherige Seite"));
            }
            item.setItemMeta(meta);
        }
        return item;
    }
}
