package com.supermod.client;

import com.supermod.ModParticles;
import com.supermod.ability.Ability;
import com.supermod.entity.ModEntities;
import com.supermod.network.CastAbilityPayload;
import com.supermod.network.EnergySyncPayload;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.particle.FlameParticle;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class SuperModClient implements ClientModInitializer {
    private static final String CATEGORY = "key.category.supermod";

    // urutan array = urutan enum Ability
    private static final KeyBinding[] KEYS = new KeyBinding[Ability.values().length];

    private static int energy = 100;
    private static int maxEnergy = 100;

    @Override
    public void onInitializeClient() {
        // ganti key di sini
        KEYS[Ability.FIRE_BURST.ordinal()] = bind("key.supermod.fire_burst", GLFW.GLFW_KEY_R);
        KEYS[Ability.ENERGY_BEAM.ordinal()] = bind("key.supermod.energy_beam", GLFW.GLFW_KEY_Z);

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player == null) return;
            for (int i = 0; i < KEYS.length; i++) {
                while (KEYS[i].wasPressed()) {
                    ClientPlayNetworking.send(new CastAbilityPayload(i));
                }
            }
        });

        ClientPlayNetworking.registerGlobalReceiver(EnergySyncPayload.ID, (payload, context) -> {
            energy = payload.energy();
            maxEnergy = payload.max();
        });

        // particle custom (tekstur: textures/particle/fire_spark.png)
        ParticleFactoryRegistry.getInstance().register(ModParticles.FIRE_SPARK, FlameParticle.Factory::new);

        // renderer 3D model GeckoLib
        EntityRendererRegistry.register(ModEntities.BEAM, BeamRenderer::new);

        // HUD energy bar
        HudRenderCallback.EVENT.register((ctx, tickCounter) -> {
            MinecraftClient mc = MinecraftClient.getInstance();
            if (mc.player == null || mc.options.hudHidden) return;
            int w = 80, h = 6;
            int x = ctx.getScaledWindowWidth() / 2 - 91;
            int y = ctx.getScaledWindowHeight() - 56;
            int filled = (int) (w * (energy / (float) Math.max(1, maxEnergy)));
            ctx.fill(x - 1, y - 1, x + w + 1, y + h + 1, 0xAA000000);
            ctx.fill(x, y, x + filled, y + h, 0xFF30C8FF);
        });
    }

    private static KeyBinding bind(String name, int key) {
        return KeyBindingHelper.registerKeyBinding(new KeyBinding(name, InputUtil.Type.KEYSYM, key, CATEGORY));
    }
}
