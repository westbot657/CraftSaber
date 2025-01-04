package io.github.westbot.craftsaber.client.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import io.github.westbot.craftsaber.ModComponents;
import io.github.westbot.craftsaber.ModItems;
import io.github.westbot.craftsaber.client.render.SaberTrailRenderer;
import io.github.westbot.craftsaber.data.Stash;
import io.github.westbot.craftsaber.mixin_data.ItemStackWithStash;
import net.minecraft.client.render.*;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.ItemEntityRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Pair;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemEntityRenderer.class)
public abstract class ItemEntityRendererMixin extends EntityRenderer<ItemEntity> {
    protected ItemEntityRendererMixin(EntityRendererFactory.Context ctx) {
        super(ctx);
    }

    @Inject(
        method = "render(Lnet/minecraft/entity/ItemEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/render/entity/ItemEntityRenderer;renderStack(Lnet/minecraft/client/render/item/ItemRenderer;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;ILnet/minecraft/item/ItemStack;Lnet/minecraft/client/render/model/BakedModel;ZLnet/minecraft/util/math/random/Random;)V",
            args = ""
        )
    )
    public void renderInject(ItemEntity entity, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci, @Local BakedModel bakedModel) {

        boolean depth = bakedModel.hasDepth();

        ItemStack stack = entity.getStack();
        if (stack.isOf(ModItems.SABER)) {

            MatrixStack matrix = new MatrixStack();

            float j = MathHelper.sin(((float)entity.getItemAge() + tickDelta) / 10.0F + entity.uniqueOffset) * 0.1F + 0.1F;
            float k = bakedModel.getTransformation().getTransformation(ModelTransformationMode.GROUND).scale.y();
            matrix.translate(0.0F, j + 0.25F * k, 0.0F);
            float l = entity.getRotation(tickDelta);
            matrix.multiply(RotationAxis.POSITIVE_Y.rotation(l));

            float dx = bakedModel.getTransformation().ground.scale.x();
            float dy = bakedModel.getTransformation().ground.scale.y();
            float dz = bakedModel.getTransformation().ground.scale.z();
            if (!depth) {
                float fx = -0.0F * (float) (0) * 0.5F * dx;
                float fy = -0.0F * (float) (0) * 0.5F * dy;
                float fz = -0.09375F * (float) (0) * 0.5F * dz;
                matrix.translate(fx, fy, fz);
            }
            matrix.push();
            bakedModel.getTransformation().getTransformation(ModelTransformationMode.GROUND).apply(false, matrix);

            matrix.push();
            matrix.translate(0, (6/8f) * 0.6, 0);
            Vector3f blade_base = matrix.peek().getPositionMatrix().getTranslation(new Vector3f());
            matrix.pop();

            matrix.push();
            matrix.translate(0, (28/8f) * 0.6, 0);
            Vector3f blade_tip = matrix.peek().getPositionMatrix().getTranslation(new Vector3f());
            matrix.pop();

            Vec3d pos = entity.getPos();
            blade_base = blade_base.add((float) pos.x, (float) pos.y, (float) pos.z);
            blade_tip = blade_tip.add((float) pos.x, (float) pos.y, (float) pos.z);
            Stash<Pair<Vector3f, Vector3f>> stash = ((ItemStackWithStash) ((Object) stack)).craftSaber$getStash();
            var col = stack.getOrDefault(ModComponents.SABER_COLOR, 0);

            SaberTrailRenderer.queueRender(blade_base, blade_tip, stash, col);
        }

    }

    @Override
    public boolean shouldRender(ItemEntity entity, Frustum frustum, double x, double y, double z) {
        if (entity.getStack().isOf(ModItems.SABER)) {
            return true;
        }

        return super.shouldRender(entity, frustum, x, y, z);
    }
}
