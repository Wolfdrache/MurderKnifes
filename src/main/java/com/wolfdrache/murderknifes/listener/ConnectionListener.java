package com.wolfdrache.murderknifes.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import com.wolfdrache.murderknifes.manager.KnifeManager;

public class ConnectionListener implements Listener {
    private final KnifeManager knifeManager;

    public ConnectionListener(KnifeManager knifeManager) {
        this.knifeManager = knifeManager;
    }

    @EventHandler 
    public void onPlayerJoin(PlayerJoinEvent event) {
        knifeManager.loadKnifePlayer(event.getPlayer());
    }

    @EventHandler 
    public void onPlayerQuit(PlayerQuitEvent event) {
        knifeManager.saveKnifePlayer(event.getPlayer());
    }
}
