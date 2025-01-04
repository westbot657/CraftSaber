package io.github.westbot.craftsaber.client.render.entity;

import io.github.westbot.craftsaber.data.LightTile;
import io.github.westbot.craftsaber.client.CraftSaberClient;
import io.github.westbot.craftsaber.entities.LightDisplayEntity;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Frustum;
import net.minecraft.client.render.RenderLayers;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.BlockRenderView;
import org.joml.Quaternionf;

import java.util.List;

public class LightDisplayEntityRenderer extends EntityRenderer<LightDisplayEntity> {
    public LightDisplayEntityRenderer(EntityRendererFactory.Context ctx) {
        super(ctx);
    }

    @Override
    public boolean shouldRender(LightDisplayEntity entity, Frustum frustum, double x, double y, double z) {
        return super.shouldRender(entity, frustum, x, y, z);
    }

    @Override
    public void render(
        LightDisplayEntity entity, float yaw, float tickDelta, MatrixStack matrices,
        VertexConsumerProvider vertexConsumers, int light
    ) {

        BlockRenderManager renderer = MinecraftClient.getInstance().getBlockRenderManager();

        Pair<Vec3d, List<Pair<Vec3d, BlockState>>> struct_data = CraftSaberClient.structure_cache.getOrDefault(entity.getStructureId(), null);

        if (struct_data == null) return;

        Vec3d center_offset = struct_data.getLeft();
        List<Pair<Vec3d, BlockState>> pairs = struct_data.getRight();

        Vec3d rotation = entity.getRotation();
        for (Pair<Vec3d, BlockState> pair : pairs) {
            Vec3d offset = pair.getLeft();
            BlockState state = pair.getRight();

            if (state.getBlock() instanceof LightTile lightTile) {

            }

            offset = offset.rotateZ((float) -rotation.z).rotateY((float) rotation.y).rotateX((float) -rotation.x);

            matrices.push();
            // structure relative translations
            matrices.translate(offset.x, offset.y, offset.z);

            // structure rotation
            matrices.multiply((new Quaternionf()).rotationXYZ((float) rotation.x, (float) rotation.y, (float) rotation.z));

            // entity alignment
            matrices.translate(-center_offset.x - 0.5, -center_offset.y - 0.5, -center_offset.z - 0.5);

            BlockPos renderOrigin = entity.getBlockPos();
            BlockRenderView world = entity.getWorld();

            renderer.renderBlock(state, renderOrigin, world, matrices, vertexConsumers.getBuffer(RenderLayers.getBlockLayer(state)), false, entity.getRandom());

            matrices.pop();
        }

        super.render(entity, yaw, tickDelta, matrices, vertexConsumers, light);
    }

    @Override
    public Identifier getTexture(LightDisplayEntity entity) {
        return PlayerScreenHandler.BLOCK_ATLAS_TEXTURE;
    }
}
