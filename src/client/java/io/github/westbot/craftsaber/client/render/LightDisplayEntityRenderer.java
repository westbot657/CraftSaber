package io.github.westbot.craftsaber.client.render;

import com.mojang.serialization.DataResult;
import io.github.westbot.craftsaber.entities.LightDisplayEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtOps;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.EulerAngle;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class LightDisplayEntityRenderer extends EntityRenderer<LightDisplayEntity> {
    public LightDisplayEntityRenderer(EntityRendererFactory.Context ctx) {
        super(ctx);
    }

    private float t;

    @Override
    public void render(
        LightDisplayEntity entity, float yaw, float tickDelta, MatrixStack matrices,
        VertexConsumerProvider vertexConsumers, int light
    ) {
        t += tickDelta;

        BlockRenderManager renderer = MinecraftClient.getInstance().getBlockRenderManager();


        matrices.push();
        BlockState testState = Blocks.IRON_BARS.getDefaultState().with(Properties.EAST, true).with(Properties.NORTH, true);

        DataResult<NbtElement> ser = BlockState.CODEC.encodeStart(NbtOps.INSTANCE, testState);

        matrices.multiply(RotationAxis.POSITIVE_X.rotation(t/100.0f));
        matrices.multiply(RotationAxis.POSITIVE_Z.rotation(t/200.0f));

        matrices.translate(-0.5, 0, -0.5);
        renderer.renderBlockAsEntity(testState, matrices, vertexConsumers, light, OverlayTexture.DEFAULT_UV);


        matrices.pop();

        super.render(entity, yaw, tickDelta, matrices, vertexConsumers, light);
    }

    @Override
    public Identifier getTexture(LightDisplayEntity entity) {
        return PlayerScreenHandler.BLOCK_ATLAS_TEXTURE;
    }
}
