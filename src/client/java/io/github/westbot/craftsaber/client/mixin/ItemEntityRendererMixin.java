package io.github.westbot.craftsaber.client.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import io.github.westbot.craftsaber.CraftSaber;
import io.github.westbot.craftsaber.ModComponents;
import io.github.westbot.craftsaber.ModItems;
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
import net.minecraft.util.math.Vec3d;
import org.joml.Quaternionf;
import org.joml.Vector3d;
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
    public void renderInject(ItemEntity entity, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci, @Local LocalRef<BakedModel> bakedModel) {

        BakedModel model = bakedModel.get();
        boolean depth = model.hasDepth();


        ItemStack stack = entity.getStack();
        if (stack.isOf(ModItems.SABER)) {

            Vec3d commonDelta = new Vec3d(0, 0, 0);
            MatrixStack matrix = new MatrixStack();
            matrix.loadIdentity();


            // call to ItemEntityRenderer#renderStack()
            float f = model.getTransformation().ground.scale.x();
            float g = model.getTransformation().ground.scale.y();
            float h = model.getTransformation().ground.scale.z();
            if (!depth) {
                float j = -0.0F * (float) (0) * 0.5F * f;
                float k = -0.0F * (float) (0) * 0.5F * g;
                float l = -0.09375F * (float) (0) * 0.5F * h;
                matrix.translate(j, k, l);
            }
            matrix.push();
            model.getTransformation().getTransformation(ModelTransformationMode.GROUND).apply(false, matrix);
            matrix.translate(-0.5f, -0.5f, -0.5f);

            //Vector3f out = matrix.peek().getPositionMatrix().getTranslation(new Vector3f());

            matrix.push();
            matrix.translate(0, 8, 0);
            Vector3f blade_base = matrix.peek().getPositionMatrix().getTranslation(new Vector3f());
            matrix.pop();

            matrix.push();
            matrix.translate(0, 32, 0);
            Vector3f blade_tip = matrix.peek().getPositionMatrix().getTranslation(new Vector3f());
            matrix.pop();

            Vec3d pos = entity.getPos();
            blade_base = blade_base.add((float) pos.x, (float) pos.y, (float) pos.z);
            blade_tip = blade_tip.add((float) pos.x, (float) pos.y, (float) pos.z);

            CraftSaber.LOGGER.info("Render positions: {}, {}", blade_base, blade_tip);

            Stash<Pair<Vector3f, Vector3f>> stash = ((ItemStackWithStash) ((Object) stack)).craftSaber$getStash();
            Vector3f current_base = blade_base;
            Vector3f current_tip = blade_tip;

            if (!stash.isEmpty()) {
                var tessellator = Tessellator.getInstance();
                var trail_buffer = tessellator.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);
                var col = stack.getOrDefault(ModComponents.SABER_COLOR, 0);

                for (Pair<Vector3f, Vector3f> ab : stash) {
                    Vector3f a = ab.getLeft();
                    Vector3f b = ab.getRight();

                    trail_buffer.vertex(
                        current_base.x,
                        current_base.y,
                        current_base.z
                    ).color(col + 0x7F000000);
                    trail_buffer.vertex(
                        a.x,
                        a.y,
                        a.z
                    ).color(col + 0x7F000000);
                    trail_buffer.vertex(
                        b.x,
                        b.y,
                        b.z
                    ).color(col + 0x7F000000);
                    trail_buffer.vertex(
                        current_tip.x,
                        current_tip.y,
                        current_tip.z
                    ).color(col + 0x7F000000);


                    current_base = a;
                    current_tip = b;

                }
                BufferRenderer.drawWithGlobalProgram(trail_buffer.end());
            }

            stash.push(new Pair<>(blade_base, blade_tip));


            // Irrelevant:
            // if true:
            // 119-131 |   bl2 = false
            // 148 |   vertexConsumer = getItemGlintConsumer(...)
            // call to this.renderBakedItemModel(...)
            // 92 |   Random random = Random.create()
            // 93 |   long l = 42L
            // set random seed to l before every call of:
            // call to renderBakedItemQuads


        }

    }

}
