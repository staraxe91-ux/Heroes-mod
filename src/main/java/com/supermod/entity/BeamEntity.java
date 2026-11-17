package com.supermod.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.World;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;

/** Entity visual beam. Panjang model = 192 unit = 12 blok (lihat energy_beam.geo.json). */
public class BeamEntity extends Entity implements GeoEntity {
    public static final double LENGTH = 12.0;
    private static final int LIFETIME = 8; // tick

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    private int life = 0;

    public BeamEntity(EntityType<? extends BeamEntity> type, World world) {
        super(type, world);
        this.noClip = true;
        this.setNoGravity(true);
    }

    @Override
    public void tick() {
        super.tick();
        if (++life >= LIFETIME && !getWorld().isClient) discard();
    }

    @Override protected void initDataTracker(DataTracker.Builder builder) {}
    @Override protected void readCustomDataFromNbt(NbtCompound nbt) {}
    @Override protected void writeCustomDataToNbt(NbtCompound nbt) {}

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        // belum ada animasi; tambah AnimationController di sini kalau sudah bikin .animation.json
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() { return cache; }
}
