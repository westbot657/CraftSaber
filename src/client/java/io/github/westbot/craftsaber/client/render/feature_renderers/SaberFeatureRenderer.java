package io.github.westbot.craftsaber.client.render.feature_renderers;

import io.github.westbot.craftsaber.ModItems;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.ModelWithArms;
import net.minecraft.client.render.entity.model.ModelWithHead;
import net.minecraft.client.render.item.HeldItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Arm;

public class SaberFeatureRenderer<T extends PlayerEntity, M extends EntityModel<T> & ModelWithArms & ModelWithHead> extends FeatureRenderer<T, M> {

    HeldItemRenderer heldItemRenderer;

    public SaberFeatureRenderer(FeatureRendererContext<T, M> context, HeldItemRenderer heldItemRenderer) {
        super(context);
        this.heldItemRenderer = heldItemRenderer;
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, T entity, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {

        ItemStack stack1 = entity.getMainHandStack();
        ItemStack stack2 = entity.getOffHandStack();

        if (!(stack1.isOf(ModItems.SABER) || stack2.isOf(ModItems.SABER))) return;

        Arm mainArm = entity.getMainArm();

        if (mainArm == Arm.LEFT) {
            ItemStack temp = stack2;
            stack2 = stack1;
            stack1 = temp;
        }

        if (stack1.isOf(ModItems.SABER)) { // right hand

        }

        if (stack2.isOf(ModItems.SABER)) { // left hand

        }

    }
}


