package com.wolfdrache.murderknifes;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;

import com.wolfdrache.murderknifes.api.MurderKnfesAPI;
import com.wolfdrache.murderknifes.listener.ConnectionListener;
import com.wolfdrache.murderknifes.listener.ProtectionListener;
import com.wolfdrache.murderknifes.manager.CoinManager;
import com.wolfdrache.murderknifes.manager.FileManager;
import com.wolfdrache.murderknifes.manager.KnifeManager;
import com.wolfdrache.murderknifes.ui.KnifeSelector;

import net.milkbowl.vault.economy.Economy;

class MurderKnifes extends JavaPlugin implements MurderKnfesAPI {

    private Economy economy;

    private CoinManager coinManager;
    private FileManager fileManager;
    private KnifeManager knifeManager;

    private KnifeSelector knifeSelector;

    @Override
    public void onEnable(){
        if (!setupEconomy()) {
            getLogger().severe("Vault not found! Disabling plugin.");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        getServer().getServicesManager().register(MurderKnfesAPI.class, this, this, ServicePriority.Normal); 

        fileManager = new FileManager(this);
        coinManager = new CoinManager(economy);
        knifeManager = new KnifeManager(fileManager); 

        knifeSelector = new KnifeSelector(knifeManager, coinManager); 

        getServer().getPluginManager().registerEvents(knifeSelector, this);
        getServer().getPluginManager().registerEvents(new ConnectionListener(knifeManager), this);
        getServer().getPluginManager().registerEvents(new ProtectionListener(knifeManager), this);
        
        getLogger().info("MurderKnifes has been enabled!");
    }
    
    @Override
    public void onDisable(){
        getLogger().info("MurderKnifes has been disabled!");
    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }

        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }

        economy = rsp.getProvider();
        return true;
    }

    @Override 
    public void openKnifeSelector(Player player) {
        knifeSelector.open(player);
    }

    @Override
    public ItemStack getKnife(Player player) {
        return knifeManager.getKnifeItem(player);
    }

    @Override
    public boolean isKnife(ItemStack item) {
        return knifeManager.isCustomKnife(item);
    }
}