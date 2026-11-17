package com.supermod.client;

import com.supermod.entity.BeamEntity;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class BeamRenderer extends GeoEntityRenderer<BeamEntity> {
    public BeamRenderer(EntityRendererFactory.Context ctx) {
        super(ctx, new BeamModel());
    }

    // render glowing (tidak kena gelap/terang dunia)
    @Override
    public RenderLayer getRenderType(BeamEntity animatable, Identifier texture, VertexConsumerProvider bufferSource, float partialTick) {
        return RenderLayer.getEyes(texture);
    }
}
