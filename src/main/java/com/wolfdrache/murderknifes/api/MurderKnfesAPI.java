package com.wolfdrache.murderknifes.api;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public interface MurderKnfesAPI {
    void openKnifeSelector(Player player);

    ItemStack getKnife(Player player); 

    boolean isKnife(ItemStack item);
}
