package io.github.westbot.craftsaber.client.render;

import io.github.westbot.craftsaber.client.CraftSaberClient;
import io.github.westbot.craftsaber.entities.LightDisplayEntity;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;
import java.util.List;

public class LightDisplayEntityRenderer extends EntityRenderer<LightDisplayEntity> {
    public LightDisplayEntityRenderer(EntityRendererFactory.Context ctx) {
        super(ctx);
    }


    @Override
    public void render(
        LightDisplayEntity entity, float yaw, float tickDelta, MatrixStack matrices,
        VertexConsumerProvider vertexConsumers, int light
    ) {

        BlockRenderManager renderer = MinecraftClient.getInstance().getBlockRenderManager();

        List<Pair<Vec3d, BlockState>> pairs = CraftSaberClient.structure_cache.getOrDefault(entity.getStructureId(), new ArrayList<>());

        for (Pair<Vec3d, BlockState> pair : pairs) {
            Vec3d offset = pair.getLeft();
            BlockState state = pair.getRight();

            matrices.push();
            // structure relative translations
            matrices.translate(offset.x, offset.y, offset.z);

            // structure rotations
            matrices.multiply(RotationAxis.POSITIVE_X.rotation((float) entity.rotation.x));
            matrices.multiply(RotationAxis.POSITIVE_Y.rotation((float) entity.rotation.y));
            matrices.multiply(RotationAxis.POSITIVE_Z.rotation((float) entity.rotation.z));

            // entity alignment
            matrices.translate(-0.5, -0.5, -0.5);
            renderer.renderBlockAsEntity(state, matrices, vertexConsumers, light, OverlayTexture.DEFAULT_UV);

            matrices.pop();
        }

        super.render(entity, yaw, tickDelta, matrices, vertexConsumers, light);
    }

    @Override
    public Identifier getTexture(LightDisplayEntity entity) {
        return PlayerScreenHandler.BLOCK_ATLAS_TEXTURE;
    }
}
