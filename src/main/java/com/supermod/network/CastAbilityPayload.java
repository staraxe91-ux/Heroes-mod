package com.supermod.network;

import com.supermod.SuperMod;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

/** Client -> Server: pemain menekan keybind skill. */
public record CastAbilityPayload(int abilityId) implements CustomPayload {
    public static final Id<CastAbilityPayload> ID = new Id<>(Identifier.of(SuperMod.MOD_ID, "cast_ability"));
    public static final PacketCodec<RegistryByteBuf, CastAbilityPayload> CODEC =
            PacketCodec.tuple(PacketCodecs.VAR_INT, CastAbilityPayload::abilityId, CastAbilityPayload::new);

    @Override
    public Id<? extends CustomPayload> getId() { return ID; }
}
