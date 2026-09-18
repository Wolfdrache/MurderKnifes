package com.wolfdrache.murderknifes;

import org.bukkit.entity.Player;

import net.milkbowl.vault.economy.Economy;

class CoinManager {
    private final Economy economy;

    public CoinManager(Economy economy) {
        this.economy = economy;
    }

    public boolean tryBuyKnife(Player player, Knife clickedKnife) {
        if (economy.getBalance(player) >= clickedKnife.price) {
            economy.withdrawPlayer(player, clickedKnife.price);
            return true;
        }
        return false;
    }
}
