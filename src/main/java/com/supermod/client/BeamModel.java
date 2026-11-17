package com.supermod.client;

import com.supermod.SuperMod;
import com.supermod.entity.BeamEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;

public class BeamModel extends DefaultedEntityGeoModel<BeamEntity> {
    public BeamModel() {
        // geo/entity/energy_beam.geo.json + textures/entity/energy_beam.png
        super(Identifier.of(SuperMod.MOD_ID, "energy_beam"));
    }

    @Override
    public void setCustomAnimations(BeamEntity entity, long instanceId, AnimationState<BeamEntity> state) {
        super.setCustomAnimations(entity, instanceId, state);
        // arahkan beam naik/turun sesuai pitch pemain
        GeoBone bone = (GeoBone) getAnimationProcessor().getBone("beam");
        if (bone != null) bone.setRotX(entity.getPitch() * MathHelper.RADIANS_PER_DEGREE);
    }
}
