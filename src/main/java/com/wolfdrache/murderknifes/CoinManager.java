package com.wolfdrache.murderknifes;

import com.wolfdrache.serverapi.coins.CoinService;
import org.bukkit.entity.Player;

class CoinManager {
    private final CoinService coinService;

    public CoinManager(CoinService coinService) {
        this.coinService = coinService;
    }

    public boolean tryBuyKnife(Player player, Knife clickedKnife) {
        if (coinService.hasEnoughCoins(player, clickedKnife.price)) {
            return coinService.spentCoins(player, clickedKnife.price);
        }
        return false;
    }
}
