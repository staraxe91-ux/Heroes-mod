package com.supermod.entity;

import com.supermod.SuperMod;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModEntities {
    public static final EntityType<BeamEntity> BEAM = Registry.register(
            Registries.ENTITY_TYPE,
            Identifier.of(SuperMod.MOD_ID, "energy_beam"),
            EntityType.Builder.create(BeamEntity::new, SpawnGroup.MISC)
                    .dimensions(0.5f, 0.5f)
                    .maxTrackingRange(16)
                    .trackingTickInterval(1)
                    .disableSaving()
                    .disableSummon()
                    .build("energy_beam"));

    public static void register() { /* memicu static init */ }
}
