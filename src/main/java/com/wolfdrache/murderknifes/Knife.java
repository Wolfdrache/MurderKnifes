package com.wolfdrache.murderknifes;

import org.bukkit.Material;

class Knife {
    public final String name;
    public final Material material;
    public final int price;
    public final int number;

    public Knife(String name, Material material, int price, int number) {
        this.name = name;
        this.material = material;
        this.price = price;
        this.number = number;
    }
}
