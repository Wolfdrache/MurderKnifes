package com.wolfdrache.murderknifes.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.wolfdrache.murderknifes.helper.ItemHelper;
import com.wolfdrache.murderknifes.helper.MessageHelper;
import com.wolfdrache.murderknifes.manager.CoinManager;
import com.wolfdrache.murderknifes.manager.KnifeManager;
import com.wolfdrache.murderknifes.models.Knife;
import com.wolfdrache.murderknifes.models.KnifePlayer;

import net.kyori.adventure.text.Component;

public class KnifeSelector implements Listener {
    private final String allKnifesPermission = "murderknifes.allKnifes";

    private static class Data {
        public int page;
        public Inventory inventory;
        public Map<Integer, Knife> knifeMap;
        public Data(int page, Inventory inventory, Map<Integer, Knife> knifeMap) {
            this.page = page;
            this.inventory = inventory;
            this.knifeMap = knifeMap;
        }
    }

    private final KnifeManager knifeManager;
    private final CoinManager coinManager;

    private final MessageHelper messageHelper = new MessageHelper();
    private final ItemHelper itemHelper = new ItemHelper();

    private final Map<Player, Data> playerDataMap = new HashMap<>();

    private final List<Integer> freeSlots = new ArrayList<>(List.of(
        10, 11, 12, 13, 14, 15, 16,
        19, 20, 21, 22, 23, 24, 25,
        28, 29, 30, 31, 32, 33, 34,
        37, 38, 39, 40, 41, 42, 43)
    );
    private final ItemStack nextArrow = itemHelper.createNextArrow(true);
    private final ItemStack previousArrow = itemHelper.createNextArrow(false);

    private final int maxPages;

    public KnifeSelector(KnifeManager knifeManager, CoinManager coinManager) {
        this.knifeManager = knifeManager;
        this.coinManager = coinManager;
        maxPages = calculateMaxPages();
    }

    private int calculateMaxPages() {
        int maxPages = knifeManager.knifes.size() / freeSlots.size();
        if (knifeManager.knifes.size() % freeSlots.size() == 0) {
            maxPages--;
        }
        return maxPages;
    }

    public void open(Player player) {
        open(player, 0);
    }

    private void open(Player player, int page) {
        Inventory gui = Bukkit.createInventory(null, 54, messageHelper.createComponent("§cMesser Auswahl: " + (page + 1)));
        for (int i = 0; i < gui.getSize(); i++) {
            if (!freeSlots.contains(i)) {
                gui.setItem(i, itemHelper.createFillerItem(Material.RED_STAINED_GLASS_PANE));
            }
        }

        if (page > 0) {
            gui.setItem(45, previousArrow);
        }
        if (page < maxPages) {
            gui.setItem(53, nextArrow);
        }

        KnifePlayer knifePlayer = knifeManager.getKnifePlayer(player);
        List<Knife> knifes;
        int firstKnife = page * freeSlots.size();
        int lastKnife = Math.min(firstKnife + freeSlots.size(), knifeManager.knifes.size());
        knifes = knifeManager.knifes.subList(firstKnife, lastKnife);
        Map<Integer, Knife> knifeMap = new HashMap<>();
        for (int i = 0; i < knifes.size(); i++) {
            Knife knife = knifes.get(i);
            ItemStack item = knifeManager.createKnifeItem(knife);
            List<Component> lore = new ArrayList<>();
            if (knifePlayer.favoriteKnifes.contains(knife)) {
                lore.add(messageHelper.createComponent("§aFavorit"));
            } else if (knifePlayer.boughtKnifes.contains(knife) || player.hasPermission(allKnifesPermission)) {
                lore.add(messageHelper.createComponent("§eGekauft"));
            } else {
                lore.add(messageHelper.createComponent("§cPreis: " + knife.price + " §6Coins"));
            }
            ItemMeta meta = item.getItemMeta();
            meta.lore(lore);
            item.setItemMeta(meta);
            gui.setItem(freeSlots.get(i), item);
            knifeMap.put(freeSlots.get(i), knife);
        }
        player.openInventory(gui);
        playerDataMap.put(player, new Data(page, gui, knifeMap));
    }

    @EventHandler 
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        if (!playerDataMap.containsKey(player)) return;
        Data data = playerDataMap.get(player);
        if (!event.getView().getTopInventory().equals(data.inventory)) return;
        event.setCancelled(true);
        ItemStack clickedItem = event.getCurrentItem();
        if (clickedItem == null) return;
        if (clickedItem.equals(previousArrow)) {
            open(player, data.page - 1);
            return;
        } else if (clickedItem.equals(nextArrow)) {
            open(player, data.page + 1);
            return;
        }
        Knife clickedKnife = data.knifeMap.get(event.getSlot());
        if (clickedKnife == null) return;
        KnifePlayer knifePlayer = knifeManager.getKnifePlayer(player);
        if (knifePlayer.favoriteKnifes.contains(clickedKnife)) {
            knifePlayer.favoriteKnifes.remove(clickedKnife);
        } else if (knifePlayer.boughtKnifes.contains(clickedKnife) || player.hasPermission(allKnifesPermission)) {
            knifePlayer.favoriteKnifes.add(clickedKnife);
        } else {
            if (coinManager.tryBuyKnife(player, clickedKnife)) {
                knifePlayer.boughtKnifes.add(clickedKnife);
                knifePlayer.favoriteKnifes.add(clickedKnife);
            }
        }
        open(player, data.page);
    }

    @EventHandler 
    public void onInventoryClose(InventoryCloseEvent event) {
        Player player = (Player) event.getPlayer();
        if (!playerDataMap.containsKey(player)) return;
        if (event.getView().getTopInventory().equals(playerDataMap.get(player).inventory)) {
            playerDataMap.remove(player);
        }
    }
}
