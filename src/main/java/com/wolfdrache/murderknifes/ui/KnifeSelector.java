package com.wolfdrache.murderknifes.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.wolfdrache.murderknifes.helper.MessageHelper;
import com.wolfdrache.murderknifes.manager.CoinManager;
import com.wolfdrache.murderknifes.manager.KnifeManager;
import com.wolfdrache.murderknifes.models.Knife;
import com.wolfdrache.murderknifes.models.KnifePlayer;

import net.kyori.adventure.text.Component;

public class KnifeSelector implements Listener {
    private final KnifeManager knifeManager;
    private final CoinManager coinManager;

    private final MessageHelper messageHelper = new MessageHelper();

    private final Map<Player, Inventory> playerInventories = new HashMap<>();
    
    public KnifeSelector(KnifeManager knifeManager, CoinManager coinManager) {
        this.knifeManager = knifeManager;
        this.coinManager = coinManager;
    }

    public void open(Player player) {
        KnifePlayer knifePlayer = knifeManager.getKnifePlayer(player);
        List<Knife> knifes = knifeManager.knifes;
        int size = calculateSize(knifes);
        Inventory gui = Bukkit.createInventory(null, size, messageHelper.createComponent("§4Messer Auswahl"));
        for (Knife knife : knifes) {
            ItemStack item = knifeManager.createKnifeItem(knife);
            List<Component> lore = new ArrayList<>();
            if (knifePlayer.favoriteKnifes.contains(knife)) {
                lore.add(messageHelper.createComponent("§aFavorit"));
            } else if (knifePlayer.boughtKnifes.contains(knife)) {
                lore.add(messageHelper.createComponent("§eGekauft"));
            } else {
                lore.add(messageHelper.createComponent("§cPreis: " + knife.price + " §6Coins"));
            }
            ItemMeta meta = item.getItemMeta();
            meta.lore(lore);
            item.setItemMeta(meta);
            gui.setItem(knifes.indexOf(knife), item);
        }
        player.openInventory(gui);
        playerInventories.put(player, gui);
    }

    private int calculateSize(List<?> list) {
        int size = 0;
        while (size < list.size()) {
            size += 9;
        }
        return size;
    }

    @EventHandler 
    public void onInventoryClose(InventoryCloseEvent event) {
        Player player = (Player) event.getPlayer();
        Inventory closedInventory = event.getInventory();
        if (playerInventories.containsKey(player) && playerInventories.get(player).equals(closedInventory)) {
            playerInventories.remove(player);
        }
    }
}
