package com.wolfdrache.murderknifes;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

class KnifePlayer {
    public final Set<Knife> favoriteKnifes = new HashSet<>();
    public final Set<Knife> boughtKnifes = new HashSet<>();

    public KnifePlayer(Set<Knife> favoriteKnifes, Set<Knife> boughtKnifes) {
        this.favoriteKnifes.addAll(favoriteKnifes);
        this.boughtKnifes.addAll(boughtKnifes);
    }

    public Knife getKnife() {
        if (!favoriteKnifes.isEmpty()) {
            List<Knife> favoriteList = new ArrayList<>(favoriteKnifes);
            int index = ThreadLocalRandom.current().nextInt(favoriteList.size());
            return favoriteList.get(index);
        }
        return null;
    }
}
