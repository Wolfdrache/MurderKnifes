package com.wolfdrache.murderknifes;

import com.wolfdrache.serverapi.coins.CoinService;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;

import com.wolfdrache.murderknifes.api.MurderKnifesAPI;

public class MurderKnifes extends JavaPlugin implements MurderKnifesAPI {

    private CoinService coinService;

    private CoinManager coinManager;
    private FileManager fileManager;
    private KnifeManager knifeManager;

    private KnifeSelector knifeSelector;

    @Override
    public void onEnable(){

        coinService = setupService(CoinService.class);

        getServer().getServicesManager().register(MurderKnifesAPI.class, this, this, ServicePriority.Normal);

        fileManager = new FileManager(this);
        coinManager = new CoinManager(coinService);
        knifeManager = new KnifeManager(fileManager); 

        knifeSelector = new KnifeSelector(knifeManager, coinManager); 

        getServer().getPluginManager().registerEvents(knifeSelector, this);
        getServer().getPluginManager().registerEvents(new ProtectionListener(knifeManager), this);
        
        getLogger().info("MurderKnifes has been enabled!");
    }
    
    @Override
    public void onDisable(){
        getLogger().info("MurderKnifes has been disabled!");
    }

    private <T> T setupService(Class<T> serviceClass) {
        RegisteredServiceProvider<T> rsp =
                getServer().getServicesManager().getRegistration(serviceClass);

        if (rsp == null) {
            return null;
        }

        return rsp.getProvider();
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