package io.github.westbot.craftsaber.client.render.item;

import io.github.westbot.craftsaber.ModComponents;
import io.github.westbot.craftsaber.items.Saber;
import net.minecraft.client.render.*;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.renderer.specialty.DynamicGeoItemRenderer;



public class SaberRenderer extends DynamicGeoItemRenderer<Saber> {
    public SaberRenderer() {
        super(new SaberModel());
    }



    @Override
    protected boolean boneRenderOverride(MatrixStack poseStack, GeoBone bone, VertexConsumerProvider bufferSource, VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay, int colour) {

        int color = currentItemStack.getOrDefault(ModComponents.SABER_COLOR, 0);

        super.renderCubesOfBone(
            poseStack, bone, buffer, packedLight, packedOverlay,
            (bone.getName().equals("blade") || bone.getName().equals("hilt_color"))
                ? color + 0xFF000000
                : 0xFFFFFFFF
        );

        return true;
    }

    @Override
    public void render(ItemStack stack, ModelTransformationMode transformType, MatrixStack poseStack, VertexConsumerProvider bufferSource, int packedLight, int packedOverlay) {

        super.render(stack, transformType, poseStack, bufferSource, packedLight, packedOverlay);
    }

    @Override
    protected void renderInGui(ModelTransformationMode transformType, MatrixStack poseStack, VertexConsumerProvider bufferSource, int packedLight, int packedOverlay, float partialTick) {

        super.renderInGui(transformType, poseStack, bufferSource, packedLight, packedOverlay, partialTick);
    }

    @Override
    public void renderFinal(MatrixStack poseStack, Saber animatable, BakedGeoModel model, VertexConsumerProvider bufferSource, @Nullable VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay, int colour) {

        super.renderFinal(poseStack, animatable, model, bufferSource, buffer, partialTick, packedLight, packedOverlay, colour);
    }
}
