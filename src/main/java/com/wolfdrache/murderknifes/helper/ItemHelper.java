package com.wolfdrache.murderknifes.helper;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ItemHelper {
    private final MessageHelper messageHelper = new MessageHelper();

    public ItemStack createFillerItem(Material material) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.displayName(messageHelper.createComponent(" "));
            item.setItemMeta(meta);
        }
        return item;
    }

    public ItemStack createNextArrow(boolean next) {
        ItemStack item = new ItemStack(Material.ARROW);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            if (next) {
                meta.displayName(messageHelper.createComponent("§aNexte Seite"));
            } else {
                meta.displayName(messageHelper.createComponent("§aVorherige Seite"));
            }
            item.setItemMeta(meta);
        }
        return item;
    }
}
