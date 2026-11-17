package com.supermod.network;

import com.supermod.SuperMod;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

/** Server -> Client: sinkron energy buat HUD. */
public record EnergySyncPayload(int energy, int max) implements CustomPayload {
    public static final Id<EnergySyncPayload> ID = new Id<>(Identifier.of(SuperMod.MOD_ID, "energy_sync"));
    public static final PacketCodec<RegistryByteBuf, EnergySyncPayload> CODEC = PacketCodec.tuple(
            PacketCodecs.VAR_INT, EnergySyncPayload::energy,
            PacketCodecs.VAR_INT, EnergySyncPayload::max,
            EnergySyncPayload::new);

    @Override
    public Id<? extends CustomPayload> getId() { return ID; }
}
