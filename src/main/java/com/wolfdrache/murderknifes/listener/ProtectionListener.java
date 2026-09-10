package com.wolfdrache.murderknifes.listener;

import org.bukkit.entity.AbstractArrow;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.inventory.ItemStack;

import com.wolfdrache.murderknifes.manager.KnifeManager;

public class ProtectionListener implements Listener {
    private final KnifeManager knifeManager;

    public ProtectionListener(KnifeManager knifeManager) {
        this.knifeManager = knifeManager;
    }

    @EventHandler (priority = EventPriority.HIGHEST)
    public void onProjectileFired(ProjectileLaunchEvent event) {
        if (!(event.getEntity() instanceof AbstractArrow projectile)) return;
        ItemStack item = projectile.getItemStack();
        if (knifeManager.isCustomKnife(item)) {
            event.setCancelled(true);
        }
    }

    @EventHandler (priority = EventPriority.HIGHEST)
    public void onTryPlaceBlockKnife(BlockPlaceEvent event) {
        ItemStack item = event.getItemInHand();
        if (knifeManager.isCustomKnife(item)) {
            event.setCancelled(true);
        }
    }
}
