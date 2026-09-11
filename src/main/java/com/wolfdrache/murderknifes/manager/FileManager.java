package com.wolfdrache.murderknifes.manager;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashSet;

import com.wolfdrache.murderknifes.models.Knife;
import com.wolfdrache.murderknifes.models.KnifePlayer;

public class FileManager {
    // private final MurderKnifes plugin;

    private final File knifesFile;
    private final File playerKnifesFile;

    public FileManager(JavaPlugin plugin) {
        // this.plugin = plugin;
        this.knifesFile = new File(plugin.getDataFolder(), "knifes.yml");
        this.playerKnifesFile = new File(plugin.getDataFolder(), "playerKnifes.yml");
    }

    public List<Knife> loadKnifes() {
        List<Knife> knifes = new ArrayList<>();
        YamlConfiguration config = YamlConfiguration.loadConfiguration(knifesFile);
        List<Map<?, ?>> knifeMaps = config.getMapList("knifes");
        for (Map<?, ?> knifeMap : knifeMaps) {
            String name = (String) knifeMap.get("name");
            String materialString = (String) knifeMap.get("material");
            Material material = Material.valueOf(materialString);
            int price = (int) knifeMap.get("price");
            int number = (int) knifeMap.get("number");
            Knife knife = new Knife(name, material, price, number);
            knifes.add(knife);
        }
        return knifes;
    }

    public KnifePlayer loadKnifePlayer(OfflinePlayer player, List<Knife> knifes) {
        YamlConfiguration config = YamlConfiguration.loadConfiguration(playerKnifesFile);
        String playerId = player.getUniqueId().toString();
        List<Integer> favoriteKnifeNumbers = config.getIntegerList(playerId + ".favoriteKnifes");
        Set<Knife> favoriteKnifes = new HashSet<>();
        for (Knife knife : knifes) {
            if (favoriteKnifeNumbers.contains(knife.number) || knife.price == -1) {
                favoriteKnifes.add(knife);
            }
        }
        List<Integer> boughtKnifeNumbers = config.getIntegerList(playerId + ".boughtKnifes");
        Set<Knife> boughtKnifes = new HashSet<>();
        for (int number : boughtKnifeNumbers) {
            for (Knife knife : knifes) {
                if (knife.number == number) {
                    boughtKnifes.add(knife);
                    break;
                }
            }
        }
        return new KnifePlayer(favoriteKnifes, boughtKnifes);
    }

    public void saveKnifePlayer(Player player, KnifePlayer knifePlayer) {
        YamlConfiguration config = YamlConfiguration.loadConfiguration(playerKnifesFile);
        String playerId = player.getUniqueId().toString();

        config.set(playerId + ".name", player.getName());

        List<Integer> favoriteKnifeNumbers = new ArrayList<>();
        for (Knife knife : knifePlayer.favoriteKnifes) {
            favoriteKnifeNumbers.add(knife.number);
        }
        config.set(playerId + ".favoriteKnifes", favoriteKnifeNumbers);

        List<Integer> boughtKnifeNumbers = new ArrayList<>();
        for (Knife knife : knifePlayer.boughtKnifes) {
            if (knife.price == -1) continue;
            boughtKnifeNumbers.add(knife.number);
        }
        config.set(playerId + ".boughtKnifes", boughtKnifeNumbers);

        try {
            config.save(playerKnifesFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
