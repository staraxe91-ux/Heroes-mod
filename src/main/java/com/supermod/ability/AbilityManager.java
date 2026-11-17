package com.supermod.ability;

import com.supermod.ModParticles;
import com.supermod.entity.BeamEntity;
import com.supermod.entity.ModEntities;
import com.supermod.network.EnergySyncPayload;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.LivingEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class AbilityManager {
    public static final int MAX_ENERGY = 100;
    private static final int REGEN_EVERY_TICKS = 4; // +1 energy tiap 4 tick (~5/detik)

    private static class PlayerData {
        int energy = MAX_ENERGY;
        final long[] cooldownEnd = new long[Ability.values().length];
    }

    private static final Map<UUID, PlayerData> DATA = new HashMap<>();

    public static void init() {
        ServerTickEvents.END_SERVER_TICK.register(server -> {
            if (server.getTicks() % REGEN_EVERY_TICKS != 0) return;
            for (ServerPlayerEntity p : server.getPlayerManager().getPlayerList()) {
                PlayerData d = data(p);
                if (d.energy < MAX_ENERGY) {
                    d.energy++;
                    sync(p, d);
                }
            }
        });
        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> sync(handler.player, data(handler.player)));
        ServerPlayConnectionEvents.DISCONNECT.register((handler, server) -> DATA.remove(handler.player.getUuid()));
    }

    private static PlayerData data(ServerPlayerEntity p) {
        return DATA.computeIfAbsent(p.getUuid(), k -> new PlayerData());
    }

    private static void sync(ServerPlayerEntity p, PlayerData d) {
        ServerPlayNetworking.send(p, new EnergySyncPayload(d.energy, MAX_ENERGY));
    }

    public static void cast(ServerPlayerEntity player, int id) {
        Ability[] all = Ability.values();
        if (id < 0 || id >= all.length) return; // jangan percaya client
        Ability ability = all[id];
        PlayerData d = data(player);
        long now = player.getWorld().getTime();

        if (now < d.cooldownEnd[id]) {
            long left = (d.cooldownEnd[id] - now + 19) / 20;
            player.sendMessage(Text.literal(ability.displayName + " cooldown " + left + "s"), true);
            return;
        }
        if (d.energy < ability.cost) {
            player.sendMessage(Text.literal("Energy kurang!"), true);
            return;
        }

        switch (ability) {
            case FIRE_BURST -> fireBurst(player);
            case ENERGY_BEAM -> energyBeam(player);
        }

        d.energy -= ability.cost;
        d.cooldownEnd[id] = now + ability.cooldown;
        sync(player, d);
    }

    // ------------------------------------------------------------------ SKILL 1
    private static void fireBurst(ServerPlayerEntity player) {
        ServerWorld world = (ServerWorld) player.getWorld();
        Vec3d c = player.getPos().add(0, 0.3, 0);

        world.playSound(null, player.getBlockPos(), SoundEvents.ENTITY_BLAZE_SHOOT, SoundCategory.PLAYERS, 1.5f, 0.8f);

        // 3 cincin particle api yang melebar keluar
        for (int ring = 0; ring < 3; ring++) {
            int points = 48;
            for (int i = 0; i < points; i++) {
                double a = i * (Math.PI * 2 / points);
                // count = 0 -> delta dipakai sebagai arah gerak particle
                world.spawnParticles(ModParticles.FIRE_SPARK,
                        c.x, c.y + ring * 0.4, c.z,
                        0, Math.cos(a), 0.05, Math.sin(a), 0.25 + ring * 0.15);
            }
        }

        double radius = 5.0;
        for (LivingEntity e : world.getEntitiesByClass(LivingEntity.class,
                player.getBoundingBox().expand(radius), t -> t != player && t.isAlive())) {
            if (e.squaredDistanceTo(player) > radius * radius) continue;
            e.damage(world.getDamageSources().playerAttack(player), 8.0f);
            e.setOnFireFor(5.0f);
            Vec3d dir = e.getPos().subtract(player.getPos()).multiply(1, 0, 1).normalize();
            e.addVelocity(dir.x * 0.8, 0.4, dir.z * 0.8);
            e.velocityModified = true;
        }
    }

    // ------------------------------------------------------------------ SKILL 2
    private static void energyBeam(ServerPlayerEntity player) {
        ServerWorld world = (ServerWorld) player.getWorld();
        Vec3d eye = player.getEyePos();
        Vec3d look = player.getRotationVec(1.0f);
        double range = BeamEntity.LENGTH;

        world.playSound(null, player.getBlockPos(), SoundEvents.ENTITY_GUARDIAN_ATTACK, SoundCategory.PLAYERS, 1.5f, 1.4f);

        // spawn model 3D beam (di-render GeckoLib di client)
        BeamEntity beam = new BeamEntity(ModEntities.BEAM, world);
        Vec3d start = eye.add(look.multiply(0.6)).add(0, -0.25, 0);
        beam.refreshPositionAndAngles(start.x, start.y, start.z, player.getYaw(), player.getPitch());
        world.spawnEntity(beam);

        // damage sepanjang garis, berhenti kalau kena blok padat
        Set<LivingEntity> hit = new HashSet<>();
        for (double dist = 1.0; dist <= range; dist += 0.5) {
            Vec3d p = eye.add(look.multiply(dist));
            BlockPos bp = BlockPos.ofFloored(p);
            if (world.getBlockState(bp).isSolidBlock(world, bp)) break;

            world.spawnParticles(ModParticles.FIRE_SPARK, p.x, p.y, p.z, 1, 0.05, 0.05, 0.05, 0.01);

            for (LivingEntity e : world.getEntitiesByClass(LivingEntity.class,
                    Box.of(p, 1.2, 1.2, 1.2), t -> t != player && t.isAlive())) {
                if (hit.add(e)) {
                    e.damage(world.getDamageSources().playerAttack(player), 10.0f);
                }
            }
        }
    }
}
