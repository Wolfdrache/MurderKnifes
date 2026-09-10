package com.wolfdrache.murderknifes.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.NamespacedKey;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import com.wolfdrache.murderknifes.helper.MessageHelper;
import com.wolfdrache.murderknifes.models.Knife;
import com.wolfdrache.murderknifes.models.KnifePlayer;

public class KnifeManager {
    private final FileManager fileManager;
    private final MessageHelper messageHelper = new MessageHelper();

    private final NamespacedKey knifeKey = new NamespacedKey("murderknifes", "custom_knife");
    
    public final List<Knife> knifes = new ArrayList<>();

    private final Map<Player, KnifePlayer> knifePlayers = new HashMap<>();

    public KnifeManager(FileManager fileManager) {
        this.fileManager = fileManager;
        this.knifes.addAll(fileManager.loadKnifes());
    }

    public void loadKnifePlayer(Player player) {
        KnifePlayer knifePlayer = fileManager.loadKnifePlayer(player, knifes);
        knifePlayers.put(player, knifePlayer);
    }

    public KnifePlayer getKnifePlayer(OfflinePlayer player) {
        if (knifePlayers.containsKey(player)) {
            return knifePlayers.get(player);
        }
        return fileManager.loadKnifePlayer(player, knifes);
    }

    public void saveKnifePlayer(Player player) {
        if (knifePlayers.containsKey(player)) {
            KnifePlayer knifePlayer = knifePlayers.get(player);
            fileManager.saveKnifePlayer(player, knifePlayer);
            knifePlayers.remove(player);
        }
    }

    public ItemStack getKnifeItem(Player player) {
        KnifePlayer knifePlayer = getKnifePlayer(player);
        if (knifePlayer == null) return null;
        Knife knife = knifePlayer.getKnife();
        if (knife == null) return null;
        return createKnifeItem(knife);
    }

    public ItemStack createKnifeItem(Knife knife) {
        ItemStack item = new ItemStack(knife.material);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.displayName(messageHelper.createComponent(knife.name));
            meta.getPersistentDataContainer().set(knifeKey, PersistentDataType.BYTE, (byte) 1);
            if (meta instanceof Damageable) {
                meta.setUnbreakable(true);
            }
            item.setItemMeta(meta);
        }
        return item;
    }

    public boolean isCustomKnife(ItemStack item) {
        if (item == null) return false;
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return false;
        return meta.getPersistentDataContainer().has(knifeKey, PersistentDataType.BYTE);
    }
}
