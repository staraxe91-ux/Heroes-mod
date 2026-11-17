package com.supermod.ability;

public enum Ability {
    //          nama          cost  cooldown(tick, 20 tick = 1 detik)
    FIRE_BURST("Fire Burst", 30, 100),
    ENERGY_BEAM("Energy Beam", 40, 160);

    public final String displayName;
    public final int cost;
    public final int cooldown;

    Ability(String displayName, int cost, int cooldown) {
        this.displayName = displayName;
        this.cost = cost;
        this.cooldown = cooldown;
    }
}
