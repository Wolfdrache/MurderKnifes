package com.wolfdrache.murderknifes.models;

import org.bukkit.Material;

public class Knife {
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
