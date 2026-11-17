package com.supermod.network;

import com.supermod.ability.AbilityManager;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

public class ModNetworking {
    public static void register() {
        PayloadTypeRegistry.playC2S().register(CastAbilityPayload.ID, CastAbilityPayload.CODEC);
        PayloadTypeRegistry.playS2C().register(EnergySyncPayload.ID, EnergySyncPayload.CODEC);

        ServerPlayNetworking.registerGlobalReceiver(CastAbilityPayload.ID, (payload, context) ->
                AbilityManager.cast(context.player(), payload.abilityId()));
    }
}
