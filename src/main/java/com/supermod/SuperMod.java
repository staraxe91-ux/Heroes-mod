package com.supermod;

import com.supermod.ability.AbilityManager;
import com.supermod.entity.ModEntities;
import com.supermod.network.ModNetworking;
import net.fabricmc.api.ModInitializer;

public class SuperMod implements ModInitializer {
    public static final String MOD_ID = "supermod";

    @Override
    public void onInitialize() {
        ModParticles.register();
        ModEntities.register();
        ModNetworking.register();
        AbilityManager.init();
    }
}
